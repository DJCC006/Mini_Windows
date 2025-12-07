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
    
    public userManagement(){
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900,600));
        
        
        //parte de titulo
        JLabel titulo= new JLabel("Administrador de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(titulo, BorderLayout.NORTH);
        
        
        //parte de panel de navegacion
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.WEST);
        
        
        //parte central4
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        
        cardPanel.add(CARD_CREAR, createCrearUsuarioPanel());
        cardPanel.add(CARD_DESACTIVAR,createDeleteUserPanel());
        
        add(cardPanel, BorderLayout.CENTER);
        
        
        
        cardLayout.show(cardPanel, CARD_CREAR);
        
        /*
       setSize(400,300);
       setLayout(new BorderLayout(10,10));
       
       
       JPanel cabezal = new JPanel(new FlowLayout(FlowLayout.CENTER));
       JLabel titulo = new JLabel("Gestionar Usuarios");
       titulo.setFont(new Font("Arial", Font.BOLD, 18));
       cabezal.add(titulo);
       
       add(cabezal, BorderLayout.NORTH);
       
       JPanel botonesP = new JPanel();
       botonesP.setLayout(new GridLayout(2,1,15,15));
       
       
       JButton createUserButton = new JButton("Crear Nuevo Usuario");
       createUserButton.setFont(new Font("Arial", Font.PLAIN, 14));
       createUserButton.addActionListener(e -> {
           String name= JOptionPane.showInputDialog(null, "Ingrese el nombre del nuevo usuario: ");
           String pass = JOptionPane.showInputDialog(null, "Ingrese contraseña para nuevo usuario: ");
           
           boolean verName=false;
           try{
                verName=UserManager.checkUsername(name);//sesionManager.userCheck(name);
           }catch(IOException e2){
               System.out.println("Erorr al checar el nombre");
           }
           
           boolean verPass = sesionManager.passwordCheck(pass);
           
           if(verName!=true && verPass!= true){
               JOptionPane.showMessageDialog(null, "No se pudo crear nuevo usuario");
           }else{
               
               try{
                 UserManager.addUser(name, pass);
                 JOptionPane.showMessageDialog(null, "Usuario Creado Exitosamente");
               }catch(IOException e2){
                   System.out.println("Erro a la hora de crear usuario extra");
               }
                
               
           
           /*
               User nuevoUsuario = new User(name, pass);
               try{
                  nuevoUsuario.createInitUserDir();
                  nuevoUsuario.createInicialDirs();
                  UsuariosControlador.getInstance().getUsuarios().add(nuevoUsuario);
                  
               }catch(IOException a){   
               }

           }
           
           
           
           System.out.println("Aqui creamos nuevo usuario");
       });
        
       
        JButton deleteUserButton = new JButton("Eliminar Usuario");
       deleteUserButton.setFont(new Font("Arial", Font.PLAIN, 14));
       deleteUserButton.addActionListener(e -> {
           System.out.println("Aqui borramos usuario");
       });
       
       botonesP.add(createUserButton);
       botonesP.add(deleteUserButton);
       
       JPanel centro = new JPanel(new GridBagLayout());
       centro.add(botonesP);
       
       add(centro, BorderLayout.CENTER);
       setVisible(true);
       */
    }
    
    
    
    private JPanel createMenuPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,1,10,10));
        panel.setPreferredSize(new Dimension(200,0));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(new Color(240,240,240));
        
        JButton crearUserBt = new JButton("<html><b>Crear Nuevo Usuario</b></html");
        crearUserBt.addActionListener(e -> {
            prepareCreatePanel();
            cardLayout.show(cardPanel, CARD_CREAR);
            System.out.println("Se proesiono el crear");
        });
        
        JButton desactivarUserBt = new JButton("<html><b>Desactivar Usuario</b></html>");
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
    
    /*
    private void refreshCardPanel(String cardNom){
        Component existingComponent = cardComponentMap.get(cardNom);
        if(existingComponent!=null){
            cardPanel.remove(existingComponent);
            cardComponentMap.remove(cardNom);
        }
        
        Component newComponent = null;
        if(cardNom.equals(CARD_MOSTRAR)){
            newComponent = createShowUsersPanel();
        }else if(cardNom.equals(CARD_DESACTIVAR)){
            newComponent = createDeleteUserPanel();
        }else if(cardNom.equals(CARD_CREAR)){
            newComponent = createCrearUsuarioPanel(); 
        }
        
        
        if(newComponent!=null){
            addCard(cardNom, newComponent);
        }
        
        
        cardLayout.show(cardPanel, cardNom);
        cardPanel.revalidate();
        cardPanel.repaint();
        this.revalidate();
        this.repaint();
    }
*/
    

    
    private JPanel createCrearUsuarioPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        panel.setBackground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Nombre de Usuario:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Contraseña Inicial:");
        JPasswordField passField = new JPasswordField(20);
        JButton saveBt= new JButton("Guardar Nuevo Usuario");
        
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
               JOptionPane.showMessageDialog(null, "No se pudo crear nuevo usuario");
           }else{
               
               try{
                 UserManager.addUser(name, password);//agrego en archivo
                 User nUsuario = new User(name, password);
                 UsuariosControlador.getInstance().getUsuarios().add(nUsuario);//agrego en controlador
                 
                 JOptionPane.showMessageDialog(null, "Usuario Creado Exitosamente");
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
        panel.setBackground(Color.LIGHT_GRAY);
        
        
        deleteUserComboBox = new JComboBox<>(new String[]{"Cargando usuarios..."});
        deleteUserComboBox.setPreferredSize(new Dimension(250, deleteUserComboBox.getPreferredSize().height));
        
        
        JLabel selectLabel = new JLabel("Seleccionar Usuario a Eliminar:");
        JButton deleteButton = new JButton("Eliminar Cuenta Permanentemente");
        
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
                    UserManager.borrarFilesUser(usuarioSelec);
                }catch(IOException e2){}
                
                
                
                
                
                
                
                
                prepareDeletePanel();
                this.revalidate();
                this.repaint();
            }
        });
        return panel;
    }
    
    /*
    public static void main(String[] args) {
        
     
        ArrayList<User> array = new ArrayList<>();
        User hola = new User("hola", "holis1");
        array.add(hola);
        UsuariosControlador.getInstance().setUsuarios(array);
        
        
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.add(new userManagement());
        frame.setVisible(true);
    }
*/
}
