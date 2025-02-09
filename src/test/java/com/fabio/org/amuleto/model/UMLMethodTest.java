package com.fabio.org.amuleto.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UMLMethodTest extends TestCase {

  /**
   * Costruttore per inizializzare il test con il nome fornito.
   *
   * @param name Il nome del test.
   */
  public UMLMethodTest(String name) {
    super(name);
  }

  /**
   * Definisce la suite di test da eseguire. In questo caso, la suite contiene il test per la classe
   * UMLMethod.
   *
   * @return La suite di test.
   */
  public static Test suite() {
    return new TestSuite(UMLMethodTest.class);
  }

  /**
   * Test per la creazione di un metodo UML. Verifica che il costruttore della classe UMLMethod
   * inizializzi correttamente i valori di visibilità, tipo di ritorno, nome e parametri.
   */
  public void testCreateMethod() {
    // Creazione di un metodo UML con visibilità, tipo di ritorno, nome e parametri
    // specificati
    UMLMethod method = new UMLMethod("+", "void", "doSomething", "int a, String b");

    // Verifica che la visibilità sia corretta
    assertEquals("+", method.getVisibility());

    // Verifica che il tipo di ritorno sia corretto
    assertEquals("void", method.getReturnType());

    // Verifica che il nome del metodo sia corretto
    assertEquals("doSomething", method.getName());

    // Verifica che i parametri siano corretti
    assertEquals("int a, String b", method.getParameters());
  }

  /**
   * Test per la conversione in stringa del metodo UML. Verifica che il metodo `toString()`
   * restituisca la rappresentazione corretta del metodo in formato UML.
   */
  public void testMethodToString() {
    // Creazione di un metodo UML
    UMLMethod method = new UMLMethod("+", "void", "doSomething", "int a, String b");

    // Verifica che la rappresentazione in stringa del metodo sia corretta
    assertEquals("+ doSomething(int a, String b) : void", method.toString());
  }
}
