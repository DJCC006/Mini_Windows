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
        this.listener=listener;
        progressTimer = new Timer(100, e-> updateProgress());
    }
    
    public void load(File song){
        stop();
        
        try(AudioInputStream audioStream = AudioSystem.getAudioInputStream(song)){
            
            //obtener el formadio de audio y la duracion
            AudioFormat format = audioStream.getFormat();
            long frames = audioStream.getFrameLength();
            
            totalDurationMillis = (long) (frames/format.getFrameRate()*1000);
            
            //obtener el clip y abrirlo
            audioClip= AudioSystem.getClip();
            audioClip.open(audioStream);
            
            
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
            
        }catch(UnsupportedAudioFileException | LineUnavailableException | IOException e){
            JOptionPane.showMessageDialog(null, "Error al cargar el audio");
            e.printStackTrace();
            audioClip=null;
        }
        currentPositionFrame=0;
        
        
        /*
        if(player!=null){
            stop();
        }
        
        this.currentSong= song;
        this.isPause=false;
        this.currentPositionMillis=0;
        
        try{
            InputStream is = new BufferedInputStream(new FileInputStream(song));
            //player = new JLayer(is);
        }catch(Exception e){
            
        }
*/
    }
    
    
    public void play(){
        
        if(audioClip == null) return;
        
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
