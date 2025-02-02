package com.fabio.org.amuleto;

import java.io.File;

import com.fabio.org.amuleto.converter.UMLGenerator;
import com.fabio.org.amuleto.view.GraphicalEditorUI;
import net.sourceforge.plantuml.FileFormat;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            // Nessun argomento: mostro l’help
            System.out.println("Utilizzo:");
            System.out.println("  Per avviare l'interfaccia grafica: java -jar app.jar gui");
            System.out.println("  Per generare un diagramma UML a partire dal codice Java:");
            System.out.println("       java -jar app.jar <percorso_progetto> [percorso_output]");
            System.exit(1);
        } else if (args[0].equalsIgnoreCase("gui")) {
            // Avvio modalità GUI
            SwingUtilities.invokeLater(() -> {
                GraphicalEditorUI editor = new GraphicalEditorUI();
                editor.setVisible(true);
            });
        } else {
            // Modalità command line: forward engineering (Java code -> UML)
            String sourceDirPath = args[0];
            String outputFilePath = (args.length >= 2) ? args[1] : "diagramma.puml";

            File sourceDir = new File(sourceDirPath);
            if (!sourceDir.exists() || !sourceDir.isDirectory()) {
                System.err.println("Errore: " + sourceDirPath + " non esiste o non è una directory.");
                System.exit(1);
            }

            File outputSVG = new File("diagramma.svg");

            UMLGenerator.generateVectorFromDirectory(sourceDir, outputSVG, FileFormat.SVG);
            System.out.println("Diagramma UML generato in formato SVG: " + outputSVG.getAbsolutePath());
        }
    }
}
