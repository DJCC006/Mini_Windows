/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

    public instaManager() {

        //creacion de carpetas principales
        File instaFolder = new File(mainRoot);
        File usersDirF = new File(usersDir);

        if (!instaFolder.exists()) {
            instaFolder.mkdir();
        } else {
            System.out.println("Ya existe instaFolder");
        }

        if (!usersDirF.exists()) {
            usersDirF.mkdir();
        } else {
            System.out.println("Ya existe userDir");
        }

        //creacion de archivo manejador de usuarios
        try {
            users = new RandomAccessFile(mainRoot + "\\users.ins", "rw");
        } catch (IOException e) {
            System.out.println("XD a la hora de crear users");
        }

    }

    /*
    
     */
    public void addNewUser(String name, char genero, String username, String password, int edad, String profilePicPath) throws IOException {
        users.seek(users.length());

        // 1) Guardar datos del usuario normalmente
        users.writeUTF(name);
        users.writeChar(genero);
        users.writeUTF(username);
        users.writeUTF(password);
        users.writeLong(Calendar.getInstance().getTimeInMillis());
        users.writeInt(edad);
        users.writeBoolean(true);

        // 2) Copiar la imagen si existe
        String finalPath = "futura referencia de imagen aqui";

        if (profilePicPath != null && !profilePicPath.isBlank()) {
            File userDir = new File(usersDir, username);
            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            int dot = profilePicPath.lastIndexOf('.');
            String ext = (dot > 0) ? profilePicPath.substring(dot + 1) : "jpg";

            File destino = new File(userDir, "profile." + ext);
            Files.copy(new File(profilePicPath).toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            finalPath = destino.getAbsolutePath();
        }

        // 3) Guardar ruta final
        users.writeUTF(finalPath);

        // 4) Crear carpetas del usuario
        initNewUserF(username);
    }

    //metodo de crear carpeta de nuevo usuario
    private void initNewUserF(String username) throws IOException {
        File newUserDir = new File(usersDir, username);

        if (!newUserDir.exists()) {
            newUserDir.mkdir();
            initUserFiles(newUserDir.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(null, "Ya existe un usuario con este nombre de usuario", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    //meotod que inicializa los archivos de nuevo usuario
    private void initUserFiles(String userFolder) throws IOException {
        RandomAccessFile followers = new RandomAccessFile(userFolder + "\\followers.ins", "rw");
        RandomAccessFile following = new RandomAccessFile(userFolder + "\\following.ins", "rw");
        RandomAccessFile posts = new RandomAccessFile(userFolder + "\\insta.ins", "rw");
        // AGREGADO: Archivo de comentarios para que no falle al iniciar
        RandomAccessFile comments = new RandomAccessFile(userFolder + "\\comments.ins", "rw");
    }

    //Metodo que servira para establecer la direccion del usuario loggeado
    public void setLoggedUser(String username) throws IOException {
        loggedUserDir = new File(usersDir, username);
        loggedUser = username;
    }

    //Metodo que borra la direccion del usuario cuando este cierra sesion
    public void loggoutUser() throws IOException {
        loggedUserDir = null;
        loggedUser = null;
    }

    //Getters de informacion de usuario
    public String getRealName(String username) throws IOException {
        users.seek(0);

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            String rName = users.readUTF();
            users.readChar();

            if (users.readUTF().equals(username)) {
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

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            char gender = users.readChar();

            if (users.readUTF().equals(username)) {
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

    public String getUsername(String username) throws IOException {
        users.seek(0);

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
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

    public String getPassword(String username) throws IOException {
        users.seek(0);

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
                String password = users.readUTF();
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

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
                users.readUTF();
                Long dateL = users.readLong();

                Date dateF = new Date(dateL);
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

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
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

    public boolean getStatusUser(String username) throws IOException {
        users.seek(0);

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
                users.readUTF();
                users.readLong();
                users.readInt();

                if (users.readBoolean()) {
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

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
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
    public void desactivateUser(String username) throws IOException {
        users.seek(0);

        while (users.getFilePointer() < users.length()) {
            //comprobar si es el mismo username
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
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

        while (users.getFilePointer() < users.length()) {
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            users.readUTF();
            users.readLong();
            users.readInt();
            users.readBoolean();
            String picDir = users.readUTF();

            if (nameUser.equals(username)) {
                return picDir;
            }
        }
        return null;
    }

    //metodo para comprobar si ya existe cuenta bajo el username
    public boolean checkUserExistance(String username) throws IOException {
        users.seek(0);

        while (users.getFilePointer() < users.length()) {
            users.readUTF();
            users.readChar();
            String nameUser = users.readUTF();

            if (nameUser.equals(username)) {
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
    private void addFollower(String username, String newFollower) throws IOException {
        //Ubicarme dentro del archivo de la cuenta que recibira el follower
        String user2Path = usersDir + File.separator + username;
        File user2File = new File(user2Path, "\\followers.ins");

        RandomAccessFile followersFile = new RandomAccessFile(user2File, "rw");

        //ubicar el puntero
        followersFile.seek(followersFile.length());
        //agregar informacion respectiva del nuevo follower
        followersFile.writeUTF(newFollower);

    }

    public boolean addFollow(String username) throws IOException {
        File userPath = new File(loggedUserDir, "\\following.ins");
        RandomAccessFile followsFile = new RandomAccessFile(userPath, "rw");

        //Verificacion de username repetido
        followsFile.seek(0);
        boolean repetido = false;
        while (followsFile.getFilePointer() < followsFile.length()) {
            String readName = followsFile.readUTF();
            if (readName.equals(username)) {
                repetido = true;
            }
        }

        //ubicarme dentro del archivo followings del usuario y agregar nuevo follow
        if (!repetido) {
            followsFile.seek(followsFile.length());
            followsFile.writeUTF(username);

            //agregar follower a cuenta seguida
            addFollower(username, loggedUser);
            return true;

        }

        return false; //el return false es que identifico el username como repetido

    }

    public void quitarFollow(String username) throws IOException {
        File fileOriginal = new File(loggedUserDir, "\\following.ins");
        File fileTemp = new File(loggedUserDir, "\\following.temp");
        RandomAccessFile oFile = new RandomAccessFile(fileOriginal, "rw");
        RandomAccessFile tFile = new RandomAccessFile(fileTemp, "rw");

        String readUsername;
        boolean encontrado = false;

        oFile.seek(0);
        tFile.seek(0);
        while (oFile.getFilePointer() < oFile.length()) {
            readUsername = oFile.readUTF();
            if (!readUsername.equals(username)) {
                tFile.writeUTF(readUsername);
            } else {
                encontrado = true;
                //No necesariamente se sale forzosamente del bucle, hay que hacer que termine
            }

        }

        if (encontrado) {
            if (!fileOriginal.delete()) {
                throw new IOException("No se pudo borrar el archivo original");
            }
            if (!fileTemp.renameTo(fileOriginal)) {
                throw new IOException("No se pudo renombrar el archivo temporal");
            }
            System.out.println("Se ha creado la sustitucion de archivos correcta para follows");

        } else {
            fileTemp.delete();
            System.out.println("No se encontro al usuario indicado");
        }

        //llamamos el metodo que hara lo mismo pero para followers
        quitarFollower(username, loggedUser);
    }

    private void quitarFollower(String username, String follower) throws IOException {
        String user2Path = usersDir + File.separator + username;
        File fileOriginal = new File(user2Path, "\\followers.ins");
        File fileTemp = new File(user2Path, "\\followers.temp");

        RandomAccessFile followersFile = new RandomAccessFile(fileOriginal, "rw");
        RandomAccessFile tFile = new RandomAccessFile(fileTemp, "rw");

        String readUsername;
        boolean encontrado = false;

        followersFile.seek(0);
        tFile.seek(0);
        while (followersFile.getFilePointer() < followersFile.length()) {
            readUsername = followersFile.readUTF();
            if (!readUsername.equals(username)) {
                tFile.writeUTF(readUsername);
            } else {
                encontrado = true;
                //No necesariamente se sale forzosamente del bucle, hay que hacer que termine
            }

        }

        if (encontrado) {
            if (!fileOriginal.delete()) {
                throw new IOException("No se pudo borrar el archivo original");
            }
            if (!fileTemp.renameTo(fileOriginal)) {
                throw new IOException("No se pudo renombrar el archivo temporal");
            }
            System.out.println("Se ha creado la sustitucion de archivos correcta para followers");

        } else {
            fileTemp.delete();
            System.out.println("No se encontro al usuario indicado");
        }
    }

    //posiblemente revisar el tema del username --> hay que considerar el user al cual se efectuara la accion
    public String showFollowers(String username) throws IOException {
        String userPath = usersDir + File.separator + username;
        File fileOriginal = new File(userPath, "\\followers.ins");
        RandomAccessFile followersFile = new RandomAccessFile(fileOriginal, "rw");

        String listaFollowers = "";

        followersFile.seek(0);
        while (followersFile.getFilePointer() < followersFile.length()) {
            String nameuser = followersFile.readUTF();
            listaFollowers += nameuser + "\n";
        }

        return listaFollowers;

    }

    public String showFollows(String username) throws IOException {
        String userPath = usersDir + File.separator + username;
        File fileOriginal = new File(userPath, "\\following.ins");
        RandomAccessFile followsFile = new RandomAccessFile(fileOriginal, "rw");

        String listaFollows = "";

        followsFile.seek(0);
        while (followsFile.getFilePointer() < followsFile.length()) {
            String nameuser = followsFile.readUTF();
            listaFollows += nameuser + "\n";
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
    public void addPost(String imagRef, String autor, String contenido) throws IOException {

        //obtener fecha y formatearla
        Calendar postDate = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateFormat = formato.format(postDate.getTime());

        //Agregar post a insta.ins
        File postFiles = new File(loggedUserDir, "\\insta.ins");//TENER EN CUENTA QUE EL AGREGADO ES EN BASE AL USUARIO LOGEADO!
        RandomAccessFile postArch = new RandomAccessFile(postFiles, "rw");

        postArch.seek(postArch.length());
        postArch.writeUTF(imagRef);
        postArch.writeUTF(autor);
        postArch.writeUTF(dateFormat);
        postArch.writeUTF(contenido);

    }

    //El metodo getPosts se hace en base a username, teniendo en cuenta la idea que hay que obtener los posts de los que sigo
    // CAMBIO: Ahora es PUBLIC para poder llamarlo desde el Perfil para llenar el GRID
    public ArrayList<String[]> getPosts(String username) throws IOException {
        String userPath = usersDir + File.separator + username;
        File fileOriginal = new File(userPath, "\\insta.ins");

        ArrayList<String[]> posts = new ArrayList<>();

        // Si no existe el archivo de posts (usuario nuevo), devolver lista vacia
        if (!fileOriginal.exists()) {
            return posts;
        }

        RandomAccessFile instaFile = new RandomAccessFile(fileOriginal, "rw");

        //Se almacena en arrays de string para poder acceder a la informacion de cada post de manera mas sencilla
        //Estructura:  IMAG REFERENCIA - AUTOR - FECHA - CONTENIDO
        instaFile.seek(0);
        while (instaFile.getFilePointer() < instaFile.length()) {
            String[] post = new String[4];
            //almacenar los componentes del comentario
            post[0] = instaFile.readUTF();
            post[1] = instaFile.readUTF();
            post[2] = instaFile.readUTF();
            post[3] = instaFile.readUTF();

            posts.add(post);//agregamos el post al arraylist de posts
            post = new String[4]; // FIX: Nueva instancia
        }

        Collections.reverse(posts);//invertimos el orden del arraylist para asi empezar con los posts mas recientes

        return posts;
        //Ya la idea seria que, a la hora de extraer los posts, se almacenan en un holder arraylist y luego se filtran a manera que solo se muestren los posts correspondientes a la imagen de publicacion

    }
    public String getPostsfromUser(String imagReferencia, String username) throws IOException {
        String listaPosts = "";

        ArrayList<String[]> misPosts = getPosts(username); //obtenemos los posts del usuario indicado

        //filtrado en base a publicacion
        for (String[] post : misPosts) {
            String imagURL = post[0];

            /*

            FORMATO
             [USERNAME] escribio:
                "bla bla bla" el [fecha]

             */
            //agregado a la lista
            if (imagURL.equals(imagReferencia)) {
                listaPosts += post[1] + " escribio: "
                        + "\n '" + post[3] + "' el [" + post[2] + "]\n\n";
            }

        }

        //devolver
        return listaPosts; //devolvemos tremenda lista con todos los posts del que seguimos con respecto a la publicacion
    }

    public void addComment(String postOwner, String imagePath, String author, String comment) throws IOException {
        String userPath = usersDir + File.separator + postOwner;
        File file = new File(userPath, "comments.ins");

        if (!file.exists()) {
            file.createNewFile();
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.seek(raf.length());
            raf.writeUTF(imagePath); 
            raf.writeUTF(author);    
            raf.writeUTF(comment);   
            raf.writeLong(Calendar.getInstance().getTimeInMillis()); 
        }
    }

    /**
     * Recupera todos los comentarios asociados a una imagen específica.
     */
    public ArrayList<String[]> getComments(String postOwner, String imagePath) throws IOException {
        ArrayList<String[]> comments = new ArrayList<>();
        String userPath = usersDir + File.separator + postOwner;
        File file = new File(userPath, "comments.ins");

        if (!file.exists()) {
            return comments;
        }

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
    
        /**
     * Busca todos los usernames que contengan el texto query (ignorando mayúsculas).
     * Devuelve lista de usernames activos que contengan la subcadena.
     */
    public ArrayList<String> searchUsers(String query) throws IOException {
        ArrayList<String> encontrados = new ArrayList<>();
        if (query == null) return encontrados;
        String lowQ = query.toLowerCase();

        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            String rName = users.readUTF();
            users.readChar();
            String uname = users.readUTF();
            // leemos el resto para dejar puntero listo
            users.readUTF(); // password
            users.readLong();
            users.readInt();
            boolean status = users.readBoolean();
            users.readUTF(); // profile pic route

            if (status && uname.toLowerCase().contains(lowQ)) {
                encontrados.add(uname);
            }
        }
        return encontrados;
    }

    /**
     * Devuelve la cantidad de followers (lineas en followers.ins) de un usuario.
     */
    public int getFollowersCount(String username) throws IOException {
        File userPath = new File(usersDir, username);
        File f = new File(userPath, "followers.ins");
        if (!f.exists()) return 0;
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            int count = 0;
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                raf.readUTF();
                count++;
            }
            return count;
        }
    }

    /**
     * Devuelve la cantidad de followings (lineas en following.ins) de un usuario.
     */
    public int getFollowingCount(String username) throws IOException {
        File userPath = new File(usersDir, username);
        File f = new File(userPath, "following.ins");
        if (!f.exists()) return 0;
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            int count = 0;
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                raf.readUTF();
                count++;
            }
            return count;
        }
    }

    /**
     * Comprueba si el usuario loggeado (loggedUser) sigue al objetivo.
     * Retorna true si en following.ins del loggedUser existe exactamente el username objetivo.
     */
    public boolean isFollowing(String targetUsername) throws IOException {
        if (loggedUserDir == null) return false;
        File file = new File(loggedUserDir, "following.ins");
        if (!file.exists()) return false;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                String u = raf.readUTF();
                if (u.equals(targetUsername)) return true;
            }
        }
        return false;
    }

    /**
     * Activa la cuenta (marca boolean = true) — útil para activar cuentas desactivadas.
     * Intenta escribir true en la posición del boolean correspondiente.
     */
    public boolean activateUser(String username) throws IOException {
        users.seek(0);
        while (users.getFilePointer() < users.length()) {
            long start = users.getFilePointer();
            users.readUTF(); // real name
            users.readChar();
            String uname = users.readUTF();
            users.readUTF(); // password
            users.readLong();
            users.readInt();
            long boolPos = users.getFilePointer();
            boolean status = users.readBoolean();
            users.readUTF();

            if (uname.equals(username)) {
                // mover al boolean y sobrescribir
                users.seek(boolPos);
                users.writeBoolean(true);
                return true;
            }
        }
        return false;
    }

}
