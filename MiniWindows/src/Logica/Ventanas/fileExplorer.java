/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
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
    private JList<File> contentList;
    private DefaultListModel<File> listModel;
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
        
        listModel = new DefaultListModel<>();
        contentList = new JList<>(listModel);
        
        setupFileTree();
        
        setupContentList();
        
        
        //Ordenar
        JPanel panelSort = new JPanel(new BorderLayout());
        pathLabel = new JLabel("Ruta Actual: "+ raizUsuario);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,5));
        panelSort.add(pathLabel, BorderLayout.WEST);
        
        setupSortControls(panelSort);
        
        
        
        
        //Panel Principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(fileTree), new JScrollPane(contentList));
        splitPane.setDividerLocation(200);
        
        pathLabel = new JLabel("Ruta Actual: "+raizUsuario);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,5));
        
        add(panelSort, BorderLayout.NORTH);
        //add(pathLabel, BorderLayout.NORTH);
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
    
    private void setupContentList(){
        contentList.setCellRenderer(new FileListRenderer());
        //content
    }
    
    
    private void displayContents(File directory){
         currentDirPath= directory.getAbsolutePath();
        listModel.clear();
        pathLabel.setText("Ruta Actual: "+ directory.getAbsolutePath());
        
        File[] contents = directory.listFiles();
        
        if(contents!= null){
            File[] directories = Arrays.stream(contents).filter(File::isDirectory).toArray(File[]::new);
            File[] files= Arrays.stream(contents).filter(File::isFile).toArray(File[]::new);
            
            
            Comparator<File> comparator = getFileComparator();
            
            
            Arrays.sort(directories, comparator);
            Arrays.sort(files, comparator);
            
            
            for(File dir: directories){
                listModel.addElement(dir);
            }
            
            for(File file: files){
                listModel.addElement(file);
            }   
        }
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
    
    
    
    
}
