package com.fabio.org.test;

import java.util.ArrayList;
import java.util.List;

public class Dipartimento {
    private String nomeDipartimento;
    @Aggregation
    private List<Corso> corsi;

    public Dipartimento(String nomeDipartimento) {
        this.nomeDipartimento = nomeDipartimento;
        this.corsi = new ArrayList<>();
    }

    public void aggiungiCorso(Corso corso) {
        corsi.add(corso);
    }

    public List<Corso> getCorsi() {
        return corsi;
    }
}
