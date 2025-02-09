package com.fabio.org.amuleto.view;

import com.fabio.org.amuleto.model.UMLAttribute;
import com.fabio.org.amuleto.model.UMLMethod;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Rappresenta graficamente una classe (o interfaccia) in un diagramma UML. Gestisce la posizione,
 * la dimensione e il rendering degli elementi (nome, attributi, costruttori, metodi) della classe.
 */
public class UMLClassShape {
  private String className;
  private List<UMLAttribute> attributes;
  private List<UMLMethod> methods;
  private List<UMLMethod> constructors;
  private boolean associationClass = false;
  private int x, y;
  private int width = 180;
  private Rectangle bounds;

  /**
   * Costruttore che inizializza il nome della classe e la posizione (x, y). Inizializza anche le
   * liste degli attributi, metodi e costruttori.
   *
   * @param className Il nome della classe.
   * @param x La coordinata X della posizione.
   * @param y La coordinata Y della posizione.
   */
  public UMLClassShape(String className, int x, int y) {
    this.className = className;
    this.x = x;
    this.y = y;
    this.attributes = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.constructors = new ArrayList<>();
  }

  /**
   * Imposta il nome della classe.
   *
   * @param className Il nuovo nome della classe.
   */
  public void setClassName(String className) {
    this.className = className;
  }

  /**
   * Restituisce il nome della classe.
   *
   * @return Il nome della classe.
   */
  public String getClassName() {
    return className;
  }

  /**
   * Imposta se la classe deve essere considerata come classe associativa.
   *
   * @param flag Valore booleano che indica se la classe è associativa.
   */
  public void setAssociationClass(boolean flag) {
    this.associationClass = flag;
  }

  /**
   * Verifica se la classe è contrassegnata come classe associativa.
   *
   * @return true se la classe è associativa, false altrimenti.
   */
  public boolean isAssociationClass() {
    return associationClass;
  }

  /**
   * Aggiunge un attributo alla lista degli attributi.
   *
   * @param attr L'attributo da aggiungere.
   */
  public void addAttribute(UMLAttribute attr) {
    attributes.add(attr);
  }

  /**
   * Aggiunge un metodo alla lista dei metodi.
   *
   * @param method Il metodo da aggiungere.
   */
  public void addMethod(UMLMethod method) {
    methods.add(method);
  }

  /**
   * Aggiunge un costruttore alla lista dei costruttori.
   *
   * @param constructor Il costruttore da aggiungere.
   */
  public void addConstructor(UMLMethod constructor) {
    constructors.add(constructor);
  }

  /**
   * Restituisce la lista degli attributi.
   *
   * @return La lista degli attributi.
   */
  public List<UMLAttribute> getAttributes() {
    return attributes;
  }

  /**
   * Restituisce la lista dei metodi.
   *
   * @return La lista dei metodi.
   */
  public List<UMLMethod> getMethods() {
    return methods;
  }

  /**
   * Restituisce la lista dei costruttori.
   *
   * @return La lista dei costruttori.
   */
  public List<UMLMethod> getConstructors() {
    return constructors;
  }

  /**
   * Imposta la posizione della classe sul canvas.
   *
   * @param x La nuova coordinata X.
   * @param y La nuova coordinata Y.
   */
  public void setLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Restituisce la coordinata X della classe.
   *
   * @return La coordinata X.
   */
  public int getX() {
    return x;
  }

  /**
   * Restituisce la coordinata Y della classe.
   *
   * @return La coordinata Y.
   */
  public int getY() {
    return y;
  }

  /**
   * Restituisce la larghezza della classe.
   *
   * @return La larghezza.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Calcola l'altezza necessaria per disegnare il contenuto della classe, includendo nome,
   * attributi, costruttori e metodi.
   *
   * @param g Il contesto grafico utilizzato per il calcolo.
   * @return L'altezza totale richiesta.
   */
  private int computeHeight(Graphics g) {
    FontMetrics fm = g.getFontMetrics();
    int headerHeight = fm.getHeight() + 10; // Altezza del nome + gap
    int attrHeight = attributes.size() * fm.getHeight() + (attributes.isEmpty() ? 0 : 15);
    int consHeight = constructors.size() * fm.getHeight() + (constructors.isEmpty() ? 0 : 15);
    int methHeight = methods.size() * fm.getHeight() + (methods.isEmpty() ? 0 : 15);
    int total =
        10
            + headerHeight
            + 1
            + attrHeight
            + (attrHeight > 0 ? 1 : 0)
            + consHeight
            + (consHeight > 0 ? 1 : 0)
            + methHeight
            + 10;
    return total;
  }

  public int getComputeHeight(Graphics g) {
    return computeHeight(g);
  }

  /**
   * Restituisce il rettangolo che rappresenta i confini della classe sul canvas.
   *
   * @param g Il contesto grafico.
   * @return Il rettangolo contenente i confini.
   */
  public Rectangle getBounds(Graphics g) {
    int height = computeHeight(g);
    bounds = new Rectangle(x, y, width, height);
    return bounds;
  }

  /**
   * Verifica se il punto specificato è contenuto all'interno dei confini della classe.
   *
   * @param p Il punto da verificare.
   * @param g Il contesto grafico.
   * @return true se il punto è all'interno, false altrimenti.
   */
  public boolean contains(Point p, Graphics g) {
    return getBounds(g).contains(p);
  }

  /**
   * Esegue il rendering della classe sul contesto grafico fornito, disegnando il rettangolo, il
   * nome e le sezioni (attributi, costruttori e metodi) con separatori e spaziature adeguate.
   *
   * @param g Il contesto grafico in cui disegnare.
   */
  public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    int height = computeHeight(g);
    bounds = new Rectangle(x, y, width, height);

    // Scegli colori per il dark theme:
    Color backgroundColor = new Color(70, 70, 70); // Un grigio scuro
    Color borderColor = new Color(180, 180, 180); // Un grigio chiaro per il bordo
    Color textColor = Color.WHITE; // Testo in bianco
    // Disegna il rettangolo con stile diverso se la classe è associativa
    if (associationClass) {
      Stroke original = g2.getStroke();
      g2.setStroke(
          new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {5}, 0));
      g2.setColor(backgroundColor.darker());
      g2.fillRect(x, y, width, height);
      g2.setColor(borderColor);
      g2.drawRect(x, y, width, height);
      g2.setStroke(original);
    } else {
      g2.setColor(backgroundColor);
      g2.fillRect(x, y, width, height);
      g2.setColor(borderColor);
      g2.drawRect(x, y, width, height);
    }

    g2.setColor(textColor);
    FontMetrics fm = g2.getFontMetrics();
    int currentY = y + 10 + fm.getAscent();

    // Disegna il nome della classe centrato
    int textWidth = fm.stringWidth(className);
    int textX = x + (width - textWidth) / 2;
    g2.drawString(className, textX, currentY);

    // Gap dopo il nome e disegno del separatore
    currentY += 10;
    g2.drawLine(x, currentY, x + width, currentY);

    // Sezione Attributi
    currentY += 15;
    for (UMLAttribute attr : attributes) {
      g2.drawString(attr.toString(), x + 5, currentY);
      currentY += fm.getHeight() + 2;
    }
    if (!attributes.isEmpty()) {
      currentY += 5;
      g2.drawLine(x, currentY, x + width, currentY);
    }

    // Sezione Costruttori
    currentY += 15;
    for (UMLMethod cons : constructors) {
      g2.drawString(cons.toString(), x + 5, currentY);
      currentY += fm.getHeight() + 2;
    }
    if (!constructors.isEmpty()) {
      currentY += 5;
      g2.drawLine(x, currentY, x + width, currentY);
    }

    // Sezione Metodi
    currentY += 15;
    for (UMLMethod meth : methods) {
      g2.drawString(meth.toString(), x + 5, currentY);
      currentY += fm.getHeight() + 12;
    }
  }

  /**
   * Apre il dialogo di editing per modificare i dati della classe. Dopo la chiusura del dialogo
   * viene forzato il repaint del componente proprietario per aggiornare la visualizzazione.
   *
   * @param parent Il componente padre da cui è invocato il dialogo.
   */
  public void openEditor(Component parent) {
    UMLClassEditorDialog dialog = new UMLClassEditorDialog(parent, this);
    dialog.setVisible(true);
    SwingUtilities.getWindowAncestor(parent).repaint();
  }
}
