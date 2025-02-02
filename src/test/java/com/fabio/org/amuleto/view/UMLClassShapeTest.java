package com.fabio.org.amuleto.view;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fabio.org.amuleto.model.UMLAttribute;
import com.fabio.org.amuleto.model.UMLMethod;

public class UMLClassShapeTest {

    /**
     * Test per il metodo addAttribute.
     * Verifica che un attributo venga correttamente aggiunto alla classe UML.
     * Dopo l'aggiunta, controlla che la lista degli attributi contenga
     * esattamente l'attributo aggiunto.
     */
    @Test
    public void testAddAttribute() {
        // Crea un'istanza di UMLClassShape e un attributo da aggiungere
        UMLClassShape classShape = new UMLClassShape("TestClass", 10, 20);
        UMLAttribute attr = new UMLAttribute("+", "int", "id");

        // Aggiunge l'attributo alla classe
        classShape.addAttribute(attr);

        // Verifica che la lista degli attributi contenga esattamente 1 attributo
        assertEquals(1, classShape.getAttributes().size());

        // Verifica che l'attributo aggiunto sia quello che ci si aspetta
        assertEquals(attr, classShape.getAttributes().get(0));
    }

    /**
     * Test per il metodo addMethod.
     * Verifica che un metodo venga correttamente aggiunto alla classe UML.
     * Dopo l'aggiunta, controlla che la lista dei metodi contenga
     * esattamente il metodo aggiunto.
     */
    @Test
    public void testAddMethod() {
        // Crea un'istanza di UMLClassShape e un metodo da aggiungere
        UMLClassShape classShape = new UMLClassShape("TestClass", 10, 20);
        UMLMethod method = new UMLMethod("+", "void", "doSomething", "int a, String b");

        // Aggiunge il metodo alla classe
        classShape.addMethod(method);

        // Verifica che la lista dei metodi contenga esattamente 1 metodo
        assertEquals(1, classShape.getMethods().size());

        // Verifica che il metodo aggiunto sia quello che ci si aspetta
        assertEquals(method, classShape.getMethods().get(0));
    }

    /**
     * Test per il metodo getClassName.
     * Verifica che il nome della classe venga restituito correttamente.
     */
    @Test
    public void testGetClassName() {
        // Crea un'istanza di UMLClassShape con un nome specificato
        UMLClassShape classShape = new UMLClassShape("TestClass", 10, 20);

        // Verifica che il nome della classe restituito sia corretto
        assertEquals("TestClass", classShape.getClassName());
    }

    /**
     * Test per il metodo setLocation.
     * Verifica che la posizione della classe venga aggiornata correttamente.
     */
    @Test
    public void testSetLocation() {
        // Crea un'istanza di UMLClassShape
        UMLClassShape classShape = new UMLClassShape("TestClass", 10, 20);

        // Imposta una nuova posizione per la classe
        classShape.setLocation(50, 50);

        // Verifica che le nuove coordinate X e Y siano corrette
        assertEquals(50, classShape.getX());
        assertEquals(50, classShape.getY());
    }
}
