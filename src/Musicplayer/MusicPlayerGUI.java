package Musicplayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerGUI extends JFrame {
    public static final Color FRAME_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;

    private MusicPlayer musicPlayer;
    private JFileChooser jFileChooser;
    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
    private JSlider playbackSlider;
    private Timer playbackTimer;
    private int elapsedSeconds = 0;
    private String currentUser;

    public MusicPlayerGUI(String username) {
        super("Music Player");
        this.currentUser = username;
        musicPlayer = new MusicPlayer(this);

        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(FRAME_COLOR);

        jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("src/Resource"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents() {
        addToolbar();

        JLabel songImage = new JLabel(loadImage("src/Resource/record.png.png"));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        songTitle = new JLabel("Song Title", SwingConstants.CENTER);
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(TEXT_COLOR);
        add(songTitle);

        songArtist = new JLabel("Artist", SwingConstants.CENTER);
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 24));
        songArtist.setForeground(TEXT_COLOR);
        add(songArtist);

        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth() / 2 - 150, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                musicPlayer.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JSlider source = (JSlider) e.getSource();
                int frame = source.getValue();
                musicPlayer.setCurrentFrame(frame);
                musicPlayer.setCurrentTimeInMilli((int) (frame / (2.08 * musicPlayer.getCurrentSong().getFrameRatePerMilliseconds())));
                musicPlayer.playCurrentSong();
                enablePauseButtonDisablePlayButton();
            }
        });
        add(playbackSlider);

        addPlaybackBtns();
    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 40);
        toolBar.setFloatable(false);

        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        JMenu songMenu = new JMenu("          Song            ");
        menuBar.add(songMenu);

        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(e -> {
            int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
            File selectedFile = jFileChooser.getSelectedFile();
            if (result == JFileChooser.APPROVE_OPTION && selectedFile != null) {
                Song song = new Song(selectedFile.getPath());
                musicPlayer.loadSong(song);
                updateSongTitleAndArtist(song);
                updatePlaybackSlider(song);
                enablePauseButtonDisablePlayButton();
            }
        });
        songMenu.add(loadSong);

        JMenu playlistMenu = new JMenu("            Playlist            ");
        menuBar.add(playlistMenu);

        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(e -> new MusicPlaylistDialog(this, currentUser).setVisible(true));
        playlistMenu.add(createPlaylist);

        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        loadPlaylist.addActionListener(e -> MusicPlaylistDialog.loadPlaylistFromDatabase(currentUser, musicPlayer));
        playlistMenu.add(loadPlaylist);

        JMenu back = new JMenu("          Log Out            ");
        menuBar.add(back);

        JMenuItem Landing = new JMenuItem("Landing");
        Landing.addActionListener(a -> {
            new Landing();
            dispose();
        });
        back.add(Landing);

        JMenuItem Login = new JMenuItem("Login");
        Login.addActionListener(e -> {
            new Elogin();
            dispose();
        });
        back.add(Login);

        add(toolBar);
    }

    private void addPlaybackBtns() {
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 435, getWidth() - 10, 80);
        playbackBtns.setBackground(null);

        JButton prevButton = new JButton(loadImage("src/Resource/previous.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        prevButton.addActionListener(e -> musicPlayer.prevSong());
        playbackBtns.add(prevButton);

        JButton playButton = new JButton(loadImage("src/Resource/play.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(e -> {
            enablePauseButtonDisablePlayButton();
            musicPlayer.playCurrentSong();
            startTimerForPlayback();
        });
        playbackBtns.add(playButton);

        JButton pauseButton = new JButton(loadImage("src/Resource/pause.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(e -> {
            enablePlayButtonDisablePauseButton();
            musicPlayer.pauseSong();
            stopTimer();
        });
        playbackBtns.add(pauseButton);

        JButton nextButton = new JButton(loadImage("src/Resource/next.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        nextButton.addActionListener(e -> musicPlayer.nextSong());
        playbackBtns.add(nextButton);

        add(playbackBtns);
    }

    public void setPlaybackSliderValue(int frame) {
        playbackSlider.setValue(frame);
    }

    public void updateSongTitleAndArtist(Song song) {
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    public void updatePlaybackSlider(Song song) {
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, createSliderLabel("00:00"));
        labelTable.put(song.getMp3File().getFrameCount(), createSliderLabel(song.getSongLength()));

        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }

    private JLabel createSliderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Dialog", Font.BOLD, 18));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    public void enablePauseButtonDisablePlayButton() {
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        playButton.setVisible(false);
        playButton.setEnabled(false);
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    public void enablePlayButtonDisablePauseButton() {
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        playButton.setVisible(true);
        playButton.setEnabled(true);
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private ImageIcon loadImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startTimerForPlayback() {
        if (playbackTimer != null) {
            playbackTimer.cancel();
        }

        playbackTimer = new Timer();
        playbackTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedSeconds++;
                updateTimeLabel(elapsedSeconds);
                updateSlider(elapsedSeconds);
            }
        }, 0, 1000);
    }

    private void updateTimeLabel(int seconds) {
        String timeString = formatTime(seconds);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, createSliderLabel("00:00"));
        labelTable.put(musicPlayer.getCurrentSong().getMp3File().getFrameCount(), createSliderLabel(musicPlayer.getCurrentSong().getSongLength()));

        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }

    private void updateSlider(int seconds) {
        int totalFrames = musicPlayer.getCurrentSong().getMp3File().getFrameCount();
        int currentFrame = (int) (seconds * (totalFrames / (2.08 * musicPlayer.getCurrentSong().getFrameRatePerMilliseconds())));
        playbackSlider.setValue(currentFrame);
    }

    public void stopTimer() {
        if (playbackTimer != null) {
            playbackTimer.cancel();
            playbackTimer = null;
        }
    }
}
