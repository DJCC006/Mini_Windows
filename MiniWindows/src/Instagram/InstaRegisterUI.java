/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author esteb
 */
public class InstaRegisterUI extends JPanel {
    
    private JTextField txtNombre;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtEdad;
    private JComboBox<String> cbGenero;
    
    private JLabel lblFotoPreview;
    private String rutaFotoSeleccionada = ""; 
    
    private JButton btnRegistrar;
    private JLabel lblBackLogin;


    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_BTN = new Color(0, 149, 246);
    private final Color COLOR_TEXT_SEC = Color.GRAY;

    public InstaRegisterUI() {
        setLayout(null);
        setBackground(COLOR_BG);
        setPreferredSize(new Dimension(400, 650));

        initComponentes();
    }

    private void initComponentes() {
        JLabel lblTitulo = new JLabel("Regístrate para ver fotos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.DARK_GRAY);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(40, 20, 320, 30);
        add(lblTitulo);
        
        JLabel lblSub = new JLabel("de tus amigos.");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSub.setForeground(Color.DARK_GRAY);
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        lblSub.setBounds(40, 50, 320, 30);
        add(lblSub);


        lblFotoPreview = new JLabel();
        lblFotoPreview.setBounds(150, 90, 100, 100);
        lblFotoPreview.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        lblFotoPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPreview.setText("Foto");
        // lblFotoPreview.setIcon(new ImageIcon("ruta/default.png")); 
        add(lblFotoPreview);

        JButton btnAddFoto = new JButton("Agregar Foto");
        btnAddFoto.setBounds(140, 200, 120, 25);
        btnAddFoto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnAddFoto.setBackground(Color.WHITE);
        btnAddFoto.addActionListener(e -> seleccionarFoto());
        add(btnAddFoto);

        int yStart = 240;
        int gap = 50;
        
        txtNombre = crearCampoTexto("Nombre completo", yStart);
        txtUsername = crearCampoTexto("Nombre de usuario", yStart + gap);
        txtPassword = crearCampoPassword("Contraseña", yStart + gap * 2);
        
        txtEdad = new JTextField();
        txtEdad.setBounds(50, yStart + gap * 3, 140, 40);
        txtEdad.setBorder(BorderFactory.createTitledBorder("Edad"));
        add(txtEdad);
        
        cbGenero = new JComboBox<>(new String[]{"M", "F"});
        cbGenero.setBounds(210, yStart + gap * 3, 140, 40);
        cbGenero.setBorder(BorderFactory.createTitledBorder("Género"));
        cbGenero.setBackground(Color.WHITE);
        add(cbGenero);

        btnRegistrar = new JButton("Registrarte");
        btnRegistrar.setBounds(50, yStart + gap * 4 + 10, 300, 35);
        btnRegistrar.setBackground(COLOR_BTN);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> realizarRegistro());
        add(btnRegistrar);

        lblBackLogin = new JLabel("¿Tienes una cuenta? Iniciar sesión");
        lblBackLogin.setBounds(50, 600, 300, 30);
        lblBackLogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblBackLogin.setForeground(COLOR_BTN);
        lblBackLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBackLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Window window = SwingUtilities.getWindowAncestor(InstaRegisterUI.this);
                if (window instanceof JFrame) {
                    JFrame frame = (JFrame) window;
                    frame.setContentPane(new InstaLoginUI());
                    frame.pack(); 
                    frame.setLocationRelativeTo(null);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
        add(lblBackLogin);
    }

    private JTextField crearCampoTexto(String titulo, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(50, y, 300, 40);
        txt.setBorder(BorderFactory.createTitledBorder(titulo));
        add(txt);
        return txt;
    }
    
    private JPasswordField crearCampoPassword(String titulo, int y) {
        JPasswordField txt = new JPasswordField();
        txt.setBounds(50, y, 300, 40);
        txt.setBorder(BorderFactory.createTitledBorder(titulo));
        add(txt);
        return txt;
    }

    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Foto de Perfil");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = file.getAbsolutePath();
            
            ImageIcon icon = new ImageIcon(rutaFotoSeleccionada);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblFotoPreview.setIcon(new ImageIcon(img));
            lblFotoPreview.setText("");
        }
    }

    private void realizarRegistro() {
        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String edadStr = txtEdad.getText().trim();
        char genero = cbGenero.getSelectedItem().toString().charAt(0);

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || edadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad < 13) {
                 JOptionPane.showMessageDialog(this, "Debes tener al menos 13 años.", "Edad inválida", JOptionPane.WARNING_MESSAGE);
                 return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            instaManager manager = instaController.getInstance().getInsta();
            
            if (manager.checkUserExistance(username)) {
                JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            manager.addNewUser(nombre, genero, username, password, edad);
            
            
            JOptionPane.showMessageDialog(this, "¡Cuenta creada exitosamente!", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
            
            txtNombre.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
            txtEdad.setText("");
            lblFotoPreview.setIcon(null);
            rutaFotoSeleccionada = "";


        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Main de prueba
    public static void main(String[] args) {
        instaManager manager = new instaManager();
        instaController.getInstance().setInsta(manager);
        
        JFrame frame = new JFrame("Registro Instagram Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new InstaRegisterUI());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}