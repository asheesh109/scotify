package Musicplayer;

import javax.swing.*;

public class App {
    public App(String username) {

        SwingUtilities.invokeLater(() -> {
            new MusicPlayerGUI(username).setVisible(true);
        });
    }
}
