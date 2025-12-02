/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
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
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
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
public class fileExplorer extends JPanel{
    private static String userName=UserLogged.getInstance().getUserLogged().getName();
    private static final String raizUsuario = "src\\Z\\Usuarios\\"+userName+"\\";
    private static final String recycleBin = raizUsuario+"Papelera";
    private genFondos panelFondo;
    
    
    
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
    
    
    private enum SortCriteria{
        NAME, DATE, SIZE, TYPE;
    }
    
    
    //cosas para portapapeles
    private List<File> clipboardFiles= new ArrayList<>();
    private boolean isCutOperation =false;
    
    
    
    
    public fileExplorer(genFondos panelFondo){
        setLayout(new BorderLayout(5,5));
        this.panelFondo=panelFondo;
        
        
        File papelera = new File(recycleBin);
        
        setupFileTree();
        setupContentTable();
        
        
        JPanel northPanel = new JPanel(new BorderLayout());
        
        JToolBar toolBar = setupToolBar();
        northPanel.add(toolBar, BorderLayout.NORTH);
        
        
        
        //Ordenar
        JPanel panelSort = new JPanel(new BorderLayout());
        pathLabel = new JLabel("Ruta Actual: "+ formatDisplayPath(raizUsuario));
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,5));
        panelSort.add(pathLabel, BorderLayout.WEST);
        
        setupSortControls(panelSort);
        northPanel.add(panelSort, BorderLayout.CENTER);
        
        
        
        //Panel Principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(fileTree), new JScrollPane(fileTable));
        splitPane.setDividerLocation(200);
        
        pathLabel = new JLabel("Ruta Actual: "+raizUsuario);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,5));
       // add(pathLabel, BorderLayout.WEST);
        
        //add(panelSort, BorderLayout.NORTH);
        add(northPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        
        File rootFile = new File(raizUsuario);
        currentDirPath= raizUsuario;
        displayContents(rootFile);
        
        /*ESTO EN CASO QUE SE BUGUE
        File raizDir = new File(raizUsuario);
        if(!raizDir.exists()){
            raizDir.mkdirs()
        }
        */
        
    }
    
    
    private String formatDisplayPath(String userString){
        int rootIndex = userString.indexOf(raizUsuario);
        
        return userString.substring(4);
        
//        
//        if(rootIndex!=-1){
//            return userString.substring(rootIndex);
//        }
//        return userString;
    }
    
    
    private JToolBar setupToolBar(){
        JToolBar toolBar= new JToolBar();
        toolBar.setFloatable(false);
        
        JButton newFolderButton = new JButton("<html><b>Nueva Carpeta<b></html>", UIManager.getIcon("FileChooser.newFolderIcon"));
        newFolderButton.setToolTipText("Crear una nueva carpeta en la ubicacion actual");
        newFolderButton.addActionListener(e ->createNewFolder());
        toolBar.add(newFolderButton);
        
        
        JButton newFileButton = new JButton("<html><b>Nuevo Archivo</b></html>",UIManager.getIcon("FileChooser.getFolderIcon"));
        newFileButton.setToolTipText("Crear un nuevo archivo de texto (.txt) en la ubicacion actual");
        newFileButton.addActionListener(e -> createNewFile());
        toolBar.add(newFileButton);
        
        //Creacion de opciones de portapapeles
        JButton copiarBt= new JButton("Copiar", UIManager.getIcon("Table.copyIcon"));
        copiarBt.setToolTipText("Copia el archivo o carpeta seleccionada");
        copiarBt.addActionListener(e -> copySelectedFiles(false));
        toolBar.add(copiarBt);
        
        JButton pegarBt = new JButton("Pegar", UIManager.getIcon("Table.pasteIcon"));
        pegarBt.setToolTipText("Pega los archivos/carpetas copiados/cortados");
        pegarBt.addActionListener(e -> pasteFiles());
        toolBar.add(pegarBt);
        
        toolBar.addSeparator();
        
        JButton eliminarBt = new JButton("Eliminar", UIManager.getIcon("InternalFrame.closeIcon"));
        eliminarBt.setToolTipText("Mueve el archivo o carpeta a la Papelera");
        eliminarBt.addActionListener(e -> deleteSelectedFiles());
        toolBar.add(eliminarBt);
        
        
        JButton abrirBt = new JButton("Abrir", UIManager.getIcon("InternalFrame.openIcon"));
        abrirBt.setToolTipText("Abrir el archivo seleccionado");
        abrirBt.addActionListener(e -> openSelectedFile());
        toolBar.add(abrirBt);
        
        
        
        
        
        return toolBar;
    }
    
    
    private void createNewFolder(){
        String folderName = JOptionPane.showInputDialog(this, "Ingresa el nombre de la nueva carpeta:",
                "Crear Carpeta",
                JOptionPane.PLAIN_MESSAGE);
        
        if(folderName!= null && !folderName.trim().isEmpty()){
            File newFolder = new File(currentDirPath, folderName.trim());
            
            if(newFolder.exists()){
                JOptionPane.showMessageDialog(this,
                        "La Carpeta ya existe",
                        "Error de Creacion",
                        JOptionPane.ERROR_MESSAGE);
            }else{
                if(newFolder.mkdir()){
                    displayContents(new File(currentDirPath));
                    updateTree(new File(currentDirPath));
                }else{
                    JOptionPane.showMessageDialog(this, "Error al crear la carpeta",
                            "Error de creacion",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    
    
    private void createNewFile(){
        String fileName = JOptionPane.showInputDialog(this, 
                "Ingresa el nombre de nuevo archivo (ej: documento.txt):",
                JOptionPane.PLAIN_MESSAGE);
        
        if(fileName!=null && !fileName.trim().isEmpty()){
            String finalFileName = fileName.trim();
            if(!finalFileName.contains(".")){
                finalFileName+=".txt";
            }
            
             File newFile = new File(currentDirPath, finalFileName);
             
             if(newFile.exists()){
                 JOptionPane.showMessageDialog(this, 
                         "El archivo ya existe",
                         "Error de Creacion",
                         JOptionPane.ERROR_MESSAGE);
             }else{
                 try{
                     if(newFile.createNewFile()){
                         displayContents(new File(currentDirPath));
                     }else{
                         JOptionPane.showMessageDialog(this, 
                                 "Error al crear el archivo",
                                 "Error de Creacion",
                                 JOptionPane.ERROR_MESSAGE);
                     }
                 }catch(IOException ex){
                     JOptionPane.showMessageDialog(this, 
                             "Error de E/S al crear el archivo: "+ex.getMessage(),
                             "Error de Creacion",
                             JOptionPane.ERROR_MESSAGE);
                 } 
             }
        }   
    }
    
    
    
    private void openSelectedFile(){
        int selectedRow = fileTable.getSelectedRow();
        //verificar que no sea un directorio
            
        File  file= (File) fileTable.getValueAt(selectedRow,0);

        if(file.isDirectory()){
            JOptionPane.showMessageDialog(this, "Solamente abrir ARCHIVOS, no directorios", "Atencion", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        String nameFile = file.getName();

        //ejecutar codigo de identificador

        //Checkar si es tipo para musica
        if(nameFile.toLowerCase().endsWith(".wav") || nameFile.toLowerCase().endsWith(".mp3")){
            JInternalFrame newImages= createMusicWindow();
            panelFondo.add(newImages);
             try{
                 newImages.setSelected(true);
             }catch(java.beans.PropertyVetoException ex){
                 //ignore
             }

        }



        if(nameFile.toLowerCase().endsWith(".png") || nameFile.toLowerCase().endsWith(".jpg")){
            JInternalFrame newImages= createGalleryWindow();
            panelFondo.add(newImages);
             try{
                 newImages.setSelected(true);
             }catch(java.beans.PropertyVetoException ex){
                 //ignore
             }
        }
            
    }
    
    
    private void copySelectedFiles(boolean isCut){
        int[] selectedRows = fileTable.getSelectedRows();
        if(selectedRows.length==0){
            JOptionPane.showMessageDialog(this, "Selecciona los archivos o carpetas a copiar/cortar", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        
        clipboardFiles.clear();
        isCutOperation = isCut;
        
        for(int row: selectedRows){
            File file= (File) tableModel.getValueAt(row, 0);
            clipboardFiles.add(file);
        }
        
        String operation = isCut ? "Cortar" : "Copiar";
        JOptionPane.showMessageDialog(this, selectedRows.length + "elemento(s) preparado (s) para "+operation +".", "Portapapeles", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void pasteFiles(){
        
        if(clipboardFiles.isEmpty()){
            JOptionPane.showMessageDialog(this, "El portapapeles esta vacio", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        File targetDir= new File(currentDirPath);
        
        if(!targetDir.isDirectory()){
            JOptionPane.showMessageDialog(this,"La ubicacion de destino no es una carpeta valida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = true;
        List<File> sourceFiles = new ArrayList<>(clipboardFiles);
        
        for(File sourceFile: sourceFiles){
            try{
                File destFile = new File(targetDir, sourceFile.getName());
                
                
                if(isCutOperation){
                    Files.move(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }else{
                    if(sourceFile.isDirectory()){
                        recursiveCopy(sourceFile.toPath(), destFile.toPath());
                    }else{
                        Files.copy(sourceFile.toPath(), destFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                
            }catch(IOException e){
                success = false;
                JOptionPane.showMessageDialog(this, "Error al "+(isCutOperation ? "mover" : "copiar")+" "+ sourceFile.getName()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if(success){
            if(isCutOperation){
                clipboardFiles.clear();
                isCutOperation=false;
            }
            displayContents(targetDir);
            updateTree(targetDir);
            JOptionPane.showMessageDialog(this, "Elementos pegados con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
        }  
    }
    
    
    
    
    private void recursiveCopy(Path source, Path dest) throws IOException{
        if(!Files.exists(dest)){
            Files.createDirectories(dest);
        }
        
        
        
        try(var Stream = Files.walk(source)){
            Stream.forEach(sourcePath -> {
                
                try{
                    Path relative = source.relativize(sourcePath);
                    Path destPath = dest.resolve(relative);


                    if(Files.isDirectory(sourcePath)){
                        if(!Files.exists(destPath)){
                            Files.createDirectory(destPath);
                        }
                    }else{
                        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    }
                }catch(IOException e){
                    System.err.println("Error copiando "+sourcePath + ": "+e.getMessage());
                }
            });
        }
    }
    
    
    
    private void deleteSelectedFiles(){
        int[] selectedRows= fileTable.getSelectedRows();
        if(selectedRows.length==0){
            JOptionPane.showMessageDialog(this, "Selecciona los archivos o carpetas a eliminar.", "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        
        List<File> filesToDelete = new ArrayList<>();
        for(int row: selectedRows){
            File file= (File) tableModel.getValueAt(row,0);
            filesToDelete.add(file);
        }
        
        
        int confirmation = JOptionPane.showConfirmDialog(this, 
                "¿Esta seguro de que quiere mover "+selectedRows.length+ "elemento(s) a la Papelera?",
                "Confirmar Eliminacion",
                JOptionPane.YES_NO_OPTION);
        
        if(confirmation!= JOptionPane.YES_NO_OPTION){
            return ;
        }
        
        File papelera = new File(recycleBin);
        boolean success=true;
        
        
        for(File file: filesToDelete){
            
            if(file.getAbsolutePath().equals(recycleBin)){
                JOptionPane.showMessageDialog(this, "No se puede eliminar la Papelera de Reciclaje", "Error de Eliminacion", JOptionPane.ERROR_MESSAGE);
                success=false;
                continue;
            }
            
            
            
            //File file= (File) tableModel.getValueAt(row,0);
            
            
            String uniqueName = file.getName()+"-"+ UUID.randomUUID().toString().substring(0,4);
            File destFile = new File(recycleBin, uniqueName);
            
            
            try{
                Files.move(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch(IOException e){
                success = false;
                JOptionPane.showMessageDialog(this, "Error al mover "+file.getName()+ " a la Papelera: "+ e.getMessage(), "Error de eliminacion", JOptionPane.ERROR_MESSAGE);
            }
            
            if(success){
                File currentDir = new File(currentDirPath);
                displayContents(currentDir);
                updateTree(currentDir);
                JOptionPane.showMessageDialog(this, "Elementos movidos a la Papelera con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    
    
    
    
    
    
    private void updateTree(File parentDir){
        
        DefaultMutableTreeNode nodeToUpdate = findNode(raizNodo, parentDir);
        
        if(nodeToUpdate!= null){
            populateNode(nodeToUpdate);
            treeModel.nodeStructureChanged(nodeToUpdate);
        } 
    }
    
    
    //recursiva de apoyo
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode startNode, File targetFile){
        if(startNode.getUserObject().equals(targetFile)){
            return startNode;
        }
        
        for(int i=0; i< startNode.getChildCount(); i++){
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)  startNode.getChildAt(i);
            
            
            if(child.getUserObject() instanceof File){
                DefaultMutableTreeNode found = findNode(child, targetFile);
                if(found!=null){
                    return found;
                }
            }
        }
        return null;
    }
    
    
    
    private void setupSortControls(JPanel panelCambios){
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10 ,0));
        opcionesOrdenar = new JComboBox<>(new String[]{"Nombre", "Fecha", "Tamaño", "Tipo"});
        opcionesOrdenar.setSelectedItem("Nombre");
        
        
        
        sortPanel.add(new JLabel("Ordenar por:"));
        sortPanel.add(opcionesOrdenar);
        
        
        opcionesOrdenar.addActionListener(e ->{
            String selected =  (String) opcionesOrdenar.getSelectedItem();
            
            if("Nombre".equals(selected)){
                sortCriteria= SortCriteria.NAME;
            }else if("Fecha".equals(selected)){
                sortCriteria = SortCriteria.DATE;
            }else if("Tamaño".equals(selected)){
                sortCriteria = SortCriteria.SIZE;
            }else if("Tipo".equals(selected)){
                sortCriteria = SortCriteria.SIZE;
            }
            
            displayContents(new File(currentDirPath)); //posiblemente el curretnDirPath no se actualiza
        });
        
        
        panelCambios.add(sortPanel, BorderLayout.EAST);
        
    }
            
    
    
    private void setupFileTree(){
        raizNodo = new DefaultMutableTreeNode(new File(raizUsuario));
        treeModel = new DefaultTreeModel(raizNodo);
        fileTree = new JTree(treeModel);
        
        fileTree.setCellRenderer(new FileTreeRenderer());
        fileTree.setRootVisible(true);
        
        populateNode(raizNodo);
        
        fileTree.addTreeSelectionListener(new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent e){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if(node==null) return;
                
                
                
                Object userObject = node.getUserObject();
                if(!(userObject instanceof File)) return;
                
                File file = (File) userObject;
                
                
                if(file.isDirectory()){
                    displayContents(file);
                    
                    if(node.getChildCount()==0 || (node.getChildCount() == 1 && node.getFirstChild().toString().equals("Cargando..."))){
                        populateNode(node);
                    }
                } 
            }
        });
    }
    
    private void populateNode(DefaultMutableTreeNode parentNode){
        parentNode.removeAllChildren();
        File parentFile = (File) parentNode.getUserObject();
        File[] children = parentFile.listFiles(File::isDirectory);
        
        if(children != null){
            Arrays.sort(children, Comparator.comparing(File::getName)); //posiblemente quitar esto para dejarlo a merced de usuario
            
            for(File child: children){
                
                if(child.getAbsolutePath().equals(recycleBin))continue;
                
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                
                if(child.listFiles(File::isDirectory)!=null && child.listFiles(File::isDirectory).length>0){
                    childNode.add(new DefaultMutableTreeNode("Cargando..."));
                }
                treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
            }
        }
    }
    
    private void setupContentTable(){
        
        tableModel = new FileTableModel();
        fileTable= new JTable(tableModel);
        
        
        fileTable.getColumnModel().getColumn(0).setCellRenderer(new FileNameRender());
        
        
        fileTable.getColumnModel().getColumn(0).setPreferredWidth(250);// columna nombre
        fileTable.getColumnModel().getColumn(1).setPreferredWidth(150);//columna ultima modif
        fileTable.getColumnModel().getColumn(2).setPreferredWidth(100);//columann tipo
        fileTable.getColumnModel().getColumn(3).setPreferredWidth(80);//columna size
        
        fileTable.getTableHeader().setReorderingAllowed(false);
        
        
        fileTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            
            public void mouseClicked(java.awt.event.MouseEvent e){
                int col = fileTable.columnAtPoint(e.getPoint());
                
                if(col==0) sortCriteria = SortCriteria.NAME;
                else if(col==1)sortCriteria=SortCriteria.DATE;
                else if(col ==2) sortCriteria= SortCriteria.TYPE;
                else if(col==3) sortCriteria = SortCriteria.SIZE;
                
                
                opcionesOrdenar.setSelectedIndex(col);
                
                displayContents(new File(currentDirPath));
            }
            
        });
        
        fileTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        fileTable.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e){
                if(e.getClickCount()==2){
                    int row = fileTable.getSelectedRow();
                    if(row!=-1){
                        File file = (File) tableModel.getValueAt(fileTable.convertRowIndexToModel(row),0);
                        
                        if(file.isDirectory()){
                            displayContents(file);
                            updateTree(file);
                        }
                    }
                }
            }
        });
    }
    
    
    
    private class FileNameRender extends DefaultTableCellRenderer{
        private final FileSystemView fsv = FileSystemView.getFileSystemView();
        
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            File file= (File) value;
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setIcon(fsv.getSystemIcon(file));
            setText(fsv.getSystemDisplayName(file));
            return this;
        }
    }
    
    
    private void displayContents(File directory){
        currentDirPath= directory.getAbsolutePath();
        //listModel.clear();
        pathLabel.setText("Ruta Actual: "+ directory.getAbsolutePath());
        
        File[] contents = directory.listFiles();
        List<File> filesToShow = new ArrayList<>();
        
        if(contents!= null){
            
            List<File> directorios = new ArrayList<>();
            List<File> files = new ArrayList<>();
            
            for(File file:contents){
                
                if(file.getAbsolutePath().equals(recycleBin) && !directory.getAbsolutePath().equals(raizUsuario)){
                    continue;
                }
                
                
                if(file.isDirectory()){
                    directorios.add(file);
                }else{
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
    
    
    
    
    private Comparator<File> getFileComparator(){
        switch(sortCriteria){
            
            case DATE:
                return Comparator.comparingLong(File::lastModified).reversed();
                
            case SIZE:
                return Comparator.comparingLong(File::length).reversed();
                
            case TYPE:
                return Comparator.comparing(f ->{
                   if(f.isDirectory()) return "Directory";
                   String name =f.getName();
                   int lastDot = name.lastIndexOf('.');
                   return (lastDot==-1) ? "" : name.substring(lastDot+1);
                });
            
            case NAME:
                default:
                return Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER);
        }
    }
    
    
    
    
    
    private class FileTableModel extends AbstractTableModel{
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
        public String getColumnName(int col){
            return columnNames[col];
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            File file = fileList.get(rowIndex);
            
            
            switch(columnIndex){
                case 0:
                    return file;
                    
                case 1:
                    return dateFormat.format(file.lastModified());
                    
                case 2:
                    if(file.isDirectory()){
                        return "Carpeta de Archivos";
                    }
                    return fsv.getSystemTypeDescription(file);
                    
                case 3:
                    if(file.isDirectory()){
                        return "";
                    }
                    return formatSize(file.length());
                    
                default:
                    return null;
            }
        }
        
        
         public void setFiles(List<File> files){
             this.fileList = files;
             fireTableDataChanged();
         }
         
         
         
         
         private String formatSize(long size){
             if(size<=0) return "0 bytes";
             final String[] units = new String[]{"bytes","KB", "MB", "GB", "TB"};
             int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
             return String.format("%.1f %s", size/Math.pow(1024, digitGroups),units[digitGroups]);
         }
        
        
    }
    
    
    
   
    
    private class FileTreeRenderer extends DefaultTreeCellRenderer{
        private final FileSystemView fsv = FileSystemView.getFileSystemView();
        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();
            
            if(userObject instanceof File){
                File file = (File) userObject;
                
                setText(fsv.getSystemDisplayName(file));
                
                setIcon(fsv.getSystemIcon(file));
            }else{
                setIcon(null);
            }
            return this;
        }
    }
    
    
    
    
    
    //METODOS PARA EJECUTADORES
     private JInternalFrame createMusicWindow(){
        JInternalFrame musicFrame = new JInternalFrame("MUSIC INSANO", true, true, true, true);
        audioPlayer musicPanel = new audioPlayer();
        musicFrame.add(musicPanel, BorderLayout.CENTER);
        
        musicFrame.setSize(650,450);
        musicFrame.setLocation(100, 100);
        musicFrame.setVisible(true);
        return musicFrame;
    }
    
    
     private JInternalFrame createGalleryWindow(){
        JInternalFrame galleryFrame = new JInternalFrame("GALERIA INSANA", true, true, true, true);
        VisorImagenes galeriapanel = new VisorImagenes();
        galleryFrame.add(galeriapanel, BorderLayout.CENTER);
        
        galleryFrame.setSize(900,600);
        galleryFrame.setLocation(100, 100);
        galleryFrame.setVisible(true);
        return galleryFrame;
    }
     
     
     
     
    /*
    
    private class FileTreeRenderer extends DefaultTreeCellRenderer{
        private final FileSystemView fsv = FileSystemView.getFileSystemView();
        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus ){
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();
            
            
            if(userObject instanceof File){
                File file = (File) userObject;
                setText(fsv.getSystemDisplayName(file));
                setIcon(fsv.getSystemIcon(file));
            }else{
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
    
    
    
}
