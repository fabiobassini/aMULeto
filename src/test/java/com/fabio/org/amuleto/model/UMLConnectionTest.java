package com.fabio.org.amuleto.model;

import static org.junit.Assert.*;

import com.fabio.org.amuleto.view.RelationshipType;
import com.fabio.org.amuleto.view.UMLClassShape;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.junit.Test;

public class UMLConnectionTest {

  /**
   * Test per la creazione di una connessione UML tra due classi. Verifica che la connessione venga
   * correttamente inizializzata con la classe sorgente, la classe di destinazione e il tipo di
   * relazione specificato.
   */
  @Test
  public void testCreateConnection() {
    // Creazione delle due forme UML (classi)
    UMLClassShape classA = new UMLClassShape("ClassA", 10, 20);
    UMLClassShape classB = new UMLClassShape("ClassB", 100, 200);

    // Creazione della connessione con tipo di relazione "ASSOCIATION"
    UMLConnection connection = new UMLConnection(classA, classB, RelationshipType.ASSOCIATION);

    // Verifica che la connessione sia correttamente associata alle classi
    assertEquals(classA, connection.getSource());
    assertEquals(classB, connection.getTarget());
    assertEquals(RelationshipType.ASSOCIATION, connection.getType());
  }

  /**
   * Test per il metodo di rendering della connessione (draw). Verifica che il metodo draw non
   * generi eccezioni quando viene eseguito su un contesto grafico simulato. Utilizza un'immagine
   * per creare un contesto grafico (Graphics2D).
   */
  @Test
  public void testConnectionDraw() {
    // Creazione di due forme UML (classi) per la connessione
    UMLClassShape classA = new UMLClassShape("ClassA", 10, 20);
    UMLClassShape classB = new UMLClassShape("ClassB", 100, 200);

    // Creazione di una connessione tra le due classi con tipo "ASSOCIATION"
    UMLConnection connection = new UMLConnection(classA, classB, RelationshipType.ASSOCIATION);

    // Creazione di un'immagine per simulare il contesto grafico
    // in alternativa avrei dovuto aggiungere Mockito alle dipendenze per simulare
    // la UI
    BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = img.createGraphics();

    // Esecuzione del metodo draw per disegnare la connessione
    connection.draw(g2);

    // Verifica che il metodo draw non lanci eccezioni durante l'esecuzione
    assertNotNull(g2);

    // Pulizia delle risorse grafiche
    g2.dispose();
  }
}
