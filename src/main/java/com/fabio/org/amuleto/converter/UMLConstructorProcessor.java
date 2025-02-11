package com.fabio.org.amuleto.converter;

import java.util.List;

import com.fabio.org.amuleto.utils.UMLUtils;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;

public class UMLConstructorProcessor {

  public static void processConstructor(ConstructorDeclaration constructor, StringBuilder uml) {
    String consVisibility = UMLUtils.getVisibilitySymbol(constructor);
    StringBuilder params = new StringBuilder();
    List<Parameter> parameters = constructor.getParameters();
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
    uml.append("  ")
        .append(consVisibility)
        .append(" ")
        .append(constructor.getNameAsString())
        .append("(")
        .append(params)
        .append(")")
        .append("\n");
  }
}
