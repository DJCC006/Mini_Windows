/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.ManejoUsuarios;

/**
 *
 * @author David
 */
public class UserUtilities {
      private String name;
    private String password;
    private String typ;
    
    public UserUtilities(String name, String password){
        this.name=name;
        this.password=password;
    }
    
    
    //setters
    public void setTyp(String typ){
        this.typ=typ;
    }
    
    public void setName(String name){
        this.name=name;
    }
    
    public void setPassword(String password){
        this.password=password;
    }
    
    
    //getters
    public String getName(){
        return name;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String typ(){
        return typ;
    }
    
}
