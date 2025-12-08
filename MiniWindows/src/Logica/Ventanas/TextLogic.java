/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import java.awt.Component;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author esteb
 */
public class TextLogic {
    
    private String rutaUsuario;
    private File archivoActual;

    public TextLogic() {
        configurarRuta();
    }
    
    private void configurarRuta() {
        try {
            if (UserLogged.getInstance().getUserLogged() != null) {
                String currentUser = UserLogged.getInstance().getUserLogged().getName();
                rutaUsuario = "src\\Z\\Usuarios\\" + currentUser + "\\Mis Documentos";
            } else {
                rutaUsuario = "src\\Z\\Usuarios\\TEST_USER\\Mis Documentos";
            }
            
            File carpeta = new File(rutaUsuario);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
        } catch (Exception e) {
            rutaUsuario = "src\\Z\\Usuarios\\ADMIN\\Mis Documentos";
            new File(rutaUsuario).mkdirs();
        }
    }

    public void guardarArchivo(Component parent, JTextPane textPane) {
        JFileChooser fileChooser = new JFileChooser(rutaUsuario);
        fileChooser.setDialogTitle("Guardar Documento");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Texto (.txt)", "txt"));
        
        if (archivoActual != null) {
            fileChooser.setSelectedFile(archivoActual);
        }
        
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            
            try (FileWriter fw = new FileWriter(file)) {
                textPane.write(fw);
                archivoActual = file;
                JOptionPane.showMessageDialog(parent, "Archivo guardado exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void abrirArchivo(Component parent, JTextPane textPane) {
        JFileChooser fileChooser = new JFileChooser(rutaUsuario);
        fileChooser.setDialogTitle("Abrir Documento");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Texto (.txt)", "txt"));

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                textPane.setText("");
                
                FileReader reader = new FileReader(file);
                textPane.read(reader, file);
                
                archivoActual = file;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Error al abrir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    
    public void abrirExterno(Component parent, JTextPane textPane, File file){
         try {
                textPane.setText("");
                
                FileReader reader = new FileReader(file);
                textPane.read(reader, file);
                
                archivoActual = file;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Error al abrir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    }
    
    public File getArchivoActual() {
        return archivoActual;
    }
}