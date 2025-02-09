package com.fabio.org.amuleto.utils;

import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.Type;

public class UMLUtils {

  /**
   * Restituisce il simbolo di visibilità per un nodo che implementa NodeWithAnnotations e
   * NodeWithModifiers. Se il nodo è annotato con "derived", viene restituito '/'. Altrimenti: -
   * Public: + - Protected: # - Private: - - Package-private: ~
   */
  public static <T extends Node & NodeWithAnnotations<T> & NodeWithModifiers<?>>
      String getVisibilitySymbol(T node) {
    for (AnnotationExpr ann : node.getAnnotations()) {
      if (ann.getNameAsString().equalsIgnoreCase("derived")) {
        return "/";
      }
    }
    if (node.getModifiers().contains(com.github.javaparser.ast.Modifier.publicModifier())) {
      return "+";
    } else if (node.getModifiers()
        .contains(com.github.javaparser.ast.Modifier.protectedModifier())) {
      return "#";
    } else if (node.getModifiers().contains(com.github.javaparser.ast.Modifier.privateModifier())) {
      return "-";
    }
    return "~"; // package-private
  }

  /** Verifica se un FieldDeclaration contiene una determinata annotazione (case-insensitive). */
  public static boolean hasAnnotation(FieldDeclaration field, String annotationName) {
    for (AnnotationExpr ann : field.getAnnotations()) {
      if (ann.getNameAsString().equalsIgnoreCase(annotationName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determina se il nome di un tipo è una delle collezioni comuni (es. List, Set, Collection,
   * ecc.).
   */
  public static boolean isCollectionType(String typeName) {
    return typeName.equals("List")
        || typeName.equals("Set")
        || typeName.equals("Collection")
        || typeName.equals("ArrayList")
        || typeName.equals("LinkedList")
        || typeName.equals("HashSet");
  }

  /** Controlla se un tipo è primitivo o uno dei tipi built-in di Java. */
  public static boolean isPrimitiveOrJavaType(String typeName) {
    List<String> primitives =
        Arrays.asList("int", "short", "long", "double", "float", "boolean", "char", "byte");
    if (primitives.contains(typeName)) {
      return true;
    }
    return typeName.startsWith("String")
        || typeName.startsWith("Integer")
        || typeName.startsWith("Long")
        || typeName.startsWith("Double")
        || typeName.startsWith("Float")
        || typeName.startsWith("Boolean")
        || typeName.startsWith("Character")
        || typeName.startsWith("Byte");
  }

  /** Converte un oggetto Type in una stringa rappresentativa, gestendo anche gli array. */
  public static String getTypeString(Type type) {
    if (type instanceof ArrayType) {
      ArrayType at = (ArrayType) type;
      return getTypeString(at.getComponentType()) + "[]";
    }
    return type.toString();
  }
}
