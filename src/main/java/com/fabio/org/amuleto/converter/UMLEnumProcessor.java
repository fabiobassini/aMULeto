package com.fabio.org.amuleto.converter;

import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

public class UMLEnumProcessor {

  public static void processEnum(EnumDeclaration enumDecl, StringBuilder uml) {
    String enumName = enumDecl.getNameAsString();
    uml.append("enum ").append(enumName).append(" {\n");
    for (EnumConstantDeclaration constant : enumDecl.getEntries()) {
      uml.append("  ").append(constant.getNameAsString()).append("\n");
    }
    uml.append("}\n\n");
  }
}
