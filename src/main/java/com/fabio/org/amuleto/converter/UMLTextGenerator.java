package com.fabio.org.amuleto.converter;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import com.fabio.org.amuleto.utils.UMLFileUtils;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

public class UMLTextGenerator {

  public static void generateFromDirectory(File sourceDir, File outputFile) throws Exception {
    // Recupera ricorsivamente tutti i file Java nella directory
    List<File> javaFiles = UMLFileUtils.getJavaFiles(sourceDir);

    // Mappa per raggruppare i tipi per package
    Map<String, List<TypeDeclaration<?>>> packageMap = new HashMap<>();
    // Set per raccogliere i nomi dei tipi definiti
    Set<String> definedTypes = new HashSet<>();
    // Set di nomi da escludere (per ora vuoto)
    Set<String> excludedTypes = new HashSet<>(Arrays.asList(""));

    // Parsing di ogni file e raggruppamento per package
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

    // Costruzione del diagramma UML in PlantUML
    StringBuilder uml = new StringBuilder();
    StringBuilder relationships = new StringBuilder();

    uml.append("@startuml\n");
    // Parametri per il dark theme
    uml.append("skinparam classAttributeIconSize 0\n\n");
    uml.append("skinparam backgroundColor #2E2E2E\n");
    uml.append("skinparam shadowing false\n");
    uml.append("skinparam classFontColor white\n");
    uml.append("skinparam classBackgroundColor #3E3E3E\n");
    uml.append("skinparam classBorderColor #AAAAAA\n");
    uml.append("skinparam defaultTextColor white\n");
    uml.append("skinparam arrowColor #CCCCCC\n");
    uml.append("skinparam arrowFontColor white\n");
    uml.append("skinparam packageFontColor white\n");
    uml.append("skinparam packageTitleFontColor white\n");
    uml.append("skinparam stereotypeFontColor white\n");
    uml.append("skinparam packageBorderColor white\n\n");

    // Itera sui package in ordine alfabetico
    List<String> pkgNames = new ArrayList<>(packageMap.keySet());
    Collections.sort(pkgNames);
    for (String pkgName : pkgNames) {
      if (!pkgName.isEmpty()) {
        uml.append("package ").append(pkgName).append(" {\n\n");
      }
      for (TypeDeclaration<?> type : packageMap.get(pkgName)) {
        // Delegazione a UMLProcessor (che si suppone sia già implementato)
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
        uml.append("}\n\n");
      }
    }
    uml.append("\n").append(relationships);
    uml.append("\n@enduml");

    // Scrive la stringa UML nel file di output
    try (FileWriter writer = new FileWriter(outputFile)) {
      writer.write(uml.toString());
    }
  }
}
