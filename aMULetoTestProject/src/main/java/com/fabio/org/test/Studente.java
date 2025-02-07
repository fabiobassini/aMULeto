package com.fabio.org.test;

import java.util.ArrayList;
import java.util.List;

public class Studente extends Persona {
    int matricola;
    @Navigable
    private List<Iscrizione> iscrizioni;

    public Studente(String nome, String cognome, int matricola) {
        super(nome, cognome);
        this.matricola = matricola;
        this.iscrizioni = new ArrayList<>();
    }

    @Override
    public String getIdentificativo() {
        return "STU-" + matricola;
    }

    public void aggiungiIscrizione(Iscrizione iscrizione) {
        iscrizioni.add(iscrizione);
    }

    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    public double calcolaMedia() {
        double somma = 0;
        int count = 0;
        for (Iscrizione i : iscrizioni) {
            if (i.getVoto() >= 0) {
                somma += i.getVoto();
                count++;
            }
        }
        return count > 0 ? somma / count : 0;
    }
}
