package com.fabio.org.amuleto.converter;

import java.util.Set;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class UMLTypeProcessor {

  public static void processType(
      ClassOrInterfaceDeclaration cid,
      StringBuilder uml,
      StringBuilder relationships,
      Set<String> definedTypes) {
    String className = cid.getNameAsString();
    boolean isAssociationClass = false;
    for (AnnotationExpr ann : cid.getAnnotations()) {
      if (ann.getNameAsString().equalsIgnoreCase("AssociationClass")) {
        isAssociationClass = true;
        break;
      }
    }
    if (cid.isInterface()) {
      uml.append("interface ").append(className);
    } else if (cid.isAbstract() && !cid.isInterface()) {
      uml.append("abstract class ").append(className);
    } else {
      uml.append("class ").append(className);
    }
    if (isAssociationClass) {
      uml.append(" <<association>>");
    }
    uml.append(" {\n");

    // Processa i campi
    for (FieldDeclaration field : cid.getFields()) {
      UMLFieldProcessor.processFields(
          field, className, uml, relationships, definedTypes, isAssociationClass);
    }

    // Processa i costruttori
    for (ConstructorDeclaration constructor : cid.getConstructors()) {
      UMLConstructorProcessor.processConstructor(constructor, uml);
    }

    // Processa i metodi
    for (MethodDeclaration method : cid.getMethods()) {
      UMLMethodProcessor.processMethod(method, className, uml, relationships, definedTypes);
    }

    uml.append("}\n\n");

    // Processa ereditarietà e implementazioni se non associativa
    if (!isAssociationClass) {
      NodeList<ClassOrInterfaceType> extendedTypes = cid.getExtendedTypes();
      for (ClassOrInterfaceType extendedType : extendedTypes) {
        String parent = extendedType.getNameAsString();
        relationships.append(parent).append(" <|-- ").append(className).append("\n");
      }
      NodeList<ClassOrInterfaceType> implementedTypes = cid.getImplementedTypes();
      for (ClassOrInterfaceType implType : implementedTypes) {
        String iface = implType.getNameAsString();
        relationships.append(iface).append(" ..|> ").append(className).append("\n");
      }
    }
  }
}
