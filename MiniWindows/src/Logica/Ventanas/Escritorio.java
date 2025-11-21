/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import javax.swing.JFrame;

/**
 *
 * @author David
 */
public class Escritorio {
    public Escritorio(){
        JFrame screen = new JFrame();
        screen.setSize(1920,1200);  //Tamaño standard para menus
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setLocationRelativeTo(null);
        screen.setLayout(null);
        
        
        
        
        
        
        
        
        screen.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        Escritorio ventana = new Escritorio();
    }
}
