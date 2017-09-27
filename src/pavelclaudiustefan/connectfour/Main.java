package pavelclaudiustefan.connectfour;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ConnectFour game = new ConnectFour();
            game.setVisible(true);
        });
    }

}
