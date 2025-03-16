package Musicplayer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MusicPlaylistDialog extends JDialog {
    private MusicPlayerGUI musicPlayerGUI;
    private String currentUsername;

    // Store all of the paths to be written to the database
    private ArrayList<String> songPaths;

    public MusicPlaylistDialog(MusicPlayerGUI musicPlayerGUI, String currentUsername) {
        this.musicPlayerGUI = musicPlayerGUI;
        this.currentUsername = currentUsername;
        songPaths = new ArrayList<>();

        // Configure dialog
        setTitle("Create Playlist");
        setSize(400, 400);
        setResizable(false);
        getContentPane().setBackground(MusicPlayerGUI.FRAME_COLOR);
        setLayout(null);
        setModal(true); // This property makes it so that the dialog has to be closed to give focus
        setLocationRelativeTo(musicPlayerGUI);

        addDialogComponents();
    }

    private void addDialogComponents() {
        // Container to hold each song path
        JPanel songContainer = new JPanel();
        songContainer.setLayout(new BoxLayout(songContainer, BoxLayout.Y_AXIS));
        songContainer.setBounds((int) (getWidth() * 0.025), 10, (int) (getWidth() * 0.90), (int) (getHeight() * 0.75));
        add(songContainer);

        // Add song button
        JButton addSongButton = new JButton("Add");
        addSongButton.setBounds(60, (int) (getHeight() * 0.80), 100, 25);
        addSongButton.setFont(new Font("Dialog", Font.BOLD, 14));
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
                jFileChooser.setCurrentDirectory(new File("src/Resource"));
                int result = jFileChooser.showOpenDialog(MusicPlaylistDialog.this);

                File selectedFile = jFileChooser.getSelectedFile();
                if (result == JFileChooser.APPROVE_OPTION && selectedFile != null) {
                    JLabel filePathLabel = new JLabel(selectedFile.getPath());
                    filePathLabel.setFont(new Font("Dialog", Font.BOLD, 12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    // Add to the list
                    songPaths.add(filePathLabel.getText());

                    // Add to container
                    songContainer.add(filePathLabel);

                    // Refresh dialog to show newly added JLabel
                    songContainer.revalidate();
                }
            }
        });
        add(addSongButton);

        // Save playlist button
        JButton savePlaylistButton = new JButton("Save");
        savePlaylistButton.setBounds(215, (int) (getHeight() * 0.80), 100, 25);
        savePlaylistButton.setFont(new Font("Dialog", Font.BOLD, 14));
        savePlaylistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePlaylistToDatabase();
            }
        });
        add(savePlaylistButton);
    }

    private void savePlaylistToDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/music", "root", "Ashish030406");

            // Insert playlist into database
            String insertQuery = "INSERT INTO list (username, song_path) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            for (String songPath : songPaths) {
                preparedStatement.setString(1, currentUsername);
                preparedStatement.setString(2, songPath);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            JOptionPane.showMessageDialog(this, "Playlist saved successfully!");

            connection.close();

            // Close dialog
            dispose();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadPlaylistFromDatabase(String username, MusicPlayer musicPlayer) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicplayer", "root", "password");

            String selectQuery = "SELECT song_path FROM playlists WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> playlist = new ArrayList<>();

            while (resultSet.next()) {
                String songPath = resultSet.getString("song_path");
                playlist.add(songPath);
            }

            musicPlayer.loadPlaylist(playlist);
            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}