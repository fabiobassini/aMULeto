package com.fabio.org.amuleto.view.shapes;

import java.awt.*;

public abstract class UMLShape {
  protected int x, y;
  protected int width = 180;
  protected Rectangle bounds;

  public UMLShape(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  /**
   * Calcola l'altezza necessaria per disegnare la forma.
   *
   * @param g Il contesto grafico utilizzato per il calcolo.
   * @return L'altezza della forma.
   */
  protected abstract int computeHeight(Graphics g);

  /**
   * Restituisce il rettangolo dei confini della forma.
   *
   * @param g Il contesto grafico.
   * @return Il rettangolo contenente i confini della forma.
   */
  public Rectangle getBounds(Graphics g) {
    int height = computeHeight(g);
    bounds = new Rectangle(x, y, width, height);
    return bounds;
  }

  public boolean contains(Point p, Graphics g) {
    return getBounds(g).contains(p);
  }
}
