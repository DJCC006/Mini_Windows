/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import Logica.ManejoUsuarios.sesionManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
 *
 * @author David
 */
public class LogInWindow  extends sesionManager{
     
    private textFMod nameField= new textFMod(20);
    private passwordFMod psswordfield = new passwordFMod(100);
    private JFrame screen = new JFrame();
    
    public LogInWindow(){
        genFondos panelFondo = new genFondos("src\\recursos\\wallpapers\\Background2.png");
        
        
        //Obtencion de informacion de componentes graficos de pantalla local del usuario
        //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice gd =ge.getDefaultScreenDevice();
        screen.setUndecorated(true);//limpieza inicial
        
        //----NO OLVIDAR VOLVER A ACTIVAR ESTO----
       // gd.setFullScreenWindow(screen);//linea que pone las cosas en pantalla completa//screen
        //screen.requestFocusInWindow();
        
        screen.setContentPane(panelFondo);
        screen.setTitle("Pantall Inicio Sesion");
        screen.setSize(1920,1200);  //Tamaño standard para menus
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setLocationRelativeTo(null); //cosas para tamaño personalizado
        screen.setLayout(null);
        
        //Cargar imagen fondo
        
        
        Color colorsemiOpaque = new Color(64,64,64,130);
        nameField.setBounds(600, 450, 350, 30);
        psswordfield.setBounds(600, 500, 350, 30);
        
        nameField.setBackground(colorsemiOpaque);
        nameField.setForeground(Color.WHITE);

        psswordfield.setBackground(colorsemiOpaque);
        psswordfield.setForeground(Color.WHITE);
        
        nameField.setText("Username");
        
        nameField.addActionListener(e ->{
            psswordfield.requestFocusInWindow();
        });
        
        
        psswordfield.addActionListener(e->{
            ejecutarLogIn();
        });
        
        
        
        ImageIcon usuarioIcon = new ImageIcon("src\\recursos\\iconos\\usuario.png");
        float transparency= 0.9f;
        
        labelMod labelUser = new labelMod(usuarioIcon, transparency);
        labelUser.setBounds(680, 190, 200, 200);
        
        JLabel nameUser= new JLabel("INICIAR SESION");
        nameUser.setFont(new Font("Arial", Font.BOLD, 45));
        nameUser.setForeground(Color.WHITE);
        nameUser.setBounds(600, 310, 500, 200);
        
        //Botones de pop
        JPopupMenu popupmenu= new JPopupMenu();
        
        JMenuItem apagarItem = new JMenuItem("Apagar");
        
        apagarItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(screen, "Apagando sistema...", "Apagando",JOptionPane.INFORMATION_MESSAGE);
            screen.dispose();
        });
        
        popupmenu.add(apagarItem);
        
        JButton opcionesButton = new JButton("Opciones");
        
        opcionesButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
            
        });
        
        
        
        screen.add(nameUser);
        screen.add(labelUser);
        screen.add(nameField);
        screen.add(psswordfield);
        screen.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        LogInWindow ventana = new LogInWindow();
    }
    
    
    
    private void ejecutarLogIn(){
         String username = nameField.getText();
              
         char[] tempPass = psswordfield.getPassword();
         String passwordString = new String(tempPass);
         
         if(sesionManager.LogIn2(username, passwordString, psswordfield, screen)){
             screen.dispose();
             Escritorio ventana = new Escritorio();
         }
         
         
         /*
          if(sesionManager.LogIn(username, passwordString, psswordfield, screen)){
              screen.dispose();
              Escritorio ventana = new Escritorio();
                  
        }
*/
    }
            
    
    
    
    
    
    
    
    
}
