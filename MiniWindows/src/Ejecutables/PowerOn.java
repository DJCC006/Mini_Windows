/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejecutables;

import Instagram.instaController;
import Instagram.instaManager;
import Logica.ManejoUsuarios.User;
import Logica.ManejoUsuarios.UserManager;
import Logica.ManejoUsuarios.UsuariosControlador;
import Logica.ManejoUsuarios.sesionManager;
import Logica.Ventanas.LogInWindow;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.UIManager;

/**
 *
 * @author David
 */ 
public class PowerOn {

    public static void main(String[] args) {

        try {
            // Opcional: Establecer Look and Feel del sistema para mejor apariencia
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignorar si no se puede establecer; la aplicación seguirá con el L&F por defecto
        }

        //Crear archivo gestor de usuarios  
        UserManager userManagement = new UserManager();
        User admin = null;
        //crear cuenta admin CON NUEVO METOODO
        try {
            userManagement.addUser("ADMIN2", "ADMIN123"); //agregar cuenta admin al archivo
            admin = new User("ADMIN2", "ADMIN123");//crear objeto admin para controlador
        } catch (IOException e) {
        }

        //Creacion de carpeta que maneja externamente Instagram
        instaManager instagram = new instaManager();
        instaController.getInstance().setInsta(instagram);//inicializacion de singelton para trabajar con la informacion de isntagram

        //Creacion de controlador de usuarios
        ArrayList<User> usuarios = new ArrayList<>();
        UsuariosControlador.getInstance().setUsuarios(usuarios);
        try {
            sesionManager.loadUsers(); //cargar como objetos users todos los usuarios almacenados en el archivo
        } catch (IOException e2) {
        };

        LogInWindow ventana = new LogInWindow();
    }
}
