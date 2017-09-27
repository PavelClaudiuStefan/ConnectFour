package pavelclaudiustefan.connectfour;

import javax.swing.*;
import java.awt.*;

class StatsFrame extends JFrame{

    private ConnectFour game;
    private JLabel fpsLabel;

    StatsFrame(ConnectFour game) {
        super("Stats");
        this.game = game;
        initLabels();
        pack();
    }

    private void initLabels() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        fpsLabel = new JLabel("     FPS: 0");
        panel.add(fpsLabel);

        add(panel);
    }

    void resetLocation() {
        setSize(250, 90);
        setLocation(game.getX() + game.getWidth() - 14, game.getY());
    }

    void updateFPS(int frames) {
        fpsLabel.setText("     FPS: " + String.valueOf(frames));
        repaint();
    }

}
