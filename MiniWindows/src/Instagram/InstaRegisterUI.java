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

    private final Color COLOR_BG = Color.BLACK;
    private final Color COLOR_BTN = new Color(255, 69, 0);
    private final Color COLOR_TEXT = Color.WHITE;
    private final Color COLOR_BORDER = new Color(100, 100, 100);
    private final Font FONT_CAOS = new Font("Comic Sans MS", Font.BOLD, 12);
    private final Font FONT_TITLE = new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 28);

    public InstaRegisterUI() {
        setLayout(null);
        setBackground(COLOR_BG);
        setPreferredSize(new Dimension(400, 650));

        initComponentes();
    }

    private void initComponentes() {
        JLabel lblTitulo = new JLabel("Únete al Caos");
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(COLOR_TEXT);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(40, 20, 320, 30);
        add(lblTitulo);

        JLabel lblSub = new JLabel("y stalkea a tus enemigos.");
        lblSub.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblSub.setForeground(Color.LIGHT_GRAY);
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        lblSub.setBounds(40, 50, 320, 30);
        add(lblSub);

        lblFotoPreview = new JLabel();
        lblFotoPreview.setBounds(150, 90, 100, 100);
        lblFotoPreview.setBorder(new LineBorder(COLOR_BTN, 2));
        lblFotoPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPreview.setForeground(COLOR_TEXT);
        lblFotoPreview.setFont(FONT_CAOS);
        lblFotoPreview.setText("Tu Cara");
        add(lblFotoPreview);

        JButton btnAddFoto = new JButton("Subir Evidencia");
        btnAddFoto.setBounds(130, 200, 140, 25);
        btnAddFoto.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        btnAddFoto.setBackground(Color.DARK_GRAY);
        btnAddFoto.setForeground(Color.WHITE);
        btnAddFoto.setFocusPainted(false);
        btnAddFoto.addActionListener(e -> seleccionarFoto());
        add(btnAddFoto);

        int yStart = 240;
        int gap = 50;

        txtNombre = crearCampoTexto("Nombre de Mortal", yStart);
        txtUsername = crearCampoTexto("Alias Malvado", yStart + gap);
        txtPassword = crearCampoPassword("Secreto Oscuro", yStart + gap * 2);

        // Edad
        txtEdad = new JTextField();
        txtEdad.setBounds(50, yStart + gap * 3, 140, 40);
        estilizarCampo(txtEdad, "Edad");
        add(txtEdad);

        cbGenero = new JComboBox<>(new String[]{"M", "F"});
        cbGenero.setBounds(210, yStart + gap * 3, 140, 40);
        cbGenero.setBackground(new Color(30, 30, 30));
        cbGenero.setForeground(COLOR_TEXT);
        cbGenero.setFont(FONT_CAOS);
        cbGenero.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER), "Género", 0, 0, new Font("Comic Sans MS", Font.PLAIN, 12), Color.LIGHT_GRAY));
        add(cbGenero);

        btnRegistrar = new JButton("Invocar Cuenta");
        btnRegistrar.setBounds(50, yStart + gap * 4 + 10, 300, 35);
        btnRegistrar.setBackground(COLOR_BTN);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> realizarRegistro());
        add(btnRegistrar);

        lblBackLogin = new JLabel("¿Ya sirves al caos? Entra aquí");
        lblBackLogin.setBounds(50, 600, 300, 30);
        lblBackLogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblBackLogin.setForeground(COLOR_BTN);
        lblBackLogin.setFont(new Font("Comic Sans MS", Font.ITALIC, 13));
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

    private void estilizarCampo(JTextField txt, String titulo) {
        txt.setBackground(new Color(30, 30, 30));
        txt.setForeground(COLOR_TEXT);
        txt.setCaretColor(COLOR_TEXT);
        txt.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                titulo,
                0,
                0,
                new Font("Comic Sans MS", Font.PLAIN, 12),
                Color.LIGHT_GRAY));
    }

    private JTextField crearCampoTexto(String titulo, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(50, y, 300, 40);
        estilizarCampo(txt, titulo);
        add(txt);
        return txt;
    }

    private JPasswordField crearCampoPassword(String titulo, int y) {
        JPasswordField txt = new JPasswordField();
        txt.setBounds(50, y, 300, 40);
        estilizarCampo(txt, titulo);
        add(txt);
        return txt;
    }

    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Evidencia");
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
            JOptionPane.showMessageDialog(this, "¡El vacío no es aceptable! Llena todo.", "Error Fatal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad < 13) {
                JOptionPane.showMessageDialog(this, "Eres muy joven para la oscuridad (Min 13).", "Edad Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número, no jeroglíficos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            instaManager manager = instaController.getInstance().getInsta();

            if (manager.checkUserExistance(username)) {
                JOptionPane.showMessageDialog(this, "Ese alias ya fue reclamado por otro demonio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.addNewUser(nombre, genero, username, password, edad);

            JOptionPane.showMessageDialog(this, "¡Bienvenido a la legión! Tu alma es nuestra.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtNombre.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
            txtEdad.setText("");
            lblFotoPreview.setIcon(null);
            lblFotoPreview.setText("Tu Cara");
            rutaFotoSeleccionada = "";

            lblBackLogin.dispatchEvent(new MouseEvent(lblBackLogin, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false));

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error catastrófico: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

}
