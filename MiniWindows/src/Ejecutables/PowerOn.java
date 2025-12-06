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
import Logica.Ventanas.LogInWindow;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class PowerOn {
    public static void main(String[] args) {
        
        //Crear gestor de usuarios
        UserManager userManagement = new UserManager();
        
        //crear cuenta admin CON NUEVO METOODO
        try{
            userManagement.addUser("ADMIN2", "ADMIN123");
        }catch(IOException e){}
        
        /*
        User adminUser = new User("ADMIN","ADMIN123");
        try{
            adminUser.createInitUserDir();
            adminUser.createInicialDirs();
        }catch(IOException e){
            System.out.println("ERROR AL CREAR ADMIN");
        }
        */
        //Creacion de carpeta que maneja externamente Instagram
        instaManager instagram= new instaManager();
        instaController.getInstance().setInsta(instagram);//inicializacion de singelton para trabajar con la informacion de isntagram
        
        /*
        
        //Esto es pasable para admin. Este mismo bloque se ejecuta cuando se cree algun otro usuario
        adminUser.setTyp("ADMIN");
        ArrayList<User> usuarios = new ArrayList<>();
        usuarios.add(adminUser);
        UsuariosControlador.getInstance().setUsuarios(usuarios);
*/
        LogInWindow ventana = new LogInWindow();
    }
}
