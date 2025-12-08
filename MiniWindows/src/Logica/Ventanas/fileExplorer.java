/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.Excepciones.nullSelected;
import Logica.ManejoUsuarios.UserLogged;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author David
 */
public class fileExplorer extends JPanel {

    private static String userName = UserLogged.getInstance().getUserLogged().getName();
    private String raizUsuario = "";
    private String setUserRoute = "src\\Z\\Usuarios\\" + userName + "\\";
    private String recycleBin = "";
    private genFondos panelFondo;

    private final Color COLOR_FONDO = new Color(30, 30, 30);
    private final Color COLOR_PANEL = new Color(45, 45, 48);
    private final Color COLOR_NARANJA = new Color(233, 84, 32);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    private final Color COLOR_SELECCION = new Color(233, 84, 32, 100);

    private JTree fileTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode raizNodo;

    private JTable fileTable;
    private FileTableModel tableModel;

    private JLabel pathLabel;

    //elementos para ordenar lo que es los archivos dependiendo del criterio seleccionado
    private JComboBox<String> opcionesOrdenar;
    private String currentDirPath;
    private SortCriteria sortCriteria = SortCriteria.NAME;

    private enum SortCriteria {
        NAME, DATE, SIZE, TYPE;
    }

    //cosas para portapapeles
    private List<File> clipboardFiles = new ArrayList<>();
    private List<File> organizerFiles = new ArrayList<>();
    private List<File> papeleraFiles = new ArrayList<>();
    private boolean isCutOperation = false;

    JPanel northPanel = new JPanel(new BorderLayout());

    JButton restaurar;
    JButton eliminar;

    public fileExplorer(genFondos panelFondo) {
        setLayout(new BorderLayout(5, 5));
        this.panelFondo = panelFondo;
        setBackground(COLOR_FONDO);

        //Creacion de rutas dinamicas
        raizUsuario = getRutaCondicionada();
        recycleBin = setUserRoute + "Papelera";

        File papelera = new File(recycleBin);

        setupFileTree();
        setupContentTable();

        northPanel.setBackground(COLOR_PANEL);
        northPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolBar = setupToolBar();
        northPanel.add(toolBar, BorderLayout.NORTH);

        //Ordenar
        JPanel panelSort = new JPanel(new BorderLayout());
        panelSort.setBackground(COLOR_PANEL);
        pathLabel = new JLabel("Ruta Actual: " + formatDisplayPath(raizUsuario));
        pathLabel.setForeground(COLOR_TEXTO);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        panelSort.add(pathLabel, BorderLayout.WEST);

        setupSortControls(panelSort);

        //Panel Principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(fileTree), new JScrollPane(fileTable));
        splitPane.setDividerLocation(200);
        splitPane.setBackground(COLOR_FONDO);
        splitPane.setBorder(null);

        styleScrollPane((JScrollPane) splitPane.getLeftComponent());
        styleScrollPane((JScrollPane) splitPane.getRightComponent());

        pathLabel = new JLabel("Ruta Actual: " + raizUsuario);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        // add(pathLabel, BorderLayout.WEST);

        //add(panelSort, BorderLayout.NORTH);
        northPanel.add(panelSort, BorderLayout.CENTER);
        
        add(northPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        File rootFile = new File(raizUsuario);
        currentDirPath = raizUsuario;
        displayContents(rootFile);

        /*ESTO EN CASO QUE SE BUGUE
        File raizDir = new File(raizUsuario);
        if(!raizDir.exists()){
            raizDir.mkdirs()
        }
         */
    }

    private void styleScrollPane(JScrollPane scroll) {
        scroll.setBorder(new LineBorder(COLOR_PANEL, 1));
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.setBackground(COLOR_FONDO);
    }

    private String getRutaCondicionada() {
        String rutaUsuarios = "src\\Z\\Usuarios";

        String nameActual = UserLogged.getInstance().getUserLogged().getName();

        if (UserLogged.getInstance().getUserLogged().isAdmin()) {
            return rutaUsuarios;
        } else {
            String userroot = rutaUsuarios + "\\" + nameActual + "\\";
            return userroot;
        }
    }

    private String formatDisplayPath(String userString) {
        int rootIndex = userString.indexOf(raizUsuario);
        if (userString.length() > 4) {
            return userString.substring(4);
        }

//        
//        if(rootIndex!=-1){
//            return userString.substring(rootIndex);
//        }
//        return userString;
        return userString;
    }

    /*
    private JToolBar setupPapeleraBar(){
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
      
        
        return toolBar;
    }
    
     */
//    private void actualizarVistaBotones(File carpeta){
//        if(carpeta.getName().equals("Papelera")){
//            eliminar.setVisible(true);
//            restaurar.setVisible(true);
//            northPanel.revalidate();
//            northPanel.repaint();
//        }
//    }
//    
    private void actualizarVistaBotones(File carpeta) {
        northPanel.removeAll();
        if (carpeta.getName().equals("Papelera")) {
            northPanel.add(setupPapeleraBar(), BorderLayout.NORTH);

        } else {
            northPanel.add(setupToolBar(), BorderLayout.NORTH);
        }
        
        // Se vuelve a agregar el panel de sort porque el removeAll lo quita
        JPanel panelSort = new JPanel(new BorderLayout());
        panelSort.setBackground(COLOR_PANEL);
        pathLabel.setText("Ruta Actual: " + formatDisplayPath(carpeta.getAbsolutePath()));
        pathLabel.setForeground(COLOR_TEXTO);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        panelSort.add(pathLabel, BorderLayout.WEST);
        setupSortControls(panelSort);
        northPanel.add(panelSort, BorderLayout.CENTER);

        northPanel.revalidate();
        northPanel.repaint();
    }

    private JToolBar setupPapeleraBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(COLOR_PANEL);
        toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));

        BotonModerno restaurar = new BotonModerno("Restaurar Archivos", COLOR_NARANJA, Color.WHITE);
        restaurar.setToolTipText("Restaura los archivos seleccionados");
        restaurar.addActionListener(e -> restuararFiles());
        restaurar.setPreferredSize(new Dimension(140, 30));
        toolBar.add(restaurar);
        toolBar.addSeparator(new Dimension(10, 0));

        BotonModerno eliminar = new BotonModerno("Eliminar", new Color(200, 50, 50), Color.WHITE);
        eliminar.setToolTipText("Elimina los archivos permanentemente");
        eliminar.addActionListener(e -> borrarFiles());
        eliminar.setPreferredSize(new Dimension(100, 30));
        toolBar.add(eliminar);

        return toolBar;
    }

    private void borrarFiles() {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona los archivos o carpetas a borrar", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Desea borrar permanente los archivos seleccionados?", "Confirmar Eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            papeleraFiles.clear();

            //agregamos archivos seleccionados a papelera
            for (int row : selectedRows) {
                File arc = (File) tableModel.getValueAt(row, 0);
                papeleraFiles.add(arc);
            }

            boolean success = true;

            for (File sourcefile : papeleraFiles) {
                try {
                    borrar(sourcefile);
                } catch (Exception e) {
                    success = false;
                }
            }

            if (success) {
                organizerFiles.clear();
                displayContents(new File(currentDirPath));
                updateTree(new File(currentDirPath));
                JOptionPane.showMessageDialog(this, "Elementos eliminados con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void restaurarArchivo(File archivoEnPapelera) {

        String nombreOriginal = archivoEnPapelera.getName();

        String rutaDocs = setUserRoute + "Mis Documentos";
        File dirDestino = new File(rutaDocs);
        File archivoRestaurado = new File(dirDestino, nombreOriginal);

        if (!dirDestino.exists() || !dirDestino.isDirectory()) {
            System.out.println("Se direcciona a otro lugar");
            return;
        }

        if (archivoEnPapelera.renameTo(archivoRestaurado)) {
            System.out.println("Se ha restaurado el archivo");
        } else {
            System.out.println("No se pudo restaurar");
        }
    }

    private void restuararFiles() {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona los archivos o carpetas a borrar", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Desea restaurar los archivos seleccionados?", "Confirmar Restauracion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            papeleraFiles.clear();

            //agregamos archivos seleccionados a papelera
            for (int row : selectedRows) {
                File arc = (File) tableModel.getValueAt(row, 0);
                papeleraFiles.add(arc);
            }

            boolean success = true;

            for (File sourcefile : papeleraFiles) {
                try {
                    restaurarArchivo(sourcefile);
                } catch (Exception e) {
                    success = false;
                }
            }

            if (success) {
                organizerFiles.clear();
                displayContents(new File(currentDirPath));
                updateTree(new File(currentDirPath));
                JOptionPane.showMessageDialog(this, "Elementos restaurados con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private JToolBar setupToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(COLOR_PANEL);
        toolBar.setBorder(new EmptyBorder(5, 5, 5, 5));

        addButtonToToolbar(toolBar, "Nueva Carpeta", UIManager.getIcon("FileChooser.newFolderIcon"), "Crear una nueva carpeta en la ubicacion actual", e -> createNewFolder());
        addButtonToToolbar(toolBar, "Nuevo Archivo", UIManager.getIcon("FileChooser.getFolderIcon"), "Crear un nuevo archivo de texto (.txt) en la ubicacion actual", e -> createNewFile());
        addButtonToToolbar(toolBar, "Importar", null, "Importar Archivos", e -> ImportFiles());

        toolBar.addSeparator();

        //Creacion de opciones de portapapeles
        addButtonToToolbar(toolBar, "Organizar", null, "Organizar archivos en distintas carpetas", e -> organizeFiles());
        addButtonToToolbar(toolBar, "Copiar", UIManager.getIcon("Table.copyIcon"), "Copia el archivo o carpeta seleccionada", e -> copySelectedFiles(false));
        addButtonToToolbar(toolBar, "Pegar", UIManager.getIcon("Table.pasteIcon"), "Pega los archivos/carpetas copiados/cortados", e -> pasteFiles());

        toolBar.addSeparator();

        addButtonToToolbar(toolBar, "Eliminar", UIManager.getIcon("InternalFrame.closeIcon"), "Mueve el archivo o carpeta a la Papelera", e -> deleteSelectedFiles());

        BotonModerno renameBt = new BotonModerno("Renombrar", new Color(60, 60, 60), COLOR_TEXTO);
        renameBt.setPreferredSize(new Dimension(100, 30));
        renameBt.setToolTipText("Cambiar el nombre de archivo o carpeta");
        renameBt.addActionListener(e -> {
            try {
                realizarRenombre();
            } catch (nullSelected nullmsg2) {
                JOptionPane.showMessageDialog(this, nullmsg2.getMessage(), "Atencion", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        toolBar.add(renameBt);
        toolBar.add(Box.createHorizontalStrut(5));

        BotonModerno abrirBt = new BotonModerno("Abrir", new Color(60, 60, 60), COLOR_TEXTO);
        abrirBt.setPreferredSize(new Dimension(80, 30));
        abrirBt.setIcon(UIManager.getIcon("InternalFrame.openIcon"));
        abrirBt.setToolTipText("Abrir el archivo seleccionado");
        abrirBt.addActionListener(e -> {

            //AQUI CASO TRY CATCH CON EXCEPCION PERSONALIZADA
            try {
                openSelectedFile();
            } catch (nullSelected nullmsg) {
                JOptionPane.showMessageDialog(this, nullmsg.getMessage(), "Atencion", JOptionPane.INFORMATION_MESSAGE);
            }

        });
        toolBar.add(abrirBt);
        
        return toolBar;
    }

    private void addButtonToToolbar(JToolBar bar, String text, Icon icon, String tooltip, java.awt.event.ActionListener action) {
        BotonModerno btn = new BotonModerno(text, new Color(60, 60, 60), COLOR_TEXTO);
        if (icon != null) {
            btn.setIcon(icon);
        }
        btn.addActionListener(action);
        if (tooltip != null) {
            btn.setToolTipText(tooltip);
        }
        btn.setPreferredSize(new Dimension(110, 30));
        bar.add(btn);
        bar.add(Box.createHorizontalStrut(5));
    }

    private String obtenerExtension(File archivo) {
        if (archivo.isDirectory()) {
            return "";
        }

        String nombre = archivo.getName();
        int ultimoPunto = nombre.lastIndexOf('.');

        if (ultimoPunto > 0 && ultimoPunto < nombre.length() - 1) {
            return nombre.substring(ultimoPunto);
        }
        return "";

    }

    private void organizeFiles() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setDialogTitle("Seleccionar Carpeta de Destino");
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setMultiSelectionEnabled(true);
        String rutaUsuarios = "src\\Z\\Usuarios\\" + userName;

        //ejecutar proceso de copiado
        copySelectedFilesO();//ver si no tira exception

        //set de directorio de usuario logeado
        try {
            folderChooser.setCurrentDirectory(new File(rutaUsuarios));
        } catch (Exception ex) {

        }

        int userSelection = folderChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File destinationFolder = folderChooser.getSelectedFile();

            try {
                String destinationRute = destinationFolder.getCanonicalPath();
                File dirDestino = new File(destinationRute);
                pasteFilesO(destinationFolder);
            } catch (Exception e3) {
            };

            //displayContents(new File(currentDirPath));
        }
    }

    private boolean renombrar(File archivoOriginal, String nuevoNombre) {
        File dirPadre = archivoOriginal.getParentFile();
        File nuevoArchivo = new File(dirPadre, nuevoNombre);

        if (nuevoArchivo.exists()) {
            JOptionPane.showMessageDialog(this, "Ya existe un archivo/directorio con este nombre");
            return false;
        }

        boolean exito = archivoOriginal.renameTo(nuevoArchivo);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Archivo renombrado con exito");
        } else {
            JOptionPane.showMessageDialog(this, "Erro al intentar renombrar el archivo");
        }
        return exito;
    }

    private void realizarRenombre() throws nullSelected {
        //para obtener archivo
        int selectedRow = fileTable.getSelectedRow();

        //Caso que no se haya seleccionado nada
        if (selectedRow == -1) {
            throw new nullSelected("No se ha seleccionado ningun archivo");
        }

        //verificar que no sea un directorio
        File file = (File) fileTable.getValueAt(selectedRow, 0);

        String nameActual = file.getName();
        String extension = obtenerExtension(file);

        System.out.println(extension);

        String nameBase;
        if (extension.isEmpty()) {
            nameBase = nameActual;
        } else {
            nameBase = nameActual.substring(0, nameActual.lastIndexOf('.'));
        }

        String newName = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre: ");

        if (newName != null && !newName.trim().isEmpty()) {
            String newNameFinal = newName.trim() + extension;

            if (!newName.equals(nameActual)) {
                boolean exito = renombrar(file, newNameFinal);
                if (exito) {
                    displayContents(new File(currentDirPath));
                }
            }
        } else if (newName != null && newName.equals(nameActual)) {

        } else if (newName != null) {
            JOptionPane.showMessageDialog(null, "El nuevo nombre no puede estar vacio");
        }

    }

    private void ImportFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("IMPORTAR ARCHIVOS");
        chooser.setMultiSelectionEnabled(true);

        int result = chooser.showOpenDialog(this);//posiblemente aqui agruegar el screen de pantalla principal

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            File dirDestino = new File(currentDirPath);

            for (File sourcefile : selectedFiles) {
                try {

                    //AQUI POSIBLEMENTE AGREGAR LA COMPROBACION DE NO PERMITIR ARCHIVOS REPETIDOS
                    File destination = new File(dirDestino, sourcefile.getName());
                    Files.copy(sourcefile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Importado: " + sourcefile.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            displayContents(new File(currentDirPath));

        }

    }

    private void createNewFolder() {
        String folderName = JOptionPane.showInputDialog(this, "Ingresa el nombre de la nueva carpeta:",
                "Crear Carpeta",
                JOptionPane.PLAIN_MESSAGE);

        if (folderName != null && !folderName.trim().isEmpty()) {
            File newFolder = new File(currentDirPath, folderName.trim());

            if (newFolder.exists()) {
                JOptionPane.showMessageDialog(this,
                        "La Carpeta ya existe",
                        "Error de Creacion",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                if (newFolder.mkdir()) {
                    displayContents(new File(currentDirPath));
                    updateTree(new File(currentDirPath));
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear la carpeta",
                            "Error de creacion",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void createNewFile() {
        String fileName = JOptionPane.showInputDialog(this,
                "Ingresa el nombre de nuevo archivo (ej: documento.txt):",
                JOptionPane.PLAIN_MESSAGE);

        if (fileName != null && !fileName.trim().isEmpty()) {
            String finalFileName = fileName.trim();
            if (!finalFileName.contains(".")) {
                finalFileName += ".txt";
            }

            File newFile = new File(currentDirPath, finalFileName);

            if (newFile.exists()) {
                JOptionPane.showMessageDialog(this,
                        "El archivo ya existe",
                        "Error de Creacion",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    if (newFile.createNewFile()) {
                        displayContents(new File(currentDirPath));
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error al crear el archivo",
                                "Error de Creacion",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error de E/S al crear el archivo: " + ex.getMessage(),
                            "Error de Creacion",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void openSelectedFile() throws nullSelected {

        //POSIBLEMENTE AQUI EXCEPTION
        int selectedRow = fileTable.getSelectedRow();

        //Caso que no se haya seleccionado nada
        if (selectedRow == -1) {
            throw new nullSelected("No se ha seleccionado ningun archivo");
        }

        //verificar que no sea un directorio
        File file = (File) fileTable.getValueAt(selectedRow, 0);

        if (file.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Solamente abrir ARCHIVOS, no directorios", "Atencion", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nameFile = file.getName();

        //ejecutar codigo de identificador
        //Checkar si es tipo para musica
        if (nameFile.toLowerCase().endsWith(".wav") || nameFile.toLowerCase().endsWith(".mp3")) {
            audioPlayer musicPanel = new audioPlayer();
            JInternalFrame newImages = createMusicWindow(musicPanel);
            audioLogic logicPlayer = new audioLogic(musicPanel);
            musicPanel.setPlayerExterno(logicPlayer);
            panelFondo.add(newImages);
            try {
                newImages.setSelected(true);
                logicPlayer.load(file);

                musicPanel.playExterno(file);
            } catch (java.beans.PropertyVetoException ex) {
                //ignore
            }

        } else if (nameFile.toLowerCase().endsWith(".png") || nameFile.toLowerCase().endsWith(".jpg")) {
            VisorImagenes galeriapanel = new VisorImagenes();
            JInternalFrame newImages = createGalleryWindow(galeriapanel);
            ImagenesLogic logicImag = new ImagenesLogic();
            panelFondo.add(newImages);
            try {
                logicImag.ImportarImagenesExterno(galeriapanel, file);
                newImages.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                //ignore
            }
        } else if (nameFile.toLowerCase().endsWith(".txt")) {
            TextLogic logictxt = new TextLogic();
            TextoPanel textEditPanel = new TextoPanel();
            JTextPane txtpane = textEditPanel.getTextPane();
            JInternalFrame newText = createTextWindow(textEditPanel);
            panelFondo.add(newText);
            try {
                newText.setSelected(true);
                logictxt.abrirExterno(textEditPanel, txtpane, file);
            } catch (java.beans.PropertyVetoException ex2) {

            }
        } else {
            JOptionPane.showMessageDialog(null, "Extension de archivo no soportada por sistema", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void copySelectedFiles(boolean isCut) {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona los archivos o carpetas a copiar/cortar", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clipboardFiles.clear();
        isCutOperation = isCut;

        for (int row : selectedRows) {
            File file = (File) tableModel.getValueAt(row, 0);
            clipboardFiles.add(file);
        }

        String operation = isCut ? "Cortar" : "Copiar";
        JOptionPane.showMessageDialog(this, selectedRows.length + "elemento(s) preparado (s) para " + operation + ".", "Portapapeles", JOptionPane.INFORMATION_MESSAGE);
    }

    private void copySelectedFilesO() {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona los archivos o carpetas a copiar/cortar", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        organizerFiles.clear();

        for (int row : selectedRows) {
            File file = (File) tableModel.getValueAt(row, 0);
            organizerFiles.add(file);
        }
        JOptionPane.showMessageDialog(this, selectedRows.length + "elemento(s) preparado (s) para organizar .", "Portapapeles", JOptionPane.INFORMATION_MESSAGE);
    }

    private void pasteFilesO(File destination) {

        if (organizerFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se han seleccionado archivos para organizar", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File targetDir = destination; //modificar esto con el directorio obtenido

        if (!targetDir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "La ubicacion de destino no es una carpeta valida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = true;
        List<File> sourceFiles = new ArrayList<>(organizerFiles);

        for (File sourceFile : sourceFiles) {
            try {
                File destFile = new File(targetDir, sourceFile.getName());

                Files.move(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                success = false;
                JOptionPane.showMessageDialog(this, "Error al " + (isCutOperation ? "mover" : "copiar") + " " + sourceFile.getName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (success) {
            organizerFiles.clear();
            displayContents(targetDir);
            updateTree(targetDir);
            JOptionPane.showMessageDialog(this, "Elementos organizados con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void pasteFiles() {

        if (clipboardFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El portapapeles esta vacio", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File targetDir = new File(currentDirPath);

        if (!targetDir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "La ubicacion de destino no es una carpeta valida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = true;
        List<File> sourceFiles = new ArrayList<>(clipboardFiles);

        for (File sourceFile : sourceFiles) {
            try {
                File destFile = new File(targetDir, sourceFile.getName());

                if (isCutOperation) {
                    Files.move(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    if (sourceFile.isDirectory()) {
                        recursiveCopy(sourceFile.toPath(), destFile.toPath());
                    } else {
                        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }

            } catch (IOException e) {
                success = false;
                JOptionPane.showMessageDialog(this, "Error al " + (isCutOperation ? "mover" : "copiar") + " " + sourceFile.getName() + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (success) {
            if (isCutOperation) {
                clipboardFiles.clear();
                isCutOperation = false;
            }
            displayContents(targetDir);
            updateTree(targetDir);
            JOptionPane.showMessageDialog(this, "Elementos pegados con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void recursiveCopy(Path source, Path dest) throws IOException {
        if (!Files.exists(dest)) {
            Files.createDirectories(dest);
        }

        try (var Stream = Files.walk(source)) {
            Stream.forEach(sourcePath -> {

                try {
                    Path relative = source.relativize(sourcePath);
                    Path destPath = dest.resolve(relative);

                    if (Files.isDirectory(sourcePath)) {
                        if (!Files.exists(destPath)) {
                            Files.createDirectory(destPath);
                        }
                    } else {
                        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    }
                } catch (IOException e) {
                    System.err.println("Error copiando " + sourcePath + ": " + e.getMessage());
                }
            });
        }
    }

    private void deleteSelectedFiles() {
        int[] selectedRows = fileTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona los archivos o carpetas a eliminar.", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<File> filesToDelete = new ArrayList<>();
        for (int row : selectedRows) {
            File file = (File) tableModel.getValueAt(row, 0);
            filesToDelete.add(file);
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "¿Esta seguro de que quiere mover " + selectedRows.length + "elemento(s) a la Papelera?",
                "Confirmar Eliminacion",
                JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_NO_OPTION) {
            return;
        }

        File papelera = new File(recycleBin);
        if (!papelera.exists()) {
            papelera.mkdir();
            File currentDir = new File(currentDirPath);
            displayContents(currentDir);
            updateTree(currentDir);

        }
        boolean success = true;

        for (File file : filesToDelete) {

            if (file.getAbsolutePath().equals(recycleBin)) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar la Papelera de Reciclaje", "Error de Eliminacion", JOptionPane.ERROR_MESSAGE);
                success = false;
                continue;
            }

            //File file= (File) tableModel.getValueAt(row,0);
            File destFile = new File(recycleBin, file.getName());

            try {
                Files.move(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                success = false;
                JOptionPane.showMessageDialog(this, "Error al mover " + file.getName() + " a la Papelera: " + e.getMessage(), "Error de eliminacion", JOptionPane.ERROR_MESSAGE);
            }

            if (success) {
                File currentDir = new File(currentDirPath);
                displayContents(currentDir);
                updateTree(currentDir);
                JOptionPane.showMessageDialog(this, "Elementos movidos a la Papelera con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean borrarAux(File mf) {

        if (mf.isDirectory()) {
            for (File arc : mf.listFiles()) {
                borrarAux(arc);
            }
        }
        return mf.delete();
    }

    public boolean borrar(File mifile) {

        if (mifile.isDirectory()) {
            for (File arc : mifile.listFiles()) {
                borrarAux(arc);
            }
        }
        return mifile.delete();
    }

    private void updateTree(File parentDir) {

        DefaultMutableTreeNode nodeToUpdate = findNode(raizNodo, parentDir);

        if (nodeToUpdate != null) {
            populateNode(nodeToUpdate);
            treeModel.nodeStructureChanged(nodeToUpdate);
        }
    }

    //recursiva de apoyo
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode startNode, File targetFile) {
        if (startNode.getUserObject().equals(targetFile)) {
            return startNode;
        }

        for (int i = 0; i < startNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) startNode.getChildAt(i);

            if (child.getUserObject() instanceof File) {
                DefaultMutableTreeNode found = findNode(child, targetFile);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void setupSortControls(JPanel panelCambios) {
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sortPanel.setBackground(COLOR_PANEL);
        JLabel lblSort = new JLabel("Ordenar por:");
        lblSort.setForeground(COLOR_TEXTO);
        
        // CORREGIDO: El orden de las opciones coincide con el índice de las columnas de la tabla
        // 0: Nombre, 1: Fecha, 2: Tipo, 3: Tamaño
        opcionesOrdenar = new JComboBox<>(new String[]{"Nombre", "Fecha", "Tipo", "Tamaño"});
        opcionesOrdenar.setSelectedItem("Nombre");

        sortPanel.add(lblSort);
        sortPanel.add(opcionesOrdenar);

        opcionesOrdenar.addActionListener(e -> {
            String selected = (String) opcionesOrdenar.getSelectedItem();

            if ("Nombre".equals(selected)) {
                sortCriteria = SortCriteria.NAME;
            } else if ("Fecha".equals(selected)) {
                sortCriteria = SortCriteria.DATE;
            } else if ("Tamaño".equals(selected)) {
                sortCriteria = SortCriteria.SIZE;
            } else if ("Tipo".equals(selected)) {
                sortCriteria = SortCriteria.TYPE;
            }

            displayContents(new File(currentDirPath)); //posiblemente el curretnDirPath no se actualiza
        });

        panelCambios.add(sortPanel, BorderLayout.EAST);

    }

    private void setupFileTree() {
        raizNodo = new DefaultMutableTreeNode(new File(raizUsuario));
        treeModel = new DefaultTreeModel(raizNodo);
        fileTree = new JTree(treeModel);

        fileTree.setCellRenderer(new FileTreeRenderer());
        fileTree.setRootVisible(true);
        fileTree.setBackground(COLOR_FONDO);
        fileTree.setForeground(COLOR_TEXTO);

        populateNode(raizNodo);

        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }

                Object userObject = node.getUserObject();
                if (!(userObject instanceof File)) {
                    return;
                }

                File file = (File) userObject;

                if (file.isDirectory()) {

                    actualizarVistaBotones(file);
                    displayContents(file);

                    if (node.getChildCount() == 0 || (node.getChildCount() == 1 && node.getFirstChild().toString().equals("Cargando..."))) {
                        populateNode(node);
                    }
                }
            }
        });
    }

    private void populateNode(DefaultMutableTreeNode parentNode) {
        parentNode.removeAllChildren();
        File parentFile = (File) parentNode.getUserObject();
        File[] children = parentFile.listFiles(File::isDirectory);

        String currentusername = UserLogged.getInstance().getUserLogged().getName();

        if (children != null) {
            Arrays.sort(children, Comparator.comparing(File::getName)); //posiblemente quitar esto para dejarlo a merced de usuario

            for (File child : children) {

                if (child.getAbsolutePath().equals(recycleBin)) {
                    continue;
                }

                //revision para evitar duplicados
                if (!UserLogged.getInstance().getUserLogged().isAdmin()
                        && !child.getAbsolutePath().equalsIgnoreCase(parentFile.getAbsolutePath())
                        && !child.getName().equalsIgnoreCase(currentusername)) {
                    if (parentFile.getName().equalsIgnoreCase("Usuarios") && !child.getName().equalsIgnoreCase(currentusername)) {
                        continue;
                    }
                }

                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

                if (child.listFiles(File::isDirectory) != null && child.listFiles(File::isDirectory).length > 0) {
                    childNode.add(new DefaultMutableTreeNode("Cargando..."));
                }
                treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
            }
        }
    }

    private void setupContentTable() {

        tableModel = new FileTableModel();
        fileTable = new JTable(tableModel);

        fileTable.setBackground(COLOR_FONDO);
        fileTable.setForeground(COLOR_TEXTO);
        fileTable.setSelectionBackground(COLOR_SELECCION);
        fileTable.setSelectionForeground(Color.WHITE);
        fileTable.setGridColor(new Color(50, 50, 50));
        fileTable.setShowGrid(false);
        fileTable.setShowHorizontalLines(true);
        fileTable.setRowHeight(30);

        DarkHeaderRenderer headerRenderer = new DarkHeaderRenderer();
        for (int i = 0; i < fileTable.getColumnModel().getColumnCount(); i++) {
            fileTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        fileTable.getColumnModel().getColumn(0).setCellRenderer(new FileNameRender());

        fileTable.getColumnModel().getColumn(0).setPreferredWidth(250);// columna nombre
        fileTable.getColumnModel().getColumn(1).setPreferredWidth(150);//columna ultima modif
        fileTable.getColumnModel().getColumn(2).setPreferredWidth(100);//columann tipo
        fileTable.getColumnModel().getColumn(3).setPreferredWidth(80);//columna size

        fileTable.getTableHeader().setReorderingAllowed(false);

        fileTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override

            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = fileTable.columnAtPoint(e.getPoint());
                
                // Mapeamos el clic en la cabecera con el criterio de ordenamiento correcto
                if (col == 0) {
                    sortCriteria = SortCriteria.NAME;
                } else if (col == 1) {
                    sortCriteria = SortCriteria.DATE;
                } else if (col == 2) {
                    sortCriteria = SortCriteria.TYPE;
                } else if (col == 3) {
                    sortCriteria = SortCriteria.SIZE;
                }
                
                // Actualizamos el ComboBox para que coincida
                if(col >= 0 && col < 4) {
                    opcionesOrdenar.setSelectedIndex(col);
                }

                actualizarVistaBotones(new File(currentDirPath));
                displayContents(new File(currentDirPath));
            }

        });

        fileTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        fileTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {

                    //QUIZA AQUI EXCEPTION
                    int row = fileTable.getSelectedRow();
                    if (row != -1) {
                        File file = (File) tableModel.getValueAt(fileTable.convertRowIndexToModel(row), 0);

                        if (file.isDirectory()) {

                            //aqui logica de botones y todo lo demas
                            displayContents(file);
                            updateTree(file);
                            actualizarVistaBotones(file);
                        }
                    }

                }
            }
        });
    }

    private class FileNameRender extends DefaultTableCellRenderer {

        private final FileSystemView fsv = FileSystemView.getFileSystemView();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            File file = (File) value;

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setIcon(fsv.getSystemIcon(file));
            setText(fsv.getSystemDisplayName(file));
            
            if (!isSelected) {
                setBackground(COLOR_FONDO);
                setForeground(COLOR_TEXTO);
            } else {
                setBackground(COLOR_SELECCION);
                setForeground(Color.WHITE);
            }
            
            return this;
        }
    }
    
    private class DarkHeaderRenderer extends DefaultTableCellRenderer {
        public DarkHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.LEFT);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setBackground(COLOR_PANEL);
            setForeground(COLOR_NARANJA);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(60, 60, 60)));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value.toString());
            return this;
        }
    }

    private void displayContents(File directory) {
        currentDirPath = directory.getAbsolutePath();
        //listModel.clear();
        pathLabel.setText("Ruta Actual: " + directory.getAbsolutePath());

        File[] contents = directory.listFiles();
        List<File> filesToShow = new ArrayList<>();

        if (contents != null) {

            List<File> directorios = new ArrayList<>();
            List<File> files = new ArrayList<>();

            for (File file : contents) {

                if (file.getAbsolutePath().equals(recycleBin) && !directory.getAbsolutePath().equals(raizUsuario)) {
                    continue;
                }

                if (file.isDirectory()) {
                    directorios.add(file);
                } else {
                    files.add(file);
                }
            }

            //File[] directories = Arrays.stream(contents).filter(File::isDirectory).toArray(File[]::new);
            //File[] files= Arrays.stream(contents).filter(File::isFile).toArray(File[]::new);
            Comparator<File> comparator = getFileComparator();

            directorios.sort(comparator);
            files.sort(comparator);

            //Arrays.sort(directories, comparator);
            //Arrays.sort(files, comparator);
//            for(File dir: directories){
//                listModel.addElement(dir);
//            }
//            
//            for(File file: files){
//                listModel.addElement(file);
//            }    
            filesToShow.addAll(directorios);
            filesToShow.addAll(files);
        }
        tableModel.setFiles(filesToShow);
    }

    private Comparator<File> getFileComparator() {
        switch (sortCriteria) {

            case DATE:
                return Comparator.comparingLong(File::lastModified).reversed();

            case SIZE:
                return Comparator.comparingLong(File::length).reversed();

            case TYPE:
                return Comparator.comparing(f -> {
                    if (f.isDirectory()) {
                        return "Directory";
                    }
                    String name = f.getName();
                    int lastDot = name.lastIndexOf('.');
                    return (lastDot == -1) ? "" : name.substring(lastDot + 1);
                });

            case NAME:
            default:
                return Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER);
        }
    }

    private class FileTableModel extends AbstractTableModel {

        private final String[] columnNames = {"Nombre", "Ultima Modificacion", "Tipo", "Tamaño"};
        private List<File> fileList = new ArrayList<>();
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        private final FileSystemView fsv = FileSystemView.getFileSystemView();

        @Override
        public int getRowCount() {
            return fileList.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            File file = fileList.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return file;

                case 1:
                    return dateFormat.format(file.lastModified());

                case 2:
                    if (file.isDirectory()) {
                        return "Carpeta de Archivos";
                    }
                    return fsv.getSystemTypeDescription(file);

                case 3:
                    if (file.isDirectory()) {
                        return "";
                    }
                    return formatSize(file.length());

                default:
                    return null;
            }
        }

        public void setFiles(List<File> files) {
            this.fileList = files;
            fireTableDataChanged();
        }

        private String formatSize(long size) {
            if (size <= 0) {
                return "0 bytes";
            }
            final String[] units = new String[]{"bytes", "KB", "MB", "GB", "TB"};
            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            return String.format("%.1f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
        }

    }

    private class FileTreeRenderer extends DefaultTreeCellRenderer {

        private final FileSystemView fsv = FileSystemView.getFileSystemView();
        
        public FileTreeRenderer() {
            setBackgroundNonSelectionColor(COLOR_FONDO);
            setTextNonSelectionColor(COLOR_TEXTO);
            setBackgroundSelectionColor(COLOR_NARANJA);
            setTextSelectionColor(Color.WHITE);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if (userObject instanceof File) {
                File file = (File) userObject;

                setText(fsv.getSystemDisplayName(file));

                setIcon(fsv.getSystemIcon(file));
            } else {
                setIcon(null);
            }
            return this;
        }
    }

    /*
    private class FileListRenderer  extends DefaultListCellRenderer {
        private final FileSystemView fsv= FileSystemView.getFileSystemView();
        
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if(value instanceof File){
                File file = (File) value;
                
                setText(fsv.getSystemDisplayName(file));
                setIcon(fsv.getSystemIcon(file));
            }
            return this;
        }
    }
     */
    //METODOS PARA EJECUTADORES
    private JInternalFrame createMusicWindow(audioPlayer player) {
        JInternalFrame musicFrame = new JInternalFrame("MUSIC INSANO", true, true, true, true);
        musicFrame.add(player, BorderLayout.CENTER);

        musicFrame.addInternalFrameListener(new InternalFrameAdapter() {

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                audioLogic playing = player.getPlayer();
                if (playing.isPlaying()) {
                    player.stopPlayback();
                }

                musicFrame.dispose();
            }
        });

        musicFrame.setSize(650, 450);
        musicFrame.setLocation(100, 100);
        musicFrame.setVisible(true);

        return musicFrame;
    }

    private JInternalFrame createGalleryWindow(VisorImagenes galeriapanel) {
        JInternalFrame galleryFrame = new JInternalFrame("GALERIA INSANA", true, true, true, true);
        galleryFrame.add(galeriapanel, BorderLayout.CENTER);

        galleryFrame.setSize(900, 600);
        galleryFrame.setLocation(100, 100);
        galleryFrame.setVisible(true);
        return galleryFrame;
    }

    private JInternalFrame createTextWindow(TextoPanel panel) {
        JInternalFrame fManagerFrame = new JInternalFrame("EDITOR DE TEXTO INSANO", true, true, true, true);
        fManagerFrame.add(panel, BorderLayout.CENTER);

        fManagerFrame.setSize(900, 600);
        fManagerFrame.setLocation(50, 50);
        fManagerFrame.setVisible(true);
        return fManagerFrame;
    }
    
    private class BotonModerno extends JButton {
        private final Color colorBase;
        private final Color colorHover;

        public BotonModerno(String text, Color bg, Color fg) {
            super(text);
            this.colorBase = bg;
            this.colorHover = bg.brighter();
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(colorHover);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(colorBase);
                }
            });
            setBackground(colorBase);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}