package com.fabio.org.amuleto.converter;

import static org.junit.Assert.*;

import java.io.File;

import com.fabio.org.amuleto.loader.ConfigLoader;
import org.junit.Test;

public class UMLGeneratorTest {

  /**
   * Test per la generazione di un diagramma UML in formato PlantUML a partire da una directory di
   * codice sorgente. Verifica che il file di output venga correttamente creato nella directory
   * specificata.
   *
   * @throws Exception Se si verifica un errore durante la generazione del diagramma.
   */
  @Test
  public void testGenerateFromDirectory() throws Exception {
    // Carica la configurazione dal file config.properties
    ConfigLoader configLoader = new ConfigLoader();
    String projectDirectory = configLoader.getProjectDirectory();

    // Imposta la directory di origine contenente i file Java da processare
    File sourceDir = new File(projectDirectory);

    // Imposta il file di output in cui sarà generato il diagramma UML
    File outputFile = new File("output.puml");

    // Chiamata al metodo che genera il diagramma UML dalla directory di codice
    UMLGenerator.generateFromDirectory(sourceDir, outputFile);

    // Verifica che il file di output sia stato correttamente creato
    assertTrue(outputFile.exists());
  }

  /**
   * Test per la generazione di un diagramma UML in formato vettoriale (SVG) a partire da una
   * directory di codice sorgente. Verifica che il file SVG di output venga correttamente creato.
   *
   * @throws Exception Se si verifica un errore durante la generazione del diagramma SVG.
   */
  @Test
  public void testGenerateVectorFromDirectory() throws Exception {
    // Carica la configurazione dal file config.properties
    ConfigLoader configLoader = new ConfigLoader();
    String projectDirectory = configLoader.getProjectDirectory();

    // Imposta la directory di origine contenente i file Java da processare
    File sourceDir = new File(projectDirectory);

    // Imposta il file di output in cui sarà generato il diagramma UML in formato
    // SVG
    File outputFile = new File("output.svg");

    // Chiamata al metodo che genera il diagramma UML in formato vettoriale (SVG)
    UMLGenerator.generateVectorFromDirectory(
        sourceDir, outputFile, net.sourceforge.plantuml.FileFormat.SVG);

    // Verifica che il file SVG di output sia stato correttamente creato
    assertTrue(outputFile.exists());
  }
}
