package com.fabio.org.amuleto.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.batik.swing.JSVGCanvas;

public class SVGViewer {

  public static void displaySVG(File svgFile) {
    JFrame svgFrame = new JFrame("Diagramma UML SVG");
    svgFrame.getContentPane().setBackground(new Color(45, 45, 45));
    svgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    svgFrame.setLayout(new BorderLayout());

    JSVGCanvas svgCanvas = new JSVGCanvas();
    svgCanvas.setBackground(new Color(45, 45, 45));
    svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
    try {
      svgCanvas.setURI(svgFile.toURI().toString());
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Errore nel caricamento dell'SVG: " + ex.getMessage());
      return;
    }

    final Point[] lastPoint = new Point[1];
    svgCanvas.addMouseListener(
        new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            lastPoint[0] = e.getPoint();
          }
        });
    svgCanvas.addMouseMotionListener(
        new MouseAdapter() {
          public void mouseDragged(MouseEvent e) {
            if (lastPoint[0] != null) {
              Point currentPoint = e.getPoint();
              double dx = currentPoint.getX() - lastPoint[0].getX();
              double dy = currentPoint.getY() - lastPoint[0].getY();
              AffineTransform at = svgCanvas.getRenderingTransform();
              at.translate(dx, dy);
              svgCanvas.setRenderingTransform(at, true);
              lastPoint[0] = currentPoint;
            }
          }
        });

    JSlider zoomSlider = new JSlider(10, 200, 100);
    zoomSlider.setMajorTickSpacing(50);
    zoomSlider.setMinorTickSpacing(10);
    zoomSlider.setPaintTicks(true);
    zoomSlider.setPaintLabels(true);
    zoomSlider.addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            double scale = zoomSlider.getValue() / 100.0;
            AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
            svgCanvas.setRenderingTransform(at, true);
          }
        });

    JScrollPane scrollPane = new JScrollPane(svgCanvas);
    scrollPane.getViewport().setBackground(new Color(45, 45, 45));
    svgFrame.add(scrollPane, BorderLayout.CENTER);
    svgFrame.add(zoomSlider, BorderLayout.SOUTH);

    svgFrame.setSize(800, 600);
    svgFrame.setLocationRelativeTo(null);
    svgFrame.setVisible(true);
  }
}
