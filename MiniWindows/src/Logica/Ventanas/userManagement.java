/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.User;
import Logica.ManejoUsuarios.UserManager;
import Logica.ManejoUsuarios.UsuariosControlador;
import Logica.ManejoUsuarios.sesionManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
/**
 *
 * @author David
 */
public class userManagement extends JPanel {
    
    
    //Componentes iniciales
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
  
    
   private JTextField  createUserField;
   private JPasswordField createPassField;
   private JComboBox<String> deleteUserComboBox;
   
    private static final String CARD_CREAR = "CrearUsuario";
    private static final String CARD_DESACTIVAR="DesactivarUsuario";
    
    
    private final Color COLOR_FONDO = new Color(30, 30, 30);
    private final Color COLOR_PANEL = new Color(45, 45, 48);
    private final Color COLOR_NARANJA = new Color(233, 84, 32);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    
    
    
    
    public userManagement(){
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900,600));
        this.setBackground(COLOR_FONDO);
        
        
        //parte de titulo
        JLabel titulo= new JLabel("Administrador de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD,30 ));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(titulo, BorderLayout.NORTH);
        
        
        //parte de panel de navegacion
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.WEST);
        
        
        //parte central4
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_FONDO);
        
        
        cardPanel.add(CARD_CREAR, createCrearUsuarioPanel());
        cardPanel.add(CARD_DESACTIVAR,createDeleteUserPanel());
        
        add(cardPanel, BorderLayout.CENTER);
        
        
        
        cardLayout.show(cardPanel, CARD_CREAR);
    }
    
    
    
    private JPanel createMenuPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,1,10,10));
        panel.setPreferredSize(new Dimension(200,0));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(COLOR_FONDO);
        
        BotonModerno crearUserBt = new BotonModerno("Crear Nuevo Usuario",COLOR_NARANJA, COLOR_TEXTO);
        crearUserBt.addActionListener(e -> {
            prepareCreatePanel();
            cardLayout.show(cardPanel, CARD_CREAR);
            System.out.println("Se proesiono el crear");
        });
        
        BotonModerno desactivarUserBt = new BotonModerno("Eliminar Usuario", COLOR_NARANJA, COLOR_TEXTO);
        desactivarUserBt.addActionListener(e ->{
            prepareCreatePanel();
            cardLayout.show(cardPanel, CARD_DESACTIVAR);
            System.out.println("Se proesiono el desactivar");
        });
        
        
        
        panel.add(crearUserBt);
        panel.add(desactivarUserBt);
        
        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        
        return panel;
        
    }
    
    private void prepareCreatePanel(){
        if(createUserField!= null) createUserField.setText("");
        if(createPassField!= null) createPassField.setText("");
    }
    
    
    
    private void prepareDeletePanel(){
        if(deleteUserComboBox!=null){
            String[] usernames = UsuariosControlador.getInstance().getUsuarios().stream()
                .filter(u-> !u.getName().equalsIgnoreCase("ADMIN2") || u.getStatus()==true).map(User::getName).toArray(String[]::new);
            
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) deleteUserComboBox.getModel();
            model.removeAllElements();
            
            if(usernames.length>0){
                for(String name: usernames){
                    model.addElement(name);
                }
                deleteUserComboBox.setEnabled(true);
                //trabajar aqui estado
            }else{
                model.addElement("No hay usuarios a eliminar");
                deleteUserComboBox.setEnabled(false);
            }
        }
    }


    
    private JPanel createCrearUsuarioPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        panel.setBackground(COLOR_FONDO);
        
        JLabel userLabel = new JLabel("Nombre de Usuario:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        userLabel.setForeground(COLOR_TEXTO);
        JTextField userField = new JTextField(20);
        
        JLabel passLabel = new JLabel("Contraseña Inicial:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        passLabel.setForeground(COLOR_TEXTO);
        JPasswordField passField = new JPasswordField(20);
        BotonModerno saveBt= new BotonModerno("Guardar Nuevo Usuario", COLOR_NARANJA, COLOR_TEXTO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,10,10,10);
        gbc.fill= GridBagConstraints.HORIZONTAL;
        
        gbc.gridx =0; gbc.gridy=0; gbc.anchor= GridBagConstraints.EAST; panel.add(userLabel, gbc);
        gbc.gridx=1; gbc.anchor= GridBagConstraints.WEST; panel.add(userField, gbc);
        
        
        gbc.gridx =0; gbc.gridy=1; gbc.anchor= GridBagConstraints.EAST; panel.add(passLabel, gbc);
        gbc.gridx=1; gbc.anchor= GridBagConstraints.WEST; panel.add(passField, gbc);
        
        gbc.gridx =1; gbc.gridy=2; gbc.anchor= GridBagConstraints.CENTER; panel.add(saveBt, gbc);
        
        saveBt.addActionListener(e -> {
            
            String name = userField.getText();
            char[] elements = passField.getPassword();
            String password = String.valueOf(elements);
            
            
           boolean verName=false;
           try{
                verName=UserManager.checkUsername(name);//sesionManager.userCheck(name);
           }catch(IOException e2){
               System.out.println("Erorr al checar el nombre");
           }
           
           boolean verPass = sesionManager.passwordCheck(password);
           
           if(verName!=true && verPass!= true){
               JOptionPane.showMessageDialog(this, "No se pudo crear nuevo usuario");
           }else{
               
               try{
                 UserManager.addUser(name, password);//agrego en archivo
                 User nUsuario = new User(name, password);
                 UsuariosControlador.getInstance().getUsuarios().add(nUsuario);//agrego en controlador
                 
                 JOptionPane.showMessageDialog(this, "Usuario Creado Exitosamente");
               }catch(IOException e2){
                   System.out.println("Erro a la hora de crear usuario extra");
               }
           }
        });
        return panel;
        
    }
    
    
    private JPanel createDeleteUserPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        panel.setBackground(COLOR_FONDO);
        
        
        deleteUserComboBox = new JComboBox<>(new String[]{"Cargando usuarios..."});
        deleteUserComboBox.setPreferredSize(new Dimension(250, deleteUserComboBox.getPreferredSize().height));
        
        
        JLabel selectLabel = new JLabel("Seleccionar Usuario a Eliminar:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        selectLabel.setForeground(COLOR_TEXTO);
        BotonModerno deleteButton = new BotonModerno("Eliminar Cuenta Permanentemente", COLOR_NARANJA, COLOR_TEXTO);
        
        prepareDeletePanel();
        
        
   
        
       
        
    
        
        GridBagConstraints gbc= new GridBagConstraints();
        gbc.insets= new Insets(10,10,10,10);
        gbc.fill= GridBagConstraints.HORIZONTAL;
        
        gbc.gridx=0; gbc.gridy=0; gbc.anchor = GridBagConstraints.EAST; panel.add(selectLabel,gbc);
        gbc.gridx=1; gbc.anchor=GridBagConstraints.WEST; panel.add(deleteUserComboBox, gbc);
        
        gbc.gridx=1; gbc.gridy=1; gbc.anchor = GridBagConstraints.CENTER; panel.add(deleteButton,gbc);
        
        deleteButton.addActionListener(e -> {
            String usuarioSelec = (String) deleteUserComboBox.getSelectedItem();
            if(usuarioSelec==null || usuarioSelec.equals("No hay usuarios para eliminar") || usuarioSelec.equals("Cargando usuarios...")){
                JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario valido para eliminar");
                return;
            }
            
            int confirmation = JOptionPane.showConfirmDialog(this, 
                    "¿Esta seguro que desea eliminar permanentemente al usuario '"+usuarioSelec+"'? Esta accion es irreversible",
                    "Confirmar Eliminacion", JOptionPane.YES_NO_OPTION);
            
            if(confirmation == JOptionPane.YES_OPTION){
                
                //LOGIC AQUI DE LIMINAR
                //Eliminamos de controlador
                ArrayList<User> usuarios =UsuariosControlador.getInstance().getUsuarios();
                for(User us: usuarios){
                    if(us.getName().equals(usuarioSelec)){
                        us.changStatus();
                    }
                }
                UsuariosControlador.getInstance().getUsuarios().remove(usuarioSelec);
                
                
                //Cambiamos estado en archivo
                try{
                    UserManager.changeStatus(usuarioSelec, false);
                    
                    //eliminamos archivos fisicos
                    if(UserManager.borrarFilesUser(usuarioSelec)){
                        JOptionPane.showMessageDialog(this, "Se ha eliminado el usuario exitosamente");
                    }
                }catch(IOException e2){JOptionPane.showMessageDialog(this, "Error para eliminar usuario");}
                
                prepareDeletePanel();
                this.revalidate();
                this.repaint();
            }
        });
        return panel;
    }
    
   private class BotonModerno extends JButton {

        private Color colorBase;
        private Color colorHover;

        public BotonModerno(String text, Color bg, Color fg) {
            super(text);
            this.colorBase = bg;
            this.colorHover = bg.brighter();
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
