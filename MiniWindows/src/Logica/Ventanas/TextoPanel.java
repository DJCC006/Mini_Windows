/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
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

    private JTextPane textPane;
    private JComboBox<String> fontCombo;
    private JComboBox<Integer> sizeCombo;
    private BotonModerno btnColor; 

    private final TextLogic logic;

    private final Color COLOR_FONDO = new Color(30, 30, 30);      
    private final Color COLOR_PANEL = new Color(45, 45, 48);      
    private final Color COLOR_NARANJA = new Color(233, 84, 32);   
    private final Color COLOR_TEXTO = new Color(240, 240, 240);  

    public TextoPanel() {
        this.logic = new TextLogic();

        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setMargin(new Insets(20, 20, 20, 20));
        
        textPane.setBackground(COLOR_FONDO);
        textPane.setForeground(COLOR_TEXTO);
        textPane.setCaretColor(Color.WHITE); 
        textPane.setSelectionColor(COLOR_NARANJA);
        textPane.setSelectedTextColor(Color.WHITE);

        textPane.setText("<html><body style='font-family:Arial; font-size:12px; color:#f0f0f0; background-color:#1e1e1e;'>Escribe aquí...</body></html>");

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setBorder(new LineBorder(COLOR_PANEL, 1));
        scroll.getViewport().setBackground(COLOR_FONDO);

        JPanel toolBar = crearBarraHerramientas();

        add(toolBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearBarraHerramientas() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        barra.setBackground(COLOR_PANEL);
        barra.setBorder(new EmptyBorder(5, 5, 5, 5));

        BotonModerno btnAbrir = new BotonModerno("Abrir", new Color(60, 60, 60), COLOR_TEXTO);
        btnAbrir.setIcon(UIManager.getIcon("Tree.openIcon")); 
        btnAbrir.setPreferredSize(new Dimension(90, 30));
        btnAbrir.addActionListener(e -> logic.abrirArchivo(this, textPane));

        BotonModerno btnGuardar = new BotonModerno("Guardar", COLOR_NARANJA, Color.WHITE);
        btnGuardar.setIcon(UIManager.getIcon("FileView.floppyDriveIcon")); 
        btnGuardar.setPreferredSize(new Dimension(100, 30));
        btnGuardar.addActionListener(e -> logic.guardarArchivo(this, textPane));

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontCombo = new JComboBox<>(fonts);
        fontCombo.setSelectedItem("Arial");
        fontCombo.setPreferredSize(new Dimension(160, 30));
        estilarComboBox(fontCombo); 
        fontCombo.addActionListener(e -> cambiarFuente((String) fontCombo.getSelectedItem()));

        Integer[] sizes = {8, 10, 11, 12, 14, 16, 18, 20, 24, 28, 36, 48, 72};
        sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setSelectedItem(12);
        sizeCombo.setPreferredSize(new Dimension(60, 30));
        estilarComboBox(sizeCombo); 
        sizeCombo.addActionListener(e -> cambiarTamano((Integer) sizeCombo.getSelectedItem()));

        btnColor = new BotonModerno("Color", Color.WHITE, Color.BLACK);
        btnColor.setPreferredSize(new Dimension(70, 30));
        btnColor.addActionListener(e -> cambiarColor());

        JLabel lblFont = new JLabel("Fuente:");
        lblFont.setForeground(COLOR_TEXTO);
        
        JLabel lblSize = new JLabel("Size:");
        lblSize.setForeground(COLOR_TEXTO);

        barra.add(btnAbrir);
        barra.add(btnGuardar);
        
        barra.add(Box.createHorizontalStrut(15)); 
        
        barra.add(lblFont);
        barra.add(fontCombo);
        
        barra.add(Box.createHorizontalStrut(5)); 
        
        barra.add(lblSize);
        barra.add(sizeCombo);
        
        barra.add(Box.createHorizontalStrut(15)); 
        
        barra.add(btnColor);

        return barra;
    }
    
    private void estilarComboBox(JComboBox<?> combo) {
        combo.setBackground(COLOR_FONDO);
        combo.setForeground(COLOR_TEXTO);
        combo.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        combo.setUI(new BasicComboBoxUI() {
            
            @Override 
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setContentAreaFilled(false);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                btn.setIcon(new Icon() {
                    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        g2.setColor(COLOR_TEXTO); 
                        
                        int size = 8;
                        int tx = (getIconWidth() - size) / 2;
                        int ty = (getIconHeight() - size) / 2;
                        g2.fillPolygon(new int[]{tx, tx+size, tx+size/2}, new int[]{ty, ty, ty+size}, 3);
                        g2.dispose();
                    }
                    @Override public int getIconWidth() { return 20; }
                    @Override public int getIconHeight() { return 20; }
                });
                return btn;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(COLOR_FONDO); 
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    setBackground(COLOR_NARANJA); 
                    setForeground(Color.WHITE);   
                } else {
                    setBackground(COLOR_FONDO);   
                    setForeground(COLOR_TEXTO);   
                }
                return this;
            }
        });
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

            btnColor.setColorBase(color);
            
            double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
            if (luminance > 0.5) {
                btnColor.setForeground(Color.BLACK);
            } else {
                btnColor.setForeground(Color.WHITE);
            }
            btnColor.repaint();
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

    public JTextPane getTextPane() {
        return textPane;
    }

    private class BotonModerno extends JButton {
        private Color colorBase;
        private Color colorHover;

        public BotonModerno(String text, Color bg, Color fg) {
            super(text);
            setColorBase(bg);
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(colorHover);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(colorBase);
                }
            });
            setBackground(colorBase);
        }
        
        public void setColorBase(Color bg) {
            this.colorBase = bg;
            this.colorHover = bg.brighter();
            setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            super.paintComponent(g2);
            g2.dispose();
        }
    }
}