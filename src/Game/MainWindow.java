package Game;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(){
        setTitle("Snake");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640,640);
        setLocation(300,200);
        add(new GameField());
        setVisible(true);

    }

}
