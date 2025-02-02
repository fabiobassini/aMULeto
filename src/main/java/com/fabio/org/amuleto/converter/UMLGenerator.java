package com.fabio.org.amuleto.converter;

import com.fabio.org.amuleto.utils.UMLUtils;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.*;

/**
 * Classe che fornisce metodi statici per generare un diagramma UML a partire
 * dal codice Java. Il diagramma viene generato analizzando ricorsivamente i
 * file
 * Java in una directory e costruendo una rappresentazione testuale in stile
 * PlantUML.
 */
public class UMLGenerator {

    /**
     * Genera il diagramma UML in formato testo (PlantUML) a partire da tutti i file
     * Java contenuti in una directory (ricorsivamente) e scrive il risultato nel
     * file
     * di output.
     * I tipi vengono raggruppati per package e vengono esclusi i file appartenenti
     * al generatore stesso.
     *
     * @param sourceDir  La directory sorgente contenente i file Java.
     * @param outputFile Il file in cui verrà scritto il diagramma UML in formato
     *                   testo.
     * @throws Exception In caso di errori durante il parsing o la scrittura.
     */
    public static void generateFromDirectory(File sourceDir, File outputFile) throws Exception {
        // Recupera ricorsivamente tutti i file Java nella directory
        List<File> javaFiles = getJavaFiles(sourceDir);

        // Mappa per raggruppare i tipi (classi, interfacce, enum) per package
        Map<String, List<TypeDeclaration<?>>> packageMap = new HashMap<>();
        // Set per raccogliere i nomi di tutti i tipi definiti (utile per gestire le
        // dependency)
        Set<String> definedTypes = new HashSet<>();
        // Set di nomi delle classi da escludere (quelle appartenenti al generatore)
        // Set<String> excludedTypes = new HashSet<>(Arrays.asList("App",
        // "UMLGenerator", "UMLUtils"));
        Set<String> excludedTypes = new HashSet<>(Arrays.asList("")); // per ora nulla

        // Parsing di ogni file Java e raggruppamento dei tipi in base al package
        for (File file : javaFiles) {
            CompilationUnit cu = StaticJavaParser.parse(file);
            // Determina il package; se non presente, si utilizza una stringa vuota
            String pkgName = "";
            Optional<PackageDeclaration> pkgDecl = cu.getPackageDeclaration();
            if (pkgDecl.isPresent()) {
                pkgName = pkgDecl.get().getNameAsString();
            }
            // Itera su tutti i tipi presenti nel file
            for (TypeDeclaration<?> type : cu.getTypes()) {
                String typeName = type.getNameAsString();
                if (excludedTypes.contains(typeName)) {
                    continue; // Salta le classi del generatore
                }
                // Raggruppa il tipo in base al package
                packageMap.computeIfAbsent(pkgName, k -> new ArrayList<>()).add(type);
                // Aggiunge il nome del tipo al set dei tipi definiti
                definedTypes.add(typeName);
            }
        }

        // Costruisce la stringa contenente la definizione del diagramma UML
        StringBuilder uml = new StringBuilder();
        // StringBuilder per raccogliere le relazioni tra i tipi (ereditarietà,
        // implementazioni, ecc.)
        StringBuilder relationships = new StringBuilder();
        // Set per evitare duplicati nelle relazioni di dependency
        Set<String> dependencyRels = new HashSet<>();

        // Inizio del diagramma PlantUML
        uml.append("@startuml\n\n");
        uml.append("skinparam classAttributeIconSize 0\n\n");

        // Itera sui nomi dei package in ordine alfabetico
        List<String> pkgNames = new ArrayList<>(packageMap.keySet());
        Collections.sort(pkgNames);
        for (String pkgName : pkgNames) {
            if (!pkgName.isEmpty()) {
                // Apre la definizione di un package
                uml.append("package ").append(pkgName).append(" {\n\n");
            }
            // Per ogni tipo nel package, viene processato
            for (TypeDeclaration<?> type : packageMap.get(pkgName)) {
                if (type instanceof ClassOrInterfaceDeclaration) {
                    processType((ClassOrInterfaceDeclaration) type, uml, relationships, definedTypes);
                } else if (type instanceof EnumDeclaration) {
                    processEnum((EnumDeclaration) type, uml);
                }
            }
            if (!pkgName.isEmpty()) {
                // Chiude la definizione del package
                uml.append("}\n\n");
            }
        }

        // Aggiunge le relazioni raccolte alla fine del diagramma
        uml.append("\n").append(relationships);
        uml.append("\n@enduml");

        // Scrive la stringa UML nel file di output
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(uml.toString());
        }
    }

    /**
     * Genera direttamente un file vettoriale (SVG o PDF) a partire dai file Java
     * nella directory.
     *
     * @param sourceDir        La directory sorgente contenente i file Java.
     * @param outputVectorFile Il file di output in cui verrà salvato il diagramma
     *                         (con estensione .svg o .pdf).
     * @param vectorFormat     Il formato vettoriale desiderato (ad esempio
     *                         FileFormat.SVG).
     * @throws Exception In caso di errori durante il processo di generazione.
     */
    public static void generateVectorFromDirectory(File sourceDir, File outputVectorFile, FileFormat vectorFormat)
            throws Exception {
        // Recupera ricorsivamente tutti i file Java dalla directory
        List<File> javaFiles = getJavaFiles(sourceDir);
        // Mappa per raggruppare i tipi per package
        Map<String, List<TypeDeclaration<?>>> packageMap = new HashMap<>();
        // Set per raccogliere i nomi dei tipi definiti
        Set<String> definedTypes = new HashSet<>();
        // Set di nomi da escludere (quelle relative al generatore)
        Set<String> excludedTypes = new HashSet<>(Arrays.asList("App", "UMLGenerator", "UMLUtils"));

        // Parsing e raggruppamento per package, simile al metodo precedente
        for (File file : javaFiles) {
            CompilationUnit cu = StaticJavaParser.parse(file);
            String pkgName = "";
            Optional<PackageDeclaration> pkgDecl = cu.getPackageDeclaration();
            if (pkgDecl.isPresent()) {
                pkgName = pkgDecl.get().getNameAsString();
            }
            for (TypeDeclaration<?> type : cu.getTypes()) {
                String typeName = type.getNameAsString();
                if (excludedTypes.contains(typeName)) {
                    continue;
                }
                packageMap.computeIfAbsent(pkgName, k -> new ArrayList<>()).add(type);
                definedTypes.add(typeName);
            }
        }

        // Costruzione della stringa UML
        StringBuilder uml = new StringBuilder();
        StringBuilder relationships = new StringBuilder();
        uml.append("@startuml\n\n");

        // Imposta parametri di rendering per un'alta risoluzione e scalabilità
        // automatica
        uml.append("skinparam classAttributeIconSize 0\n");
        uml.append("skinparam dpi 300\n");
        uml.append("scale max 2000 width\n\n");

        // Itera sui package in ordine alfabetico
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
        // Aggiunge le relazioni raccolte e chiude il diagramma
        uml.append(relationships);
        uml.append("\n@enduml");

        // Genera il file vettoriale (SVG o PDF) utilizzando l'API di PlantUML
        try (FileOutputStream fos = new FileOutputStream(outputVectorFile)) {
            SourceStringReader reader = new SourceStringReader(uml.toString());
            // Scrive l'immagine nel formato specificato sull'output stream
            reader.outputImage(fos, new FileFormatOption(vectorFormat));
            System.out.println(
                    "Diagramma generato in formato " + vectorFormat + " in: " + outputVectorFile.getAbsolutePath());
        }
    }

    /**
     * Processa una classe o interfaccia, generando la sua definizione in UML
     * e le relative relazioni (ereditarietà, implementazioni, associazioni,
     * dependency).
     *
     * @param cid           Il tipo (classe o interfaccia) da processare.
     * @param uml           Il StringBuilder in cui verrà aggiunta la definizione
     *                      della classe.
     * @param relationships Il StringBuilder in cui verranno aggiunte le relazioni.
     * @param definedTypes  L'insieme dei nomi dei tipi definiti, per il controllo
     *                      delle dependency.
     */
    private static void processType(ClassOrInterfaceDeclaration cid, StringBuilder uml, StringBuilder relationships,
            Set<String> definedTypes) {
        String className = cid.getNameAsString();

        // Verifica se la classe è marcata come associativa tramite l'annotazione
        // @AssociationClass
        boolean isAssociationClass = false;
        for (AnnotationExpr ann : cid.getAnnotations()) {
            if (ann.getNameAsString().equalsIgnoreCase("AssociationClass")) {
                isAssociationClass = true;
                break;
            }
        }

        // Costruisce l'intestazione della classe o interfaccia
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

        // Processa i campi della classe
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

                // Raccoglie eventuali proprietà aggiuntive tramite le annotazioni
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

                // Se la classe non è associativa, processa le associazioni per il campo
                if (!isAssociationClass) {
                    processAssociation(var.getType(), field, className, fieldName, uml, relationships, definedTypes);
                }
            }
        }

        // Processa i costruttori della classe
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

        // Processa i metodi della classe
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

            // Aggiunge le dependency (uses) per i tipi dei parametri, se applicabile
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

        // Se la classe non è associativa, processa ereditarietà e implementazioni
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
            // Per una classe associativa, elabora le associazioni speciali
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
     * Processa un'enumerazione e aggiunge la sua definizione al diagramma UML.
     *
     * @param enumDecl L'enumerazione da processare.
     * @param uml      Il StringBuilder in cui aggiungere la definizione dell'enum.
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
     * Processa un campo per determinare se genera un'associazione (se il tipo
     * non è primitivo).
     * Questo metodo viene utilizzato per le classi NON associative.
     *
     * @param fieldType     Il tipo del campo.
     * @param field         La dichiarazione del campo.
     * @param className     Il nome della classe che contiene il campo.
     * @param fieldName     Il nome del campo.
     * @param uml           Il StringBuilder in cui aggiungere la definizione della
     *                      classe.
     * @param relationships Il StringBuilder in cui aggiungere le relazioni.
     * @param definedTypes  L'insieme dei nomi dei tipi definiti.
     */
    static void processAssociation(Type fieldType, FieldDeclaration field, String className, String fieldName,
            StringBuilder uml, StringBuilder relationships, Set<String> definedTypes) {
        String associatedType = null;
        boolean isCollection = false;
        // Se il campo è di tipo classico o interfaccia, verificare se si tratta di una
        // collezione
        if (fieldType.isClassOrInterfaceType()) {
            ClassOrInterfaceType cit = fieldType.asClassOrInterfaceType();
            String typeName = cit.getNameAsString();
            if (UMLUtils.isCollectionType(typeName)) {
                isCollection = true;
                // Se sono presenti argomenti di tipo, prendi il primo argomento
                if (cit.getTypeArguments().isPresent() && !cit.getTypeArguments().get().isEmpty()) {
                    Type argType = cit.getTypeArguments().get().get(0);
                    associatedType = UMLUtils.getTypeString(argType);
                }
            }
        }
        // Se non è una collezione, usa il tipo diretto
        if (associatedType == null) {
            String fieldTypeStr = UMLUtils.getTypeString(fieldType);
            if (!UMLUtils.isPrimitiveOrJavaType(fieldTypeStr) && Character.isUpperCase(fieldTypeStr.charAt(0))) {
                associatedType = fieldTypeStr;
            }
        }
        // Se è stato determinato un tipo associato, costruisci la relazione
        if (associatedType != null) {
            String arrow = " --> ";
            // Modifica la freccia in base alle annotazioni "composition" o "aggregation"
            if (UMLUtils.hasAnnotation(field, "composition")) {
                arrow = " *-->";
            } else if (UMLUtils.hasAnnotation(field, "aggregation")) {
                arrow = " o-->";
            }
            // Imposta la molteplicità: se è una collezione, usa "0..*", altrimenti "1"
            String multiplicity = isCollection ? "\"0..*\"" : "\"1\"";
            relationships.append(className)
                    .append(" \"1\"").append(arrow)
                    .append(multiplicity).append(" ").append(associatedType)
                    .append(" : ").append(fieldName).append("\n");
        }
    }

    /**
     * Restituisce una lista di tutti i file Java (con estensione .java) presenti
     * in una directory, effettuando una ricerca ricorsiva.
     *
     * @param dir La directory in cui cercare i file Java.
     * @return Una lista di file Java.
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
