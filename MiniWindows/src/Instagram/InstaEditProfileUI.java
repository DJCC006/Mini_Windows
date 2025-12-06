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

    // Components
    private JToggleButton toggleBuscar;
    private JPanel panelCentral;
    private JTextField txtBuscar;
    private DefaultListModel<String> listModel;
    private JList<String> resultList;
    private JTextField txtEntrar;
    private JPanel panelPerfilDetalle;
    private JLabel lblNombreDet, lblGeneroDet, lblEdadDet, lblIngresoDet, lblFollowersDet, lblFollowingDet, lblSigoDet;
    private JButton btnFollowToggle, btnVerTweets;
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

        JLabel title = new JLabel("Editar Perfil");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT);
        title.setBounds(15, 10, 300, 40);
        p.add(title);

        // Toggle visual entre Buscar/Entrar
        toggleBuscar = new JToggleButton("Buscar personas");
        toggleBuscar.setBounds(250, 12, 130, 30);
        estilizarToggle(toggleBuscar);
        toggleBuscar.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelCentral.getLayout();
            if (toggleBuscar.isSelected()) {
                cl.show(panelCentral, "BUSCAR");
            } else {
                cl.show(panelCentral, "ENTRAR");
            }
        });
        // por defecto seleccionado BUSCAR
        toggleBuscar.setSelected(true);
        p.add(toggleBuscar);

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

        // doble click -> abrir perfil
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String sel = resultList.getSelectedValue();
                    if (sel != null) abrirPerfilDesdeBusqueda(sel);
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
        txtEntrar.setBounds(10, 10, 260, 40);
        estilizarCampo(txtEntrar);
        top.add(txtEntrar);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(280, 10, 90, 40);
        estilizarBoton(btnEntrar);
        btnEntrar.addActionListener(e -> buscarYMostrarPerfil());
        top.add(btnEntrar);

        p.add(top, BorderLayout.NORTH);

        // Panel detalle de perfil
        panelPerfilDetalle = new JPanel();
        panelPerfilDetalle.setLayout(new BoxLayout(panelPerfilDetalle, BoxLayout.Y_AXIS));
        panelPerfilDetalle.setBackground(COLOR_BG);
        panelPerfilDetalle.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblNombreDet = new JLabel("Nombre: ");
        lblNombreDet.setForeground(COLOR_TEXT);
        lblNombreDet.setFont(FONT_CAOS);

        lblGeneroDet = new JLabel("Genero: ");
        lblGeneroDet.setForeground(Color.LIGHT_GRAY);
        lblGeneroDet.setFont(FONT_CAOS);

        lblEdadDet = new JLabel("Edad: ");
        lblEdadDet.setForeground(Color.LIGHT_GRAY);
        lblEdadDet.setFont(FONT_CAOS);

        lblIngresoDet = new JLabel("Desde: ");
        lblIngresoDet.setForeground(Color.LIGHT_GRAY);
        lblIngresoDet.setFont(FONT_CAOS);

        lblFollowersDet = new JLabel("Followers: 0");
        lblFollowersDet.setForeground(COLOR_TEXT);
        lblFollowersDet.setFont(FONT_CAOS);

        lblFollowingDet = new JLabel("Following: 0");
        lblFollowingDet.setForeground(COLOR_TEXT);
        lblFollowingDet.setFont(FONT_CAOS);

        lblSigoDet = new JLabel("Estado: -");
        lblSigoDet.setForeground(COLOR_TEXT);
        lblSigoDet.setFont(FONT_CAOS);

        panelPerfilDetalle.add(lblNombreDet);
        panelPerfilDetalle.add(Box.createVerticalStrut(6));
        panelPerfilDetalle.add(lblGeneroDet);
        panelPerfilDetalle.add(lblEdadDet);
        panelPerfilDetalle.add(lblIngresoDet);
        panelPerfilDetalle.add(Box.createVerticalStrut(10));
        panelPerfilDetalle.add(lblFollowersDet);
        panelPerfilDetalle.add(lblFollowingDet);
        panelPerfilDetalle.add(Box.createVerticalStrut(10));
        panelPerfilDetalle.add(lblSigoDet);

        // botones de acción
        JPanel pAcc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pAcc.setBackground(COLOR_BG);

        btnFollowToggle = new JButton("Seguir");
        estilizarBoton(btnFollowToggle);
        btnFollowToggle.addActionListener(e -> accionFollowToggle());
        pAcc.add(btnFollowToggle);

        btnVerTweets = new JButton("Ver sus Tweets");
        estilizarBoton(btnVerTweets);
        btnVerTweets.addActionListener(e -> verTweetsDelPerfil());
        pAcc.add(btnVerTweets);

        panelPerfilDetalle.add(Box.createVerticalStrut(12));
        panelPerfilDetalle.add(pAcc);

        p.add(panelPerfilDetalle, BorderLayout.CENTER);

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

    // acciones de UI --------------------------------

    private void ejecutarBusqueda() {
        String q = txtBuscar.getText().trim();
        listModel.clear();
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;
            ArrayList<String> res = manager.searchUsers(q);
            if (res.isEmpty()) {
                listModel.addElement("No se encontraron usuarios");
            } else {
                for (String u : res) {
                    boolean sigo = false;
                    // aseguramos que loggedUser esté seteado
                    manager.setLoggedUser(currentUser);
                    sigo = manager.isFollowing(u);
                    String linea = u + " - " + (sigo ? "LO SIGO" : "NO LO SIGO");
                    listModel.addElement(linea);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error buscando: " + ex.getMessage());
        }
    }

    private void abrirPerfilDesdeBusqueda(String linea) {
        // linea puede ser "username - ...", extraer el username
        String username = linea.split(" - ")[0];
        txtEntrar.setText(username);
        // cambiar toggle a ENTRAR (desactivar el toggle)
        toggleBuscar.setSelected(false);
        CardLayout cl = (CardLayout) panelCentral.getLayout();
        cl.show(panelCentral, "ENTRAR");
        buscarYMostrarPerfil();
    }

    private void buscarYMostrarPerfil() {
        String target = txtEntrar.getText().trim();
        if (target.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un username para buscar.");
            return;
        }
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;

            // comprobar existencia
            if (!manager.checkUserExistance(target)) {
                JOptionPane.showMessageDialog(this, "Ese usuario no existe o está desactivado.");
                return;
            }

            // cargar datos generales
            String realName = manager.getRealName(target);
            char g = manager.getGender(target);
            int age = manager.getAge(target);
            String date = manager.getEntryDate(target);
            String genTxt = (g == 'M') ? "Demonio" : (g == 'F' ? "Bruja" : "Ente");

            lblNombreDet.setText("Nombre: " + (realName != null ? realName : target));
            lblGeneroDet.setText("Genero: " + genTxt);
            lblEdadDet.setText("Edad: " + age);
            lblIngresoDet.setText("Desde: " + (date != null ? date : "Desconocido"));

            // followers / following
            int followers = manager.getFollowersCount(target);
            int following = manager.getFollowingCount(target);
            lblFollowersDet.setText("Followers: " + followers);
            lblFollowingDet.setText("Following: " + following);

            // sigo o no
            manager.setLoggedUser(currentUser);
            boolean sigo = manager.isFollowing(target);
            lblSigoDet.setText("Estado: " + (sigo ? "LO SIGO" : "NO LO SIGO"));
            btnFollowToggle.setText(sigo ? "Dejar de seguir" : "Seguir");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar perfil: " + ex.getMessage());
        }
    }

    private void accionFollowToggle() {
        String target = txtEntrar.getText().trim();
        if (target.isEmpty()) return;
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;

            manager.setLoggedUser(currentUser);
            boolean sigo = manager.isFollowing(target);
            if (!sigo) {
                boolean ok = manager.addFollow(target);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Ahora sigues a " + target);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo seguir o ya lo seguías.");
                }
            } else {
                int resp = JOptionPane.showConfirmDialog(this, "¿Seguro quieres dejar de seguir a " + target + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    manager.quitarFollow(target);
                    JOptionPane.showMessageDialog(this, "Has dejado de seguir a " + target);
                }
            }
            // refrescar detalles
            buscarYMostrarPerfil();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error follow: " + ex.getMessage());
        }
    }

    private void verTweetsDelPerfil() {
        String target = txtEntrar.getText().trim();
        if (target.isEmpty()) return;
        try {
            instaManager manager = instaController.getInstance().getInsta();
            ArrayList<String[]> posts = manager.getPosts(target);
            // construir cadena de todos los posts (más reciente primero ya lo hace getPosts)
            if (posts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay tweets/post de " + target);
            } else {
                StringBuilder sb = new StringBuilder();
                for (String[] p : posts) {
                    String autor = (p.length > 1) ? p[1] : "(?)";
                    String fecha = (p.length > 2) ? p[2] : "(fecha)";
                    String contenido = (p.length > 3) ? p[3] : "";
                    sb.append(autor).append(": '").append(contenido).append("'   [").append(fecha).append("]\n\n");
                }
                JTextArea area = new JTextArea(sb.toString());
                area.setEditable(false);
                area.setBackground(new Color(20, 20, 20));
                area.setForeground(Color.WHITE);
                area.setCaretColor(Color.WHITE);
                JScrollPane sp = new JScrollPane(area);
                sp.setPreferredSize(new Dimension(380, 400));
                JOptionPane.showMessageDialog(this, sp, "Tweets de " + target, JOptionPane.PLAIN_MESSAGE);

                // opción para "Entrar al perfil" desde aquí:
                int entrar = JOptionPane.showConfirmDialog(this, "¿Entrar al perfil de " + target + "?", "Ir al perfil", JOptionPane.YES_NO_OPTION);
                if (entrar == JOptionPane.YES_OPTION) {
                    // Abrir perfil completo (ir a InstaProfileUI)
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window instanceof JFrame) {
                        JFrame frame = (JFrame) window;
                        frame.setContentPane(new InstaProfileUI(target));
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.revalidate();
                        frame.repaint();
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando tweets: " + ex.getMessage());
        }
    }

    private void toggleCuenta() {
        try {
            instaManager manager = instaController.getInstance().getInsta();
            if (manager == null) return;
            // saber estado actual
            boolean estado = manager.getStatusUser(currentUser);
            if (estado) {
                int resp = JOptionPane.showConfirmDialog(this, "¿Deseas desactivar tu cuenta? (se ocultará de búsquedas)", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    manager.desactivateUser(currentUser);
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada.");
                }
            } else {
                // si está desactivada, la activamos sin preguntar según requerimiento
                boolean ok = manager.activateUser(currentUser);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cuenta reactivada.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo activar la cuenta.");
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en operación de cuenta: " + ex.getMessage());
        }
    }

    // utilidades estéticas --------------------------
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

    private void estilizarToggle(JToggleButton t) {
        t.setBackground(new Color(30, 30, 30));
        t.setForeground(COLOR_TEXT);
        t.setFont(FONT_CAOS);
        t.setFocusPainted(false);
        t.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        t.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}

