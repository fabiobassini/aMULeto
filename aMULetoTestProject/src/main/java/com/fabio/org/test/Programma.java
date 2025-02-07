package com.fabio.org.test;

import java.util.List;

public class Programma {
    @Ordered
    private List<String> argomenti;

    public Programma(List<String> argomenti) {
        this.argomenti = argomenti;
    }

    public List<String> getArgomenti() {
        return argomenti;
    }
}
