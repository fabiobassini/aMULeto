package com.fabio.org.amuleto.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.SwingUtilities;

import com.fabio.org.amuleto.model.UMLConnection;
import com.fabio.org.amuleto.view.shapes.UMLClassShape;

public class UMLCanvasMouseController
    implements MouseListener, MouseMotionListener, MouseWheelListener {

  private UMLCanvas canvas;
  // Variabili per la gestione del drag & drop e della creazione di connessioni
  private UMLClassShape selectedShape = null; // Forma selezionata per il movimento
  private Point lastMousePosition = null; // Ultima posizione del mouse (per calcolare il delta)
  private UMLClassShape connectionSource =
      null; // Forma sorgente per la creazione di una connessione
  private boolean isCreatingConnection = false; // Flag per indicare la creazione di una connessione
  private boolean relationshipMode = false; // Modalità attiva per la creazione di relazioni
  // Tipo corrente di relazione da creare (assumendo che RelationshipType sia
  // definito nel package view)
  private RelationshipType currentRelType = RelationshipType.ASSOCIATION;

  public UMLCanvasMouseController(UMLCanvas canvas) {
    this.canvas = canvas;
  }

  // Metodo per impostare la modalità relazione (accessibile dal pannello
  // laterale)
  public void setRelationshipMode(boolean mode) {
    this.relationshipMode = mode;
  }

  // Metodo per impostare il tipo di relazione corrente
  public void setCurrentRelationshipType(RelationshipType type) {
    this.currentRelType = type;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    Point p = canvas.translatePoint(e.getPoint());
    if (SwingUtilities.isLeftMouseButton(e)) {
      if (relationshipMode) {
        // In modalità relazione, cerca la forma sotto il mouse
        for (UMLClassShape shape : canvas.getShapes()) {
          if (shape.contains(p, canvas.getGraphics())) {
            connectionSource = shape;
            isCreatingConnection = true;
            break;
          }
        }
      } else {
        // In modalità normale, cerca una forma per selezionarla o aprirne l'editor in
        // doppio click
        for (UMLClassShape shape : canvas.getShapes()) {
          if (shape.contains(p, canvas.getGraphics())) {
            selectedShape = shape;
            lastMousePosition = p;
            if (e.getClickCount() == 2) {
              shape.openEditor(canvas);
            }
            break;
          }
        }
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (isCreatingConnection && connectionSource != null) {
      Point p = canvas.translatePoint(e.getPoint());
      for (UMLClassShape shape : canvas.getShapes()) {
        if (shape.contains(p, canvas.getGraphics()) && shape != connectionSource) {
          // Crea una connessione tra la forma sorgente e la forma target
          UMLConnection conn = new UMLConnection(connectionSource, shape, currentRelType);
          canvas.addConnection(conn);
          break;
        }
      }
      isCreatingConnection = false;
      connectionSource = null;
      canvas.repaint();
    }
    selectedShape = null;
    lastMousePosition = null;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    Point p = canvas.translatePoint(e.getPoint());
    if (isCreatingConnection) {
      canvas.repaint();
    } else if (selectedShape != null && lastMousePosition != null) {
      int dx = p.x - lastMousePosition.x;
      int dy = p.y - lastMousePosition.y;
      selectedShape.setLocation(selectedShape.getX() + dx, selectedShape.getY() + dy);
      lastMousePosition = p;
      canvas.repaint();
    }
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    double delta = 0.05 * e.getWheelRotation();
    double newZoom = Math.max(0.1, canvas.getZoomFactor() - delta);
    canvas.setZoomFactor(newZoom);
  }

  // Metodi non utilizzati
  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {}
}
