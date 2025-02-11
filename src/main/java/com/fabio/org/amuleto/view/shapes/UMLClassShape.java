package com.fabio.org.amuleto.view.shapes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

import com.fabio.org.amuleto.model.UMLAttribute;
import com.fabio.org.amuleto.model.UMLMethod;

public class UMLClassShape extends UMLShape {
  private String className;
  private List<UMLAttribute> attributes;
  private List<UMLMethod> methods;
  private List<UMLMethod> constructors;
  private boolean associationClass = false;

  public UMLClassShape(String className, int x, int y) {
    super(x, y);
    this.className = className;
    this.attributes = new ArrayList<>();
    this.methods = new ArrayList<>();
    this.constructors = new ArrayList<>();
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  public void setAssociationClass(boolean flag) {
    this.associationClass = flag;
  }

  public boolean isAssociationClass() {
    return associationClass;
  }

  public void addAttribute(UMLAttribute attr) {
    attributes.add(attr);
  }

  public void addMethod(UMLMethod method) {
    methods.add(method);
  }

  public void addConstructor(UMLMethod constructor) {
    constructors.add(constructor);
  }

  public List<UMLAttribute> getAttributes() {
    return attributes;
  }

  public List<UMLMethod> getMethods() {
    return methods;
  }

  public List<UMLMethod> getConstructors() {
    return constructors;
  }

  @Override
  protected int computeHeight(Graphics g) {
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

  /**
   * Esegue il rendering della classe sul contesto grafico. Utilizza colori coerenti con un dark
   * theme.
   *
   * @param g Il contesto grafico.
   */
  public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    int height = computeHeight(g);
    // Colori per il dark theme
    Color backgroundColor = new Color(70, 70, 70); // Sfondo scuro
    Color borderColor = new Color(180, 180, 180); // Bordo chiaro
    Color textColor = Color.WHITE; // Testo bianco

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

    // Separatore
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
   * Apre il dialogo di editing per modificare i dati della classe.
   *
   * @param parent Il componente padre da cui viene invocato il dialogo.
   */
  public void openEditor(Component parent) {
    com.fabio.org.amuleto.view.dialogs.UMLClassEditorDialog dialog =
        new com.fabio.org.amuleto.view.dialogs.UMLClassEditorDialog(parent, this);
    dialog.setVisible(true);
    SwingUtilities.getWindowAncestor(parent).repaint();
  }
}
