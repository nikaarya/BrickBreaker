package brickbreaker;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame object = new JFrame();
        GamePlay gameplay = new GamePlay();

        object.setBounds(10, 10, 700, 600);
        object.setTitle("Break Out Ball");
        object.setResizable(false);
        object.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        object.add(gameplay); //Had an error before i added "extends JPanel" in the class GamePlay
        object.setVisible(true);
    }
}
