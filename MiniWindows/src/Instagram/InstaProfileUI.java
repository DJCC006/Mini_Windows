/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import Logica.Ventanas.genFondos;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.LineBorder;

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

    // COLORES "OUTSTAGRAM"
    private final Color COLOR_BG = Color.BLACK;
    private final Color COLOR_BTN = new Color(255, 69, 0);
    private final Color COLOR_TEXT = Color.WHITE;
    private final Color COLOR_BORDER = new Color(100, 100, 100);
    private final Font FONT_TEXT = new Font("Comic Sans MS", Font.PLAIN, 12);
    
    private genFondos panelFondo;
    public InstaProfileUI(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        
        setPreferredSize(new Dimension(400, 650));
        setBackground(COLOR_BG);

        JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setBackground(COLOR_BG);
        
        contentContainer.add(crearPanelSuperior(), BorderLayout.NORTH);
        contentContainer.add(crearPanelGrid(), BorderLayout.CENTER); 
        
        JScrollPane scroll = new JScrollPane(contentContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setBackground(COLOR_BG);
        
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
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
        
        for (int i = 0; i < 9; i++) {
            JPanel placeholder = new JPanel(new BorderLayout());
            placeholder.setBackground(new Color(20, 20, 20));
            placeholder.setBorder(BorderFactory.createLineBorder(new Color(50,50,50)));
            placeholder.setPreferredSize(new Dimension(100, 100)); 
            
            JLabel lbl = new JLabel("?", SwingConstants.CENTER);
            lbl.setForeground(new Color(50, 50, 50));
            lbl.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            placeholder.add(lbl, BorderLayout.CENTER);
            
            gridFotos.add(placeholder);
        }
        
        container.add(gridFotos, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel crearBarraNavegacionInferior() {
        JPanel bar = new JPanel(new GridLayout(1, 4)); 
        bar.setBackground(new Color(20, 20, 20)); 
        bar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_BTN)); 
        bar.setPreferredSize(new Dimension(400, 60));
        
        bar.add(crearBotonNav("Inicio", "🏠"));
        bar.add(crearBotonNav("Buscar", "🔎"));
        bar.add(crearBotonNav("Subir", "⬆"));
        
        JButton btnPerfil = crearBotonNav("Perfil", "👤");
        btnPerfil.setForeground(COLOR_BTN);
        bar.add(btnPerfil);
        
        return bar;
    }
    
    private JButton crearBotonNav(String texto, String emoji) {
        JButton btn = new JButton(texto);
        

        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        
        btn.setText(emoji);
        btn.setToolTipText(texto);
        btn.setBackground(new Color(20, 20, 20));
        btn.setForeground(Color.GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        
        return btn;
    }


    private void cargarDatosPerfil() {
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;

            String rutaFoto = manager.getProfilePic(username);
            if (rutaFoto != null && !rutaFoto.isEmpty() && !rutaFoto.equals("futura referencia de imagen aqui")) {
                File f = new File(rutaFoto);
                if (f.exists()) {
                    ImageIcon icon = new ImageIcon(rutaFoto);
                    Image img = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(img));
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
            
            lblStats.setText("0 Evidencias   0 Acosadores   0 Víctimas");

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
    
//    
//    private JInternalFrame createLogWindow(){
//        JInternalFrame Frame = new JInternalFrame("OUTSTAGRAM", true, true, true, true);
//         //InstaLoginUI instaPanel = new InstaLoginUI(panelFondo);
//         Frame.add(instaPanel, BorderLayout.CENTER);
//         
//         Frame.setSize(400,650);
//         Frame.setLocation(100, 50);
//         Frame.setVisible(true);
//        return Frame;
//    }
//    
    
}