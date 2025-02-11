package com.fabio.org.amuleto.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import com.fabio.org.amuleto.model.UMLConnection;
import com.fabio.org.amuleto.view.shapes.UMLClassShape;

public class UMLCanvas extends JPanel {
  // Lista delle forme UML (classi, interfacce, ecc.)
  private List<UMLClassShape> shapes;
  // Lista delle connessioni tra le forme
  private List<UMLConnection> connections;
  // Fattore di zoom attuale (1.0 = 100%)
  private double zoomFactor = 1.0;

  // Riferimento al controller degli eventi del mouse
  private UMLCanvasMouseController mouseController;

  public UMLCanvas() {
    shapes = new ArrayList<>();
    connections = new ArrayList<>();
    // Imposta lo sfondo in dark-theme
    setBackground(new Color(45, 45, 45));
    // Inizializza il controller degli eventi del mouse e registralo
    // Crea e registra il controller del mouse
    mouseController = new UMLCanvasMouseController(this);
    addMouseListener(mouseController);
    addMouseMotionListener(mouseController);
    addMouseWheelListener(mouseController);
  }

  // Metodi per accedere e modificare le forme e le connessioni
  public List<UMLClassShape> getShapes() {
    return shapes;
  }

  public List<UMLConnection> getConnections() {
    return connections;
  }

  public void setShapes(List<UMLClassShape> shapes) {
    this.shapes = shapes;
    repaint();
  }

  public void setConnections(List<UMLConnection> connections) {
    this.connections = connections;
    repaint();
  }

  public void addShape(UMLClassShape shape) {
    shapes.add(shape);
    repaint();
  }

  public void addConnection(UMLConnection conn) {
    connections.add(conn);
    repaint();
  }

  public void setZoomFactor(double factor) {
    this.zoomFactor = factor;
    repaint();
  }

  public double getZoomFactor() {
    return zoomFactor;
  }

  /**
   * Esegue un layout dinamico delle forme sul canvas. Le forme vengono disposte in una griglia
   * calcolata in base al numero totale di forme.
   */
  public void doDynamicLayout() {
    int cols = (int) Math.ceil(Math.sqrt(shapes.size()));
    int spacing = 50;
    int cellWidth = 250, cellHeight = 200;
    int i = 0;
    for (UMLClassShape shape : shapes) {
      int row = i / cols;
      int col = i % cols;
      shape.setLocation(
          spacing + col * (cellWidth + spacing), spacing + row * (cellHeight + spacing));
      i++;
    }
    repaint();
  }

  // Metodi delegati per la gestione delle relazioni
  public void setRelationshipMode(boolean mode) {
    mouseController.setRelationshipMode(mode);
  }

  public void setCurrentRelationshipType(RelationshipType type) {
    mouseController.setCurrentRelationshipType(type);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    g2.scale(zoomFactor, zoomFactor);

    // Disegna le connessioni (linee) tra le forme
    for (UMLConnection conn : connections) {
      conn.draw(g2);
    }
    // Disegna le forme UML
    for (UMLClassShape shape : shapes) {
      shape.draw(g2);
    }
    g2.dispose();
  }

  /**
   * Traduce le coordinate del punto in base al fattore di zoom corrente.
   *
   * @param p Il punto originale.
   * @return Il punto tradotto in base allo zoom.
   */
  public Point translatePoint(Point p) {
    return new Point((int) (p.x / zoomFactor), (int) (p.y / zoomFactor));
  }
}
