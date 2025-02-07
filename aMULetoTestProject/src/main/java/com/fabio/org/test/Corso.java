package com.fabio.org.test;

import java.util.ArrayList;
import java.util.List;

public class Corso {
    public String nomeCorso;
    @Composition
    private Programma programma;
    private List<Iscrizione> iscrizioni;
    @Composition
    private List<Lezione> lezioni;

    public Corso(String nomeCorso, Programma programma) {
        this.nomeCorso = nomeCorso;
        this.programma = programma;
        this.iscrizioni = new ArrayList<>();
        this.lezioni = new ArrayList<>();
    }

    public Programma getProgramma() {
        return programma;
    }

    public void aggiungiIscrizione(Iscrizione iscrizione) {
        iscrizioni.add(iscrizione);
    }

    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    public void aggiungiLezione(Lezione lezione) {
        lezioni.add(lezione);
    }

    public List<Lezione> getLezioni() {
        return lezioni;
    }
}
