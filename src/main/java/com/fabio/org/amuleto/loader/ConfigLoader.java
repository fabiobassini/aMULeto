package com.fabio.org.amuleto.loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static final String CONFIG_FILE = "config.properties";
    private Properties properties;

    public ConfigLoader() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Restituisce il percorso del progetto Java configurato nel file di
     * configurazione.
     *
     * @return La directory del progetto Java.
     */
    public String getProjectDirectory() {
        return properties.getProperty("projectDirectory");
    }
}
