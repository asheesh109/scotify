 package Musicplayer;

import javax.swing.*;
        import java.awt.*;
        import java.sql.*;

class Nlogin extends JFrame {
    Nlogin() {
        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel l1 = new JLabel("Set Username");
        JTextField t1 = new JTextField(10);

        JLabel l2 = new JLabel("Set Password");
        JTextField t2 = new JTextField(10);

        JLabel l3 = new JLabel("Confirm Password");
        JTextField t3 = new JTextField(10);

        JButton b1 = new JButton("Submit");
        JButton b2 = new JButton("Back");

        JLabel l4 = new JLabel("Enter Mobile no.");
        JTextField t4 = new JTextField(10);



        JLabel title = new JLabel("Sign up !", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(300, 10, 200, 40);

        l1.setFont(f2);
        t1.setFont(f2);
        l2.setFont(f2);
        t2.setFont(f2);
        b1.setFont(f2);
        l3.setFont(f2);
        t3.setFont(f2);
        l4.setFont(f2);
        t4.setFont(f2);
        b2.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);

        l1.setBounds(140, 110, 200, 30);
        t1.setBounds(140, 150, 200, 30);

        l2.setBounds(140, 200, 200, 30);
        t2.setBounds(140, 240, 200, 30);

        l3.setBounds(450, 110, 200, 30);
        t3.setBounds(450, 150, 200, 30);

        l4.setBounds(450, 200, 200, 30);
        t4.setBounds(450, 240, 200, 30);

        b1.setBounds(300, 340, 200, 40);
        b2.setBounds(300, 410, 200, 40);

        c.add(title);
        c.add(l1);
        c.add(t1);
        c.add(l2);
        c.add(t2);
        c.add(l3);
        c.add(t3);
        c.add(l4);
        c.add(t4);
        c.add(b1);
        c.add(b2);

        b2.addActionListener(a->
        {
            new Landing();
            dispose();
        });

        b1.addActionListener(
                a->
                {
                    if(t2.getText().equals(t3.getText()))
                    {
                        String no = t4.getText();
                        if(no.length()==10)
                        {
                            String url="jdbc:mysql://localhost:3306/music";

                            try(Connection conn = DriverManager.getConnection(url,"root","Ashish030406"))
                            {
                                String sql= "insert into users(username,password,phone) values(?,?,?);";
                                try (PreparedStatement pst= conn.prepareStatement(sql))
                                {
                                    String user=t1.getText();
                                    String pass=t2.getText();
                                    String contact=t4.getText();


                                    pst.setString(1,user);
                                    pst.setString(2,pass);
                                    pst.setString(3,contact);

                                    pst.executeUpdate();
                                    JOptionPane.showMessageDialog(null,"Successfully Log In");

                                    new App(user);
                                    dispose();

                                }

                            }
                            catch(SQLException e)
                            {
                                JOptionPane.showMessageDialog(null,e.getMessage());
                            }



                        }

                        else
                        {
                            JOptionPane.showMessageDialog(null,"Invalid Phone Number.");
                        }
                    }

                    else
                    {
                        JOptionPane.showMessageDialog(null,"Password does not match");
                        t1.setText("");
                        t2.setText("");
                        t3.setText("");
                        t4.setText("");
                    }
                }
        );

        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Signup");
    }

    public static void main(String[] args) {
        Nlogin a = new Nlogin();
    }
}
