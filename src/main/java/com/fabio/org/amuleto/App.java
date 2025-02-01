/**
 * aMULeto
 *
 * @author
 *   Fabio Bassini
 *
 * @license MIT
 *   Vedere LICENSE
 */

package com.fabio.org.amuleto;

import java.io.File;

import net.sourceforge.plantuml.FileFormat;

public class App {
    public static void main(String[] args) throws Exception {
        // Inizializzazione delle variabili con valori di default
        String sourceDirPath = "";
        String outputFilePath = "";

        // Controllo degli argomenti da linea di comando
        if (args.length == 0) {
            System.out.println("Utilizzo: java com.org.progetto.App <percorso_progetto> [percorso_output]");
            System.out.println("Esempio: java com.org.progetto.App src/main/java/com/org/progetto diagramma.puml");
            System.exit(1);
        } else if (args.length == 1) {
            sourceDirPath = args[0];
            outputFilePath = "diagramma.puml"; // Nome di default per il file di output
        } else {
            sourceDirPath = args[0];
            outputFilePath = args[1];
        }

        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.err.println("Errore: " + sourceDirPath + " non esiste o non è una directory.");
            System.exit(1);
        }

        File outputFile = new File(outputFilePath);
        File outputSVG = new File("diagramma.svg"); // nome del file di output

        // Avvia la generazione del diagramma UML a partire dalla directory specificata
        // UMLGenerator.generateFromDirectory(sourceDir, outputFile);
        UMLGenerator.generateVectorFromDirectory(sourceDir, outputSVG, FileFormat.SVG);

        System.out.println("File UML generato: " + outputFile.getAbsolutePath());
    }
}
