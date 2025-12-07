/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author esteb
 */
public class InstaEditProfileUI extends JPanel {

    private final String currentUser;

    private final Color COLOR_BG = Color.BLACK;
    private final Color COLOR_BTN = new Color(255, 69, 0);
    private final Color COLOR_TEXT = Color.WHITE;
    private final Color COLOR_BORDER = new Color(100, 100, 100);
    private final Font FONT_TITLE = new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 20);
    private final Font FONT_CAOS = new Font("Comic Sans MS", Font.PLAIN, 12);

    private JPanel panelCentral;
    private JTextField txtBuscar;
    private DefaultListModel<String> listModel;
    private JList<String> resultList;

    private JTextField txtEntrar;
    private JButton btnToggleCuenta;

    public InstaEditProfileUI(String currentUser) {
        this.currentUser = currentUser;

        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setPreferredSize(new Dimension(400, 650));

        add(crearHeader(), BorderLayout.NORTH);

        panelCentral = new JPanel(new CardLayout());
        panelCentral.setBackground(COLOR_BG);

        panelCentral.add(crearPanelBuscar(), "BUSCAR");
        panelCentral.add(crearPanelEntrar(), "ENTRAR");
        panelCentral.add(crearPanelDesactivar(), "CUENTA");

        add(panelCentral, BorderLayout.CENTER);
        add(crearBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel p = new JPanel(null);
        p.setPreferredSize(new Dimension(400, 60));
        p.setBackground(COLOR_BG);

        JLabel lblBack = new JLabel("←");
        lblBack.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBack.setForeground(COLOR_BTN);
        lblBack.setBounds(10, 12, 30, 30);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volverAlPerfilPropio();
            }
        });
        p.add(lblBack);

        JLabel title = new JLabel("Alterar Realidad");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT);
        title.setBounds(50, 10, 300, 40);
        p.add(title);

        return p;
    }

    private JPanel crearPanelBuscar() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(COLOR_BG);

        JPanel arriba = new JPanel(null);
        arriba.setPreferredSize(new Dimension(380, 70));
        arriba.setBackground(COLOR_BG);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(10, 10, 260, 40);
        estilizarCampo(txtBuscar);
        txtBuscar.addActionListener(e -> ejecutarBusqueda());
        arriba.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(280, 10, 90, 40);
        estilizarBoton(btnBuscar);
        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        arriba.add(btnBuscar);

        p.add(arriba, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setBackground(new Color(20, 20, 20));
        resultList.setForeground(COLOR_TEXT);
        resultList.setFont(FONT_CAOS);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.setBorder(new LineBorder(COLOR_BORDER));
        JScrollPane sp = new JScrollPane(resultList);
        sp.setBorder(null);

        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String sel = resultList.getSelectedValue();
                    if (sel != null && !sel.equals("No se encontraron usuarios")) {
                        String username = sel.split(" - ")[0];
                        abrirPerfilExternoconRetroceso(username);
                    }
                }
            }
        });

        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelEntrar() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(COLOR_BG);

        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(380, 70));
        top.setBackground(COLOR_BG);

        txtEntrar = new JTextField();
        txtEntrar.setBounds(10, 10, 360, 40);
        estilizarCampo(txtEntrar);
        txtEntrar.addActionListener(e -> {
            String target = txtEntrar.getText().trim();
            if (!target.isEmpty()) {
                abrirPerfilExternoconRetroceso(target);
            }
        });
        top.add(txtEntrar);

        p.add(top, BorderLayout.NORTH);

        JPanel infoHolder = new JPanel();
        infoHolder.setLayout(new BoxLayout(infoHolder, BoxLayout.Y_AXIS));
        infoHolder.setBackground(COLOR_BG);
        infoHolder.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel hint = new JLabel("Escribe un username y presiona Enter para ir al perfil.");
        hint.setForeground(Color.LIGHT_GRAY);
        hint.setFont(FONT_CAOS);
        infoHolder.add(hint);

        p.add(infoHolder, BorderLayout.CENTER);

        return p;
    }

    private JPanel crearPanelDesactivar() {
        JPanel p = new JPanel(null);
        p.setBackground(COLOR_BG);

        btnToggleCuenta = new JButton("Desactivar / Activar cuenta");
        btnToggleCuenta.setBounds(40, 80, 320, 40);
        estilizarBoton(btnToggleCuenta);
        btnToggleCuenta.addActionListener(e -> toggleCuenta());
        p.add(btnToggleCuenta);

        JLabel lblInfo = new JLabel("<html><div style='width:320px;color:lightgray'>" +
                "Desactivar ocultará tu cuenta de búsquedas y hará que otros no vean tus comentarios. " +
                "Si la cuenta está desactivada, este botón la reactivará automáticamente." +
                "</div></html>");
        lblInfo.setBounds(40, 140, 320, 80);
        lblInfo.setForeground(Color.LIGHT_GRAY);
        lblInfo.setFont(FONT_CAOS);
        p.add(lblInfo);

        return p;
    }

    private JPanel crearBarraInferior() {
        JPanel bar = new JPanel(new GridLayout(1, 1));
        bar.setPreferredSize(new Dimension(400, 50));
        bar.setBackground(new Color(20, 20, 20));
        bar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_BTN));

        JLabel hint = new JLabel("Doble clic en la lista para entrar al perfil", SwingConstants.CENTER);
        hint.setForeground(Color.GRAY);
        hint.setFont(FONT_CAOS);
        bar.add(hint);

        return bar;
    }


    private void ejecutarBusqueda() {
        String q = txtBuscar.getText().trim();
        listModel.clear();
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;

            ArrayList<String> res = manager.searchUsers(q);
            if (res == null || res.isEmpty()) {
                listModel.addElement("No se encontraron usuarios");
                return;
            }

            for (String u : res) {
                if (u.equalsIgnoreCase(currentUser)) continue;

                manager.setLoggedUser(currentUser);
                boolean sigo = manager.isFollowing(u);
                String linea = u + " - " + (sigo ? "LO SIGO" : "NO LO SIGO");
                listModel.addElement(linea);
            }

            if (listModel.isEmpty()) {
                listModel.addElement("No se encontraron usuarios");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error buscando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirPerfilExternoconRetroceso(String username) {
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;

            if (!manager.checkUserExistance(username)) {
                JOptionPane.showMessageDialog(this, "Ese usuario no existe o está desactivado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Window window = SwingUtilities.getWindowAncestor(this);
            if (!(window instanceof JFrame)) return;
            JFrame frame = (JFrame) window;

            VisibilidadProfileUI vp = new VisibilidadProfileUI(username, currentUser);
            frame.setContentPane(vp);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir perfil: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleCuenta() {
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;

            boolean estado = manager.getStatusUser(currentUser);
            if (estado) {
                int resp = JOptionPane.showConfirmDialog(this, "¿Deseas desactivar tu cuenta? (se ocultará de búsquedas)", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    manager.desactivateUser(currentUser);
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada.");
                }
            } else {
                boolean ok = manager.activateUser(currentUser);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cuenta reactivada.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo activar la cuenta.");
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en operación de cuenta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlPerfilPropio() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.setContentPane(new InstaProfileUI(currentUser)); // vuelve al perfil propio
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.revalidate();
            frame.repaint();
        }
    }

    private void estilizarCampo(JTextField txt) {
        txt.setBackground(new Color(30, 30, 30));
        txt.setForeground(COLOR_TEXT);
        txt.setCaretColor(COLOR_TEXT);
        txt.setFont(FONT_CAOS);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)));
    }

    private void estilizarBoton(JButton btn) {
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(COLOR_TEXT);
        btn.setFont(FONT_CAOS);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
