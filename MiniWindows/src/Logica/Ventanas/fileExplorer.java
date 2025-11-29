/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
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
    
    
    public fileExplorer(){
        setLayout(new BorderLayout(5,5));
        
        listModel = new DefaultListModel<>();
        contentList = new JList<>(listModel);
        
        setupFileTree();
        
        setupContentList();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(fileTree), new JScrollPane(contentList));
        splitPane.setDividerLocation(200);
        
        pathLabel = new JLabel("Ruta Actual: "+raizUsuario);
        pathLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,5));
        
        add(pathLabel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        displayContents(new File(raizUsuario));
        
        /*ESTO EN CASO QUE SE BUGUE
        File raizDir = new File(raizUsuario);
        if(!raizDir.exists()){
            raizDir.mkdirs()
        }
        */
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
                
                File file = (File) node.getUserObject();
                
                
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
        listModel.clear();
        pathLabel.setText("Ruta Actual: "+ directory.getAbsolutePath());
        
        File[] contents = directory.listFiles();
        
        if(contents!= null){
            File[] directories = Arrays.stream(contents).filter(File::isDirectory).toArray(File[]::new);
            File[] files= Arrays.stream(contents).filter(File::isFile).toArray(File[]::new);
            
            Arrays.sort(directories, Comparator.comparing(File::getName));
            Arrays.sort(files, Comparator.comparing(File:: getName));
            
            
            for(File dir: directories){
                listModel.addElement(dir);
            }
            
            for(File file: files){
                listModel.addElement(file);
            }   
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
