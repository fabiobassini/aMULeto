package com.fabio.org.amuleto.view.dialogs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.stream.Collectors;
import javax.swing.*;

import com.fabio.org.amuleto.view.shapes.UMLClassShape;

public class UMLClassEditorDialog extends JDialog {
  private UMLClassShape umlClass;
  private JTextField nameField;
  private JTextArea attributesArea;
  private JTextArea constructorsArea;
  private JTextArea methodsArea;

  public UMLClassEditorDialog(Component parent, UMLClassShape umlClass) {
    super(
        SwingUtilities.getWindowAncestor(parent),
        "Modifica Classe: " + umlClass.getClassName(),
        ModalityType.APPLICATION_MODAL);
    this.umlClass = umlClass;
    initComponents();
    pack();
    setLocationRelativeTo(parent);
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    JPanel formPanel = new JPanel(new GridLayout(4, 1));

    nameField = new JTextField(umlClass.getClassName());
    formPanel.add(labeledPanel("Nome:", nameField));

    attributesArea = new JTextArea(5, 20);
    attributesArea.setText(
        umlClass.getAttributes().stream().map(Object::toString).collect(Collectors.joining("\n")));
    formPanel.add(labeledPanel("Attributi (uno per riga):", new JScrollPane(attributesArea)));

    constructorsArea = new JTextArea(5, 20);
    constructorsArea.setText(
        umlClass.getConstructors().stream()
            .map(Object::toString)
            .collect(Collectors.joining("\n")));
    formPanel.add(labeledPanel("Costruttori (uno per riga):", new JScrollPane(constructorsArea)));

    methodsArea = new JTextArea(5, 20);
    methodsArea.setText(
        umlClass.getMethods().stream().map(Object::toString).collect(Collectors.joining("\n")));
    formPanel.add(labeledPanel("Metodi (uno per riga):", new JScrollPane(methodsArea)));

    add(formPanel, BorderLayout.CENTER);

    JPanel buttonsPanel = new JPanel();
    JButton okButton = new JButton("OK");
    okButton.addActionListener(
        (ActionEvent e) -> {
          updateUMLClass();
          getOwner().repaint();
          dispose();
        });
    buttonsPanel.add(okButton);
    JButton cancelButton = new JButton("Annulla");
    cancelButton.addActionListener(e -> dispose());
    buttonsPanel.add(cancelButton);

    add(buttonsPanel, BorderLayout.SOUTH);
  }

  private void updateUMLClass() {
    umlClass.setClassName(nameField.getText().trim());
    umlClass.getAttributes().clear();
    for (String line : attributesArea.getText().split("\\n")) {
      line = line.trim();
      if (!line.isEmpty()) {
        int colonIndex = line.indexOf(":");
        if (colonIndex > 0) {
          String beforeColon = line.substring(0, colonIndex).trim();
          String afterColon = line.substring(colonIndex + 1).trim();
          String[] parts = beforeColon.split("\\s+");
          if (parts.length >= 2) {
            String vis = parts[0];
            String name = parts[1];
            String type = afterColon;
            umlClass.addAttribute(new com.fabio.org.amuleto.model.UMLAttribute(vis, type, name));
          }
        }
      }
    }
    umlClass.getConstructors().clear();
    for (String line : constructorsArea.getText().split("\\n")) {
      line = line.trim();
      if (!line.isEmpty()) {
        String[] parts = line.split("\\s+");
        if (parts.length >= 2) {
          umlClass.addConstructor(
              new com.fabio.org.amuleto.model.UMLMethod(parts[0], "", parts[1], ""));
        }
      }
    }
    umlClass.getMethods().clear();
    for (String line : methodsArea.getText().split("\\n")) {
      line = line.trim();
      if (!line.isEmpty()) {
        int colonIndex = line.indexOf(":");
        if (colonIndex > 0) {
          String beforeColon = line.substring(0, colonIndex).trim();
          String afterColon = line.substring(colonIndex + 1).trim();
          String[] parts = beforeColon.split("\\s+");
          if (parts.length >= 2) {
            String vis = parts[0];
            String nameAndParams = beforeColon.substring(vis.length()).trim();
            umlClass.addMethod(
                new com.fabio.org.amuleto.model.UMLMethod(vis, afterColon, nameAndParams, ""));
          }
        }
      }
    }
  }

  private JPanel labeledPanel(String labelText, Component comp) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JLabel(labelText), BorderLayout.NORTH);
    panel.add(comp, BorderLayout.CENTER);
    return panel;
  }
}
