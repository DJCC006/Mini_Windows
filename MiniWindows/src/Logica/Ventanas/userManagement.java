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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author David
 */
public class userManagement extends JPanel {
    public userManagement(){
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
*/
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
       
    }
}
