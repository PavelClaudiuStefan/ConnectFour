package pavelclaudiustefan.connectfour;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class StartingMenu extends JPanel {

    private final static int WIDTH = 384, HEIGHT = 240;
    private BufferedImage backgroundImage;
    private ConnectFour game;

    StartingMenu(ConnectFour game) {
        this.game = game;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initBackgroundImage();
    }

    private void initBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/pavelclaudiustefan/connectfour/backgroundImage.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);

        initGameName(g);
        initButtons();
    }

    private void initGameName(Graphics g) {
        String name = "Connect Four";
        Font font = new Font("arial", Font.BOLD, 30);
        g.setFont(font);
        g.setColor(Color.cyan);
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (WIDTH - metrics.stringWidth(name)) / 2;
        g.drawString(name, x, HEIGHT / 4);
    }

    private void initButtons() {
        setLayout(null);

        JButton newGameButton = new JButton("Start new game");
        newGameButton.setBounds((WIDTH - 150) / 2, (HEIGHT - 50) / 2, 150, 30);
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(e -> game.startGame());
        add(newGameButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds((WIDTH - 150) / 2, (HEIGHT - 50) / 2 + 50, 150, 30);
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
    }

}