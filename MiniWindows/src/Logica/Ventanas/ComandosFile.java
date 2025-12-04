/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author esteb
 */
public class ComandosFile {

    private File pathActual;
    private final File rootPath;

    public ComandosFile(String pathInicial, String pathRaiz) {
        this.pathActual = new File(pathInicial);
        this.rootPath = new File(pathRaiz);

        if (!this.pathActual.exists()) {
            this.pathActual = this.rootPath;
        }
    }

    public ComandosFile(String pathInicial) {
        this(pathInicial, pathInicial);
    }

    public File getPathActual() {
        return pathActual;
    }

    public boolean cd(File nuevaRuta) {
        if (nuevaRuta != null && nuevaRuta.exists() && nuevaRuta.isDirectory()) {
            if (!nuevaRuta.getAbsolutePath().startsWith(rootPath.getAbsolutePath())) {
                return false;
            }
            pathActual = nuevaRuta;
            return true;
        }
        return false;
    }

    public boolean cdBack() {
        if (pathActual.equals(rootPath) || pathActual.getAbsolutePath().equals(rootPath.getAbsolutePath())) {
            return false;
        }

        File padre = pathActual.getParentFile();

        if (padre == null || !padre.getAbsolutePath().startsWith(rootPath.getAbsolutePath())) {
            return false;
        }

        pathActual = padre;
        return true;
    }

    public String mkdir(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: nombre no válido";
        }
        File objetivo = new File(pathActual, nombre);
        if (objetivo.exists()) {
            return "Error: ya existe.";
        }
        if (objetivo.mkdirs()) {
            return "Directorio creado.";
        }
        return "Error: fallo al crear.";
    }

    public String mfile(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: nombre no válido";
        }
        File objetivo = new File(pathActual, nombre);
        if (objetivo.exists()) {
            return "Error: ya existe.";
        }

        File parent = objetivo.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try {
            if (objetivo.createNewFile()) {
                return "Archivo creado.";
            }
        } catch (IOException e) {
            return "Error IO: " + e.getMessage();
        }
        return "Error creando archivo.";
    }

    public String rm(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: nombre no válido";
        }
        File target = new File(pathActual, nombre);

        if (target.getAbsolutePath().equals(rootPath.getAbsolutePath())) {
            return "Error: No puedes borrar la raíz.";
        }
        if (!target.exists()) {
            return "Error: no existe.";
        }

        if (borrarRecursivo(target)) {
            return "Eliminado.";
        }
        return "Error eliminando.";
    }

    private boolean borrarRecursivo(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (!borrarRecursivo(child)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    public String dir(String nombre) {
        File archivoDir;
        if (nombre == null || nombre.trim().isEmpty() || ".".equals(nombre)) {
            archivoDir = pathActual;
        } else {
            archivoDir = new File(pathActual, nombre);
        }

        if (!archivoDir.getAbsolutePath().startsWith(rootPath.getAbsolutePath())) {
            return "Acceso denegado.";
        }
        if (!archivoDir.exists() || !archivoDir.isDirectory()) {
            return "Error: directorio no encontrado.";
        }

        return dirInterno(archivoDir);
    }

    private String dirInterno(File archivoDir) {
        StringBuilder contenido = new StringBuilder();

        int archivos = 0;
        int directorios = 0;
        long bytesTotal = 0;

        File[] hijos = archivoDir.listFiles();
        if (hijos != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
            for (File child : hijos) {
                String fecha = sdf.format(new Date(child.lastModified()));
                String tipo = child.isDirectory() ? "<DIR>" : "     ";
                String tam = child.isDirectory() ? "" : String.format("%,d", child.length());
                String nom = child.getName();

                if (child.isDirectory()) {
                    directorios++;
                } else {
                    archivos++;
                    bytesTotal += child.length();
                }

                contenido.append(String.format("%s    %-5s          %10s %s%n", fecha, tipo, tam, nom));
            }
        }
        contenido.append(String.format("%16d archivos %15s bytes%n", archivos, String.format("%,d", bytesTotal)));
        contenido.append(String.format("%16d directorios %14s bytes libres%n", directorios, String.format("%,d", archivoDir.getFreeSpace())));

        return contenido.toString();
    }

    public String escribirTexto(String nombre, String texto) {
        File target = new File(pathActual, nombre);
        if (target.isDirectory()) {
            return "Error: es una carpeta.";
        }
        if (texto == null) {
            texto = "";
        }
        try (FileWriter fw = new FileWriter(target, false); PrintWriter pw = new PrintWriter(fw)) {
            pw.print(texto);
            return "Guardado.";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String leerTexto(String nombre) {
        File target = new File(pathActual, nombre);
        if (!target.exists() || target.isDirectory()) {
            return "Error leyendo archivo.";
        }
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(target))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append(System.lineSeparator());
            }
        } catch (IOException e) {
            return "Error IO.";
        }
        return contenido.toString();
    }
}
