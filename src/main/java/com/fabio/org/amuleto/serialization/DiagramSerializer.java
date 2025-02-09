package com.fabio.org.amuleto.serialization;

import com.fabio.org.amuleto.model.UMLDiagram;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe responsabile della serializzazione e deserializzazione del modello UMLDiagram. Utilizza
 * Gson per convertire l'oggetto UMLDiagram in formato JSON e viceversa.
 */
public class DiagramSerializer {
  // Istanza di Gson configurata per la stampa in formato leggibile (pretty
  // printing)
  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  /**
   * Serializza il modello UMLDiagram e lo salva in un file JSON.
   *
   * @param filePath Il percorso del file in cui salvare il modello.
   * @param diagram L'istanza di UMLDiagram da serializzare.
   * @throws IOException Se si verifica un errore durante la scrittura del file.
   */
  public static void saveDiagram(String filePath, UMLDiagram diagram) throws IOException {
    try (FileWriter writer = new FileWriter(filePath)) {
      // Converte l'oggetto diagram in una stringa JSON e la scrive sul file
      gson.toJson(diagram, writer);
    }
  }

  /**
   * Deserializza il contenuto di un file JSON per ricostruire un'istanza di UMLDiagram.
   *
   * @param filePath Il percorso del file contenente il modello JSON.
   * @return L'istanza di UMLDiagram ricostruita.
   * @throws IOException Se si verifica un errore durante la lettura del file.
   */
  public static UMLDiagram loadDiagram(String filePath) throws IOException {
    try (FileReader reader = new FileReader(filePath)) {
      // Converte il contenuto JSON del file in un'istanza di UMLDiagram
      return gson.fromJson(reader, UMLDiagram.class);
    }
  }
}
