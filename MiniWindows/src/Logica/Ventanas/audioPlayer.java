/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Ventanas;

import Logica.ManejoUsuarios.UserLogged;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 *
 * @author David
 */
public class audioPlayer extends JPanel implements audioLogic.ProgressUpdateListener {

    
    private DefaultListModel<String> modeloLista;
    private JList<String> cancionesList;
    private JSlider progressSlider;
    private static String actualUserName = UserLogged.getInstance().getUserLogged().getName();
    private static final String myMusicFolder = "src\\Z\\Usuarios\\" + actualUserName + "\\Musica";

    private audioLogic player;
    private JLabel timeLabel;
    private BotonModerno playPauseButton;
    private BotonModerno stopButton;
    private String nowPlaying;

    private VinylPanel visualizerPanel;

    private final Color COLOR_FONDO = new Color(30, 30, 30);
    private final Color COLOR_PANEL = new Color(45, 45, 48);
    private final Color COLOR_NARANJA = new Color(233, 84, 32);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);

    public audioPlayer() {
        this.nowPlaying = null;
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(COLOR_FONDO);

        this.player = new audioLogic(this);
        this.modeloLista = new DefaultListModel<>();

        this.cancionesList = new JList<>(modeloLista);
        this.cancionesList.setBackground(COLOR_FONDO);
        this.cancionesList.setForeground(COLOR_TEXTO);
        this.cancionesList.setSelectionBackground(COLOR_NARANJA);
        this.cancionesList.setSelectionForeground(Color.WHITE);

        this.progressSlider = new JSlider(0, 100, 0);
        this.progressSlider.setOpaque(false);
        this.progressSlider.setUI(new DarkSliderUI(progressSlider));

        this.timeLabel = new JLabel("00:00 / 00:00", SwingConstants.CENTER);
        this.timeLabel.setForeground(COLOR_TEXTO);
        this.timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        visualizerPanel = new VinylPanel();

        setupSongListPanel();

        this.add(visualizerPanel, BorderLayout.CENTER);

        setupControlPanel();

        cargarCanciones();
    }

    private void setupSongListPanel() {
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.setPreferredSize(new Dimension(220, 0));
        listContainer.setBackground(COLOR_PANEL);
        listContainer.setBorder(new LineBorder(new Color(60, 60, 60), 1));

        JLabel titleLbl = new JLabel("Mi Música (Disk Z)", SwingConstants.CENTER);
        titleLbl.setForeground(COLOR_NARANJA);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setBorder(new EmptyBorder(10, 5, 10, 5));

        listContainer.add(titleLbl, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(cancionesList);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COLOR_FONDO);
        listContainer.add(scroll, BorderLayout.CENTER);

        BotonModerno importarBt = new BotonModerno("Importar Música", new Color(60, 60, 60), COLOR_TEXTO);
        importarBt.setPreferredSize(new Dimension(0, 40));
        importarBt.addActionListener(e -> importarMusica());
        listContainer.add(importarBt, BorderLayout.SOUTH);

        this.add(listContainer, BorderLayout.WEST);

        cancionesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && cancionesList.getSelectedValue() != null) {
                String songName = cancionesList.getSelectedValue();
                File songFile = new File(myMusicFolder, songName);
                if (songFile.exists()) {
                    player.load(songFile);
                    playPauseButton.setText("PLAY");
                    visualizerPanel.setSongTitle(songName);
                    visualizerPanel.reset();
                }
            }
        });
    }

    private void cargarCanciones() {
        File musicDir = new File(myMusicFolder);
        if (!musicDir.exists()) {
            musicDir.mkdirs();
            return;
        }

        modeloLista.clear();
        File[] files = musicDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav") || name.toLowerCase().endsWith("wav") || name.toLowerCase().endsWith(".mp3") || name.toLowerCase().endsWith("mp3"));

        if (files != null) {
            for (File file : files) {
                modeloLista.addElement(file.getName());
            }
        }
        this.revalidate();
        this.repaint();
    }

    public void importarMusica() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("IMPORTAR MUSICA");
        chooser.setMultiSelectionEnabled(true);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            File dirDestino = new File(myMusicFolder);

            if (!dirDestino.exists()) {
                dirDestino.mkdirs();
            }

            for (File sourcefile : selectedFiles) {
                if (!sourcefile.getName().toLowerCase().endsWith(".wav") && !sourcefile.getName().toLowerCase().endsWith(".mp3")) {
                    continue;
                }

                try {
                    File destination = new File(dirDestino, sourcefile.getName());
                    Files.copy(sourcefile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Importado: " + sourcefile.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cargarCanciones();
        }
    }

    private void setupControlPanel() {
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(COLOR_PANEL);
        controlsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        controlsPanel.add(progressSlider, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBackground(COLOR_PANEL);

        playPauseButton = new BotonModerno("PLAY", COLOR_NARANJA, Color.WHITE);
        playPauseButton.setPreferredSize(new Dimension(100, 35));

        stopButton = new BotonModerno("STOP", new Color(200, 50, 50), Color.WHITE);
        stopButton.setPreferredSize(new Dimension(100, 35));

        playPauseButton.addActionListener(e -> togglePlayback());
        stopButton.addActionListener(e -> stopPlayback());

        buttonPanel.add(playPauseButton);
        buttonPanel.add(stopButton);

        controlsPanel.add(buttonPanel, BorderLayout.CENTER);
        controlsPanel.add(timeLabel, BorderLayout.EAST);

        this.add(controlsPanel, BorderLayout.SOUTH);

        progressSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                seekToPosition(progressSlider.getValue());
            }
        });
    }

    private void togglePlayback() {
        if (player.isPlaying()) {
            player.pause();
            playPauseButton.setText("PLAY");
            visualizerPanel.stopSpinning();
        } else {
            if (cancionesList.getSelectedValue() == null && modeloLista.getSize() > 0 && nowPlaying == null) {
                cancionesList.setSelectedIndex(0);
            }

            boolean started = false;
            if (player.audioClip != null) {
                player.play();
                nowPlaying = player.audioClip.toString();
                playPauseButton.setText("PAUSE");
                started = true;
            } else if (cancionesList.getSelectedValue() != null) {
                File songFile = new File(myMusicFolder, cancionesList.getSelectedValue());
                player.load(songFile);
                player.play();
                playPauseButton.setText("PAUSE");
                started = true;
            }

            if (started) {
                visualizerPanel.startSpinning(); 
            }
        }
    }

    public void playExterno(File song) {
        nowPlaying = song.getName();
        visualizerPanel.setSongTitle(nowPlaying);

        if (player.isPlaying()) {
            player.pause();
            playPauseButton.setText("PLAY");
            visualizerPanel.stopSpinning();
        } else {
            if (player.audioClip != null) {
                player.play();
                playPauseButton.setText("PAUSE");
                visualizerPanel.startSpinning();
            }
        }
    }

    public void stopPlayback() {
        player.stop();
        System.out.println("Se detuvo la reproduccion");
        playPauseButton.setText("PLAY");
        visualizerPanel.reset();
    }

    private void seekToPosition(int sliderValue) {
        long newPositionMillis = (long) sliderValue * 1000;
        player.setPosition(newPositionMillis);
        progressSlider.setValue(sliderValue);
    }

    @Override
    public void updateProgress(long current, long total) {
        if (total > 0) {
            int currentSeconds = (int) (current / 1000);
            int totalSeconds = (int) (total / 1000);

            if (progressSlider.getMaximum() != totalSeconds) {
                progressSlider.setMaximum(totalSeconds);
            }

            String currentTime = formatTime(currentSeconds);
            String totalTime = formatTime(totalSeconds);
            timeLabel.setText(currentTime + " / " + totalTime);

            if (!progressSlider.getValueIsAdjusting()) {
                SwingUtilities.invokeLater(() -> {
                    progressSlider.setValue(currentSeconds);
                });
            }
        } else {
            progressSlider.setValue(0);
            timeLabel.setText("00:00 / 00:00");
        }
    }

    @Override
    public void onPlaybackFinished() {
        playPauseButton.setText("PLAY");
        progressSlider.setValue(0);
        long duration = (player != null) ? player.getTotalDuration() : 0;
        timeLabel.setText("00:00 / " + formatTime((int) (duration / 1000)));
        visualizerPanel.reset();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setPlayerExterno(audioLogic externo) {
        player = externo;
    }

    public audioLogic getPlayer() {
        return player;
    }

    private class VinylPanel extends JPanel {

        private Timer rotationTimer;
        private double angle = 0;
        private boolean isSpinning = false;
        private String currentTitle = "";

        public VinylPanel() {
            setBackground(COLOR_FONDO);
            rotationTimer = new Timer(15, e -> {
                angle += 2.0;
                if (angle >= 360) {
                    angle = 0;
                }
                repaint();
            });
        }

        public void setSongTitle(String title) {
            this.currentTitle = title;
            repaint();
        }

        public void startSpinning() {
            if (!rotationTimer.isRunning()) {
                rotationTimer.start();
            }
        }

        public void stopSpinning() {
            rotationTimer.stop();
        }

        public void reset() {
            stopSpinning();
            angle = 0;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int centerX = w / 2;
            int centerY = h / 2;

            if (currentTitle.isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                String msg = "Selecciona una música para empezar...";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, centerX - fm.stringWidth(msg) / 2, centerY);
                return;
            }

            int discSize = Math.min(w, h) - 70; 
            if (discSize < 50) {
                discSize = 50;
            }

            AffineTransform oldTransform = g2.getTransform();

            g2.rotate(Math.toRadians(angle), centerX, centerY);

            g2.setColor(new Color(20, 20, 20));
            g2.fillOval(centerX - discSize / 2, centerY - discSize / 2, discSize, discSize);

            g2.setColor(new Color(45, 45, 45));
            for (int i = 15; i < discSize / 2 - 25; i += 8) {
                g2.drawOval(centerX - i, centerY - i, i * 2, i * 2);
            }

            int labelSize = discSize / 3;
            g2.setColor(COLOR_NARANJA);
            g2.fillOval(centerX - labelSize / 2, centerY - labelSize / 2, labelSize, labelSize);

            g2.setColor(new Color(255, 255, 255, 100));
            g2.fillArc(centerX - labelSize / 2 + 5, centerY - labelSize / 2 + 5, labelSize - 10, labelSize - 10, 0, 90);

            g2.setColor(COLOR_FONDO);
            g2.fillOval(centerX - 4, centerY - 4, 8, 8);

            g2.setTransform(oldTransform);

            g2.setColor(COLOR_TEXTO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));

            String displayTitle = currentTitle;
            if (displayTitle.length() > 30) {
                displayTitle = displayTitle.substring(0, 27) + "...";
            }

            FontMetrics fm = g2.getFontMetrics();
            int textX = centerX - fm.stringWidth(displayTitle) / 2;
            int textY = centerY + discSize / 2 + 30; 

            g2.drawString(displayTitle, textX, textY);
        }
    }

    private class BotonModerno extends JButton {

        private Color colorBase;
        private Color colorHover;

        public BotonModerno(String text, Color bg, Color fg) {
            super(text);
            this.colorBase = bg;
            this.colorHover = bg.brighter();
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(colorHover);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(colorBase);
                }
            });
            setBackground(colorBase);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    private class DarkSliderUI extends BasicSliderUI {

        public DarkSliderUI(JSlider b) {
            super(b);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Rectangle trackBounds = trackRect;
            g2.setColor(new Color(60, 60, 60));
            g2.fillRect(trackBounds.x, trackBounds.y + (trackBounds.height / 2) - 2, trackBounds.width, 4);

            int thumbX = thumbRect.x + (thumbRect.width / 2);
            g2.setColor(COLOR_NARANJA);
            g2.fillRect(trackBounds.x, trackBounds.y + (trackBounds.height / 2) - 2, thumbX - trackBounds.x, 4);
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
        }
    }
}
