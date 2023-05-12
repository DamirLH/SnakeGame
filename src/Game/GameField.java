package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

        private final int SIZE=640;
        private final int DOT_SIZE=32;
        private final int TOTAL_DOTS=400;
        private Image snake;
        private Image snakeTail;
        private Image snakeHead;
        private Image food;
        private int snakeLength; // размер змеи
        private int foodX;
        private int foodY;
        private int[] snakeX=new int[TOTAL_DOTS];
        private int[] snakeY=new int[TOTAL_DOTS];
        private Timer timer;
        private boolean inGame = true;
        private boolean right = true;
        private boolean left;
        private boolean down;
        private boolean up;


    public GameField(){
        setBackground(Color.DARK_GRAY);
        imageLoad();
        gameStarter();

        addKeyListener(new KeyListener());
        setFocusable(true);
    }

    public void gameStarter(){
        snakeLength=3;
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i]=160-i*DOT_SIZE;
            snakeY[i]=320;
        } timer=new Timer(250,this); timer.start();
        createFood();
    }

    @Override
    public void actionPerformed(ActionEvent e) { // процесс самой игры
        if (inGame){
            checkFood();
            checkFail();
            move();
        }
        repaint();
    }

    public void move(){
        for (int i = snakeLength; i >0 ; i--) {
            snakeX[i]=snakeX[i-1];
            snakeY[i]=snakeY[i-1];
        } if (right){
            snakeX[0]+=DOT_SIZE;
        } else if (left){
            snakeX[0]-=DOT_SIZE;
        } else if (up){
            snakeY[0]-=DOT_SIZE;
        } else if (down){
            snakeY[0]+=DOT_SIZE;
        }
    }

    @Override
    protected void paintComponent(Graphics g) { /** Drawing the objects **/
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(food, foodX, foodY, this);
            for (int i = 0; i < snakeLength; i++) {
//                int current = i;
//                int currentX = snakeX[i];
//                int currentY = snakeY[i];
                /** Head drawing and rotation **/
                if (i == 0) {
                    if (snakeY[i] < snakeY[i + 1]) {
                        ImageIcon headImage = new ImageIcon("Images/head-up.png");
                        snakeHead = headImage.getImage();
                        g.drawImage(snakeHead, snakeX[0], snakeY[0], this);
                    } else if (snakeY[i] > snakeY[i + 1]) {
                        ImageIcon headImage = new ImageIcon("Images/head-down.png");
                        snakeHead = headImage.getImage();
                        g.drawImage(snakeHead, snakeX[0], snakeY[0], this);
                    } else if (snakeX[i] < snakeX[i + 1]) {
                        ImageIcon headImage = new ImageIcon("Images/head-left.png");
                        snakeHead = headImage.getImage();
                        g.drawImage(snakeHead, snakeX[0], snakeY[0], this);
                    } else if (snakeX[i] > snakeX[i + 1]) {
                        ImageIcon headImage = new ImageIcon("Images/head-right.png");
                        snakeHead = headImage.getImage();
                        g.drawImage(snakeHead, snakeX[0], snakeY[0], this);
                    }
                }
                /** Tail segment drawing+rotation. We should compare tail element with element before him **/
                else if (i == snakeLength-1 ) {
                    if (snakeX[i] < snakeX[i-1]) {
                        ImageIcon tailImage = new ImageIcon("Images/tail1.png");
                        snakeTail = tailImage.getImage();
                        g.drawImage(snakeTail, snakeX[i], snakeY[i], this);
                    }
                    else if (snakeX[i] > snakeX[i-1]) {
                        ImageIcon tailImage = new ImageIcon("Images/tail3.png");
                        snakeTail = tailImage.getImage();
                        g.drawImage(snakeTail, snakeX[i], snakeY[i], this);
                    }
                    else if (snakeY[i] < snakeY[i-1]) {
                        ImageIcon tailImage = new ImageIcon("Images/tail2.png");
                        snakeTail = tailImage.getImage();
                        g.drawImage(snakeTail, snakeX[i], snakeY[i], this);
                    }
                    else if (snakeY[i] > snakeY[i-1]) {
                        ImageIcon tailImage = new ImageIcon("Images/tail4.png");
                        snakeTail = tailImage.getImage();
                        g.drawImage(snakeTail, snakeX[i], snakeY[i], this);
                    }
                }
                /**Snake body. Vertical/Horizontal or rotation moving sprite !**/
                else {
                    if (snakeX[i-1]<snakeX[i] && snakeX[i]<snakeX[i+1] || snakeX[i-1]>snakeX[i] && snakeX[i]>snakeX[i+1]){
                        ImageIcon snakeImage = new ImageIcon("Images/snake-hor.png");
                        snake=snakeImage.getImage(); /** Horizontal moving **/
                        g.drawImage(snake,snakeX[i],snakeY[i],this);
                    }
                    else if (snakeY[i-1]<snakeY[i] && snakeY[i]<snakeY[i+1] || snakeY[i-1]>snakeY[i] && snakeY[i]>snakeY[i+1]){
                        ImageIcon snakeImage = new ImageIcon("Images/snake-vert.png");
                        snake=snakeImage.getImage(); /** Vertical moving **/
                        g.drawImage(snake,snakeX[i],snakeY[i],this);
                    }
                    else if (snakeX[i-1]<snakeX[i] && snakeY[i+1]>snakeY[i] || snakeX[i+1]<snakeX[i] && snakeY[i-1] >snakeY[i]) {
                        ImageIcon snakeImage = new ImageIcon("Images/rotation3.png");
                        snake = snakeImage.getImage(); /** Rotation move **/
                        g.drawImage(snake, snakeX[i], snakeY[i], this);
                    }
                    else if (snakeX[i+1]<snakeX[i] && snakeY[i-1]<snakeY[i] || snakeX[i-1]<snakeX[i] && snakeY[i+1] <snakeY[i]) {
                        ImageIcon snakeImage = new ImageIcon("Images/rotation1.png");
                        snake = snakeImage.getImage(); /** Rotation move **/
                        g.drawImage(snake, snakeX[i], snakeY[i], this);
                    }
                    else if (snakeX[i+1]>snakeX[i] && snakeY[i-1]>snakeY[i] || snakeX[i-1]>snakeX[i] && snakeY[i+1] >snakeY[i]) {
                        ImageIcon snakeImage = new ImageIcon("Images/rotation2.png");
                        snake = snakeImage.getImage(); /** Rotation move **/
                        g.drawImage(snake, snakeX[i], snakeY[i], this);
                    }
                    else if (snakeX[i+1]>snakeX[i] && snakeY[i-1]<snakeY[i] || snakeX[i-1]>snakeX[i] && snakeY[i+1] <snakeY[i]) {
                        ImageIcon snakeImage = new ImageIcon("Images/rotation4.png");
                        snake = snakeImage.getImage(); /** Rotation move **/
                        g.drawImage(snake, snakeX[i], snakeY[i], this);
                    }
                }
            }
        }
        else {
            String str = "Game Over";
            g.setColor(Color.white);
            g.drawString(str,SIZE/2,SIZE/2);
        }
    }

    public void createFood(){
        foodX=new Random().nextInt(18+1)*DOT_SIZE;
        foodY=new Random().nextInt(18+1)*DOT_SIZE;
    }

    public void checkFood(){
        if (snakeX[0]==foodX&&snakeY[0]==foodY){
            snakeLength++;
            createFood();
        }
    }

    public void checkFail() {
        for (int i = snakeLength; i > 0; i--) {
            if (snakeLength > 4 && snakeX[0] == snakeX[i] && snakeY[0]==snakeY[i]){
                inGame=false;
            }
        }
        if (snakeX[0]>SIZE || snakeX[0]<0){
            inGame=false;
        } if (snakeY[0]>SIZE || snakeY[0]<0){
            inGame=false;
        }
    }

    public void imageLoad(){
        ImageIcon foodImage = new ImageIcon("Images/food.png");
        food=foodImage.getImage();
        ImageIcon snakeImage = new ImageIcon("Images/snake-hor.png");
        snake=snakeImage.getImage();
        ImageIcon headImage = new ImageIcon("Images/head-right.png");
        snakeHead=headImage.getImage();
        ImageIcon tailImage = new ImageIcon("Images/tail1.png");
        snakeTail=tailImage.getImage();
    }

    class KeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key==KeyEvent.VK_LEFT && !right){
                left=true; up=false; down=false;
            }
            if (key==KeyEvent.VK_RIGHT && !left){
                right=true; up=false; down=false;
            }
            if (key==KeyEvent.VK_DOWN && !up){
                down=true; left=false; right=false;
            }
            if (key==KeyEvent.VK_UP && !down){
                up=true; left=false; right=false;
            }
        }
    }
}
