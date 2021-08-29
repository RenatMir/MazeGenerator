package com.company;

import javax.swing.*;

public class Frame extends JFrame{

    public static void main(String[] args) {
	new Frame();
    }
    Frame(){

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MyPanel panel = new MyPanel();

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
