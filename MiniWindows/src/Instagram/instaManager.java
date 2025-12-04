/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class instaManager {
    private RandomAccessFile users;
    
    private String mainRoot = "Instagram";
    private String usersDir = "Instagram\\users";
    private File loggedUserDir;
    private String loggedUser;
    
    public instaManager(){
        
        //creacion de carpetas principales
        File instaFolder = new File(mainRoot);
        File usersDirF = new File(usersDir);
        
        
        if(!instaFolder.exists()){
            instaFolder.mkdir();
        }else{
            System.out.println("Ya existe instaFolder");
        }
        
        if(!usersDirF.exists()){
            usersDirF.mkdir();
        }else{
            System.out.println("Ya existe userDir");
        }
        
        //creacion de archivo manejador de usuarios
        try{
           users= new RandomAccessFile(mainRoot+"\\users.ins", "rw");
        }catch(IOException e){
            System.out.println("XD a la hora de crear users");
        }
        
    }
    
    
    /*
    
    */
    public void addNewUser(String name, char genero, String username, String password, int edad) throws IOException{
        users.seek(users.length());//para ubicarnos despues del ultimo agregado
        
        users.writeUTF(name);
        users.writeChar(genero);
        users.writeUTF(username);
        users.writeUTF(password);
        users.writeLong(Calendar.getInstance().getTimeInMillis());
        users.writeInt(edad);
        users.writeBoolean(true);
        users.writeUTF("futura referencia de imagen aqui");
        
        
        //inicializa la carpeta del nuevo usuario
        initNewUserF(username);
        
    }
    
    
    //metodo de crear carpeta de nuevo usuario
    private void initNewUserF(String username) throws IOException{
        File newUserDir = new File(usersDir, username);
        
        if(!newUserDir.exists()){
            newUserDir.mkdir();
            initUserFiles(newUserDir.getAbsolutePath());
        }else{
            JOptionPane.showMessageDialog(null, "Ya existe un usuario con este nombre de usuario", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    
    //meotod que inicializa los archivos de nuevo usuario
    private void initUserFiles(String userFolder) throws IOException{
        RandomAccessFile followers= new RandomAccessFile(userFolder+"\\followers.ins", "rw");
        RandomAccessFile following = new RandomAccessFile(userFolder+"\\following.ins", "rw");
        RandomAccessFile posts = new RandomAccessFile(userFolder+"\\insta.ins", "rw");
    }
    
    
    
    //Metodo que servira para establecer la direccion del usuario loggeado
    public void setLoggedUser(String username) throws IOException{
        loggedUserDir = new File(usersDir, username);
        loggedUser=username;
    }
    
    
    //Metodo que borra la direccion del usuario cuando este cierra sesion
    public void loggoutUser() throws IOException{
        loggedUserDir= null;
        loggedUser=null;
    }
    
    
    //Getters de informacion de usuario
    public String getRealName(String username) throws IOException{
        users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            String rName =users.readUTF();
            users.readChar();
            
            if(users.readUTF().equals(username)){
                return rName;
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        
        return null;
    }
    
    
    
    public char getGender(String username) throws IOException {
        users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            char gender =users.readChar();
            
            if(users.readUTF().equals(username)){
                return gender;
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        return 0;
    }
    
    
    public String getUsername(String username) throws IOException{
        users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                return nameUser;
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        return null;
    }
    
    
    public String getPassword(String username) throws IOException{
         users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                String password= users.readUTF();
                return password;
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
    
        return null;
    }
    
    
    public String getEntryDate(String username) throws IOException {
         users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                users.readUTF();
                Long dateL = users.readLong();
                
                Date dateF= new Date(dateL);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String dateS = format.format(dateF);
                
                return dateS;
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        
        return null;
    }
    
    
    public int getAge(String username) throws IOException {
        users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                users.readUTF();
                users.readLong();
                
                int edad = users.readInt();
                return edad;
                
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        
        return 0;
    }
    
    
    
    public boolean getStatusUser(String username) throws IOException{
        users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                users.readUTF();
                users.readLong();
                users.readInt();
                
                if(users.readBoolean()){
                    return true;
                }
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        
        return false;
    }
    
    
    public String getProfilPicRoute(String username) throws IOException {
        users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                users.readUTF();
                users.readLong();
                users.readInt();
                 users.readBoolean();
                return users.readUTF();
                
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        
        
        return null;
    }
    
    
    //Metodo para desactivar cuenta
    public void desactivateUser(String username) throws IOException{
        users.seek(0);
        
        while(users.getFilePointer()< users.length()){
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                users.readUTF();
                users.readLong();
                users.readInt();
                 users.writeBoolean(false);
                 break;
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
    }
    
    
    
    //metodo para obtener la profil pic de determinado user
    public String getProfilePic(String username) throws IOException {
        users.seek(0);
        
        while(users.getFilePointer()<users.length()){
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            String picDir = users.readUTF();
            
            if(nameUser.equals(username)){
                return picDir;
            }
        }
        return null;
    }
    
    
    //metodo para comprobar si ya existe cuenta bajo el username
    public boolean checkUserExistance(String username) throws IOException{
        users.seek(0);
        
        while(users.getFilePointer()<users.length()){
            users.readUTF();
            users.readChar();
            String nameUser =users.readUTF();
            
            if(nameUser.equals(username)){
                return true;
            }
            
            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            users.readUTF();
        }
        return false;
    }
    
    
    
    //----METODOS PARA OBTENER INFORMACION PROPIA DEL USUARIO LOGEADO-----
    
    private void addFollower(String username, String newFollower) throws IOException{
        //Ubicarme dentro del archivo de la cuenta que recibira el follower
        String user2Path = usersDir+File.separator+username;
        File user2File = new File(user2Path, "\\followers.ins");
        
        RandomAccessFile followersFile = new RandomAccessFile(user2File, "rw");
        
        
        //ubicar el puntero
        followersFile.seek(followersFile.length());
        //agregar informacion respectiva del nuevo follower
        followersFile.writeUTF(newFollower);
        
        
    }
    
    
    public void addFollow(String username) throws IOException{
        File userPath = new File(loggedUserDir, "\\following.ins");
        RandomAccessFile followsFile = new RandomAccessFile(userPath, "rw");
        
        //ubicarme dentro del archivo followings del usuario y agregar nuevo follow
        followsFile.seek(followsFile.length());
        followsFile.writeUTF(username);
        
        
        //agregar follower a cuenta seguida
        addFollower(username,loggedUser);
        
    }
   
    
    
    public void quitarFollow(String username) throws IOException{
        File fileOriginal = new File(loggedUserDir, "\\following.ins");
        File fileTemp = new File(loggedUserDir, "\\following.temp");
        RandomAccessFile oFile= new RandomAccessFile(fileOriginal, "rw");
        RandomAccessFile tFile= new RandomAccessFile(fileTemp, "rw");
        
        
        String readUsername;
        boolean encontrado=false;
        
        oFile.seek(0);
        tFile.seek(0);
        while(oFile.getFilePointer()<oFile.length()){
            readUsername=oFile.readUTF();
            if(!readUsername.equals(username)){
                tFile.writeUTF(readUsername);
            }else{
                encontrado=true;
                //No necesariamente se sale forzosamente del bucle, hay que hacer que termine
            }
            
        }
        
        if(encontrado){
            if(!fileOriginal.delete()){
                throw new IOException("No se pudo borrar el archivo original");
            }
            if(!fileTemp.renameTo(fileOriginal)){
                throw new IOException("No se pudo renombrar el archivo temporal");
            }
            System.out.println("Se ha creado la sustitucion de archivos correcta para follows");
            
        }else{
            fileTemp.delete();
            System.out.println("No se encontro al usuario indicado");
        }
        
        
        //llamamos el metodo que hara lo mismo pero para followers
        quitarFollower(username, loggedUser);
    }
    
    
    
    private void quitarFollower(String username, String follower) throws IOException{
        String user2Path = usersDir+File.separator+username;
        File fileOriginal = new File(user2Path, "\\followers.ins");
        File fileTemp = new File(user2Path, "\\followers.temp");
        
        RandomAccessFile followersFile = new RandomAccessFile(fileOriginal, "rw");
        RandomAccessFile tFile = new RandomAccessFile(fileTemp, "rw");
        
        
        String readUsername;
        boolean encontrado=false;
        
        followersFile.seek(0);
        tFile.seek(0);
        while(followersFile.getFilePointer()<followersFile.length()){
            readUsername=followersFile.readUTF();
            if(!readUsername.equals(username)){
                tFile.writeUTF(readUsername);
            }else{
                encontrado=true;
                //No necesariamente se sale forzosamente del bucle, hay que hacer que termine
            }
            
        }
        
        if(encontrado){
            if(!fileOriginal.delete()){
                throw new IOException("No se pudo borrar el archivo original");
            }
            if(!fileTemp.renameTo(fileOriginal)){
                throw new IOException("No se pudo renombrar el archivo temporal");
            }
            System.out.println("Se ha creado la sustitucion de archivos correcta para followers");
            
        }else{
            fileTemp.delete();
            System.out.println("No se encontro al usuario indicado");
        }
    }
    
    
    //posiblemente revisar el tema del username --> hay que considerar el user al cual se efectuara la accion
    public String showFollowers(String username) throws IOException{
        String userPath = usersDir+File.separator+username;
        File fileOriginal = new File(userPath, "\\followers.ins");
        RandomAccessFile followersFile = new RandomAccessFile(fileOriginal, "rw");
        
        String listaFollowers="";
        
        
        followersFile.seek(0);
        while(followersFile.getFilePointer()<followersFile.length()){
            String nameuser = followersFile.readUTF();
            listaFollowers+=nameuser+"\n";
        }
        
        
        return listaFollowers;
        
    }
    
    
    public String showFollows(String username) throws IOException{
        String userPath = usersDir+File.separator+username;
        File fileOriginal = new File(userPath, "\\following.ins");
        RandomAccessFile followsFile = new RandomAccessFile(fileOriginal, "rw");
        
        String listaFollows="";
        
        
        followsFile.seek(0);
        while(followsFile.getFilePointer()<followsFile.length()){
            String nameuser = followsFile.readUTF();
            listaFollows+=nameuser+"\n";
        }
        
        
        return listaFollows;
    }
    
    
}
