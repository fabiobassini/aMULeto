package com.fabio.org.amuleto.view;

import com.fabio.org.amuleto.model.UMLAttribute;
import com.fabio.org.amuleto.model.UMLMethod;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.stream.Collectors;

/**
 * Dialogo per la modifica di una classe UML.
 * Permette di aggiornare il nome, gli attributi, i costruttori e i metodi
 * della classe, in base all'input fornito dall'utente.
 */
public class UMLClassEditorDialog extends JDialog {
    private UMLClassShape umlClass; // Istanza della classe UML da modificare
    private JTextField nameField; // Campo per il nome della classe
    private JTextArea attributesArea; // Area di testo per gli attributi
    private JTextArea constructorsArea; // Area di testo per i costruttori
    private JTextArea methodsArea; // Area di testo per i metodi

    /**
     * Costruttore del dialogo.
     * Viene impostato il titolo in base al nome della classe e il dialogo viene
     * reso modale.
     *
     * @param parent   Componente padre da cui viene invocato il dialogo.
     * @param umlClass Istanza della classe UML da modificare.
     */
    public UMLClassEditorDialog(Component parent, UMLClassShape umlClass) {
        super(SwingUtilities.getWindowAncestor(parent), "Modifica Classe: " + umlClass.getClassName(),
                ModalityType.APPLICATION_MODAL);
        this.umlClass = umlClass;
        initComponents(); // Inizializza i componenti grafici
        pack(); // Regola le dimensioni del dialogo in base al contenuto
        setLocationRelativeTo(parent); // Posiziona il dialogo rispetto al componente padre
    }

    /**
     * Inizializza i componenti del dialogo e la disposizione degli stessi.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Creazione del pannello per il modulo di editing con layout a griglia (4
        // righe)
        JPanel formPanel = new JPanel(new GridLayout(4, 1));

        // Campo per il nome della classe, inizializzato con il valore corrente
        nameField = new JTextField(umlClass.getClassName());
        formPanel.add(labeledPanel("Nome:", nameField));

        // Area di testo per gli attributi; il contenuto viene precompilato
        // utilizzando il metodo toString() di ogni attributo e separato da nuove righe
        attributesArea = new JTextArea(5, 20);
        attributesArea.setText(
                umlClass.getAttributes().stream().map(Object::toString).collect(Collectors.joining("\n")));
        formPanel.add(labeledPanel("Attributi (uno per riga, formato: visibilità tipo nome):",
                new JScrollPane(attributesArea)));

        // Area di testo per i costruttori; il contenuto viene precompilato in modo
        // simile
        constructorsArea = new JTextArea(5, 20);
        constructorsArea.setText(
                umlClass.getConstructors().stream().map(Object::toString).collect(Collectors.joining("\n")));
        formPanel.add(labeledPanel("Costruttori (uno per riga):", new JScrollPane(constructorsArea)));

        // Area di testo per i metodi; il contenuto viene precompilato in modo simile
        methodsArea = new JTextArea(5, 20);
        methodsArea.setText(umlClass.getMethods().stream().map(Object::toString).collect(Collectors.joining("\n")));
        formPanel.add(labeledPanel("Metodi (uno per riga):", new JScrollPane(methodsArea)));

        add(formPanel, BorderLayout.CENTER);

        // Creazione del pannello per i pulsanti "OK" e "Annulla"
        JPanel buttonsPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener((ActionEvent e) -> {
            updateUMLClass(); // Aggiorna i dati dell'istanza UML in base all'input
            getOwner().repaint(); // Forza il repaint della finestra proprietaria per aggiornare la
                                  // visualizzazione
            dispose(); // Chiude il dialogo
        });
        buttonsPanel.add(okButton);
        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Aggiorna l'istanza UMLClassShape con i dati immessi dall'utente nelle aree di
     * testo.
     * Il metodo svuota le liste degli attributi, costruttori e metodi, e le riempie
     * con i nuovi valori, parsati in base a un formato predefinito.
     */
    private void updateUMLClass() {
        // Aggiorna il nome della classe
        umlClass.setClassName(nameField.getText().trim());

        // Aggiorna gli attributi: svuota la lista esistente
        umlClass.getAttributes().clear();
        // Per ogni riga nell'area degli attributi, parsa la stringa e crea un nuovo
        // UMLAttribute
        for (String line : attributesArea.getText().split("\\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                // Si assume che il formato sia "visibilità nome : tipo"
                // Esempio: "+ id : int"
                int colonIndex = line.indexOf(":");
                if (colonIndex > 0) {
                    String beforeColon = line.substring(0, colonIndex).trim(); // Parte prima dei due punti (es. "+ id")
                    String afterColon = line.substring(colonIndex + 1).trim(); // Parte dopo i due punti (es. "int")
                    String[] parts = beforeColon.split("\\s+");
                    if (parts.length >= 2) {
                        String vis = parts[0]; // Esempio: "+"
                        String name = parts[1]; // Esempio: "id"
                        String type = afterColon; // Esempio: "int"
                        umlClass.addAttribute(new UMLAttribute(vis, type, name));
                    }
                }
            }
        }

        // Aggiorna i costruttori: svuota la lista esistente
        umlClass.getConstructors().clear();
        // Per ogni riga nell'area dei costruttori, parsa la stringa e crea un nuovo
        // UMLMethod
        for (String line : constructorsArea.getText().split("\\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                // Si assume un formato semplificato: "visibilità nome"
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    // Utilizza il primo elemento come visibilità e il secondo come nome
                    umlClass.addConstructor(new UMLMethod(parts[0], "", parts[1], ""));
                }
            }
        }

        // Aggiorna i metodi: svuota la lista esistente
        umlClass.getMethods().clear();
        // Per ogni riga nell'area dei metodi, parsa la stringa e crea un nuovo
        // UMLMethod
        for (String line : methodsArea.getText().split("\\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                // Si assume un formato: "visibilità nome(parametri) : returnType"
                int colonIndex = line.indexOf(":");
                if (colonIndex > 0) {
                    String beforeColon = line.substring(0, colonIndex).trim(); // Esempio: "+ metodo(parametri)"
                    String afterColon = line.substring(colonIndex + 1).trim(); // Esempio: "returnType"
                    String[] parts = beforeColon.split("\\s+");
                    if (parts.length >= 2) {
                        String vis = parts[0]; // Visibilità
                        // Il nome e i parametri vengono presi come stringa intera dopo la visibilità
                        String nameAndParams = beforeColon.substring(vis.length()).trim();
                        umlClass.addMethod(new UMLMethod(vis, afterColon, nameAndParams, ""));
                    }
                }
            }
        }
    }

    /**
     * Crea un pannello etichettato.
     * Viene creato un pannello con un'etichetta in alto e il componente fornito
     * al centro.
     *
     * @param labelText Il testo dell'etichetta.
     * @param comp      Il componente da visualizzare sotto l'etichetta.
     * @return Un pannello con layout BorderLayout contenente l'etichetta e il
     *         componente.
     */
    private JPanel labeledPanel(String labelText, Component comp) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(labelText), BorderLayout.NORTH);
        panel.add(comp, BorderLayout.CENTER);
        return panel;
    }
}
