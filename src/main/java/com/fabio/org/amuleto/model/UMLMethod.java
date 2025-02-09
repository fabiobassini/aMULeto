package com.fabio.org.amuleto.model;

/**
 * Rappresenta un metodo (o costruttore) in un diagramma UML. Contiene informazioni sulla
 * visibilità, sul tipo di ritorno, sul nome e sui parametri del metodo.
 */
public class UMLMethod {
  // Visibilità del metodo (ad esempio "+", "-", "#", "~")
  private String visibility;
  // Tipo di ritorno del metodo (per i costruttori può essere una stringa vuota)
  private String returnType;
  // Nome del metodo
  private String name;
  // Parametri del metodo, rappresentati come una stringa
  private String parameters;

  /**
   * Costruttore che inizializza tutte le proprietà del metodo.
   *
   * @param visibility La visibilità del metodo.
   * @param returnType Il tipo di ritorno del metodo.
   * @param name Il nome del metodo.
   * @param parameters I parametri del metodo, come stringa.
   */
  public UMLMethod(String visibility, String returnType, String name, String parameters) {
    this.visibility = visibility;
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
  }

  /**
   * Restituisce la visibilità del metodo.
   *
   * @return La visibilità.
   */
  public String getVisibility() {
    return visibility;
  }

  /**
   * Restituisce il tipo di ritorno del metodo.
   *
   * @return Il tipo di ritorno.
   */
  public String getReturnType() {
    return returnType;
  }

  /**
   * Restituisce il nome del metodo.
   *
   * @return Il nome.
   */
  public String getName() {
    return name;
  }

  /**
   * Restituisce la stringa dei parametri del metodo.
   *
   * @return I parametri.
   */
  public String getParameters() {
    return parameters;
  }

  /**
   * Converte il metodo in una rappresentazione testuale in stile UML. Se la stringa dei parametri è
   * vuota, non vengono mostrate le parentesi.
   *
   * <p>Esempio di output: "+ metodo(param1, param2) : returnType" oppure "+ costruttore : " (se non
   * vi sono parametri).
   *
   * @return La rappresentazione testuale del metodo.
   */
  @Override
  public String toString() {
    // Se i parametri sono vuoti, non viene mostrata alcuna coppia di parentesi
    String paramsPart = parameters.isEmpty() ? "" : "(" + parameters + ")";
    return visibility + " " + name + paramsPart + " : " + returnType;
  }
}
