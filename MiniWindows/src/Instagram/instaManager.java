/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
        // AGREGADO: Archivo de comentarios para que no falle al iniciar
        RandomAccessFile comments = new RandomAccessFile(userFolder+"\\comments.ins", "rw");
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
    
    
    public String getProfilePicRoute(String username) throws IOException {
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
    
    
    public boolean addFollow(String username) throws IOException{
        File userPath = new File(loggedUserDir, "\\following.ins");
        RandomAccessFile followsFile = new RandomAccessFile(userPath, "rw");
        
        //Verificacion de username repetido
        followsFile.seek(0);
        boolean repetido=false;
        while(followsFile.getFilePointer()< followsFile.length()){
            String readName= followsFile.readUTF();
            if(readName.equals(username)){
                repetido=true;
            }
        }
        
        //ubicarme dentro del archivo followings del usuario y agregar nuevo follow
        if(!repetido){
            followsFile.seek(followsFile.length());
            followsFile.writeUTF(username);
            
            //agregar follower a cuenta seguida
            addFollower(username,loggedUser);
            return true;
            
        }
        
        return false; //el return false es que identifico el username como repetido

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
    
    
    /*
        FORMATO
        STRING refImag
        STRING autor
        STRING fecha de post
        STRING contenido
    
    */
    
    
    //CONSIDERACION ->Se debe que realizar antes de usar esto una verificacion de la longitud del post
    public void addPost(String imagRef, String autor, String contenido) throws IOException{
        
        //obtener fecha y formatearla
        Calendar postDate = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateFormat= formato.format(postDate.getTime());
        
        //Agregar post a insta.ins
        File postFiles= new File(loggedUserDir, "\\insta.ins");//TENER EN CUENTA QUE EL AGREGADO ES EN BASE AL USUARIO LOGEADO!
        RandomAccessFile postArch= new RandomAccessFile(postFiles,"rw");
        
        postArch.seek(postArch.length());
        postArch.writeUTF(imagRef);
        postArch.writeUTF(autor);
        postArch.writeUTF(dateFormat);
        postArch.writeUTF(contenido);
        
        
    }
    
    
    //El metodo getPosts se hace en base a username, teniendo en cuenta la idea que hay que obtener los posts de los que sigo
    // CAMBIO: Ahora es PUBLIC para poder llamarlo desde el Perfil para llenar el GRID
    public ArrayList<String[]> getPosts(String username) throws IOException{
        String userPath = usersDir+File.separator+username;
        File fileOriginal = new File(userPath, "\\insta.ins");
        
        ArrayList<String[]> posts = new ArrayList<>();
        
        // Si no existe el archivo de posts (usuario nuevo), devolver lista vacia
        if (!fileOriginal.exists()) return posts;
        
        RandomAccessFile instaFile = new RandomAccessFile(fileOriginal, "rw");
        
        //Se almacena en arrays de string para poder acceder a la informacion de cada post de manera mas sencilla
        //Estructura:  IMAG REFERENCIA - AUTOR - FECHA - CONTENIDO
        
        
        instaFile.seek(0);
        while(instaFile.getFilePointer()< instaFile.length()){
            String[] post= new String[4];
            //almacenar los componentes del comentario
            post[0]=instaFile.readUTF();
            post[1]= instaFile.readUTF();
            post[2]= instaFile.readUTF();
            post[3]= instaFile.readUTF();
            
            posts.add(post);//agregamos el post al arraylist de posts
            post = new String[4]; // FIX: Nueva instancia
        }
        
        Collections.reverse(posts);//invertimos el orden del arraylist para asi empezar con los posts mas recientes
        
        return posts;
        //Ya la idea seria que, a la hora de extraer los posts, se almacenan en un holder arraylist y luego se filtran a manera que solo se muestren los posts correspondientes a la imagen de publicacion
        
    }
            
    //LOGICA DE OBTENER POSTS DE SEGUIDOS ----!!!
    //La idea es que cada usuario almacena dentro de si sus posts. A la hora de mostrar los comentarios, para mostrar los comentarios de los follows, habria que iterar
    //sobre todos usuarios que seguimos y obteners sus posts directamente
    //Se evita el enfoque de escribirlos directamente tambien en los posts de los seguidores ya que, cuando se deje de seguir a alguien, estos posts seguiran almacenados
    //mucho trabajo y hay que mimimimi son la 1:04AM
    //Esteban, ¿Qué pasó master, tan temprano se durmió?    <---cuando leas esto borralo, no sea que lo lean los que revisen el codigo despues XD
    
    
    
    //Metodo que intenta simular esta logica, para simplemente agregarlo [FALTA PROBARLO Y TESTEARLO]
      public String getPostsfromUser(String imagReferencia, String username) throws IOException{
        String listaPosts="";
        
        ArrayList<String[]> misPosts= getPosts(username); //obtenemos los posts del usuario indicado
        
        //filtrado en base a publicacion
        for(String[] post: misPosts){
            String imagURL = post[0];
            
            
            
            /*

            FORMATO
             [USERNAME] escribio:
                "bla bla bla" el [fecha]

            */            
            //agregado a la lista
            if(imagURL.equals(imagReferencia)){
                listaPosts+= post[1]+" escribio: "
                        + "\n '"+post[3]+"' el ["+post[2]+"]\n\n";
            }
            
        }
        
        //devolver
        return listaPosts; //devolvemos tremenda lista con todos los posts del que seguimos con respecto a la publicacion
    }

    // --- NUEVO: SISTEMA DE COMENTARIOS (ESTO ERA LO QUE FALTABA) ---
    
    /**
     * Guarda un comentario en el archivo 'comments.ins' del DUEÑO DEL POST.
     * Estructura: PathImagen (ID) | AutorComentario | Texto | Fecha(long)
     */
    public void addComment(String postOwner, String imagePath, String author, String comment) throws IOException {
        String userPath = usersDir + File.separator + postOwner;
        File file = new File(userPath, "comments.ins");
        
        // Asegurar que existe (aunque initUserFiles lo crea)
        if (!file.exists()) file.createNewFile();

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.seek(raf.length());
            raf.writeUTF(imagePath); // Identificador: La ruta de la imagen comentada
            raf.writeUTF(author);    // Quién comentó
            raf.writeUTF(comment);   // Qué dijo
            raf.writeLong(Calendar.getInstance().getTimeInMillis()); // Cuándo
        }
    }

    /**
     * Recupera todos los comentarios asociados a una imagen específica.
     */
    public ArrayList<String[]> getComments(String postOwner, String imagePath) throws IOException {
        ArrayList<String[]> comments = new ArrayList<>();
        String userPath = usersDir + File.separator + postOwner;
        File file = new File(userPath, "comments.ins");
        
        if (!file.exists()) return comments;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                String path = raf.readUTF();
                String author = raf.readUTF();
                String text = raf.readUTF();
                long date = raf.readLong();

                // Filtramos: Solo devolver comentarios de ESTA foto específica
                if (path.equals(imagePath)) {
                    comments.add(new String[]{author, text});
                }
            }
        }
        return comments;
    }
}