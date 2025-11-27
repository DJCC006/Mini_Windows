/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.text.BadLocationException;

/**
 *
 * @author David
 */
public class CMDPanel extends JPanel {

    private final JTextArea area;
    private int inicioEntrada = 0;

    private ComandosFile manejador;
    private final String rutaBase;
    private UserUtilities usuarioActual = null;

    private JInternalFrame parentFrame;

    public CMDPanel(JInternalFrame parentFrame) {
        //super("CMD Insano");
        this.parentFrame = parentFrame;
        rutaBase = System.getProperty("user.dir");
        manejador = new ComandosFile(rutaBase);

        this.setLayout(new BorderLayout());

        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //setSize(1100, 650);
        //setLocationRelativeTo(null);
        area = new JTextArea();
        area.setEditable(true);
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(area);

        //add(scroll);
        this.add(scroll, BorderLayout.CENTER);
        //this.add(area, BorderLayout.SOUTH);

        appendText("Microsoft Windows [Versión 10.0.22621.521]\n");
        appendText("(c) Microsoft Corporation. Todos los derechos reservados.\n");
        appendText("Si ocupas ayuda usa el comando 'help'.\n");

        writePrompt();

        area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                int caretPos = area.getCaretPosition();

                if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_HOME)
                        && caretPos <= inicioEntrada) {
                    e.consume();
                    area.setCaretPosition(inicioEntrada);
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && caretPos <= inicioEntrada) {
                    e.consume();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_DELETE && caretPos < inicioEntrada) {
                    e.consume();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    try {
                        int len = area.getDocument().getLength();
                        String comando = area.getText(inicioEntrada, len - inicioEntrada).trim();
                        appendText("\n");
                        procesar(comando);
                    } catch (BadLocationException ex) {
                        appendText("\nError: " + ex.getMessage() + "\n");
                    }
                    writePrompt();
                }
            }
        });

        setVisible(true);
    }

    private void appendText(String s) {
        area.append(s);
        area.setCaretPosition(area.getDocument().getLength());
    }

    private void writePrompt() {
        String userPart = (usuarioActual != null ? "[" + usuarioActual.getName() + "] " : "");
        appendText(userPart + manejador.getPathActual().getAbsolutePath() + ">");
        inicioEntrada = area.getDocument().getLength();
    }

    private void procesar(String raw) {

        if (raw == null || raw.isEmpty()) {
            return;
        }

        String[] parts = raw.split("\\s+");
        String cmd = parts[0].toLowerCase();

        try {

            switch (cmd) {

                case "help":
                    appendText("Comandos disponibles:\n");
                    appendText("  cd <ruta>\n");
                    appendText("  cd.. | ... | cdback\n");
                    appendText("  mkdir <nombre>\n");
                    appendText("  mfile <nombre>\n");
                    appendText("  rm <nombre>\n");
                    appendText("  dir [ruta]\n");
                    appendText("  wr <archivo> <texto>\n");
                    appendText("  rd <archivo>\n");
                    appendText("  time\n");
                    appendText("  date\n");
                    appendText("  cls\n");
                    appendText("  exit\n");
                    break;
                //-------------------------------------------------------
                // USERADD (usa UserUtilities sin modificarlo)
                //-------------------------------------------------------
                case "useradd":
                    if (parts.length < 3) {
                        appendText("Uso: useradd <nombre> <password>\n");
                        return;
                    }

                    String uName = parts[1];
                    String uPass = parts[2];

                    UserUtilities newUser = new UserUtilities(uName, uPass);

                    newUser.createInitUserDir();
                    newUser.createInicialDirs();

                    appendText("Usuario creado: " + uName + "\n");
                    return;

                //-------------------------------------------------------
                // LOGIN (cambia directorio actual)
                //-------------------------------------------------------
                case "login":
                    if (parts.length < 2) {
                        appendText("Uso: login <nombre>\n");
                        return;
                    }

                    String nameLogin = parts[1];
                    File carpeta = new File("src\\Z\\Usuarios\\" + nameLogin);

                    if (!carpeta.exists() || !carpeta.isDirectory()) {
                        appendText("No existe el usuario: " + nameLogin + "\n");
                        return;
                    }

                    usuarioActual = new UserUtilities(nameLogin, "");
                    manejador = new ComandosFile(usuarioActual.getUserRoute());

                    appendText("Sesión iniciada como: " + nameLogin + "\n");
                    return;

                //-------------------------------------------------------
                // LOGOUT
                //-------------------------------------------------------
                case "logout":
                    usuarioActual = null;
                    manejador = new ComandosFile(rutaBase);
                    appendText("Sesión cerrada.\n");
                    return;

                //-------------------------------------------------------
                // CD
                //-------------------------------------------------------
                case "cd":
                    if (parts.length < 2) {
                        appendText(manejador.getPathActual().getAbsolutePath() + "\n");
                        return;
                    }

                    String ruta = raw.substring(raw.indexOf(" ") + 1).trim();
                    File base = manejador.getPathActual();

                    File nuevo;
                    if (ruta.equals("..")) {
                        nuevo = base.getParentFile();
                    } else {
                        nuevo = ruta.startsWith("/") || ruta.contains(":")
                                ? new File(ruta)
                                : new File(base, ruta);
                    }

                    if (!manejador.cd(nuevo)) {
                        appendText("No existe la ruta.\n");
                    } else {
                        appendText(nuevo.getAbsolutePath() + "\n");
                    }
                    return;

                case "cd..":
                case "cdback":
                case "...":
                    if (!manejador.cdBack()) {
                        appendText("No se puede subir más.\n");
                    } else {
                        appendText(manejador.getPathActual().getAbsolutePath() + "\n");
                    }
                    return;

                //-------------------------------------------------------
                // ARCHIVOS
                //-------------------------------------------------------
                case "mkdir":
                    appendText(manejador.mkdir(parts[1]) + "\n");
                    return;

                case "mfile":
                    appendText(manejador.mfile(parts[1]) + "\n");
                    return;

                case "rm":
                    appendText(manejador.rm(parts[1]) + "\n");
                    return;

                case "dir":
                    String arg = (parts.length < 2 ? "." : raw.substring(raw.indexOf(" ") + 1));
                    appendText(manejador.dir(arg) + "\n");
                    return;

                case "wr":
                    if (parts.length < 3) {
                        appendText("Uso: wr <archivo> <texto>\n");
                        return;
                    }
                    String archivo = parts[1];
                    String texto = raw.substring(raw.indexOf(parts[2]));
                    appendText(manejador.escribirTexto(archivo, texto) + "\n");
                    return;

                case "rd":
                    appendText(manejador.leerTexto(parts[1]) + "\n");
                    return;

                //-------------------------------------------------------
                // SISTEMA
                //-------------------------------------------------------
                case "time":
                    appendText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                    return;

                case "date":
                    appendText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
                    return;

                case "cls":
                    area.setText("");
                    appendText("Microsoft Windows [Versión 10.0.22621.521]\n");
                    appendText("(c) Microsoft Corporation. Todos los derechos reservados.\n");
                    appendText("Si ocupas ayuda usa el comando 'help'.\n");
                    return;

                case "exit":
                    if (parentFrame != null) {
                        parentFrame.dispose();
                    }

                    return;

                //-------------------------------------------------------
                default:
                    appendText("Comando no reconocido: " + cmd + "\n");
            }

        } catch (Exception ex) {
            appendText("Error: " + ex.getMessage() + "\n");
        }
    }
}
