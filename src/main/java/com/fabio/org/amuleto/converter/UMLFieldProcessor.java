package com.fabio.org.amuleto.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fabio.org.amuleto.utils.UMLUtils;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;

public class UMLFieldProcessor {

  public static void processFields(
      FieldDeclaration field,
      String className,
      StringBuilder uml,
      StringBuilder relationships,
      Set<String> definedTypes,
      boolean isAssociationClass) {
    String modifiersMarker = "";
    if (field.isStatic()) {
      modifiersMarker += " {static}";
    }
    if (field.isFinal()) {
      modifiersMarker += " {final}";
    }
    for (VariableDeclarator var : field.getVariables()) {
      String fieldName = var.getNameAsString();
      String fieldType = UMLUtils.getTypeString(var.getType());
      String visibility = UMLUtils.getVisibilitySymbol(field);
      List<String> properties = new ArrayList<>();
      for (AnnotationExpr ann : field.getAnnotations()) {
        String annName = ann.getNameAsString().toLowerCase();
        if (annName.equals("readonly")
            || annName.equals("unique")
            || annName.equals("nonunique")
            || annName.equals("ordered")
            || annName.equals("unordered")
            || annName.equals("aggregation")
            || annName.equals("composition")
            || annName.equals("navigable")
            || annName.equals("nonnavigable")
            || annName.equals("derived")) {
          properties.add(annName);
        }
      }
      String propStr = properties.isEmpty() ? "" : " {" + String.join(", ", properties) + "}";
      uml.append("  ")
          .append(visibility)
          .append(modifiersMarker)
          .append(" ")
          .append(fieldName)
          .append(" : ")
          .append(fieldType)
          .append(propStr)
          .append("\n");

      // Se la classe non è associativa, delega l’elaborazione delle associazioni
      if (!isAssociationClass) {
        UMLAssociationProcessor.processAssociation(
            var.getType(), field, className, fieldName, uml, relationships, definedTypes);
      }
    }
  }
}
