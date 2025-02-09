package com.fabio.org.amuleto.controller;

import com.fabio.org.amuleto.model.UMLDiagram;
import com.fabio.org.amuleto.view.UMLCanvas;

public class UMLController {
  private UMLDiagram diagram;
  private UMLCanvas canvas;

  public UMLController(UMLDiagram diagram, UMLCanvas canvas) {
    this.diagram = diagram;
    this.canvas = canvas;
  }

  // Aggiorna il modello a partire dal contenuto del canvas
  public void updateModelFromCanvas() {
    diagram.setShapes(canvas.getShapes());
    diagram.setConnections(canvas.getConnections());
  }

  // Aggiorna il canvas a partire dal modello
  public void updateCanvasFromModel() {
    canvas.setShapes(diagram.getShapes());
    canvas.setConnections(diagram.getConnections());
    canvas.repaint();
  }
}
