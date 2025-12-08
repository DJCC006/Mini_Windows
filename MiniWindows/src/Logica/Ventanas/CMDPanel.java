/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.User;
import Logica.ManejoUsuarios.UserLogged;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class CMDPanel extends JPanel {

    private final JTextArea area;
    private int inicioEntrada = 0;

    private ComandosFile manejador;
    private User usuarioActual = null;
    private JInternalFrame parentFrame;
    private File rootDir;

    public CMDPanel(JInternalFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.usuarioActual = UserLogged.getInstance().getUserLogged();
        
        String pathZ = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Z";
        rootDir = new File(pathZ);
        if(!rootDir.exists()){
            rootDir.mkdirs();
        }

        String startPath = pathZ;
        if (usuarioActual != null) {
            File userDir = new File(rootDir, "Usuarios" + File.separator + usuarioActual.getName());
            if (userDir.exists()) {
                startPath = userDir.getAbsolutePath();
            }
        }

        manejador = new ComandosFile(startPath, pathZ);

        this.setLayout(new BorderLayout());

        area = new JTextArea();
        area.setEditable(true);
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(area);
        this.add(scroll, BorderLayout.CENTER);

        printDuckLogo();

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

                if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) 
                        && caretPos <= inicioEntrada) {
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
        String currentPath = manejador.getPathActual().getAbsolutePath();
        String rootPathStr = rootDir.getAbsolutePath();
        
        String displayPath;
        
        if (currentPath.equals(rootPathStr)) {
            displayPath = "Z:\\";
        } else if (currentPath.startsWith(rootPathStr)) {
            String sub = currentPath.substring(rootPathStr.length());
            if (!sub.startsWith(File.separator)) {
                 sub = File.separator + sub;
            }
            displayPath = "Z:" + sub;
        } else {
             displayPath = "Z:\\"; 
        }
        
        displayPath = displayPath.replace("/", "\\");
        
        appendText(displayPath + ">");
        inicioEntrada = area.getDocument().getLength();
    }

    private void procesar(String raw) {
        if (raw == null || raw.isEmpty()) return;

        String[] parts = raw.split("\\s+");
        String cmd = parts[0].toLowerCase();

        try {
            switch (cmd) {
                case "help":
                    appendText("CD         Muestra el nombre del directorio actual o cambia a otro.\n");
                    appendText("DIR        Muestra una lista de archivos y subdirectorios en un directorio.\n");
                    appendText("MKDIR      Crea un directorio.\n");
                    appendText("RM         Elimina uno o más archivos.\n");
                    appendText("CLS        Borra la pantalla.\n");
                    appendText("EXIT       Sale del programa CMD.EXE.\n");
                    appendText("TIME       Muestra la hora del sistema.\n");
                    appendText("DATE       Muestra la fecha.\n");
                    appendText("CD..       Retroceder Carpetas.\n");
                    break;

                case "cd":
                    if (parts.length < 2) return;
                    String ruta = raw.substring(raw.indexOf(" ") + 1).trim();
                    File base = manejador.getPathActual();
                    File nuevo = ruta.equals("..") ? base.getParentFile() : new File(base, ruta);
                    if (!manejador.cd(nuevo)) appendText("El sistema no puede encontrar la ruta especificada.\n");
                    return;

                case "cd..":
                case "cdback":
                case "...":
                    manejador.cdBack();
                    return;

                case "dir":
                    String arg = (parts.length < 2 ? "." : raw.substring(raw.indexOf(" ") + 1));
                    File targetDir = ".".equals(arg) ? manejador.getPathActual() : new File(manejador.getPathActual(), arg);
                    
                    appendText(simularHeaderDir(targetDir));
                    
                    String contenido = manejador.dir(arg);
                    if(contenido.startsWith("Error")) appendText(contenido + "\n");
                    else appendText(contenido + "\n"); 
                    return;

                case "mkdir":
                    if (parts.length < 2) { appendText("Sintaxis incorrecta.\n"); return; }
                    String resMk = manejador.mkdir(parts[1]);
                    if (resMk.startsWith("Error")) appendText(resMk + "\n");
                    return;

                case "mfile": 
                    if (parts.length < 2) { appendText("Sintaxis incorrecta.\n"); return; }
                    String resMf = manejador.mfile(parts[1]);
                    if (resMf.startsWith("Error")) appendText(resMf + "\n");
                    return;

                case "rm":
                    if (parts.length < 2) { appendText("Sintaxis incorrecta.\n"); return; }
                    String resRm = manejador.rm(parts[1]);
                    if (resRm.startsWith("Error")) appendText(resRm + "\n");
                    return;
                
                case "wr":
                    if (parts.length < 3) { appendText("Sintaxis incorrecta.\n"); return; }
                    String archivo = parts[1];
                    String texto = raw.substring(raw.indexOf(parts[2]));
                    String resWr = manejador.escribirTexto(archivo, texto);
                    if (resWr.startsWith("Error")) appendText(resWr + "\n");
                    return;

                case "rd":
                    if (parts.length < 2) { appendText("Sintaxis incorrecta.\n"); return; }
                    appendText(manejador.leerTexto(parts[1]) + "\n");
                    return;

                case "time":
                    appendText("La hora actual es: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SS")) + "\n");
                    return;

                case "date":
                    appendText("La fecha actual es: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
                    return;

                case "cls":
                    area.setText("");
                    printDuckLogo();
                    return;

                case "exit":
                    if (parentFrame != null) parentFrame.dispose();
                    return;

                default:
                    appendText("'" + cmd + "' no se reconoce como un comando interno o externo.\n");
            }
        } catch (Exception ex) {
            appendText("Error.\n");
        }
    }
    
    private String simularHeaderDir(File dir) {
        StringBuilder sb = new StringBuilder();
        sb.append(" El volumen de la unidad Z es MiniOS\n");
        sb.append(" El número de serie del volumen es A1B2-C3D4\n\n");
        
        String pathStr = dir.getAbsolutePath();
        String rootStr = rootDir.getAbsolutePath();
        if(pathStr.startsWith(rootStr)) {
             String sub = pathStr.substring(rootStr.length());
             if(!sub.startsWith(File.separator) && !sub.isEmpty()) sub = File.separator + sub;
             pathStr = "Z:" + sub;
        } else {
             pathStr = "Z:\\";
        }
        pathStr = pathStr.replace("/", "\\");
        
        sb.append(" Directorio de " + pathStr + "\n\n");
        return sb.toString();
    }
    
    private void printDuckLogo() {
        appendText("\n");
        appendText("           ,~~.\n");
        appendText("          (  6 )-_,     \n");
        appendText("     (\\___ )=='-'      \n");
        appendText("      \\ .   ) )         \n");
        appendText("       \\ `-' /          \n");
        appendText("        `---'           \n");
        
        appendText("  ____             _     ___  ____  \n");
        appendText(" |  _ \\ _   _  ___| | __/ _ \\/ ___| \n");
        appendText(" | | | | | | |/ __| |/ / | | \\___ \\ \n");
        appendText(" | |_| | |_| | (__|   <| |_| |___) |\n");
        appendText(" |____/ \\__,_|\\___|_|\\_\\\\___/|____/ \n");
        
        appendText("\n");
        appendText("      CORPORATION (c) 2025\n");
        appendText("   --------------------------\n");
        appendText("   Quack Protocol: ENABLED\n");
    }
}