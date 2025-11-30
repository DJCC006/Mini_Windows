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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
    
    
    
    public fileExplorer(){
        setLayout(new BorderLayout(5,5));
        
        
        
        setupFileTree();
        
        //setupContentList();
        setupContentTable();
        
        
        JPanel northPanel = new JPanel(new BorderLayout());
        
        JToolBar toolBar = setupToolBar();
        northPanel.add(toolBar, BorderLayout.NORTH);
        
        
        
        //Ordenar
        JPanel panelSort = new JPanel(new BorderLayout());
        pathLabel = new JLabel("Ruta Actual: "+ raizUsuario);
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
