package Musicplayer;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Home extends JFrame {
    Home(String username) {
        double balance = 0.0;
        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Welcome " + username, JLabel.CENTER);
        JButton b1 = new JButton("Listen Music");
        JButton b2 = new JButton("Withdraw");
        JButton b3 = new JButton("Logout");

        title.setFont(f);

        b1.setFont(f2);
        b2.setFont(f2);
        b3.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);

        title.setBounds(100, 30, 600, 50);

        b1.setBounds(300, 150, 200, 40);
        b2.setBounds(300, 220, 200, 40);
        b3.setBounds(300, 290, 200, 40);

        c.add(title);

        c.add(b1);
        c.add(b2);
        c.add(b3);



        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Home");
    }

    public static void main(String[] args) {
        new Home("Ashish");
    }
}
