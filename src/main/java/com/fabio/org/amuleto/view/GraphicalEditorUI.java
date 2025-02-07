package com.fabio.org.amuleto.view;

import com.fabio.org.amuleto.controller.UMLController;
import com.fabio.org.amuleto.converter.UMLGenerator;
import com.fabio.org.amuleto.model.UMLDiagram;
import com.fabio.org.amuleto.model.UMLAttribute;
import com.fabio.org.amuleto.model.UMLMethod;
import com.fabio.org.amuleto.model.UMLConnection;
import com.fabio.org.amuleto.serialization.DiagramSerializer;
import net.sourceforge.plantuml.FileFormat;
import org.apache.batik.swing.JSVGCanvas;
// import org.w3c.dom.events.MouseEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
// import java.awt.geom.AffineTransform;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// import java.awt.event.MouseMotionListener;

/**
 * Finestra principale dell'editor UML.
 * Questa classe gestisce l'interfaccia grafica principale, il menu, la sidebar
 * e il canvas dove vengono disegnate le forme UML e le loro connessioni.
 * Include anche opzioni per salvare/caricare il diagramma in formato JSON,
 * generare il codice UML e generare/visualizzare un diagramma SVG.
 */
public class GraphicalEditorUI extends JFrame {

    private UMLCanvas canvas; // Area di disegno per il diagramma UML
    private UMLController controller; // Controller per la sincronizzazione tra modello e vista
    private UMLDiagram diagram; // Modello che rappresenta il diagramma UML

    /**
     * Costruttore della finestra principale.
     * Imposta il titolo, le dimensioni e inizializza il modello, il canvas e il
     * controller.
     */
    public GraphicalEditorUI() {
        setTitle("aMULeto GUI");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Imposta lo sfondo della finestra (opzionale, FlatDarkLaf lo gestisce già)
        getContentPane().setBackground(new Color(45, 45, 45));

        diagram = new UMLDiagram();
        canvas = new UMLCanvas();
        controller = new UMLController(diagram, canvas);
        initComponents(); // Inizializza i componenti dell'interfaccia
    }

    /**
     * Inizializza i componenti dell'interfaccia grafica, inclusa la sidebar,
     * il pannello principale e il menu.
     */
    private void initComponents() {
        // Creazione della sidebar
        JPanel sidebar = createSidebar();

        // Imposta lo sfondo della sidebar
        sidebar.setBackground(new Color(45, 45, 45));
        // Creazione di un JSplitPane per dividere la sidebar dal canvas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, new JScrollPane(canvas));
        splitPane.setDividerLocation(250);
        splitPane.setBackground(new Color(45, 45, 45));
        getContentPane().add(splitPane, BorderLayout.CENTER);

        // Creazione del menu bar e del menu File
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(60, 60, 60));
        JMenu fileMenu = new JMenu("File");

        // Menu item per salvare il diagramma (modello) in formato JSON
        JMenuItem saveItem = new JMenuItem("Salva Diagramma");
        saveItem.addActionListener((ActionEvent e) -> {
            controller.updateModelFromCanvas();
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(GraphicalEditorUI.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    DiagramSerializer.saveDiagram(chooser.getSelectedFile().getAbsolutePath(), diagram);
                    JOptionPane.showMessageDialog(this, "Diagramma salvato correttamente.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + ex.getMessage());
                }
            }
        });
        fileMenu.add(saveItem);

        // Menu item per caricare un diagramma da un file JSON
        JMenuItem loadItem = new JMenuItem("Carica Diagramma");
        loadItem.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(GraphicalEditorUI.this) == JFileChooser.APPROVE_OPTION) {
                try {
                    diagram = DiagramSerializer.loadDiagram(chooser.getSelectedFile().getAbsolutePath());
                    controller = new UMLController(diagram, canvas);
                    controller.updateCanvasFromModel();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante il caricamento: " + ex.getMessage());
                }
            }
        });
        fileMenu.add(loadItem);

        // Menu item per generare il codice UML in formato PlantUML
        JMenuItem genUMLItem = new JMenuItem("Genera Codice UML");
        genUMLItem.addActionListener((ActionEvent e) -> generateUMLCode());
        fileMenu.add(genUMLItem);

        // Menu item per generare e visualizzare il diagramma SVG a partire dal codice
        // Java
        JMenuItem genSVGItem = new JMenuItem("Genera Diagramma SVG");
        genSVGItem.addActionListener((ActionEvent e) -> {
            // Apertura di un JFileChooser per selezionare la directory del progetto Java
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Seleziona la directory del progetto Java");
            if (chooser.showOpenDialog(GraphicalEditorUI.this) == JFileChooser.APPROVE_OPTION) {
                File sourceDir = chooser.getSelectedFile();
                // Determina il percorso sul Desktop: Desktop/aMULeto/diagramma.svg
                File desktop = new File(System.getProperty("user.home"), "Desktop");
                File aMULetoDir = new File(desktop, "aMULeto");
                if (!aMULetoDir.exists()) {
                    aMULetoDir.mkdirs();
                }
                File outputSVG = new File(aMULetoDir, "diagramma.svg");
                try {
                    UMLGenerator.generateVectorFromDirectory(sourceDir, outputSVG, FileFormat.SVG);
                    displaySVG(outputSVG); // Visualizza il diagramma SVG in una nuova finestra
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(GraphicalEditorUI.this,
                            "Errore durante la generazione del diagramma: " + ex.getMessage());
                }
            }
        });
        fileMenu.add(genSVGItem);

        // Nuova opzione: visualizza un file SVG selezionato
        JMenuItem viewSVGItem = new JMenuItem("Visualizza Diagramma SVG");
        viewSVGItem.addActionListener((ActionEvent e) -> {
            // Utilizza JFileChooser per selezionare un file con estensione .svg
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleziona file SVG");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // Imposta un filtro per mostrare solo file SVG
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".svg");
                }

                @Override
                public String getDescription() {
                    return "File SVG (*.svg)";
                }
            });
            if (fileChooser.showOpenDialog(GraphicalEditorUI.this) == JFileChooser.APPROVE_OPTION) {
                File svgFile = fileChooser.getSelectedFile();
                displaySVG(svgFile); // Visualizza il file SVG selezionato
            }
        });
        fileMenu.add(viewSVGItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Crea e restituisce la sidebar contenente i pulsanti e i controlli per
     * aggiungere forme, selezionare il tipo di relazione, controllare lo zoom e
     * il layout automatico.
     *
     * @return Il pannello della sidebar.
     */

    private JPanel createSidebar() {
        // Pannello principale con layout verticale e bordo esterno
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(45, 45, 45));

        // === Sezione Aggiunta Elementi ===
        JPanel addElementsPanel = new JPanel();
        addElementsPanel.setLayout(new BoxLayout(addElementsPanel, BoxLayout.Y_AXIS));
        addElementsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)), "Aggiungi Elementi"));
        addElementsPanel.setBackground(new Color(45, 45, 45));

        // Pulsante per aggiungere una classe
        JButton addClassBtn = new JButton("Aggiungi Classe");
        addClassBtn.addActionListener((ActionEvent e) -> {
            String className = JOptionPane.showInputDialog(GraphicalEditorUI.this, "Nome della Classe:");
            if (className != null && !className.trim().isEmpty()) {
                UMLClassShape shape = new UMLClassShape(className.trim(), 50, 50);
                shape.addAttribute(new UMLAttribute("+", "int", "id"));
                shape.addConstructor(new UMLMethod("+", "", className.trim(), ""));
                shape.addMethod(new UMLMethod("+", "void", "metodo", ""));
                canvas.addShape(shape);
            }
        });
        addClassBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addElementsPanel.add(addClassBtn);
        addElementsPanel.add(Box.createVerticalStrut(10));

        // Pulsante per aggiungere un'interfaccia
        JButton addInterfaceBtn = new JButton("Aggiungi Interfaccia");
        addInterfaceBtn.addActionListener((ActionEvent e) -> {
            String interfaceName = JOptionPane.showInputDialog(GraphicalEditorUI.this, "Nome dell'Interfaccia:");
            if (interfaceName != null && !interfaceName.trim().isEmpty()) {
                UMLClassShape shape = new UMLClassShape(interfaceName.trim(), 100, 100);
                canvas.addShape(shape);
            }
        });
        addInterfaceBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addElementsPanel.add(addInterfaceBtn);
        addElementsPanel.add(Box.createVerticalStrut(10));

        // Pulsante per aggiungere una classe associativa
        JButton addAssocClassBtn = new JButton("Aggiungi Classe Associativa");
        addAssocClassBtn.addActionListener((ActionEvent e) -> {
            String assocName = JOptionPane.showInputDialog(GraphicalEditorUI.this, "Nome della Classe Associativa:");
            if (assocName != null && !assocName.trim().isEmpty()) {
                UMLClassShape shape = new UMLClassShape(assocName.trim(), 150, 150);
                shape.setAssociationClass(true);
                canvas.addShape(shape);
            }
        });
        addAssocClassBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addElementsPanel.add(addAssocClassBtn);
        addElementsPanel.add(Box.createVerticalStrut(10));

        panel.add(addElementsPanel);
        panel.add(Box.createVerticalStrut(15));

        // === Sezione Relazioni ===
        JPanel relationsPanel = new JPanel();
        relationsPanel.setLayout(new BoxLayout(relationsPanel, BoxLayout.Y_AXIS));
        relationsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)), "Relazioni"));
        relationsPanel.setBackground(new Color(45, 45, 45));

        // Toggle per abilitare la modalità relazione
        JToggleButton relModeToggle = new JToggleButton("Modalità Relazione");
        relModeToggle.addActionListener(e -> canvas.setRelationshipMode(relModeToggle.isSelected()));
        relModeToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        relationsPanel.add(relModeToggle);
        relationsPanel.add(Box.createVerticalStrut(10));

        // Pannello orizzontale per la label e il combo box
        JPanel relTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        relTypePanel.setBackground(new Color(45, 45, 45));
        JLabel relTypeLabel = new JLabel("Tipo Relazione:");
        relTypeLabel.setForeground(Color.WHITE);
        relTypePanel.add(relTypeLabel);
        JComboBox<RelationshipType> relTypeCombo = new JComboBox<>(RelationshipType.values());
        relTypeCombo.addActionListener(
                e -> canvas.setCurrentRelationshipType((RelationshipType) relTypeCombo.getSelectedItem()));
        relTypePanel.add(relTypeCombo);
        relationsPanel.add(relTypePanel);

        panel.add(relationsPanel);
        panel.add(Box.createVerticalStrut(15));

        // === Sezione Zoom e Layout ===
        JPanel zoomLayoutPanel = new JPanel();
        zoomLayoutPanel.setLayout(new BoxLayout(zoomLayoutPanel, BoxLayout.Y_AXIS));
        zoomLayoutPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)), "Zoom & Layout"));
        zoomLayoutPanel.setBackground(new Color(45, 45, 45));

        JLabel zoomLabel = new JLabel("Zoom:");
        zoomLabel.setForeground(Color.WHITE);
        zoomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        zoomLayoutPanel.add(zoomLabel);
        zoomLayoutPanel.add(Box.createVerticalStrut(5));

        JSlider zoomSlider = new JSlider(10, 200, 100);
        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setMinorTickSpacing(10);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(e -> canvas.setZoomFactor(zoomSlider.getValue() / 100.0));
        zoomSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        zoomLayoutPanel.add(zoomSlider);
        zoomLayoutPanel.add(Box.createVerticalStrut(10));

        JButton layoutBtn = new JButton("Layout Automatico");
        layoutBtn.addActionListener(e -> canvas.doDynamicLayout());
        layoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        zoomLayoutPanel.add(layoutBtn);

        panel.add(zoomLayoutPanel);

        return panel;
    }

    /**
     * Genera il codice UML in formato PlantUML partendo dal contenuto del canvas.
     * Costruisce una stringa contenente la definizione delle classi e delle
     * relazioni,
     * e la visualizza in un dialogo.
     */
    private void generateUMLCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("skinparam classAttributeIconSize 0\n\n");
        for (UMLClassShape shape : canvas.getShapes()) {
            if (shape.isAssociationClass()) {
                sb.append("class ").append(shape.getClassName()).append(" <<association>> {\n");
            } else {
                sb.append("class ").append(shape.getClassName()).append(" {\n");
            }
            for (UMLAttribute attr : shape.getAttributes()) {
                sb.append("  ").append(attr.toString()).append("\n");
            }
            for (UMLMethod cons : shape.getConstructors()) {
                sb.append("  ").append(cons.toString()).append("\n");
            }
            for (UMLMethod meth : shape.getMethods()) {
                sb.append("  ").append(meth.toString()).append("\n");
            }
            sb.append("}\n");
        }
        for (UMLConnection conn : canvas.getConnections()) {
            sb.append(conn.getSource().getClassName()).append(" ");
            switch (conn.getType()) {
                case INHERITANCE:
                    sb.append("<|--");
                    break;
                case IMPLEMENTATION:
                    sb.append("..|>");
                    break;
                case AGGREGATION:
                    sb.append("o--");
                    break;
                case COMPOSITION:
                    sb.append("*--");
                    break;
                case DEPENDENCY:
                    sb.append("..>");
                    break;
                default:
                    sb.append("--");
                    break;
            }
            sb.append(" ").append(conn.getTarget().getClassName()).append("\n");
        }
        sb.append("@enduml");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scrollPane, "Codice UML Generato", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra il file SVG generato in una nuova finestra.
     * Viene creato un JSVGCanvas per visualizzare l'SVG, con supporto per il pan
     * (interazione
     * predefinita) e uno slider per il controllo dello zoom.
     * Il file SVG è stato salvato nella cartella "aMULeto" sul Desktop.
     *
     * @param svgFile Il file SVG da visualizzare.
     */
    private void displaySVG(File svgFile) {
        JFrame svgFrame = new JFrame("Diagramma UML SVG");
        svgFrame.getContentPane().setBackground(new Color(45, 45, 45));
        svgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        svgFrame.setLayout(new BorderLayout());

        // Crea il canvas per visualizzare l'SVG utilizzando Batik
        JSVGCanvas svgCanvas = new JSVGCanvas();

        svgCanvas.setBackground(new Color(45, 45, 45));

        svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        try {
            svgCanvas.setURI(svgFile.toURI().toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dell'SVG: " + ex.getMessage());
            return;
        }

        // un array per conservare il punto dell'ultimo evento
        final Point[] lastPoint = new Point[1];

        // un MouseListener per registrare il punto di partenza
        svgCanvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastPoint[0] = e.getPoint();
            }
        });

        // un MouseMotionListener per gestire il trascinamento
        svgCanvas.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (lastPoint[0] != null) {
                    Point currentPoint = e.getPoint();
                    double dx = currentPoint.getX() - lastPoint[0].getX();
                    double dy = currentPoint.getY() - lastPoint[0].getY();

                    // Ottieni il transform corrente e applica la traduzione
                    AffineTransform at = svgCanvas.getRenderingTransform();
                    at.translate(dx, dy);
                    svgCanvas.setRenderingTransform(at, true);

                    // Aggiorno il punto di riferimento
                    lastPoint[0] = currentPoint;
                }
            }
        });

        // Creo uno slider per controllare lo zoom (valori da 20% a 200%)
        JSlider zoomSlider = new JSlider(10, 200, 100);
        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setMinorTickSpacing(10);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double scale = zoomSlider.getValue() / 100.0;
                AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
                svgCanvas.setRenderingTransform(at, true);
            }
        });

        // Aggiunge il canvas in un JScrollPane per supportare il pan se necessario
        JScrollPane scrollPane = new JScrollPane(svgCanvas);
        svgFrame.add(scrollPane, BorderLayout.CENTER);
        svgFrame.add(zoomSlider, BorderLayout.SOUTH);

        svgFrame.setSize(800, 600);
        svgFrame.setLocationRelativeTo(this);
        svgFrame.setVisible(true);
    }

    /**
     * Metodo main per avviare l'applicazione.
     *
     * @param args Argomenti della riga di comando.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicalEditorUI editor = new GraphicalEditorUI();
            editor.setVisible(true);
        });
    }
}
