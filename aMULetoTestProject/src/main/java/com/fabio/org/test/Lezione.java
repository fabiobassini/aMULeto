package com.fabio.org.test;

import java.util.Date;

public class Lezione {
    @Readonly
    private Date dataLezione;
    private String argomento;
    private String aula;

    public Lezione(Date dataLezione, String argomento, String aula) {
        this.dataLezione = dataLezione;
        this.argomento = argomento;
        this.aula = aula;
    }

    public Date getDataLezione() {
        return dataLezione;
    }

    public String getArgomento() {
        return argomento;
    }

    public String getAula() {
        return aula;
    }
}
