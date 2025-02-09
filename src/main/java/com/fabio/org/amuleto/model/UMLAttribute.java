package com.fabio.org.amuleto.model;

/**
 * Rappresenta un attributo di una classe in un diagramma UML. Un attributo è definito dalla sua
 * visibilità, dal tipo e dal nome.
 */
public class UMLAttribute {
  // Visibilità dell'attributo (ad es. "+", "-", "#", "~")
  private String visibility;
  // Tipo dell'attributo (ad esempio "int", "String", ecc.)
  private String type;
  // Nome dell'attributo
  private String name;

  /**
   * Costruttore per inizializzare un attributo UML con visibilità, tipo e nome.
   *
   * @param visibility La visibilità dell'attributo.
   * @param type Il tipo dell'attributo.
   * @param name Il nome dell'attributo.
   */
  public UMLAttribute(String visibility, String type, String name) {
    this.visibility = visibility;
    this.type = type;
    this.name = name;
  }

  /**
   * Restituisce la visibilità dell'attributo.
   *
   * @return La visibilità come stringa.
   */
  public String getVisibility() {
    return visibility;
  }

  /**
   * Restituisce il tipo dell'attributo.
   *
   * @return Il tipo dell'attributo.
   */
  public String getType() {
    return type;
  }

  /**
   * Restituisce il nome dell'attributo.
   *
   * @return Il nome dell'attributo.
   */
  public String getName() {
    return name;
  }

  /**
   * Restituisce una rappresentazione testuale dell'attributo in stile UML. Il formato è "visibilità
   * nome : tipo".
   *
   * @return La stringa formattata dell'attributo.
   */
  @Override
  public String toString() {
    return visibility + " " + name + " : " + type;
  }
}
