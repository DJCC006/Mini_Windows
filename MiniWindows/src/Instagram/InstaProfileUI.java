/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author esteb
 */
public class InstaProfileUI extends JPanel {

    private final String username;
    private JLabel lblFoto;
    private JLabel lblName;
    private JLabel lblInfo;
    private JLabel lblStats;
    private JPanel gridFotos;

    private final Color COLOR_BG = Color.BLACK;
    private final Color COLOR_BTN = new Color(255, 69, 0);
    private final Color COLOR_TEXT = Color.WHITE;
    private final Color COLOR_BORDER = new Color(100, 100, 100);
    private final Font FONT_TEXT = new Font("Comic Sans MS", Font.PLAIN, 12);

    public InstaProfileUI(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        
        setPreferredSize(new Dimension(400, 650));
        setBackground(COLOR_BG);

        JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setBackground(COLOR_BG);
        
        contentContainer.add(crearPanelSuperior(), BorderLayout.NORTH);
        
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(COLOR_BG);
        gridWrapper.add(crearPanelGrid(), BorderLayout.NORTH);
        
        contentContainer.add(gridWrapper, BorderLayout.CENTER); 
        
        JScrollPane scroll = new JScrollPane(contentContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setBackground(COLOR_BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scroll, BorderLayout.CENTER);
        
        add(crearBarraNavegacionInferior(), BorderLayout.SOUTH);
        
        cargarDatosPerfil();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_BG);
        panel.setPreferredSize(new Dimension(400, 260));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JButton btnLogout = new JButton("Huir");
        btnLogout.setBounds(310, 10, 70, 25);
        estilizarBotonPequeno(btnLogout);
        btnLogout.addActionListener(e -> cerrarSesion());
        panel.add(btnLogout);
        
        JLabel lblTitle = new JLabel("@" + username);
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 20));
        lblTitle.setForeground(COLOR_TEXT);
        lblTitle.setBounds(15, 10, 250, 30);
        panel.add(lblTitle);

        lblFoto = new JLabel("Sin Rostro");
        lblFoto.setBounds(15, 50, 90, 90);
        lblFoto.setBorder(new LineBorder(COLOR_BTN, 3)); 
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setForeground(Color.GRAY);
        panel.add(lblFoto);

        lblStats = new JLabel("0 Evidencias   0 Acosadores   0 Víctimas");
        lblStats.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
        lblStats.setForeground(COLOR_TEXT);
        lblStats.setHorizontalAlignment(SwingConstants.CENTER);
        lblStats.setBounds(120, 50, 260, 60);
        panel.add(lblStats);
        
        JButton btnEdit = new JButton("Alterar Realidad");
        btnEdit.setBounds(130, 110, 240, 30);
        estilizarBoton(btnEdit);
        panel.add(btnEdit);

        lblName = new JLabel("Cargando nombre...");
        lblName.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblName.setForeground(COLOR_TEXT);
        lblName.setBounds(15, 150, 350, 20);
        panel.add(lblName);

        lblInfo = new JLabel("Cargando datos...");
        lblInfo.setFont(FONT_TEXT);
        lblInfo.setForeground(Color.LIGHT_GRAY);
        lblInfo.setBounds(15, 175, 360, 70);
        lblInfo.setVerticalAlignment(SwingConstants.TOP);
        panel.add(lblInfo);

        return panel;
    }

    private JPanel crearPanelGrid() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(COLOR_BG);
        
        JLabel lblGridTitle = new JLabel(" Tus Crímenes (Posts)");
        lblGridTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblGridTitle.setForeground(COLOR_BTN);
        lblGridTitle.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        container.add(lblGridTitle, BorderLayout.NORTH);

        gridFotos = new JPanel(new GridLayout(0, 3, 2, 2)); 
        gridFotos.setBackground(COLOR_BG);
        
        cargarPostsEnGrid();
        
        container.add(gridFotos, BorderLayout.CENTER);
        return container;
    }
    
    private void cargarPostsEnGrid() {
        gridFotos.removeAll(); 
        try {
            instaManager manager = instaController.getInstance().getInsta();
            ArrayList<String[]> posts = manager.getPosts(username);
            
            if (posts.isEmpty()) {
                JLabel lblVacio = new JLabel("Nada que ver aqui...", SwingConstants.CENTER);
                lblVacio.setForeground(Color.GRAY);
                lblVacio.setPreferredSize(new Dimension(380, 50));
                gridFotos.add(lblVacio);
            } else {
                for (int i = 0; i < posts.size(); i++) {
                    String[] post = posts.get(i);
                    String rutaImg = post[0];
                    // Indice final para usar en la lambda
                    final int index = i; 
                    
                    JPanel frameFoto = new JPanel(new BorderLayout());
                    frameFoto.setBackground(new Color(20, 20, 20));
                    frameFoto.setPreferredSize(new Dimension(130, 130)); 
                    frameFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    JLabel lblImg = new JLabel();
                    lblImg.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    ImageIcon icon = recortarImagenCuadrada(rutaImg, 130);
                    if (icon != null) {
                        lblImg.setIcon(icon);
                    } else {
                        lblImg.setText("?");
                        lblImg.setForeground(Color.GRAY);
                    }
                    
                    frameFoto.add(lblImg, BorderLayout.CENTER);
                    
                    frameFoto.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            // AQUÍ ESTÁ LA MAGIA: Pasamos toda la lista y el índice
                            abrirPostFeed(posts, index);
                        }
                    });
                    
                    gridFotos.add(frameFoto);
                }
            }
            
        } catch (IOException e) {
             gridFotos.add(new JLabel("Error cargando"));
        }
        gridFotos.revalidate();
        gridFotos.repaint();
    }
    
    private ImageIcon recortarImagenCuadrada(String ruta, int size) {
        try {
            File f = new File(ruta);
            if (!f.exists()) return null;
            ImageIcon originalIcon = new ImageIcon(ruta);
            if (originalIcon.getIconWidth() <= 0) return null;

            Image img = originalIcon.getImage();
            BufferedImage buffered = new BufferedImage(
                originalIcon.getIconWidth(), 
                originalIcon.getIconHeight(), 
                BufferedImage.TYPE_INT_ARGB
            );
            Graphics g = buffered.getGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();

            int w = buffered.getWidth();
            int h = buffered.getHeight();
            int cropSize = Math.min(w, h);
            int x = (w - cropSize) / 2;
            int y = (h - cropSize) / 2;

            BufferedImage cropped = buffered.getSubimage(x, y, cropSize, cropSize);
            Image scaled = cropped.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void abrirPostFeed(ArrayList<String[]> allPosts, int startIndex) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.setContentPane(new InstaPostUI(this.username, allPosts, startIndex)); 
            frame.pack();
            frame.revalidate();
            frame.repaint();
        }
    }
    
    private JPanel crearBarraNavegacionInferior() {
        JPanel bar = new JPanel(new GridLayout(1, 4)); 
        bar.setBackground(new Color(20, 20, 20)); 
        bar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_BTN)); 
        bar.setPreferredSize(new Dimension(400, 60)); 
        
        bar.add(crearBotonNav("Inicio", "🏠"));
        bar.add(crearBotonNav("Buscar", "🔎"));
        
        JButton btnSubir = crearBotonNav("Subir", "⬆");
        btnSubir.setForeground(COLOR_BTN);
        btnSubir.addActionListener(e -> subirPost());
        bar.add(btnSubir);
        
        JButton btnPerfil = crearBotonNav("Perfil", "👤");
        btnPerfil.setForeground(COLOR_BTN); 
        bar.add(btnPerfil);
        
        return bar;
    }
    
    private void subirPost() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona la evidencia");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            
            String caption = JOptionPane.showInputDialog(this, "Escribe una descripción:", "Nuevo Post", JOptionPane.PLAIN_MESSAGE);
            if (caption == null) caption = "";
            
            try {
                instaManager manager = instaController.getInstance().getInsta();
                manager.setLoggedUser(username);
                manager.addPost(path, username, caption);
                
                JOptionPane.showMessageDialog(this, "Subido con éxito.");
                cargarPostsEnGrid(); 
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al subir: " + ex.getMessage());
            }
        }
    }
    
    private JButton crearBotonNav(String texto, String emoji) {
        JButton btn = new JButton(emoji);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20)); 
        btn.setToolTipText(texto); 
        btn.setBackground(new Color(20, 20, 20));
        btn.setForeground(Color.GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarDatosPerfil() {
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;
            manager.setLoggedUser(username);

            String rutaFoto = manager.getProfilePic(username);
            if (rutaFoto != null && !rutaFoto.isEmpty() && !rutaFoto.equals("futura referencia de imagen aqui")) {
                ImageIcon icon = recortarImagenCuadrada(rutaFoto, 90);
                if (icon != null) {
                    lblFoto.setIcon(icon);
                    lblFoto.setText("");
                }
            }

            String realName = manager.getRealName(username);
            lblName.setText(realName != null ? realName : "Sin Nombre");

            int edad = manager.getAge(username);
            char genero = manager.getGender(username); 
            String fecha = manager.getEntryDate(username);
            String generoStr = (genero == 'M') ? "Demonio" : (genero == 'F' ? "Bruja" : "Ente");
            
            lblInfo.setText("<html>Edad: " + edad + " años<br>Clase: " + generoStr + "<br>Desde: " + fecha + "</html>");

        } catch (IOException e) {
            lblName.setText("Error de conexión.");
        }
    }

    private void cerrarSesion() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.setContentPane(new InstaLoginUI()); 
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        }
    }

    private void estilizarBoton(JButton btn) {
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(COLOR_TEXT);
        btn.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estilizarBotonPequeno(JButton btn) {
        btn.setBackground(COLOR_BTN);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}