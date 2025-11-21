/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import Logica.ManejoUsuarios.sesionManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author David
 */
public class LogInWindow  extends sesionManager{
     public LogInWindow(){
        genFondos panelFondo = new genFondos("src\\recursos\\wallpapers\\Background2.png");
        
        JFrame screen = new JFrame();
        screen.setContentPane(panelFondo);
        screen.setSize(1920,1200);  //Tamaño standard para menus
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setLocationRelativeTo(null);
        screen.setLayout(null);
        
        //Cargar imagen fondo
        
        
        
        
        JTextField nameField = new JTextField();
        nameField.setBounds(600, 450, 350, 30);
        JPasswordField psswordfield = new JPasswordField(100);
        psswordfield.setBounds(600, 500, 350, 30);
        JButton logIn = new JButton("Iniciar Sesion");
        logIn.setBounds(680, 550, 200, 40);
        
         logIn.addActionListener(new ActionListener(){
          @Override 
          public void actionPerformed(ActionEvent e){
              String username = nameField.getText();
              
             char[] tempPass = psswordfield.getPassword();
             String passwordString = new String(tempPass);
              
              
              if(sesionManager.LogIn(username, passwordString, psswordfield)){
                  Escritorio ventana = new Escritorio();
                  screen.dispose();
              }
          }
                    
        });
        
        
        screen.add(nameField);
        screen.add(logIn);
        screen.add(psswordfield);
        screen.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        LogInWindow ventana = new LogInWindow();
    }
    
    
    
    
    
    
    
    
}
