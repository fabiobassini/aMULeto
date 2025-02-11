package com.fabio.org.amuleto.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.fabio.org.amuleto.model.UMLAttribute;
import com.fabio.org.amuleto.model.UMLMethod;
import com.fabio.org.amuleto.view.shapes.UMLClassShape;

public class SidebarPanel extends JPanel {

  private UMLCanvas canvas;

  public SidebarPanel(UMLCanvas canvas) {
    this.canvas = canvas;
    initComponents();
  }

  private void initComponents() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setBackground(new Color(45, 45, 45));

    // --- Sezione: Aggiungi Elementi ---
    JPanel addElementsPanel = new JPanel();
    addElementsPanel.setLayout(new BoxLayout(addElementsPanel, BoxLayout.Y_AXIS));
    addElementsPanel.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70)), "Aggiungi Elementi"));
    addElementsPanel.setBackground(new Color(45, 45, 45));

    JButton addClassBtn = new JButton("Aggiungi Classe");
    addClassBtn.addActionListener(
        (ActionEvent e) -> {
          String className = JOptionPane.showInputDialog(this, "Nome della Classe:");
          if (className != null && !className.trim().isEmpty()) {
            UMLClassShape shape = new UMLClassShape(className.trim(), 50, 50);
            shape.addAttribute(new UMLAttribute("+", "int", "id"));
            shape.addConstructor(new UMLMethod("+", "", className.trim(), ""));
            shape.addMethod(new UMLMethod("+", "void", "metodo", ""));
            canvas.addShape(shape);
          }
        });
    addClassBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    addElementsPanel.add(addClassBtn);
    addElementsPanel.add(Box.createVerticalStrut(10));

    JButton addInterfaceBtn = new JButton("Aggiungi Interfaccia");
    addInterfaceBtn.addActionListener(
        (ActionEvent e) -> {
          String interfaceName = JOptionPane.showInputDialog(this, "Nome dell'Interfaccia:");
          if (interfaceName != null && !interfaceName.trim().isEmpty()) {
            UMLClassShape shape = new UMLClassShape(interfaceName.trim(), 100, 100);
            canvas.addShape(shape);
          }
        });
    addInterfaceBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    addElementsPanel.add(addInterfaceBtn);
    addElementsPanel.add(Box.createVerticalStrut(10));

    JButton addAssocClassBtn = new JButton("Aggiungi Classe Associativa");
    addAssocClassBtn.addActionListener(
        (ActionEvent e) -> {
          String assocName = JOptionPane.showInputDialog(this, "Nome della Classe Associativa:");
          if (assocName != null && !assocName.trim().isEmpty()) {
            UMLClassShape shape = new UMLClassShape(assocName.trim(), 150, 150);
            shape.setAssociationClass(true);
            canvas.addShape(shape);
          }
        });
    addAssocClassBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    addElementsPanel.add(addAssocClassBtn);
    addElementsPanel.add(Box.createVerticalStrut(10));

    add(addElementsPanel);
    add(Box.createVerticalStrut(15));

    // --- Sezione: Relazioni ---
    JPanel relationsPanel = new JPanel();
    relationsPanel.setLayout(new BoxLayout(relationsPanel, BoxLayout.Y_AXIS));
    relationsPanel.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70)), "Relazioni"));
    relationsPanel.setBackground(new Color(45, 45, 45));

    JToggleButton relModeToggle = new JToggleButton("Modalità Relazione");
    relModeToggle.addActionListener(e -> canvas.setRelationshipMode(relModeToggle.isSelected()));
    relModeToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
    relationsPanel.add(relModeToggle);
    relationsPanel.add(Box.createVerticalStrut(10));

    JPanel relTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    relTypePanel.setBackground(new Color(45, 45, 45));
    JLabel relTypeLabel = new JLabel("Tipo Relazione:");
    relTypeLabel.setForeground(Color.WHITE);
    relTypePanel.add(relTypeLabel);
    JComboBox<RelationshipType> relTypeCombo = new JComboBox<>(RelationshipType.values());
    relTypeCombo.addActionListener(
        e -> canvas.setCurrentRelationshipType((RelationshipType) relTypeCombo.getSelectedItem()));
    relTypePanel.add(relTypeCombo);
    relationsPanel.add(relTypePanel);

    add(relationsPanel);
    add(Box.createVerticalStrut(15));

    // --- Sezione: Zoom & Layout ---
    JPanel zoomLayoutPanel = new JPanel();
    zoomLayoutPanel.setLayout(new BoxLayout(zoomLayoutPanel, BoxLayout.Y_AXIS));
    zoomLayoutPanel.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70)), "Zoom & Layout"));
    zoomLayoutPanel.setBackground(new Color(45, 45, 45));

    JLabel zoomLabel = new JLabel("Zoom:");
    zoomLabel.setForeground(Color.WHITE);
    zoomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    zoomLayoutPanel.add(zoomLabel);
    zoomLayoutPanel.add(Box.createVerticalStrut(5));

    JSlider zoomSlider = new JSlider(10, 200, 100);
    zoomSlider.setMajorTickSpacing(50);
    zoomSlider.setMinorTickSpacing(10);
    zoomSlider.setPaintTicks(true);
    zoomSlider.setPaintLabels(true);
    zoomSlider.addChangeListener(e -> canvas.setZoomFactor(zoomSlider.getValue() / 100.0));
    zoomSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
    zoomLayoutPanel.add(zoomSlider);
    zoomLayoutPanel.add(Box.createVerticalStrut(10));

    JButton layoutBtn = new JButton("Layout Automatico");
    layoutBtn.addActionListener(e -> canvas.doDynamicLayout());
    layoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    zoomLayoutPanel.add(layoutBtn);

    add(zoomLayoutPanel);
  }
}
