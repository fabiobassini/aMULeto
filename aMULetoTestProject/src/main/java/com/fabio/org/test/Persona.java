package com.fabio.org.test;

public abstract class Persona {
    @Readonly
    private String nome;
    @Readonly
    private String cognome;

    public Persona(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public abstract String getIdentificativo();
}
