package com.fabio.org.amuleto.converter;

import java.util.List;
import java.util.Set;

import com.fabio.org.amuleto.utils.UMLUtils;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

public class UMLMethodProcessor {

  public static void processMethod(
      MethodDeclaration method,
      String className,
      StringBuilder uml,
      StringBuilder relationships,
      Set<String> definedTypes) {
    String methodVisibility = UMLUtils.getVisibilitySymbol(method);
    String modifiersMarker = "";
    if (method.isStatic()) {
      modifiersMarker += " {static}";
    }
    if (method.isFinal()) {
      modifiersMarker += " {final}";
    }
    if (method.isAbstract()) {
      modifiersMarker += " {abstract}";
    }
    StringBuilder params = new StringBuilder();
    List<Parameter> parameters = method.getParameters();
    for (int i = 0; i < parameters.size(); i++) {
      Parameter param = parameters.get(i);
      params
          .append(param.getNameAsString())
          .append(" : ")
          .append(UMLUtils.getTypeString(param.getType()));
      if (i < parameters.size() - 1) {
        params.append(", ");
      }
    }
    String returnType = UMLUtils.getTypeString(method.getType());
    uml.append("  ")
        .append(methodVisibility)
        .append(modifiersMarker)
        .append(" ")
        .append(method.getNameAsString())
        .append("(")
        .append(params)
        .append(") : ")
        .append(returnType)
        .append("\n");

    // Gestione delle dependency (uses)
    for (Parameter param : method.getParameters()) {
      String paramType = UMLUtils.getTypeString(param.getType());
      if (!UMLUtils.isPrimitiveOrJavaType(paramType)
          && definedTypes.contains(paramType)
          && !paramType.equals(className)) {
        String dep = className + " ..> " + paramType + " : uses";
        if (!relationships.toString().contains(dep)) {
          relationships.append(dep).append("\n");
        }
      }
    }
  }
}
