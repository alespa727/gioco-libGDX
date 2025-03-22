package io.github.ale.dialogPkg.toplayerDialog;

import java.awt.Font;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class toplayerDialog extends JFrame{
    private final int WIDTH = 1000;
    private final int HEIGHT = 200;
    private final JPanel ROOTPANEL = new JPanel();
    private final JTextField textField = new JTextField("NIGGER");
    public toplayerDialog() {
        ROOTPANEL.setSize(WIDTH, HEIGHT);
        ROOTPANEL.setLayout(null);
        this.setVisible(true);
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(new Point(0, 0));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(ROOTPANEL);
        textField.setText("NIGGER");
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.BOLD, 140));
        textField.setBounds(10, 10, WIDTH - 20, HEIGHT - 20);
        ROOTPANEL.add(textField);
    }

}
