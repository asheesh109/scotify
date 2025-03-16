package Musicplayer;

import javax.swing.*;
import java.awt.*;

class Landing extends JFrame {
    Landing() {
        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel l1 = new JLabel("Scotify Music Player", JLabel.CENTER);

        JButton b2 = new JButton("Log In");
        JLabel l2 = new JLabel("Don't have an Account?");
        JLabel l3 = new JLabel("Sign up to Listen");
        JButton b3 = new JButton("Sign up");

        l1.setFont(f);

        b2.setFont(f2);
        b3.setFont(f2);
        l2.setFont(f2);
        l3.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);


        l1.setBounds(155, 50, 500, 50);
        b2.setBounds(285, 170, 250, 50);
        l2.setBounds(300, 270, 250, 30);
        l3.setBounds(340, 310, 250, 30);
        b3.setBounds(285, 350, 250, 50);

        c.add(l1);
        c.add(b2);
        c.add(l2);
        c.add(l3);  // Added l3
        c.add(b3);

        b2.addActionListener(a -> {
            new Elogin();
            dispose();
        });

        b3.addActionListener(a -> {
            new Nlogin();
            dispose();
        });

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Musicplayer.Landing Page");

    }

    public static void main(String[] args) {
        Landing a = new Landing();
    }
}