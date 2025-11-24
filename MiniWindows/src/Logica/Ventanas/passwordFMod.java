/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPasswordField;

/**
 *
 * @author David
 */
public class passwordFMod extends JPasswordField{
    public passwordFMod(int columns){
        super(columns);
        setOpaque(false);
    }
    
     @Override 
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        
        
        Color bg =getBackground();
        g2d.setColor(new Color(bg.getRed(),bg.getGreen(), bg.getBlue(), bg.getAlpha()));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
        
        
        super.paintComponent(g);
        
    }
    
}
