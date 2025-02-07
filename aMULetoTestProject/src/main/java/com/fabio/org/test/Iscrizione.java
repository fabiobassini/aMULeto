package com.fabio.org.test;

import java.util.Date;

@AssociationClass
public class Iscrizione {
    private Studente studente;
    private Corso corso;
    @Unique
    private Date dataIscrizione;
    private double voto;

    public Iscrizione(Studente studente, Corso corso, Date dataIscrizione, double voto) {
        this.studente = studente;
        this.corso = corso;
        this.dataIscrizione = dataIscrizione;
        this.voto = voto;
    }

    public double getVoto() {
        return voto;
    }
}
