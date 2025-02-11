package com.fabio.org.amuleto.converter;

import java.util.Set;

import com.fabio.org.amuleto.utils.UMLUtils;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

public class UMLAssociationProcessor {

  public static void processAssociation(
      Type fieldType,
      FieldDeclaration field,
      String className,
      String fieldName,
      StringBuilder uml,
      StringBuilder relationships,
      Set<String> definedTypes) {
    String associatedType = null;
    boolean isCollection = false;
    if (fieldType.isClassOrInterfaceType()) {
      ClassOrInterfaceType cit = fieldType.asClassOrInterfaceType();
      String typeName = cit.getNameAsString();
      if (UMLUtils.isCollectionType(typeName)) {
        isCollection = true;
        if (cit.getTypeArguments().isPresent() && !cit.getTypeArguments().get().isEmpty()) {
          Type argType = cit.getTypeArguments().get().get(0);
          associatedType = UMLUtils.getTypeString(argType);
        }
      }
    }
    if (associatedType == null) {
      String fieldTypeStr = UMLUtils.getTypeString(fieldType);
      if (!UMLUtils.isPrimitiveOrJavaType(fieldTypeStr)
          && Character.isUpperCase(fieldTypeStr.charAt(0))) {
        associatedType = fieldTypeStr;
      }
    }
    if (associatedType != null) {
      String arrow = " --> ";
      if (UMLUtils.hasAnnotation(field, "composition")) {
        arrow = " *-->";
      } else if (UMLUtils.hasAnnotation(field, "aggregation")) {
        arrow = " o-->";
      }
      String multiplicity = isCollection ? "\"0..*\"" : "\"1\"";
      relationships
          .append(className)
          .append(" \"1\"")
          .append(arrow)
          .append(multiplicity)
          .append(" ")
          .append(associatedType)
          .append(" : ")
          .append(fieldName)
          .append("\n");
    }
  }
}
