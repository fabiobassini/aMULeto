package com.fabio.org.amuleto.view;

import com.fabio.org.amuleto.model.UMLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta il canvas in cui vengono disegnate le forme UML e le
 * relative connessioni.
 * Implementa i listener per il mouse (clic, trascinamento e rotella) per
 * gestire operazioni
 * di drag & drop, creazione di connessioni e zoom.
 */
public class UMLCanvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    // Lista delle forme UML (classi, interfacce, ecc.)
    private List<UMLClassShape> shapes;
    // Lista delle connessioni tra le forme
    private List<UMLConnection> connections;

    // Variabili per la gestione del drag & drop e della creazione di connessioni
    private UMLClassShape selectedShape = null; // Forma attualmente selezionata per il movimento
    private Point lastMousePosition = null; // Ultima posizione del mouse (per calcolare il delta)
    private UMLClassShape connectionSource = null; // Forma sorgente per la creazione di una connessione
    private Point currentMousePosition = null; // Posizione corrente del mouse durante la creazione della connessione
    private boolean isCreatingConnection = false; // Flag che indica se si sta creando una connessione
    private boolean relationshipMode = false; // Modalità attiva per la creazione di relazioni
    // Tipo corrente di relazione da creare, di default ASSOCIATION
    private RelationshipType currentRelType = RelationshipType.ASSOCIATION;
    // Fattore di zoom attuale (1.0 = 100%)
    private double zoomFactor = 1.0;

    /**
     * Costruttore di UMLCanvas.
     * Inizializza le liste delle forme e delle connessioni, aggiunge i listener per
     * il mouse
     * e imposta il colore di sfondo.
     */
    public UMLCanvas() {
        shapes = new ArrayList<>();
        connections = new ArrayList<>();
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setBackground(Color.WHITE);
    }

    /**
     * Restituisce la lista delle forme UML presenti sul canvas.
     *
     * @return Lista delle forme UML.
     */
    public List<UMLClassShape> getShapes() {
        return shapes;
    }

    /**
     * Restituisce la lista delle connessioni presenti sul canvas.
     *
     * @return Lista delle connessioni.
     */
    public List<UMLConnection> getConnections() {
        return connections;
    }

    /**
     * Imposta la lista delle forme UML.
     *
     * @param shapes Lista di forme UML.
     */
    public void setShapes(List<UMLClassShape> shapes) {
        this.shapes = shapes;
    }

    /**
     * Imposta la lista delle connessioni.
     *
     * @param connections Lista delle connessioni.
     */
    public void setConnections(List<UMLConnection> connections) {
        this.connections = connections;
    }

    /**
     * Imposta la modalità di relazione.
     *
     * @param mode true se si desidera abilitare la modalità di relazione, false
     *             altrimenti.
     */
    public void setRelationshipMode(boolean mode) {
        this.relationshipMode = mode;
    }

    /**
     * Imposta il tipo corrente di relazione.
     *
     * @param type Tipo di relazione da utilizzare per le nuove connessioni.
     */
    public void setCurrentRelationshipType(RelationshipType type) {
        this.currentRelType = type;
    }

    /**
     * Aggiunge una forma UML al canvas e forza il repaint.
     *
     * @param shape La forma UML da aggiungere.
     */
    public void addShape(UMLClassShape shape) {
        shapes.add(shape);
        repaint();
    }

    /**
     * Aggiunge una connessione al canvas e forza il repaint.
     *
     * @param conn La connessione da aggiungere.
     */
    public void addConnection(UMLConnection conn) {
        connections.add(conn);
        repaint();
    }

    /**
     * Imposta il fattore di zoom del canvas e forza il repaint.
     *
     * @param factor Nuovo fattore di zoom.
     */
    public void setZoomFactor(double factor) {
        this.zoomFactor = factor;
        repaint();
    }

    /**
     * Esegue un layout dinamico delle forme sul canvas.
     * Le forme vengono disposte in una griglia calcolata in base al numero totale
     * di forme.
     */
    public void doDynamicLayout() {
        int cols = (int) Math.ceil(Math.sqrt(shapes.size()));
        int spacing = 50;
        int cellWidth = 250, cellHeight = 200;
        int i = 0;
        for (UMLClassShape shape : shapes) {
            int row = i / cols;
            int col = i % cols;
            shape.setLocation(spacing + col * (cellWidth + spacing), spacing + row * (cellHeight + spacing));
            i++;
        }
        repaint();
    }

    /**
     * Esegue il rendering del canvas.
     * Vengono disegnate prima le connessioni e poi le forme. Se si sta creando una
     * connessione,
     * viene disegnata anche una linea rossa che collega la forma sorgente alla
     * posizione corrente del mouse.
     *
     * @param g Il contesto grafico su cui disegnare.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.scale(zoomFactor, zoomFactor);

        // Disegna le connessioni (linee) tra le forme
        for (UMLConnection conn : connections) {
            conn.draw(g2);
        }
        // Disegna le forme UML
        for (UMLClassShape shape : shapes) {
            shape.draw(g2);
        }
        // Se si sta creando una connessione, disegna una linea rossa dal centro della
        // forma sorgente alla posizione corrente del mouse
        if (isCreatingConnection && connectionSource != null && currentMousePosition != null) {
            Rectangle srcBounds = connectionSource.getBounds(g2);
            int x1 = srcBounds.x + srcBounds.width / 2;
            int y1 = srcBounds.y + srcBounds.height / 2;
            g2.setColor(Color.RED);
            g2.drawLine(x1, y1, currentMousePosition.x, currentMousePosition.y);
        }
        g2.dispose();
    }

    /**
     * Gestisce l'evento di pressione del mouse.
     * Se il pulsante sinistro viene premuto:
     * - In modalità relazione, ricerca la forma sotto il mouse e inizia la
     * creazione di una connessione.
     * - In modalità normale, ricerca la forma sotto il mouse per la selezione o per
     * l'editing (doppio click).
     *
     * @param e L'evento del mouse.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point p = translatePoint(e.getPoint());
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (relationshipMode) {
                for (UMLClassShape shape : shapes) {
                    if (shape.contains(p, getGraphics())) {
                        connectionSource = shape;
                        isCreatingConnection = true;
                        currentMousePosition = p;
                        break;
                    }
                }
            } else {
                for (UMLClassShape shape : shapes) {
                    if (shape.contains(p, getGraphics())) {
                        selectedShape = shape;
                        lastMousePosition = p;
                        if (e.getClickCount() == 2) {
                            shape.openEditor(this);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Gestisce l'evento di rilascio del mouse.
     * Se si stava creando una connessione, verifica se il mouse è sopra un'altra
     * forma e crea la connessione.
     * Al termine, forza il repaint.
     *
     * @param e L'evento del mouse.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isCreatingConnection && connectionSource != null) {
            Point p = translatePoint(e.getPoint());
            for (UMLClassShape shape : shapes) {
                if (shape.contains(p, getGraphics()) && shape != connectionSource) {
                    UMLConnection conn = new UMLConnection(connectionSource, shape, currentRelType);
                    connections.add(conn);
                    break;
                }
            }
            isCreatingConnection = false;
            connectionSource = null;
            currentMousePosition = null;
            repaint();
        }
        selectedShape = null;
        lastMousePosition = null;
    }

    /**
     * Gestisce l'evento di trascinamento del mouse.
     * Se si sta creando una connessione, aggiorna la posizione corrente del mouse e
     * forza il repaint.
     * Se è selezionata una forma, ne aggiorna la posizione in base al movimento del
     * mouse e forza il repaint.
     *
     * @param e L'evento di trascinamento del mouse.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = translatePoint(e.getPoint());
        if (isCreatingConnection) {
            currentMousePosition = p;
            repaint();
        } else if (selectedShape != null && lastMousePosition != null) {
            int dx = p.x - lastMousePosition.x;
            int dy = p.y - lastMousePosition.y;
            selectedShape.setLocation(selectedShape.getX() + dx, selectedShape.getY() + dy);
            lastMousePosition = p;
            repaint();
        }
    }

    /**
     * Metodo non utilizzato per l'evento mouseClicked.
     *
     * @param e L'evento del mouse.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Metodo non utilizzato per l'evento mouseEntered.
     *
     * @param e L'evento del mouse.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Metodo non utilizzato per l'evento mouseExited.
     *
     * @param e L'evento del mouse.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Metodo non utilizzato per l'evento mouseMoved.
     *
     * @param e L'evento del mouse.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Gestisce l'evento della rotella del mouse per il controllo dello zoom.
     * Modifica il fattore di zoom in base al numero di "scatti" della rotella e
     * forza il repaint.
     *
     * @param e L'evento della rotella del mouse.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double delta = 0.05 * e.getWheelRotation();
        zoomFactor = Math.max(0.1, zoomFactor - delta);
        repaint();
    }

    /**
     * Traduci il punto in base al fattore di zoom corrente.
     * Permette di convertire le coordinate del mouse considerando lo zoom
     * applicato.
     *
     * @param p Il punto con coordinate originali.
     * @return Il punto convertito in coordinate reali sul canvas.
     */
    private Point translatePoint(Point p) {
        return new Point((int) (p.x / zoomFactor), (int) (p.y / zoomFactor));
    }
}
