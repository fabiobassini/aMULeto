package com.fabio.org.amuleto.model;

import com.fabio.org.amuleto.view.UMLClassShape;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il modello del diagramma UML. Contiene le collezioni di forme UML (classi,
 * interfacce, ecc.) e le connessioni che intercorrono tra di esse.
 */
public class UMLDiagram {
  // Lista delle forme UML presenti nel diagramma.
  private List<UMLClassShape> shapes;

  // Lista delle connessioni (relazioni) tra le forme UML.
  private List<UMLConnection> connections;

  /** Costruttore predefinito che inizializza le liste delle forme e delle connessioni. */
  public UMLDiagram() {
    shapes = new ArrayList<>();
    connections = new ArrayList<>();
  }

  /**
   * Restituisce la lista delle forme UML.
   *
   * @return La lista delle forme UML.
   */
  public List<UMLClassShape> getShapes() {
    return shapes;
  }

  /**
   * Imposta la lista delle forme UML.
   *
   * @param shapes La lista delle forme UML da impostare.
   */
  public void setShapes(List<UMLClassShape> shapes) {
    this.shapes = shapes;
  }

  /**
   * Restituisce la lista delle connessioni tra le forme UML.
   *
   * @return La lista delle connessioni.
   */
  public List<UMLConnection> getConnections() {
    return connections;
  }

  /**
   * Imposta la lista delle connessioni.
   *
   * @param connections La lista delle connessioni da impostare.
   */
  public void setConnections(List<UMLConnection> connections) {
    this.connections = connections;
  }
}
