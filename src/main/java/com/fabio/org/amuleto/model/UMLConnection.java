package com.fabio.org.amuleto.model;

import java.awt.*;

import com.fabio.org.amuleto.view.RelationshipType;
import com.fabio.org.amuleto.view.UMLClassShape;

/**
 * Rappresenta una connessione (relazione) tra due forme UML. Contiene informazioni sulla forma
 * sorgente, forma di destinazione, tipo di relazione e le molteplicità associate a entrambe le
 * estremità della connessione.
 */
public class UMLConnection {
  // Forma UML sorgente della connessione
  private UMLClassShape source;
  // Forma UML di destinazione della connessione
  private UMLClassShape target;
  // Tipo di relazione (es. ASSOCIATION, INHERITANCE, ecc.)
  private RelationshipType type;
  // Molteplicità associata alla forma sorgente (default "1")
  private String sourceMultiplicity = "1";
  // Molteplicità associata alla forma di destinazione (default "1")
  private String targetMultiplicity = "1";

  /**
   * Costruttore per creare una nuova connessione.
   *
   * @param source La forma sorgente della connessione.
   * @param target La forma di destinazione della connessione.
   * @param type Il tipo di relazione.
   */
  public UMLConnection(UMLClassShape source, UMLClassShape target, RelationshipType type) {
    this.source = source;
    this.target = target;
    this.type = type;
  }

  /**
   * Restituisce la forma sorgente della connessione.
   *
   * @return La forma sorgente.
   */
  public UMLClassShape getSource() {
    return source;
  }

  /**
   * Restituisce la forma di destinazione della connessione.
   *
   * @return La forma di destinazione.
   */
  public UMLClassShape getTarget() {
    return target;
  }

  /**
   * Restituisce il tipo di relazione della connessione.
   *
   * @return Il tipo di relazione.
   */
  public RelationshipType getType() {
    return type;
  }

  /**
   * Esegue il rendering grafico della connessione sul contesto grafico fornito. Calcola i centri
   * delle forme sorgente e di destinazione e disegna una linea che le collega. Se la relazione
   * prevede molteplicità, queste vengono disegnate vicino ai centri delle forme. In base al tipo di
   * relazione, vengono disegnati anche simboli specifici (ad esempio, freccia per l'ereditarietà o
   * diamante per l'aggregazione).
   *
   * @param g Il contesto grafico (Graphics2D) su cui disegnare la connessione.
   */
  public void draw(Graphics2D g) {
    // Calcola i confini delle forme sorgente e di destinazione
    Rectangle srcBounds = source.getBounds(g);
    Rectangle tgtBounds = target.getBounds(g);
    // Calcola il centro della forma sorgente
    int x1 = srcBounds.x + srcBounds.width / 2;
    int y1 = srcBounds.y + srcBounds.height / 2;
    // Calcola il centro della forma di destinazione
    int x2 = tgtBounds.x + tgtBounds.width / 2;
    int y2 = tgtBounds.y + tgtBounds.height / 2;

    // Salva lo stroke originale
    Stroke originalStroke = g.getStroke();
    // Imposta uno stroke tratteggiato per i tipi di relazione DEPENDENCY e
    // IMPLEMENTATION
    if (type == RelationshipType.DEPENDENCY || type == RelationshipType.IMPLEMENTATION) {
      g.setStroke(
          new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {5}, 0));
    } else {
      g.setStroke(new BasicStroke(2));
    }

    // Disegna la linea che collega i centri delle due forme
    g.setColor(Color.BLACK);
    g.drawLine(x1, y1, x2, y2);

    // Se il tipo di relazione è ASSOCIATION, AGGREGATION o COMPOSITION,
    // disegna le molteplicità vicino ai centri delle forme
    if (type == RelationshipType.ASSOCIATION
        || type == RelationshipType.AGGREGATION
        || type == RelationshipType.COMPOSITION) {
      g.setFont(new Font("SansSerif", Font.PLAIN, 12));
      g.drawString(sourceMultiplicity, x1 - 20, y1 - 5);
      g.drawString(targetMultiplicity, x2 + 5, y2 - 5);
    }

    // In base al tipo di relazione, disegna il simbolo specifico
    switch (type) {
      case INHERITANCE:
      case IMPLEMENTATION:
        drawInheritanceArrow(g, x1, y1, x2, y2);
        break;
      case AGGREGATION:
        drawAggregationDiamond(g, x1, y1, x2, y2, false);
        break;
      case COMPOSITION:
        drawAggregationDiamond(g, x1, y1, x2, y2, true);
        break;
      default:
        break;
    }
    // Ripristina lo stroke originale
    g.setStroke(originalStroke);
  }

  /**
   * Disegna la freccia di ereditarietà (o implementazione) in prossimità del punto di destinazione
   * della connessione.
   *
   * @param g Il contesto grafico.
   * @param x1 La coordinata X del punto di partenza.
   * @param y1 La coordinata Y del punto di partenza.
   * @param x2 La coordinata X del punto di arrivo.
   * @param y2 La coordinata Y del punto di arrivo.
   */
  private void drawInheritanceArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
    double phi = Math.toRadians(20); // Angolo di deviazione per le barre laterali della freccia
    int barb = 15; // Lunghezza delle barre laterali della freccia
    double theta = Math.atan2(y2 - y1, x2 - x1);
    // Calcola i punti per le barre laterali della freccia
    int x3 = (int) (x2 - barb * Math.cos(theta + phi));
    int y3 = (int) (y2 - barb * Math.sin(theta + phi));
    int x4 = (int) (x2 - barb * Math.cos(theta - phi));
    int y4 = (int) (y2 - barb * Math.sin(theta - phi));
    // Crea il poligono per la testa della freccia
    Polygon arrowHead = new Polygon();
    arrowHead.addPoint(x2, y2);
    arrowHead.addPoint(x3, y3);
    arrowHead.addPoint(x4, y4);
    // Riempie il poligono di bianco e disegna il bordo in nero
    g.setColor(Color.WHITE);
    g.fillPolygon(arrowHead);
    g.setColor(Color.BLACK);
    g.drawPolygon(arrowHead);
  }

  /**
   * Disegna un diamante per rappresentare l'aggregazione o la composizione.
   *
   * @param g Il contesto grafico.
   * @param x1 La coordinata X del punto di partenza.
   * @param y1 La coordinata Y del punto di partenza.
   * @param x2 La coordinata X del punto di arrivo.
   * @param y2 La coordinata Y del punto di arrivo.
   * @param filled true se il diamante deve essere riempito (composizione), false se deve essere
   *     solo delineato (aggregazione).
   */
  private void drawAggregationDiamond(
      Graphics2D g, int x1, int y1, int x2, int y2, boolean filled) {
    // Calcola l'angolo theta, ossia l'angolo tra la linea che collega il punto di
    // partenza e di arrivo e l'asse X.
    double theta = Math.atan2(y2 - y1, x2 - x1);

    // Imposta la dimensione di base del diamante.
    int diamondSize = 20; // Dimensione del diamante

    // Imposta il punto A come il punto di partenza, che verrà utilizzato come
    // riferimento per il diamante.
    int xA = x1, yA = y1;

    // Calcola le coordinate del vertice B del diamante.
    // Il vertice B si ottiene spostando il punto A di "diamondSize" unità in
    // direzione definita dall'angolo theta + 30° (Math.PI/6)
    int xB = (int) (xA - diamondSize * Math.cos(theta + Math.PI / 6));
    int yB = (int) (yA - diamondSize * Math.sin(theta + Math.PI / 6));

    // Calcola le coordinate del vertice C del diamante.
    // Il vertice C si ottiene spostando il punto A di 2 volte "diamondSize" unità
    // lungo la direzione theta
    int xC = (int) (xA - 2 * diamondSize * Math.cos(theta));
    int yC = (int) (yA - 2 * diamondSize * Math.sin(theta));

    // Calcola le coordinate del vertice D del diamante.
    // Il vertice D si ottiene spostando il punto A di "diamondSize" unità in
    // direzione definita dall'angolo theta - 30° (Math.PI/6)
    int xD = (int) (xA - diamondSize * Math.cos(theta - Math.PI / 6));
    int yD = (int) (yA - diamondSize * Math.sin(theta - Math.PI / 6));

    // A questo punto, i quattro punti (A, B, C, D) definiscono il diamante che
    // verrà disegnato.
    // Il diamante viene poi riempito o semplicemente delineato in base al parametro
    // "filled".
    // Crea il poligono per il diamante
    Polygon diamond = new Polygon();
    diamond.addPoint(xA, yA);
    diamond.addPoint(xB, yB);
    diamond.addPoint(xC, yC);
    diamond.addPoint(xD, yD);
    // Riempie o disegna il diamante in base al parametro 'filled'
    if (filled) {
      g.setColor(Color.BLACK);
      g.fillPolygon(diamond);
    } else {
      g.setColor(Color.WHITE);
      g.fillPolygon(diamond);
      g.setColor(Color.BLACK);
      g.drawPolygon(diamond);
    }
  }
}
