package com.fabio.org.amuleto.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import com.fabio.org.amuleto.utils.UMLFileUtils;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class UMLVectorGenerator {

  public static void generateVectorFromDirectory(
      File sourceDir, File outputVectorFile, FileFormat vectorFormat) throws Exception {
    // Recupera ricorsivamente tutti i file Java dalla directory
    List<File> javaFiles = UMLFileUtils.getJavaFiles(sourceDir);
    Map<String, List<TypeDeclaration<?>>> packageMap = new HashMap<>();
    Set<String> definedTypes = new HashSet<>();
    Set<String> excludedTypes = new HashSet<>(Arrays.asList(""));

    for (File file : javaFiles) {
      CompilationUnit cu = StaticJavaParser.parse(file);
      String pkgName = "";
      if (cu.getPackageDeclaration().isPresent()) {
        pkgName = cu.getPackageDeclaration().get().getNameAsString();
      }
      for (TypeDeclaration<?> type : cu.getTypes()) {
        String typeName = type.getNameAsString();
        if (excludedTypes.contains(typeName)) continue;
        packageMap.computeIfAbsent(pkgName, k -> new ArrayList<>()).add(type);
        definedTypes.add(typeName);
      }
    }

    StringBuilder uml = new StringBuilder();
    StringBuilder relationships = new StringBuilder();

    uml.append("@startuml\n");
    uml.append("skinparam classAttributeIconSize 0\n");
    uml.append("skinparam dpi 300\n");
    uml.append("scale max 2000 width\n");
    uml.append("skinparam backgroundColor #2E2E2E\n");
    uml.append("skinparam shadowing false\n");
    uml.append("skinparam classFontColor white\n");
    uml.append("skinparam arrowFontColor white\n");
    uml.append("skinparam packageFontColor white\n");
    uml.append("skinparam packageTitleFontColor white\n");
    uml.append("skinparam stereotypeFontColor white\n");
    uml.append("skinparam packageBorderColor white\n");
    uml.append("skinparam classBackgroundColor #3E3E3E\n");
    uml.append("skinparam classBorderColor #AAAAAA\n");
    uml.append("skinparam defaultTextColor white\n");
    uml.append("skinparam arrowColor #CCCCCC\n");

    List<String> pkgNames = new ArrayList<>(packageMap.keySet());
    Collections.sort(pkgNames);
    for (String pkgName : pkgNames) {
      if (!pkgName.isEmpty()) {
        uml.append("package ").append(pkgName).append(" {\n");
      }
      for (TypeDeclaration<?> type : packageMap.get(pkgName)) {
        if (type instanceof com.github.javaparser.ast.body.ClassOrInterfaceDeclaration) {
          UMLProcessor.processType(
              (com.github.javaparser.ast.body.ClassOrInterfaceDeclaration) type,
              uml,
              relationships,
              definedTypes);
        } else if (type instanceof com.github.javaparser.ast.body.EnumDeclaration) {
          UMLProcessor.processEnum((com.github.javaparser.ast.body.EnumDeclaration) type, uml);
        }
      }
      if (!pkgName.isEmpty()) {
        uml.append("}\n");
      }
    }
    uml.append("\n").append(relationships);
    uml.append("\n@enduml");

    try (FileOutputStream fos = new FileOutputStream(outputVectorFile)) {
      SourceStringReader reader = new SourceStringReader(uml.toString());
      reader.outputImage(fos, new FileFormatOption(vectorFormat));
      System.out.println(
          "Diagramma generato in formato "
              + vectorFormat
              + " in: "
              + outputVectorFile.getAbsolutePath());
    }
  }
}
