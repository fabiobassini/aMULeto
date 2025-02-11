package com.fabio.org.amuleto.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import com.fabio.org.amuleto.controller.UMLController;
import com.fabio.org.amuleto.converter.UMLGenerator;
import com.fabio.org.amuleto.model.UMLConnection;
import com.fabio.org.amuleto.model.UMLDiagram;
import com.fabio.org.amuleto.serialization.DiagramSerializer;
import com.fabio.org.amuleto.view.shapes.UMLClassShape;
import net.sourceforge.plantuml.FileFormat;

public class GraphicalEditorUI extends JFrame {

  private UMLCanvas canvas; // L'area di disegno per il diagramma UML
  private UMLController controller; // Sincronizzazione tra modello e vista
  private UMLDiagram diagram; // Il modello del diagramma UML

  public GraphicalEditorUI() {
    setTitle("aMULeto GUI");
    setSize(1200, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Imposta lo sfondo generale (il dark theme)
    getContentPane().setBackground(new Color(45, 45, 45));

    diagram = new UMLDiagram();
    canvas = new UMLCanvas();
    controller = new UMLController(diagram, canvas);
    initComponents();
  }

  private void initComponents() {
    // Istanzia la sidebar (definita in un file a parte)
    SidebarPanel sidebar = new SidebarPanel(canvas);
    // Il pannello della sidebar ha già il suo sfondo dark
    JSplitPane splitPane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, new JScrollPane(canvas));
    splitPane.setDividerLocation(250);
    splitPane.setBackground(new Color(45, 45, 45));
    getContentPane().add(splitPane, BorderLayout.CENTER);
    setJMenuBar(createMenuBar());
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground(new Color(60, 60, 60));
    JMenu fileMenu = new JMenu("File");

    // Salvataggio del diagramma in formato JSON
    JMenuItem saveItem = new JMenuItem("Salva Diagramma");
    saveItem.addActionListener(
        (ActionEvent e) -> {
          controller.updateModelFromCanvas();
          JFileChooser chooser = new JFileChooser();
          if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
              DiagramSerializer.saveDiagram(chooser.getSelectedFile().getAbsolutePath(), diagram);
              JOptionPane.showMessageDialog(this, "Diagramma salvato correttamente.");
            } catch (IOException ex) {
              JOptionPane.showMessageDialog(
                  this, "Errore durante il salvataggio: " + ex.getMessage());
            }
          }
        });
    fileMenu.add(saveItem);

    // Caricamento del diagramma da file JSON
    JMenuItem loadItem = new JMenuItem("Carica Diagramma");
    loadItem.addActionListener(
        (ActionEvent e) -> {
          JFileChooser chooser = new JFileChooser();
          if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
              diagram = DiagramSerializer.loadDiagram(chooser.getSelectedFile().getAbsolutePath());
              controller = new UMLController(diagram, canvas);
              controller.updateCanvasFromModel();
            } catch (IOException ex) {
              JOptionPane.showMessageDialog(
                  this, "Errore durante il caricamento: " + ex.getMessage());
            }
          }
        });
    fileMenu.add(loadItem);

    // Genera il codice UML in formato PlantUML (visualizzazione in dialogo)
    JMenuItem genUMLItem = new JMenuItem("Genera Codice UML");
    genUMLItem.addActionListener((ActionEvent e) -> generateUMLCode());
    fileMenu.add(genUMLItem);

    // Genera il diagramma SVG e lo visualizza tramite il visualizzatore SVG
    JMenuItem genSVGItem = new JMenuItem("Genera Diagramma SVG");
    genSVGItem.addActionListener(
        (ActionEvent e) -> {
          JFileChooser chooser = new JFileChooser();
          chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          chooser.setDialogTitle("Seleziona la directory del progetto Java");
          if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File sourceDir = chooser.getSelectedFile();
            File desktop = new File(System.getProperty("user.home"), "Desktop");
            File aMULetoDir = new File(desktop, "aMULeto");
            if (!aMULetoDir.exists()) {
              aMULetoDir.mkdirs();
            }
            File outputSVG = new File(aMULetoDir, "diagramma.svg");
            try {
              UMLGenerator.generateVectorFromDirectory(sourceDir, outputSVG, FileFormat.SVG);
              SVGViewer.displaySVG(outputSVG); // Chiamata al visualizzatore (file separato)
            } catch (Exception ex) {
              JOptionPane.showMessageDialog(
                  this, "Errore durante la generazione del diagramma: " + ex.getMessage());
            }
          }
        });
    fileMenu.add(genSVGItem);

    // Visualizza un file SVG selezionato
    JMenuItem viewSVGItem = new JMenuItem("Visualizza Diagramma SVG");
    viewSVGItem.addActionListener(
        (ActionEvent e) -> {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setDialogTitle("Seleziona file SVG");
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setFileFilter(
              new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                  return f.isDirectory() || f.getName().toLowerCase().endsWith(".svg");
                }

                @Override
                public String getDescription() {
                  return "File SVG (*.svg)";
                }
              });
          if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File svgFile = fileChooser.getSelectedFile();
            SVGViewer.displaySVG(svgFile);
          }
        });
    fileMenu.add(viewSVGItem);
    menuBar.add(fileMenu);
    return menuBar;
  }

  private void generateUMLCode() {
    StringBuilder sb = new StringBuilder();
    sb.append("@startuml\n");
    sb.append("skinparam classAttributeIconSize 0\n\n");
    for (UMLClassShape shape : canvas.getShapes()) {
      if (shape.isAssociationClass()) {
        sb.append("class ").append(shape.getClassName()).append(" <<association>> {\n");
      } else {
        sb.append("class ").append(shape.getClassName()).append(" {\n");
      }
      for (var attr : shape.getAttributes()) {
        sb.append("  ").append(attr.toString()).append("\n");
      }
      for (var cons : shape.getConstructors()) {
        sb.append("  ").append(cons.toString()).append("\n");
      }
      for (var meth : shape.getMethods()) {
        sb.append("  ").append(meth.toString()).append("\n");
      }
      sb.append("}\n");
    }
    for (UMLConnection conn : canvas.getConnections()) {
      sb.append(conn.getSource().getClassName()).append(" ");
      switch (conn.getType()) {
        case INHERITANCE:
          sb.append("<|--");
          break;
        case IMPLEMENTATION:
          sb.append("..|>");
          break;
        case AGGREGATION:
          sb.append("o--");
          break;
        case COMPOSITION:
          sb.append("*--");
          break;
        case DEPENDENCY:
          sb.append("..>");
          break;
        default:
          sb.append("--");
          break;
      }
      sb.append(" ").append(conn.getTarget().getClassName()).append("\n");
    }
    sb.append("@enduml");

    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(600, 400));
    JOptionPane.showMessageDialog(
        this, scrollPane, "Codice UML Generato", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          GraphicalEditorUI editor = new GraphicalEditorUI();
          editor.setVisible(true);
        });
  }
}
