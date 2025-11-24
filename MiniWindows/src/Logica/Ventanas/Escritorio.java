/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

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
        screen.setContentPane(panelFondo);
        screen.setTitle("ESCRITORIO");
        //screen.setSize(1920,1200);  //Tamaño standard para menus
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //screen.setLocationRelativeTo(null);
        screen.setLayout(null);
        
        screen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // Cuando la ventana obtiene el foco (es decir, el usuario la selecciona)
                screen.revalidate();
                screen.repaint();
            }
        });
        
        
        
        
   
       
        
        
        
        
        
        
        
        screen.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        Escritorio ventana = new Escritorio();
    }
}
