/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author esteb
 */
public class VisorImagenes extends JPanel {

    private final ImagenesLogic logic;
    private int indiceActual = -1;

    private final Color COLOR_FONDO = new Color(30, 30, 30);      
    private final Color COLOR_PANEL = new Color(45, 45, 48);      
    private final Color COLOR_NARANJA = new Color(233, 84, 32);   
    private final Color COLOR_TEXTO = new Color(240, 240, 240);   

    private final PanelImagen panelImagenGrande = new PanelImagen();
    private final JPanel panelMiniaturas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    private final JLabel lblEstado = new JLabel("Sin imagen");

    public VisorImagenes() {
        this.logic = new ImagenesLogic();
        logic.cargarImagenes();

        setLayout(new BorderLayout(0, 0));
        setBackground(COLOR_FONDO);

        crearBarraSuperior();

        add(panelImagenGrande, BorderLayout.CENTER);

        crearPanelInferior();

        setPreferredSize(new Dimension(1000, 700));

        if (!logic.getImagenes().isEmpty()) {
            indiceActual = 0;
            reconstruirMiniaturas();
            mostrarImagenActual();
        }
    }

    private void crearBarraSuperior() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(COLOR_PANEL);
        top.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Galería Dark");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(COLOR_TEXTO);
        top.add(titulo, BorderLayout.WEST);

        BotonModerno btnCargar = new BotonModerno("Importar Imagen", COLOR_NARANJA, Color.WHITE);
        btnCargar.setPreferredSize(new Dimension(150, 35));

        btnCargar.addActionListener(e -> {
            logic.importarImagenes(this);
            if (!logic.getImagenes().isEmpty()) {
                indiceActual = logic.getImagenes().size() - 1;
                reconstruirMiniaturas();
                mostrarImagenActual();
            }
        });
        top.add(btnCargar, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
    }

    private void crearPanelInferior() {
        JPanel contenedorInferior = new JPanel(new BorderLayout());
        contenedorInferior.setBackground(COLOR_FONDO);
        contenedorInferior.setBorder(new EmptyBorder(10, 10, 10, 10));

        BotonFlecha btnIzq = new BotonFlecha(false, COLOR_NARANJA); 
        BotonFlecha btnDer = new BotonFlecha(true, COLOR_NARANJA);
        
        btnIzq.setPreferredSize(new Dimension(60, 100)); 
        btnDer.setPreferredSize(new Dimension(60, 100));

        btnIzq.addActionListener(e -> mostrarAnterior());
        btnDer.addActionListener(e -> mostrarSiguiente());

        contenedorInferior.add(btnIzq, BorderLayout.WEST);
        contenedorInferior.add(btnDer, BorderLayout.EAST);

        panelMiniaturas.setBackground(COLOR_FONDO); 
        
        JScrollPane scrollThumbs = new JScrollPane(panelMiniaturas,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollThumbs.setBorder(null); 
        scrollThumbs.getViewport().setBackground(COLOR_FONDO);
        scrollThumbs.getHorizontalScrollBar().setUnitIncrement(20);
        scrollThumbs.setPreferredSize(new Dimension(0, 120));

        contenedorInferior.add(scrollThumbs, BorderLayout.CENTER);

        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setBackground(COLOR_PANEL);
        
        lblEstado.setForeground(new Color(180, 180, 180));
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelEstado.add(lblEstado);

        JPanel surFinal = new JPanel(new BorderLayout());
        surFinal.add(contenedorInferior, BorderLayout.CENTER);
        surFinal.add(panelEstado, BorderLayout.SOUTH);

        add(surFinal, BorderLayout.SOUTH);
    }

    private void reconstruirMiniaturas() {
        List<File> imagenes = logic.getImagenes();
        panelMiniaturas.removeAll();

        LineBorder bordeSeleccion = new LineBorder(COLOR_NARANJA, 3);
        LineBorder bordeNormal = new LineBorder(new Color(80, 80, 80), 1);

        for (int i = 0; i < imagenes.size(); i++) {
            final int idx = i;
            File f = imagenes.get(i);

            PanelMiniatura panelThumb = new PanelMiniatura();
            panelThumb.setPreferredSize(new Dimension(120, 90));
            panelThumb.setBorder(idx == indiceActual ? bordeSeleccion : bordeNormal);
            panelThumb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            BufferedImage img = cargarImagenOptimizada(f, 200);
            panelThumb.setImagen(img);

            panelThumb.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    indiceActual = idx;
                    actualizarBordesMiniaturas();
                    mostrarImagenActual();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (idx != indiceActual) panelThumb.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (idx != indiceActual) panelThumb.setBorder(bordeNormal);
                }
            });

            panelMiniaturas.add(panelThumb);
        }

        panelMiniaturas.revalidate();
        panelMiniaturas.repaint();
    }

    private void actualizarBordesMiniaturas() {
        Component[] comps = panelMiniaturas.getComponents();
        LineBorder bordeSeleccion = new LineBorder(COLOR_NARANJA, 3);
        LineBorder bordeNormal = new LineBorder(new Color(80, 80, 80), 1);

        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JPanel) {
                ((JPanel) comps[i]).setBorder(i == indiceActual ? bordeSeleccion : bordeNormal);
            }
        }
    }

    private void mostrarAnterior() {
        List<File> imagenes = logic.getImagenes();
        if (imagenes.isEmpty()) return;
        indiceActual = (indiceActual - 1 + imagenes.size()) % imagenes.size();
        actualizarBordesMiniaturas();
        mostrarImagenActual();
    }

    private void mostrarSiguiente() {
        List<File> imagenes = logic.getImagenes();
        if (imagenes.isEmpty()) return;
        indiceActual = (indiceActual + 1) % imagenes.size();
        actualizarBordesMiniaturas();
        mostrarImagenActual();
    }

    private void mostrarImagenActual() {
        List<File> imagenes = logic.getImagenes();
        if (indiceActual < 0 || imagenes.isEmpty()) {
            panelImagenGrande.setImagen(null);
            lblEstado.setText("Galería vacía");
            return;
        }

        File f = imagenes.get(indiceActual);
        try {
            BufferedImage rawImage = ImageIO.read(f);
            panelImagenGrande.setImagen(rawImage);
            lblEstado.setText(" " + (indiceActual + 1) + " / " + imagenes.size() + " — " + f.getName());
        } catch (Exception ex) {
            panelImagenGrande.setImagen(null);
            lblEstado.setText(" Error al leer archivo.");
        }
    }

    private BufferedImage cargarImagenOptimizada(File f, int tamanoMax) {
        try {
            BufferedImage original = ImageIO.read(f);
            if (original == null) return null;
            int w = original.getWidth();
            int h = original.getHeight();
            if (w > tamanoMax || h > tamanoMax) {
                double scale = Math.min((double) tamanoMax / w, (double) tamanoMax / h);
                int nw = (int) (w * scale);
                int nh = (int) (h * scale);
                BufferedImage resized = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resized.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(original, 0, 0, nw, nh, null);
                g2.dispose();
                return resized;
            }
            return original;
        } catch (Exception e) { return null; }
    }

    private class BotonModerno extends JButton {
        private final Color colorBase;
        private final Color colorHover;

        public BotonModerno(String text, Color bg, Color fg) {
            super(text);
            this.colorBase = bg;
            this.colorHover = bg.brighter();
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(colorHover);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(colorBase);
                }
            });
            setBackground(colorBase);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    private class BotonFlecha extends JButton {
        private final boolean esDerecha;
        private final Color colorBase;
        private final Color colorHover;
        private boolean isHover = false;

        public BotonFlecha(boolean esDerecha, Color bg) {
            this.esDerecha = esDerecha;
            this.colorBase = bg;
            this.colorHover = bg.brighter();
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(isHover ? colorHover : colorBase);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            g2.setColor(Color.WHITE);
            int w = getWidth();
            int h = getHeight();
            int size = Math.min(w, h) / 4; 

            Polygon t = new Polygon();
            if (esDerecha) {
                t.addPoint(w/2 - size/2, h/2 - size);
                t.addPoint(w/2 + size/2, h/2);
                t.addPoint(w/2 - size/2, h/2 + size);
            } else { 
                t.addPoint(w/2 + size/2, h/2 - size);
                t.addPoint(w/2 - size/2, h/2);
                t.addPoint(w/2 + size/2, h/2 + size);
            }
            g2.fillPolygon(t);
            g2.dispose();
        }
    }

    private class PanelImagen extends JPanel {
        private BufferedImage imagenActual;

        public PanelImagen() {
            setBackground(COLOR_FONDO);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    repaint(); 
                }
            });
        }

        public void setImagen(BufferedImage img) {
            this.imagenActual = img;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenActual == null) return;
            dibujarImagenCentrada((Graphics2D) g, imagenActual, getWidth(), getHeight());
        }
    }

    private class PanelMiniatura extends JPanel {
        private BufferedImage imagenThumb;
        public PanelMiniatura() {
            setBackground(new Color(50, 50, 50));
        }
        public void setImagen(BufferedImage img) {
            this.imagenThumb = img;
            repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenThumb != null)
                dibujarImagenCentrada((Graphics2D) g, imagenThumb, getWidth(), getHeight());
        }
    }

    private void dibujarImagenCentrada(Graphics2D g2, BufferedImage img, int panelW, int panelH) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int imgW = img.getWidth();
        int imgH = img.getHeight();

        double escala = Math.min((double) panelW / imgW, (double) panelH / imgH);

        int drawW = (int) (imgW * escala);
        int drawH = (int) (imgH * escala);

        int x = (panelW - drawW) / 2;
        int y = (panelH - drawH) / 2;

        g2.drawImage(img, x, y, drawW, drawH, null);
    }
}