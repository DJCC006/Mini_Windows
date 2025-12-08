/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 *
 * @author esteb
 */
public class TextoPanel extends JPanel {

    private  JTextPane textPane;
    private JComboBox<String> fontCombo;
    private JComboBox<Integer> sizeCombo;
    private JButton btnColor;
    
    private final TextLogic logic;

    public TextoPanel() {
        this.logic = new TextLogic();
        
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setMargin(new Insets(20, 20, 20, 20));
        textPane.setText("<html><body><p style='font-family:Arial; font-size:12px;'>Escribe aquí...</p></body></html>");
        
        JScrollPane scroll = new JScrollPane(textPane);
        
        JPanel toolBar = crearBarraHerramientas();

        add(toolBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearBarraHerramientas() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBackground(new Color(220, 220, 220));
        barra.setBorder(BorderFactory.createEtchedBorder());

        JButton btnGuardar = new JButton("💾");
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.addActionListener(e -> logic.guardarArchivo(this, textPane));
        
        JButton btnAbrir = new JButton("📂");
        btnAbrir.setToolTipText("Abrir");
        btnAbrir.addActionListener(e -> logic.abrirArchivo(this, textPane));

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontCombo = new JComboBox<>(fonts);
        fontCombo.setSelectedItem("Arial");
        fontCombo.setPreferredSize(new Dimension(150, 25));
        fontCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarFuente((String) fontCombo.getSelectedItem());
            }
        });

        Integer[] sizes = {8, 10, 11, 12, 14, 16, 18, 20, 24, 28, 36, 48, 72};
        sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setSelectedItem(12);
        sizeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarTamano((Integer) sizeCombo.getSelectedItem());
            }
        });

        btnColor = new JButton("Color");
        btnColor.setBackground(Color.BLACK);
        btnColor.setForeground(Color.BLACK);
        btnColor.addActionListener(e -> cambiarColor());

        barra.add(btnAbrir);
        barra.add(btnGuardar);
        barra.add(new JSeparator(SwingConstants.VERTICAL));
        barra.add(new JLabel("Fuente:"));
        barra.add(fontCombo);
        barra.add(new JLabel("Tamaño:"));
        barra.add(sizeCombo);
        barra.add(new JSeparator(SwingConstants.VERTICAL));
        barra.add(btnColor);

        return barra;
    }
    
    private void cambiarFuente(String fontName) {
        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs, fontName);
        aplicarEstilo(attrs);
    }

    private void cambiarTamano(int size) {
        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrs, size);
        aplicarEstilo(attrs);
    }

    private void cambiarColor() {
        Color color = JColorChooser.showDialog(this, "Selecciona un color", Color.BLACK);
        if (color != null) {
            MutableAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setForeground(attrs, color);
            aplicarEstilo(attrs);
            
            btnColor.setBackground(color);
            if((color.getRed()*0.299 + color.getGreen()*0.587 + color.getBlue()*0.114) > 186){
                btnColor.setForeground(Color.BLACK);
            } else {
                btnColor.setForeground(Color.WHITE);
            }
        }
    }

    private void aplicarEstilo(AttributeSet attrs) {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start != end) {
            StyledDocument doc = textPane.getStyledDocument();
            doc.setCharacterAttributes(start, end - start, attrs, false);
        } else {
            MutableAttributeSet inputAttributes = textPane.getInputAttributes();
            inputAttributes.addAttributes(attrs);
        }
        textPane.requestFocusInWindow();
    }
    
    
    
    public JTextPane getTextPane(){
        return textPane;
    }
            
    
}