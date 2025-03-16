package Musicplayer;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Elogin extends JFrame {
    Elogin() {
        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Login", JLabel.CENTER);
        JLabel l1 = new JLabel("Enter Username");
        JTextField t1 = new JTextField(10);
        JLabel l2 = new JLabel("Enter Password");
        JPasswordField t2 = new JPasswordField(10);
        JLabel l3 = new JLabel("Enter Phone number");
        JTextField t3 = new JTextField(10);
        JButton b1 = new JButton("Submit");
        JButton b2 = new JButton("Back");

        title.setFont(f);
        l1.setFont(f2);
        t1.setFont(f2);
        l2.setFont(f2);
        t2.setFont(f2);
        l3.setFont(f2);
        t3.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);

        title.setBounds(250, 30, 300, 50);
        l1.setBounds(250, 100, 300, 30);
        t1.setBounds(250, 140, 300, 30);
        l2.setBounds(250, 200, 300, 30);
        t2.setBounds(250, 240, 300, 30);
        l3.setBounds(250, 300, 300, 30);
        t3.setBounds(250, 340, 300, 30);
        b1.setBounds(300, 390, 200, 40);
        b2.setBounds(300, 440, 200, 40);

        c.add(title);
        c.add(l1);
        c.add(t1);
        c.add(l2);
        c.add(t2);
        c.add(l3);
        c.add(t3);
        c.add(b1);
        c.add(b2);

        b2.addActionListener(a -> {
            new Landing();
            dispose();
        });

        b1.addActionListener(a -> {
            String url = "jdbc:mysql://localhost:3306/music";

            try (Connection conn = DriverManager.getConnection(url, "root", "Ashish030406")) {
                String sql = "SELECT * FROM users WHERE username=? AND password=? AND phone=?";
                try (PreparedStatement pst = conn.prepareStatement(sql)) {

                    String user = t1.getText();
                    String s1 = new String(t2.getPassword());
                    String no = t3.getText();

                    pst.setString(1, user);
                    pst.setString(2, s1);
                    pst.setString(3, no);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Successfully logged in");

                        // Start the App on the EDT (Event Dispatch Thread)
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new App(user); // Create App instance
                                dispose(); // Close the login window
                            }
                        });

                    } else {
                        JOptionPane.showMessageDialog(null, "User does not exist");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login");
    }

    public static void main(String[] args) {
        new Elogin();
    }
}
