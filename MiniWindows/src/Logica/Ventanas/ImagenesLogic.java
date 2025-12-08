/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author esteb
 */
public class ImagenesLogic {

    private final String actualUserName;
    private final String myImagesFolder;
    private final List<File> imagenes;
    
    // Lista de extensiones de imagen que se permiten
    private static final List<String> EXTENSIONES_VALIDAS = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    public ImagenesLogic() {
        if (UserLogged.getInstance().getUserLogged() != null) {
            this.actualUserName = UserLogged.getInstance().getUserLogged().getName();
        } else {
            this.actualUserName = "TEST_USER"; 
        }
        
        this.myImagesFolder = "src\\Z\\Usuarios\\" + this.actualUserName + "\\Mis Imagenes";
        this.imagenes = new ArrayList<>();
        
        File imagesDir = new File(myImagesFolder);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
    }
    
    public List<File> getImagenes() {
        return imagenes;
    }
    
    public String getMyImagesFolder() {
        return myImagesFolder;
    }
    
    public void cargarImagenes() {
        File imagesDir = new File(myImagesFolder);
        if (!imagesDir.exists()) return;

        imagenes.clear();
        
        File[] files = imagesDir.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return EXTENSIONES_VALIDAS.stream()
                .anyMatch(ext -> lowerName.endsWith("." + ext));
        });

        if (files != null) {
            for (File file : files) {
                imagenes.add(file);
            }
        }
    }
    
    public void importarImagenes(Component parentComponent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("IMPORTAR IMÁGENES");
        chooser.setMultiSelectionEnabled(true);
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Archivos de Imagen", EXTENSIONES_VALIDAS.toArray(new String[0]));
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(parentComponent);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            File dirDestino = new File(myImagesFolder);
            int importedCount = 0;
            
            for (File sourcefile : selectedFiles) {
                String fileName = sourcefile.getName().toLowerCase();
                boolean isImage = EXTENSIONES_VALIDAS.stream()
                    .anyMatch(ext -> fileName.endsWith("." + ext));

                if (!isImage) continue; // Saltar archivos no válidos

                try {
                    File destination = new File(dirDestino, sourcefile.getName());
                    Files.copy(sourcefile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    importedCount++;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(parentComponent, 
                        "Error al importar " + sourcefile.getName() + ": " + e.getMessage(), 
                        "Error de Importación", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            if (importedCount > 0) {
                cargarImagenes(); // Recargar la lista después de la importación
            }
        }
    }
    
    
    
    public void ImportarImagenesExterno(Component parentComponent, File sourcefile){
        File dirDestino = new File(myImagesFolder);
        int importedCount = 0;
        
         try {
            File destination = new File(dirDestino, sourcefile.getName());
            Files.copy(sourcefile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            importedCount++;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentComponent, 
                "Error al importar " + sourcefile.getName() + ": " + e.getMessage(), 
                "Error de Importación", JOptionPane.ERROR_MESSAGE);
        }
        
         cargarImagenes();
         
    }
    
    
    
}