/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;
import Logica.ManejoUsuarios.UserLogged;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
/**
 *
 * @author David
 */
public class audioPlayer extends JPanel implements audioLogic.ProgressUpdateListener{
    
    
    private DefaultListModel<String> modeloLista;
    private JList<String> cancionesList;
    private JSlider progressSlider;
    private static String actualUserName = UserLogged.getInstance().getUserLogged().getName();
    private static final String myMusicFolder = "src\\Z\\Usuarios\\"+actualUserName+"\\Musica";
   private audioLogic player;
   private JLabel timeLabel;
   private JButton playPauseButton;
    
    
    public audioPlayer(){
        this.setLayout(new BorderLayout(5,5));
        this.player= new audioLogic(this);
        this.modeloLista = new DefaultListModel<>();
        this.cancionesList = new JList<>(modeloLista);
        this.progressSlider = new JSlider(0,100,0); //check after this
        this.timeLabel= new JLabel("00:00 / 00:00", SwingConstants.CENTER);
        
        setupSongListPanel();
        setupControlPanel();
        
        JLabel infoLabel = new JLabel("Reproductor Z-Media",SwingConstants.CENTER);
        
        cargarCanciones();
        
        
    }
    
    
    
    private void setupSongListPanel(){
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.setPreferredSize(new Dimension(200,0));
        
        listContainer.add(new JLabel("Mi Musica(Disk Z)", SwingConstants.CENTER), BorderLayout.NORTH);
        listContainer.add(new JScrollPane(cancionesList), BorderLayout.CENTER);
        
        JButton importarBt = new JButton("Importar Musica");
        importarBt.addActionListener(e -> importarMusica());
        listContainer.add(importarBt, BorderLayout.SOUTH);
        
        this.add(listContainer, BorderLayout.WEST);
        
        
        cancionesList.addListSelectionListener(e ->{
            if(!e.getValueIsAdjusting() && cancionesList.getSelectedValue()!= null){
                String songName = cancionesList.getSelectedValue();
                File songFile = new File(myMusicFolder, songName);
                if(songFile.exists()){
                    player.load(songFile);
                    playPauseButton.setText("PLAY");
                }
                
                
                //System.out.println("Cancion Seleccionada: "+ cancionesList.getSelectedValue());
                
                //FUTURO audioPlayer.load(selectedFile);
            }
        });
        
    }
    
    
    
    
    private void cargarCanciones(){
        File musicDir = new File(myMusicFolder);
        if(!musicDir.exists()){
            musicDir.mkdirs();
            return;
        }
        
        
        modeloLista.clear();
        File[] files = musicDir.listFiles((dir,name) -> name.toLowerCase().endsWith(".wav") || name.toLowerCase().endsWith("wav"));
        
        if(files!= null){
            for(File file: files){
                modeloLista.addElement(file.getName());
            }
        }
        
    }
    
    
    //Metodo de importacion
    public void importarMusica(){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("IMPORTAR MUSICA");
        chooser.setMultiSelectionEnabled(true);
        
        int result = chooser.showOpenDialog(this);//posiblemente aqui agruegar el screen de pantalla principal
        
        if(result == JFileChooser.APPROVE_OPTION){
            File[] selectedFiles = chooser.getSelectedFiles();
            File dirDestino = new File(myMusicFolder);
            for(File sourcefile: selectedFiles){
                
                
                if(!sourcefile.getName().toLowerCase().endsWith(".wav")){
                    continue;
                }
                
                try{
                    File destination = new File(dirDestino, sourcefile.getName());
                    Files.copy(sourcefile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Importado: "+sourcefile.getName());
                    
                }catch(Exception e){
                    e.printStackTrace();
                }   
            }
            cargarCanciones();
        }
    }
    
    private void setupControlPanel(){
        JPanel controlsPanel = new JPanel(new BorderLayout());
        
        controlsPanel.add(progressSlider, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,5));
        
        playPauseButton = new JButton("PLAY");
        JButton stopButton = new JButton("REPLAY");
        
        
        playPauseButton.addActionListener(e -> togglePlayback());
        stopButton.addActionListener(e-> stopPlayback());
        
        
        buttonPanel.add(playPauseButton);
        buttonPanel.add(stopButton);
        
        controlsPanel.add(buttonPanel, BorderLayout.CENTER);
        controlsPanel.add(timeLabel, BorderLayout.EAST);
        
        this.add(controlsPanel, BorderLayout.SOUTH);
        
        progressSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt){
                seekToPosition(progressSlider.getValue());
            }
        });   
    }
    
    
    
    private void togglePlayback(){
        if(player.isPlaying()){
            player.pause();
            playPauseButton.setText("PLAY");
        }else{
            if(cancionesList.getSelectedValue()== null && modeloLista.getSize()>0){
                cancionesList.setSelectedIndex(0);
            }
            if(player.audioClip!= null){
                player.play();
                playPauseButton.setText("PAUSE");
            }else if(cancionesList.getSelectedValue()!=null){
                File songFile = new File(myMusicFolder, cancionesList.getSelectedValue());
                player.load(songFile);
                player.play();
                playPauseButton.setText("PAUSE");
            }
        }
        
        
        /*
        if(button.getText().contains("PLAY")){
            //player.play();
            button.setText("PAUSE");
        }else{
           // player.pause();
            button.setText("PLAY");
        }
*/
    }
    
    
    
    private void stopPlayback(){
       // player.stop();
       player.stop();
        //progressSlider.setValue(0);
        System.out.println("Se detuvo la reproduccion");
        playPauseButton.setText("PLAY");
        
       
    }
    
    
    private void seekToPosition(int sliderValue){
        long newPositionMillis = (long) sliderValue*1000;
        player.setPosition(newPositionMillis); 
        System.out.println("Se ha saltado de posicion");
         progressSlider.setValue(sliderValue);
    }

    @Override
    public void updateProgress(long current, long total) {
        
        if(total>0){
            int currentSeconds = (int) (current/1000);
            int totalSeconds = (int) (total/1000);
            
            
            if(progressSlider.getMaximum()!= totalSeconds){
                progressSlider.setMaximum(totalSeconds);
            }
            
            
            if(!progressSlider.getValueIsAdjusting()){
                progressSlider.setValue(currentSeconds);
            }

            
            String currentTime = formatTime(currentSeconds);
            String totalTime = formatTime(totalSeconds);
            timeLabel.setText(currentTime+" / "+totalTime);
        }else{
            progressSlider.setValue(0);
            timeLabel.setText("00:00 / 00:00");
        }
    }

    @Override
    public void onPlaybackFinished() {
        playPauseButton.setText("PLAY");
        progressSlider.setValue(0);
        timeLabel.setText("00:00 / "+formatTime((int) (player.getTotalDuration()/1000))); //Check this
    }
    
    
    private String formatTime(int totalSeconds){
        int minutes = totalSeconds/60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    
    
}
