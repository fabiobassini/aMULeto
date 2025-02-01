package com.fabio.org.amuleto;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class UMLGenerator {

    /**
     * Genera il diagramma UML a partire da tutti i file Java contenuti in una
     * directory (ricorsivamente)
     * e scrive il risultato nel file di output.
     * Il metodo raggruppa i tipi per package e esclude i file del generatore
     * stesso.
     */
    public static void generateFromDirectory(File sourceDir, File outputFile) throws Exception {
        List<File> javaFiles = getJavaFiles(sourceDir);
        // Map per raggruppare i tipi per package.
        Map<String, List<TypeDeclaration<?>>> packageMap = new HashMap<>();
        // Set per raccogliere i nomi di tutti i tipi (utilizzato per le dependency).
        Set<String> definedTypes = new HashSet<>();
        // Insieme dei nomi delle classi da escludere (le classi del generatore).
        Set<String> excludedTypes = new HashSet<>(Arrays.asList("App", "UMLGenerator", "UMLUtils"));

        // Parsing di tutti i file Java e raggruppamento per package.
        for (File file : javaFiles) {
            CompilationUnit cu = StaticJavaParser.parse(file);
            // Determina il package; se assente, usa la stringa vuota.
            String pkgName = "";
            Optional<PackageDeclaration> pkgDecl = cu.getPackageDeclaration();
            if (pkgDecl.isPresent()) {
                pkgName = pkgDecl.get().getNameAsString();
            }
            for (TypeDeclaration<?> type : cu.getTypes()) {
                String typeName = type.getNameAsString();
                if (excludedTypes.contains(typeName)) {
                    continue; // Escludi le classi del generatore.
                }
                packageMap.computeIfAbsent(pkgName, k -> new ArrayList<>()).add(type);
                definedTypes.add(typeName);
            }
        }

        // StringBuilder per il contenuto UML e per le relazioni.
        StringBuilder uml = new StringBuilder();
        StringBuilder relationships = new StringBuilder();
        // Set per evitare duplicati nelle dependency.
        Set<String> dependencyRels = new HashSet<>();

        uml.append("@startuml\n\n");
        uml.append("skinparam classAttributeIconSize 0\n\n");

        // Itera sui package (ordinati per nome).
        List<String> pkgNames = new ArrayList<>(packageMap.keySet());
        Collections.sort(pkgNames);
        for (String pkgName : pkgNames) {
            if (!pkgName.isEmpty()) {
                uml.append("package ").append(pkgName).append(" {\n\n");
            }
            for (TypeDeclaration<?> type : packageMap.get(pkgName)) {
                if (type instanceof ClassOrInterfaceDeclaration) {
                    processType((ClassOrInterfaceDeclaration) type, uml, relationships, definedTypes);
                } else if (type instanceof EnumDeclaration) {
                    processEnum((EnumDeclaration) type, uml);
                }
            }
            if (!pkgName.isEmpty()) {
                uml.append("}\n\n");
            }
        }

        // Aggiunge le relazioni al termine.
        uml.append("\n").append(relationships);
        uml.append("\n@enduml");

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(uml.toString());
        }
    }

    /**
     * Processa una classe o interfaccia e aggiunge la sua definizione al diagramma
     * UML.
     * Genera inoltre le relazioni (ereditarietà, implementazioni, associazioni,
     * dependency).
     */
    private static void processType(ClassOrInterfaceDeclaration cid, StringBuilder uml, StringBuilder relationships,
            Set<String> definedTypes) {
        String className = cid.getNameAsString();

        // Verifica se la classe è marcata come classe associativa tramite
        // @AssociationClass.
        boolean isAssociationClass = false;
        for (AnnotationExpr ann : cid.getAnnotations()) {
            if (ann.getNameAsString().equalsIgnoreCase("AssociationClass")) {
                isAssociationClass = true;
                break;
            }
        }

        // Costruisce l'intestazione della classe o interfaccia.
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

        // Elaborazione dei campi.
        for (FieldDeclaration field : cid.getFields()) {
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
                    if (annName.equals("readonly") || annName.equals("unique") ||
                            annName.equals("nonunique") || annName.equals("ordered") ||
                            annName.equals("unordered") || annName.equals("aggregation") ||
                            annName.equals("composition") || annName.equals("navigable") ||
                            annName.equals("nonnavigable") || annName.equals("derived")) {
                        properties.add(annName);
                    }
                }
                String propStr = properties.isEmpty() ? "" : " {" + String.join(", ", properties) + "}";
                uml.append("  ").append(visibility).append(modifiersMarker)
                        .append(" ").append(fieldName)
                        .append(" : ").append(fieldType).append(propStr).append("\n");

                // Se la classe non è associativa, processa le associazioni per i campi.
                if (!isAssociationClass) {
                    processAssociation(var.getType(), field, className, fieldName, uml, relationships, definedTypes);
                }
            }
        }

        // Elaborazione dei costruttori.
        for (ConstructorDeclaration constructor : cid.getConstructors()) {
            String consVisibility = UMLUtils.getVisibilitySymbol(constructor);
            StringBuilder params = new StringBuilder();
            List<Parameter> parameters = constructor.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                Parameter param = parameters.get(i);
                params.append(param.getNameAsString())
                        .append(" : ")
                        .append(UMLUtils.getTypeString(param.getType()));
                if (i < parameters.size() - 1) {
                    params.append(", ");
                }
            }
            uml.append("  ").append(consVisibility).append(" ")
                    .append(constructor.getNameAsString())
                    .append("(").append(params).append(")")
                    .append("\n");
        }

        // Elaborazione dei metodi.
        for (MethodDeclaration method : cid.getMethods()) {
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
                params.append(param.getNameAsString())
                        .append(" : ")
                        .append(UMLUtils.getTypeString(param.getType()));
                if (i < parameters.size() - 1) {
                    params.append(", ");
                }
            }
            String returnType = UMLUtils.getTypeString(method.getType());
            uml.append("  ").append(methodVisibility).append(modifiersMarker)
                    .append(" ").append(method.getNameAsString())
                    .append("(").append(params).append(")")
                    .append(" : ").append(returnType)
                    .append("\n");

            // Aggiunge le dependency (uses) per i tipi dei parametri.
            for (Parameter param : method.getParameters()) {
                String paramType = UMLUtils.getTypeString(param.getType());
                if (!UMLUtils.isPrimitiveOrJavaType(paramType) &&
                        definedTypes.contains(paramType) &&
                        !paramType.equals(className)) {
                    String dep = className + " ..> " + paramType + " : uses";
                    if (!relationships.toString().contains(dep)) {
                        relationships.append(dep).append("\n");
                    }
                }
            }
        }
        uml.append("}\n\n");

        // Se la classe non è associativa, processa ereditarietà e implementazioni.
        if (!isAssociationClass) {
            NodeList<ClassOrInterfaceType> extendedTypes = cid.getExtendedTypes();
            for (ClassOrInterfaceType extendedType : extendedTypes) {
                String parent = extendedType.getNameAsString();
                relationships.append(parent)
                        .append(" <|-- ")
                        .append(className)
                        .append("\n");
            }
            NodeList<ClassOrInterfaceType> implementedTypes = cid.getImplementedTypes();
            for (ClassOrInterfaceType implType : implementedTypes) {
                String iface = implType.getNameAsString();
                relationships.append(iface)
                        .append(" ..|> ")
                        .append(className)
                        .append("\n");
            }
        } else {
            // Per una classe associativa, elaboriamo le associazioni speciali.
            Set<String> assocClasses = new HashSet<>();
            for (FieldDeclaration field : cid.getFields()) {
                String fType = UMLUtils.getTypeString(field.getVariables().get(0).getType());
                if (!UMLUtils.isPrimitiveOrJavaType(fType) && definedTypes.contains(fType)) {
                    assocClasses.add(fType);
                }
            }
            if (assocClasses.size() == 2) {
                Iterator<String> it = assocClasses.iterator();
                String classA = it.next();
                String classB = it.next();
                String assocLine = classA + " \"1\" -- \"1\" " + classB + " : association";
                if (!relationships.toString().contains(assocLine)) {
                    relationships.append(assocLine).append("\n");
                }
                relationships.append(className)
                        .append(" ..> ").append(classA)
                        .append(" : association\n");
                relationships.append(className)
                        .append(" ..> ").append(classB)
                        .append(" : association\n");
            }
        }
    }

    /**
     * Processa un'enumerazione e ne aggiunge la definizione al diagramma UML.
     */
    private static void processEnum(EnumDeclaration enumDecl, StringBuilder uml) {
        String enumName = enumDecl.getNameAsString();
        uml.append("enum ").append(enumName).append(" {\n");
        for (EnumConstantDeclaration constant : enumDecl.getEntries()) {
            uml.append("  ").append(constant.getNameAsString()).append("\n");
        }
        uml.append("}\n\n");
    }

    /**
     * Processa un campo per verificare se genera un'associazione (se il tipo non è
     * primitivo).
     * Questo metodo viene utilizzato per le classi NON associative.
     */
    static void processAssociation(Type fieldType, FieldDeclaration field, String className, String fieldName,
            StringBuilder uml, StringBuilder relationships, Set<String> definedTypes) {
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
            if (!UMLUtils.isPrimitiveOrJavaType(fieldTypeStr) && Character.isUpperCase(fieldTypeStr.charAt(0))) {
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
            relationships.append(className)
                    .append(" \"1\"").append(arrow)
                    .append(multiplicity).append(" ").append(associatedType)
                    .append(" : ").append(fieldName).append("\n");
        }
    }

    /**
     * Restituisce una lista di tutti i file Java (con estensione .java) presenti
     * nella directory (ricorsivamente).
     */
    private static List<File> getJavaFiles(File dir) {
        List<File> files = new ArrayList<>();
        File[] entries = dir.listFiles();
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    files.addAll(getJavaFiles(entry));
                } else if (entry.getName().endsWith(".java")) {
                    files.add(entry);
                }
            }
        }
        return files;
    }
}
