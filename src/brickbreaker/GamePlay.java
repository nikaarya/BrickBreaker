package brickbreaker;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//KeyListener for detecting the arrow keys and Action listener for moving the ball
public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXdirection = -1;
    private int ballYdirection = -2;

    private MapGenerator map;

    //Constructor
    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        //Background
        g.setColor(Color.BLACK);
        g.fillRect(1,1, 692, 592); //creates a rectangle for the background

        //Drawing map (Den vita rutan högst upp på sidan).
        map.draw((Graphics2D)g);

        //Borders
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        //OBS! No border for the bottom part

        //Scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30); //"" För att det inte ska stå något innan score, det är bara siffran som ska visas

        //The paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        //The ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        //If all the bricks are gone the game is finished
        if (totalBricks <= 0) {
            play = false;
            ballXdirection = 0;
            ballYdirection = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!", 260, 300);

            //Restart
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press ENTER to Restart ", 230, 350);
        }

        //Game Over
        if (ballPosY > 570) {
            play = false;
            ballXdirection = 0;
            ballYdirection = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over\n Score: " + score, 190, 300);

            //Restart after GAME OVER
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press ENTER to Restart ", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdirection = -ballYdirection;
            }

            //the first map. is the map variable in this class and the second map. is the variable map[][] in the class mapGenerator
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        //If the ball touches the brick it should disappear
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballXdirection = -ballXdirection;
                            } else {
                                ballYdirection = -ballYdirection;
                            }
                            break A; //Breaks the first for loop labeled "A".
                        }
                    }
                }
            }

            ballPosX += ballXdirection;
            ballPosY += ballYdirection;
            if (ballPosX < 0) {
                ballXdirection = -ballXdirection;
            }
            if (ballPosY < 0) {
                ballYdirection = -ballYdirection;
            }
            if (ballPosX > 670) {
                ballXdirection = -ballXdirection;
            }
        }
        repaint(); //Restarts the paint method and paints everything again.
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            //Makes sure the ball doesn't go outside the screen (Height of the screen is 600)
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXdirection = -1;
                ballYdirection = -2;
                playerX = 310; //Styret hamnar i mitten av skärmen
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20; //If you press right the player moves 20px to the right
    }

    public void moveLeft() {
        play = true;
        playerX -= 20; //If you press left the player moves 20px to the left
    }
}
