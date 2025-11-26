/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author David
 */
public class musicTester {
    
    public musicTester(){
        JFrame screen = new JFrame();
        screen.setSize(650,450);
        screen.setLocationRelativeTo(null);
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        audioPlayer playerContent = new audioPlayer();
        screen.setLayout(new BorderLayout());
        screen.add(playerContent, BorderLayout.CENTER);
        
        
        
        screen.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        musicTester ventana = new musicTester();
    }
            
}
