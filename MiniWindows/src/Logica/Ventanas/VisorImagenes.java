/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.User;
import Logica.ManejoUsuarios.UserLogged;
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
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author esteb
 */
public class VisorImagenes extends JPanel {

    private final ImagenesLogic logic;

    private int indiceActual = -1;

    private final PanelImagen panelImagenGrande = new PanelImagen();
    private final JPanel panelMiniaturas = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
    private final JLabel lblEstado = new JLabel("Sin imagen");

    public VisorImagenes() {
        this.logic = new ImagenesLogic();
        logic.cargarImagenes();

        setLayout(new BorderLayout(8, 8));
        crearBarraSuperior();

        add(panelImagenGrande, BorderLayout.CENTER);

        crearPanelInferior();
        setPreferredSize(new Dimension(900, 600));

        if (!logic.getImagenes().isEmpty()) {
            indiceActual = 0;
            reconstruirMiniaturas();
            mostrarImagenActual();
        }
        
    }

    private void crearBarraSuperior() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JLabel titulo = new JLabel("Galería HD Pro");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
        top.add(titulo, BorderLayout.WEST);

        JButton btnCargar = new JButton("Importar Imagen");

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
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton btnIzq = new JButton("◀");
        JButton btnDer = new JButton("▶");
        btnIzq.setPreferredSize(new Dimension(48, 80));
        btnDer.setPreferredSize(new Dimension(48, 80));

        btnIzq.addActionListener(e -> mostrarAnterior());
        btnDer.addActionListener(e -> mostrarSiguiente());

        bottomWrapper.add(btnIzq, BorderLayout.WEST);
        bottomWrapper.add(btnDer, BorderLayout.EAST);

        panelMiniaturas.setBackground(new Color(245, 245, 245));

        JScrollPane scrollThumbs = new JScrollPane(panelMiniaturas,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollThumbs.setPreferredSize(new Dimension(720, 110));
        scrollThumbs.setBorder(BorderFactory.createEmptyBorder());
        scrollThumbs.getHorizontalScrollBar().setUnitIncrement(16);

        bottomWrapper.add(scrollThumbs, BorderLayout.CENTER);

        lblEstado.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JPanel estadoPanel = new JPanel(new BorderLayout());
        estadoPanel.add(lblEstado, BorderLayout.WEST);

        JPanel inferior = new JPanel(new BorderLayout(4, 4));
        inferior.add(bottomWrapper, BorderLayout.CENTER);
        inferior.add(estadoPanel, BorderLayout.SOUTH);

        add(inferior, BorderLayout.SOUTH);
    }

    private void reconstruirMiniaturas() {
        List<File> imagenes = logic.getImagenes();

        panelMiniaturas.removeAll();

        // Creamos los bordes que usaremos
        LineBorder bordeSeleccion = new LineBorder(new Color(255, 204, 51), 4);
        LineBorder bordeNormal = new LineBorder(Color.GRAY, 1);

        for (int i = 0; i < imagenes.size(); i++) {
            final int idx = i;
            File f = imagenes.get(i);

            // Creamos el panel de miniatura
            PanelMiniatura panelThumb = new PanelMiniatura();
            panelThumb.setPreferredSize(new Dimension(100, 80));
            panelThumb.setBorder(idx == indiceActual ? bordeSeleccion : bordeNormal);
            panelThumb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Cargamos la imagen
            BufferedImage img = cargarImagenOptimizada(f, 150);
            panelThumb.setImagen(img);

            panelThumb.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    indiceActual = idx;
                    actualizarBordesMiniaturas();
                    mostrarImagenActual();
                }
            });

            panelMiniaturas.add(panelThumb);
        }

        panelMiniaturas.revalidate();
        panelMiniaturas.repaint();

        SwingUtilities.invokeLater(() -> {
            if (panelMiniaturas.getParent() instanceof JViewport) {
                JScrollPane scroll = (JScrollPane) panelMiniaturas.getParent().getParent();
                scroll.getHorizontalScrollBar().setValue(scroll.getHorizontalScrollBar().getMaximum());
            }
        });
    }

    private void actualizarBordesMiniaturas() {
        Component[] comps = panelMiniaturas.getComponents();
        LineBorder bordeSeleccion = new LineBorder(new Color(255, 204, 51), 4);
        LineBorder bordeNormal = new LineBorder(Color.GRAY, 1);

        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JPanel) {
                ((JPanel) comps[i]).setBorder(i == indiceActual ? bordeSeleccion : bordeNormal);
            }
        }
    }

    private void mostrarAnterior() {
        List<File> imagenes = logic.getImagenes();
        if (imagenes.isEmpty()) {
            return;
        }
        indiceActual = (indiceActual - 1 + imagenes.size()) % imagenes.size();
        actualizarBordesMiniaturas();
        mostrarImagenActual();
    }

    private void mostrarSiguiente() {
        List<File> imagenes = logic.getImagenes();
        if (imagenes.isEmpty()) {
            return;
        }
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
            lblEstado.setText((indiceActual + 1) + "/" + imagenes.size() + " — " + f.getName());
        } catch (Exception ex) {
            panelImagenGrande.setImagen(null);
            lblEstado.setText("Error al cargar la imagen.");
        }
    }

    // Método auxiliar para cargar imágenes sin saturar la RAM
    private BufferedImage cargarImagenOptimizada(File f, int tamanoMax) {
        try {
            BufferedImage original = ImageIO.read(f);
            if (original == null) {
                return null;
            }

            int w = original.getWidth();
            int h = original.getHeight();

            if (w > tamanoMax || h > tamanoMax) {
                double scale = Math.min((double) tamanoMax / w, (double) tamanoMax / h);
                int nw = (int) (w * scale);
                int nh = (int) (h * scale);

                BufferedImage resized = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resized.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.drawImage(original, 0, 0, nw, nh, null);
                g2.dispose();
                return resized;
            }
            return original;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * CLASE 1: Panel para la imagen GRANDE (Dinámico)
     */
    private class PanelImagen extends JPanel {

        private BufferedImage imagenActual;

        public PanelImagen() {
            setBackground(new Color(30, 30, 30));
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
            if (imagenActual == null) {
                return;
            }
            dibujarImagenCentrada((Graphics2D) g, imagenActual, getWidth(), getHeight());
        }
    }

    /**
     * CLASE 2: Panel para las MINIATURAS
     */
    private class PanelMiniatura extends JPanel {

        private BufferedImage imagenThumb;

        public PanelMiniatura() {
            setBackground(Color.WHITE);
            setOpaque(true);
        }

        public void setImagen(BufferedImage img) {
            this.imagenThumb = img;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenThumb == null) {
                return;
            }
            dibujarImagenCentrada((Graphics2D) g, imagenThumb, getWidth(), getHeight());
        }
    }

    /**
     * LÓGICA DE DIBUJADO COMPARTIDA
     */
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

    // --- MAIN PARA PRUEBA ---
    public static void main(String[] args) {
        // 1. Configurar un usuario de prueba para evitar NullPointerException en ImagenesLogic
        // Esto simula que ya se pasó por la ventana de LogIn.
        String testUserName = "TEST_USER";
        File testUserDir = new File("src" + File.separator + "Z" + File.separator + "Usuarios" + File.separator + testUserName);
        File testImagesDir = new File(testUserDir, "Mis Imagenes");

        // Creamos la estructura si no existe (similar a UserUtilities.createInitUserDir/createInicialDirs)
        if (!testImagesDir.exists()) {
            System.out.println("Creando directorios de prueba: " + testImagesDir.getAbsolutePath());
            testImagesDir.mkdirs();
        }

        // Asignamos el usuario de prueba al Singleton UserLogged
        User testUser = new User(testUserName, "pass"); // Asumiendo que User extiende UserUtilities
        UserLogged.getInstance().setUserLogged(testUser);

        SwingUtilities.invokeLater(() -> {
            try {
                // Opcional: Establecer Look and Feel del sistema para mejor apariencia
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                /* Ignorar */
            }

            JFrame f = new JFrame("Galería");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Instanciamos y agregamos tu panel
            f.getContentPane().add(new VisorImagenes());

            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);

            System.out.println("\n*** PRUEBA INICIADA ***");
            System.out.println("Puedes importar imágenes a la carpeta:");
            System.out.println(testImagesDir.getAbsolutePath());
        });
    }

}
