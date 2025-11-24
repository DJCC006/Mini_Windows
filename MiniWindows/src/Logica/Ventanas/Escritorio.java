/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author David
 */
public class Escritorio {
    public Escritorio(){
        
        JFrame screen = new JFrame();
        
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd =ge.getDefaultScreenDevice();
        screen.setUndecorated(true);//limpieza inicial
        gd.setFullScreenWindow(screen);
        
        genFondos panelFondo = new genFondos("src\\recursos\\wallpapers\\Background1.png");
        //screen.setContentPane(panelFondo);
        screen.setTitle("ESCRITORIO");
        //screen.setSize(1920,1200);  //Tamaño standard para menus
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //screen.setLocationRelativeTo(null);
        screen.setLayout(new BorderLayout());
        
        
//        screen.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowGainedFocus(WindowEvent e) {
//                // Cuando la ventana obtiene el foco (es decir, el usuario la selecciona)
//                screen.revalidate();
//                screen.repaint();
//            }
//        });
        

        //Creacion de panel para barra de tareas
        JPanel taskBarPanel = new JPanel();
        taskBarPanel.setBackground(Color.DARK_GRAY);
        taskBarPanel.setPreferredSize(new Dimension(screen.getWidth(),50));
        taskBarPanel.setLayout(new BorderLayout());
        
        

        
        //Creacion de componentes de taskbar
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
        leftPanel.setOpaque(false);
        taskBarPanel.add(leftPanel,BorderLayout.WEST);
        
        //JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createHorizontalGlue());
        
        
        
        
        taskBarPanel.add(centerPanel, BorderLayout.CENTER);
        
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
        rightPanel.setOpaque(false);
        taskBarPanel.add(rightPanel,BorderLayout.EAST);
        
        
        //Agregado de botones
        final int ICON_SIZE=40;
        ImageIcon menuIcon = new ImageIcon("src\\recursos\\iconos\\menuBt.png");
        Image originalImage = menuIcon.getImage();
        Image scalatedImage = originalImage.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        ImageIcon scaledImage = new ImageIcon(scalatedImage);
        
        ImageIcon filesIcon = scaleImage("src\\recursos\\iconos\\filesIcon.png", ICON_SIZE);
        ImageIcon cmdIcon = scaleImage("src\\recursos\\iconos\\cmdIcon.png", ICON_SIZE);
        ImageIcon txtIcon = scaleImage("src\\recursos\\iconos\\txtIcon.png", ICON_SIZE);
        ImageIcon imagesIcon = scaleImage("src\\recursos\\iconos\\imagenesIcon.png", ICON_SIZE);
        ImageIcon musicIcon = scaleImage("src\\recursos\\iconos\\musicIcon.png", ICON_SIZE);
        ImageIcon instaIcon = scaleImage("src\\recursos\\iconos\\instaIcon.png", ICON_SIZE);
        
        JPopupMenu menu = new JPopupMenu();
        
      
        
        
        
        JButton menuButton = new JButton(scaledImage);
        menuButton.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        menuButton.setMaximumSize(new Dimension(ICON_SIZE, ICON_SIZE));
        
        menuButton.addActionListener(new ActionListener(){
          @Override 
          public void actionPerformed(ActionEvent e){
             JButton source = (JButton) e.getSource();
             
             int x=0;
             int y=(source.getWidth()-menu.getPreferredSize().width)/2;
             menu.show(source, x, y);
          }
                    
        });
        
        
        
        
        menuButton.setOpaque(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        leftPanel.add(menuButton);
        
        
        
        JButton filesBt = new JButton(filesIcon);
        filesBt.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        filesBt.setMaximumSize(new Dimension(ICON_SIZE, ICON_SIZE));
        filesBt.setOpaque(false);
        filesBt.setContentAreaFilled(false);
        filesBt.setBorderPainted(false);
        
        JButton cmdBt = new JButton(cmdIcon);
        cmdBt.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        cmdBt.setMaximumSize(new Dimension(ICON_SIZE, ICON_SIZE));
        cmdBt.setOpaque(false);
        cmdBt.setContentAreaFilled(false);
        cmdBt.setBorderPainted(false);
        
        JButton txtBt= new JButton(txtIcon);
        txtBt.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        txtBt.setMaximumSize(new Dimension(ICON_SIZE, ICON_SIZE));
        txtBt.setOpaque(false);
        txtBt.setContentAreaFilled(false);
        txtBt.setBorderPainted(false);
        
        JButton imagesBt = new JButton(imagesIcon);
        imagesBt.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        imagesBt.setMaximumSize(new Dimension(ICON_SIZE, ICON_SIZE));
        imagesBt.setOpaque(false);
        imagesBt.setContentAreaFilled(false);
        imagesBt.setBorderPainted(false);
        
        JButton musicBt= new JButton(musicIcon);
        musicBt.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        musicBt.setMaximumSize(new Dimension(ICON_SIZE, ICON_SIZE));
        musicBt.setOpaque(false);
        musicBt.setContentAreaFilled(false);
        musicBt.setBorderPainted(false);
        
        
        JButton instaBt = new JButton(instaIcon);
        instaBt.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        instaBt.setMaximumSize(new Dimension(ICON_SIZE, ICON_SIZE));
        instaBt.setOpaque(false);
        instaBt.setContentAreaFilled(false);
        instaBt.setBorderPainted(false);
        
        Dimension spacer = new Dimension(10,0);
        centerPanel.add(filesBt);
        centerPanel.add(Box.createRigidArea(spacer));
        centerPanel.add(txtBt);
        centerPanel.add(Box.createRigidArea(spacer));
        centerPanel.add(imagesBt);
        centerPanel.add(Box.createRigidArea(spacer));
        centerPanel.add(cmdBt);
        centerPanel.add(Box.createRigidArea(spacer));
        centerPanel.add(musicBt);
        centerPanel.add(Box.createRigidArea(spacer));
        centerPanel.add(instaBt);
        
        centerPanel.add(Box.createHorizontalGlue());
        
                
        
        
        
        
        screen.add(taskBarPanel, BorderLayout.SOUTH);
        screen.add(panelFondo, BorderLayout.CENTER);
        screen.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        Escritorio ventana = new Escritorio();
    }
    
    
    private ImageIcon scaleImage(String rutaImagen, int ICON_SIZE){
        ImageIcon menuIcon = new ImageIcon(rutaImagen);
        Image originalImage = menuIcon.getImage();
        Image scalatedImage = originalImage.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scalatedImage);
    }
    
    
}
