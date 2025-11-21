/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejecutables;

import Logica.ManejoUsuarios.User;
import Logica.ManejoUsuarios.UsuariosControlador;
import Logica.Ventanas.LogInWindow;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class PowerOn {
    public static void main(String[] args) {
        User adminUser = new User("ADMIN","ADMIN123");
        adminUser.setTyp("ADMIN");
        ArrayList<User> usuarios = new ArrayList<>();
        usuarios.add(adminUser);
        UsuariosControlador.getInstance().setUsuarios(usuarios);
        LogInWindow ventana = new LogInWindow();
    }
}
