/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayer;
import javax.swing.Timer;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.Player;



import javax.sound.sampled.*;
import javax.swing.JOptionPane;


/**
 *
 * @author David
 */
public class audioLogic {
    
    private File currentSong;
    //private Player player;
     Clip audioClip;
     public String playingSongName;
    private long totalDurationMillis =0;
    private long currentPositionMillis =0;
    private long currentPositionFrame=0;
    
    
    private Timer progressTimer;
    private boolean isPause=false;
    
    
    private ProgressUpdateListener listener;
    
    public interface ProgressUpdateListener{
        void updateProgress(long current, long total);
        void onPlaybackFinished();
    }
    
    public audioLogic(ProgressUpdateListener listener){
        playingSongName=null;
        this.listener=listener;
        progressTimer = new Timer(100, e-> updateProgress());
    }
    
    
    public ProgressUpdateListener getListener(){
        return listener;
    }
    
    
    public void load(File song){
        stop();
        
        AudioInputStream audioStream =null;
        AudioInputStream decodedStream=null;
        
        
        try{
            audioStream= AudioSystem.getAudioInputStream(song);
            
            AudioFormat baseFormat = audioStream.getFormat();
            
             if(baseFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels()*2,
                        baseFormat.getSampleRate(),
                        false
                );
                
                decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            }else{
                 decodedStream = audioStream;
             }
             
             
             long frames = audioStream.getFrameLength();
             AudioFormat format = decodedStream.getFormat();
             
             if(frames!= AudioSystem.NOT_SPECIFIED && format.getFrameRate()>0){
                 totalDurationMillis = (long) (frames/format.getFrameRate()*1000);
             }else{
                audioClip = AudioSystem.getClip();
                audioClip.open(decodedStream);
                totalDurationMillis= audioClip.getMicrosecondLength()/1000;
             }   
            
          //  totalDurationMillis = (long) (frames/baseFormat.getFrameRate()*1000);
            
            //obtener el clip y abrirlo
            //audioClip= AudioSystem.getClip();
            //audioClip.open(decodedStream);
            
            if(audioClip==null){
                audioClip= AudioSystem.getClip();
                audioClip.open(decodedStream);
            }
            
            
            
            //anadir el listener para detectar el final de la reproduccion
            audioClip.addLineListener(event ->{
                if(event.getType() == LineEvent.Type.STOP && !audioClip.isRunning()){
                    
                    //Solo si no estamos pausando y la reproduccion realmente termino
                    if(audioClip.getFramePosition()>=audioClip.getFrameLength()-1){
                        progressTimer.stop();
                        audioClip.setFramePosition(0);
                        currentPositionFrame=0;
                        listener.onPlaybackFinished();
                        System.out.println("Reproduccion finalizada");
                    }
                }
            });
             
             
             
        }catch(UnsupportedAudioFileException e){
            System.out.println("No se soporto el formato del archivo");
            e.printStackTrace();
            audioClip=null;
        }catch(LineUnavailableException | IOException e){
            System.out.println("Error al acceder al hardware de audio");
            e.printStackTrace();
            audioClip =null;
        }finally{
            try{
                if(decodedStream!= null && audioStream != decodedStream){
                    
                }
            }catch(Exception e){
                System.out.println("ERROR DE TIPO LOL");
            }
        }
        currentPositionFrame=0;
        

    }
    
    
    public void play(){
        
        
        if(audioClip == null) return;
        
        
        
        if(audioClip.isRunning()){
            audioClip.stop();
        }
        
        
        audioClip.setFramePosition((int) currentPositionFrame);
        
        
        audioClip.start();
       
        
        
        if(!progressTimer.isRunning()){
             progressTimer.start();
             System.out.println("ENTTRO AL EJECUTAR EL TIMER");
        }
        
        System.out.println("Reproducioendo desde el frame: "+currentPositionFrame);
        
        /*
        if(!audioClip.isRunning()){
            audioClip.setFramePosition((int) currentPositionFrame);
            audioClip.start();
            progressTimer.start();
            System.out.println("Reproduciendo desde Frame "+currentPositionFrame);
        }
        /*
        if(player==null && currentSong!= null){
            load(currentSong);
        }
        
        if(player!=null){
            if(isPause){
               // player.resume();
                isPause=false;
            }else{
                new Thread(()-> {
                    try {
                        player.play();
                    } catch (JavaLayerException ex) {
                       
                    }
                }).start(); 
                
            }
            progressTimer.start();
            System.out.println("Reproducciendo: "+currentSong.getName());
            
        }
        */
    }
    
    public void pause(){
        
        if(audioClip!= null && audioClip.isRunning()){
            currentPositionFrame = audioClip.getFramePosition();
            audioClip.stop();
            progressTimer.stop();
            System.out.println("Pausado en Frame: "+currentPositionFrame);
        }
        
        
        /*
        if(player!= null & !isPause){
            currentPositionMillis = player.getPosition();
            isPause=true;
            progressTimer.stop();
            System.out.println("Se ha pausado");
           // player.pause();
        }
*/
    }
    
    public void stop(){
        
        if(audioClip!=null){
            audioClip.stop();
            audioClip.close();
            audioClip=null;
            currentPositionFrame=0;
            progressTimer.stop();
            listener.updateProgress(0, totalDurationMillis);
            System.out.println("Detenido y Clip Liberado");
        }
        
        /*
        if(player!=null){
            player.close();
            player=null;
            isPause=false;
            currentPositionMillis=0;
            progressTimer.stop();
            listener.updateProgress(0, totalDurationMillis);
            System.out.println("Detenido");
        }
*/
    }
    
    
    public void setPosition(long positionMillis){
        
        
        if(audioClip == null || totalDurationMillis<=0) return;
        
        AudioFormat format = audioClip.getFormat();
        
        long targetFrame = (long) (positionMillis /1000.0 * format.getFrameRate());
        
        if(targetFrame<0 || targetFrame >= audioClip.getFrameLength()){
            return;
        }
        
        currentPositionFrame = targetFrame;
        
        if(audioClip.isRunning()){
            audioClip.stop();
            play();
        }else{
            audioClip.setFramePosition((int) currentPositionFrame);
        }
        System.out.println("Saltando a Frame: "+currentPositionFrame);
        
        /*
        if(player!=null && positionMillis>=0 && positionMillis<= totalDurationMillis){
            stop();
            currentPositionMillis = positionMillis;
            //new Thread(()-> player.pl) playFrom
            
            
            
            currentPositionMillis = positionMillis;
            if(!isPause){
                play();
            }
        }
*/
    }
    
    
    //Metodos de utilidad
    
    
    private void updateProgress(){
        if(audioClip!= null && audioClip.isRunning()){
            long currentMillis = (long) (audioClip.getMicrosecondPosition()/1000);
            //(long)
            
            if(currentMillis<=0){
                AudioFormat format = audioClip.getFormat();
                currentMillis= (long) (audioClip.getFramePosition()/format.getFrameRate()*1000);
            }
            
            if(currentMillis>=totalDurationMillis){
                //nada por aqui
            }
            
            
            listener.updateProgress(currentMillis, totalDurationMillis);
            currentPositionFrame = audioClip.getFramePosition();
        }
        
        /*
        currentPositionMillis = player.getPosition();
        
        if(!isPause && currentPositionMillis < totalDurationMillis){
            currentPositionMillis += progressTimer.getDelay();
        }
        
        
        if(currentPositionMillis >= totalDurationMillis && totalDurationMillis >0){
            progressTimer.stop();
            listener.onPlaybackFinished();
            stop();
            return;
        }
        
        listener.updateProgress(currentPositionMillis, totalDurationMillis
        */
        
    }
    
    public long getCurrentPosition(){
        return currentPositionMillis;
    }
    
    
    
    
    public long getTotalDuration(){
        return totalDurationMillis;
    }
    
    public boolean isPlaying(){
        return audioClip!= null && audioClip.isRunning();
        //return player!= null && !isPause && progressTimer.isRunning();
    }
    
    
}
