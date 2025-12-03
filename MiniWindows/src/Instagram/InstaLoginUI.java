/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author esteb
 */
public class InstaLoginUI extends JPanel {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JLabel lblRegister;
    
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_BTN = new Color(0, 149, 246);
    private final Color COLOR_BORDER = new Color(219, 219, 219);

    public InstaLoginUI() {
        setLayout(null);
        setBackground(COLOR_BG);
        setPreferredSize(new Dimension(400, 500));

        initComponentes();
    }

    private void initComponentes() {
        JLabel lblLogo = new JLabel("Instagram");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 36)); 
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBounds(50, 40, 300, 50);
        add(lblLogo);

        txtUser = new JTextField();
        txtUser.setBounds(50, 120, 300, 40);
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)));
        txtUser.setToolTipText("Teléfono, usuario o correo electrónico");
        add(new JLabel("Usuario") {{ setBounds(50, 100, 100, 20); setForeground(Color.GRAY); }});
        add(txtUser);

        txtPass = new JPasswordField();
        txtPass.setBounds(50, 190, 300, 40);
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)));
        add(new JLabel("Contraseña") {{ setBounds(50, 170, 100, 20); setForeground(Color.GRAY); }});
        add(txtPass);

        btnLogin = new JButton("Iniciar sesión");
        btnLogin.setBounds(50, 250, 300, 35);
        btnLogin.setBackground(COLOR_BTN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogin.addActionListener(e -> realizarLogin());
        add(btnLogin);

        JSeparator sep = new JSeparator();
        sep.setBounds(50, 310, 300, 10);
        sep.setForeground(COLOR_BORDER);
        add(sep);

        lblRegister = new JLabel("¿No tienes una cuenta? Regístrate");
        lblRegister.setBounds(50, 330, 300, 30);
        lblRegister.setHorizontalAlignment(SwingConstants.CENTER);
        lblRegister.setForeground(COLOR_BTN);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Window window = SwingUtilities.getWindowAncestor(InstaLoginUI.this);
                if (window instanceof JFrame) {
                    JFrame frame = (JFrame) window;
                    frame.setContentPane(new InstaRegisterUI()); // Cambiamos el contenido
                    frame.pack(); // Ajustamos el tamaño a la nueva ventana (el registro es más alto)
                    frame.setLocationRelativeTo(null); // Re-centramos
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
        add(lblRegister);
    }


    private void realizarLogin() {
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            instaManager manager = instaController.getInstance().getInsta();
            
            if (manager == null) {
                JOptionPane.showMessageDialog(this, "Error crítico: El sistema Insta no está inicializado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            String storedPass = manager.getPassword(username);

            if (storedPass == null) {
                JOptionPane.showMessageDialog(this, "El usuario no existe.", "Login Fallido", JOptionPane.ERROR_MESSAGE);
            } else {

                if (storedPass.equals(password)) {
                    JOptionPane.showMessageDialog(this, "¡Bienvenido " + username + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    
                    if (!manager.getStatusUser(username)) {
                         JOptionPane.showMessageDialog(this, "Tu cuenta estaba desactivada. Se ha reactivado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Login Fallido", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer la base de datos: " + ex.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        instaManager manager = new instaManager();
        instaController.getInstance().setInsta(manager);
        
        JFrame frame = new JFrame("Instagram Login Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new InstaLoginUI());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}