package com.fabio.org.amuleto.converter;

import java.util.Set;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

public class UMLProcessor {

  public static void processType(
      ClassOrInterfaceDeclaration cid,
      StringBuilder uml,
      StringBuilder relationships,
      Set<String> definedTypes) {
    UMLTypeProcessor.processType(cid, uml, relationships, definedTypes);
  }

  public static void processEnum(EnumDeclaration enumDecl, StringBuilder uml) {
    UMLEnumProcessor.processEnum(enumDecl, uml);
  }
}
