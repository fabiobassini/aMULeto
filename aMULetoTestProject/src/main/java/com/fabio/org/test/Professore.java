package com.fabio.org.test;

import java.util.ArrayList;
import java.util.List;

public class Professore extends Persona implements Valutabile {
    protected String dipartimento;
    @Aggregation
    private List<Corso> corsi;

    public Professore(String nome, String cognome, String dipartimento) {
        super(nome, cognome);
        this.dipartimento = dipartimento;
        this.corsi = new ArrayList<>();
    }

    @Override
    public String getIdentificativo() {
        return "PROF-" + dipartimento;
    }

    @Override
    public double valuta() {
        return 1.0;
    }

    public void aggiungiCorso(Corso corso) {
        corsi.add(corso);
    }

    public List<Corso> getCorsi() {
        return corsi;
    }
}
