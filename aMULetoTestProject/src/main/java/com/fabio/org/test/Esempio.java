package com.fabio.org.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Esempio {
    public static void main(String[] args) {
        List<String> argomenti = Arrays.asList("Introduzione", "Approfondimenti", "Progetto finale");
        Programma prog = new Programma(argomenti);

        Corso corsoJava = new Corso("Java Avanzato", prog);
        Lezione lezione1 = new Lezione(new Date(), "Concetti Avanzati di Java", "Aula 101");
        corsoJava.aggiungiLezione(lezione1);

        Dipartimento dip = new Dipartimento("Informatica");
        dip.aggiungiCorso(corsoJava);

        Professore prof = new Professore("Mario", "Rossi", "Informatica");
        prof.aggiungiCorso(corsoJava);

        Studente stud = new Studente("Luigi", "Verdi", 12345);
        Iscrizione iscrizione = new Iscrizione(stud, corsoJava, new Date(), 28.5);
        stud.aggiungiIscrizione(iscrizione);
        corsoJava.aggiungiIscrizione(iscrizione);

        System.out.println(
                "Studente: " + stud.getNome() + " " + stud.getCognome() + " - Media voti: " + stud.calcolaMedia());
    }
}
