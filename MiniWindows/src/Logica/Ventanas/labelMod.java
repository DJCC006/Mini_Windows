/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author David
 */
public class labelMod extends JLabel{
    private float alpha = 1.0f;
    private Image originalImage;
    
    
    
    public labelMod(ImageIcon icon, float alpha){
        super(icon);
        this.alpha=alpha;
        if(icon!= null){
            this.originalImage=icon.getImage();
        }
        setOpaque(false);
    }
    
    public labelMod(ImageIcon icon){
        this(icon, 1.0f);
    } 
    
    
    public void setAlpha(float alpha){
        if(alpha<=0f || alpha>1f){
            throw new IllegalArgumentException("Alpha no esta en los parametros adecuados");
        }
        this.alpha=alpha;
        repaint();
    }
    
    
    public void setIcon(ImageIcon icon){
        super.setIcon(icon);
        if(icon!=null){
            this.originalImage= icon.getImage();
        }else{
         this.originalImage= null;  
        }
        repaint();
        
    }
        
    @Override
    protected void paintComponent(Graphics g){
        if(originalImage == null){
            super.paintComponent(g);
            return;
        }
        

        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
        
        g2d.drawImage(originalImage, 0,0, getWidth(), getHeight(), this);
        
        g2d.setComposite(AlphaComposite.SrcOver);
        
        g2d.dispose();
    }
    
}

