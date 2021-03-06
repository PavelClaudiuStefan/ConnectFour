package pavelclaudiustefan.connectfour;

import javax.swing.*;
import java.awt.event.KeyEvent;

class ConnectFour extends JFrame implements Runnable{

    private Thread thread;
    private boolean running;
    private StatsFrame statsFrame;

    public ConnectFour() {
        super("Connect Four");

        setResizable(false);

        initMenuBar();
        initStartingMenu();
        initStatsFrame();

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        editMenu.setMnemonic(KeyEvent.VK_E);
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem newGameItem = new JMenuItem("Start new game");
        newGameItem.addActionListener((e) -> startGame());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((e) -> System.exit(0));

        JMenuItem repaintItem = new JMenuItem("Toggle stats frame");
        repaintItem.addActionListener((e) -> {
            if (running) {
                statsFrame.setVisible(!statsFrame.isVisible());
                statsFrame.resetLocation();
            }

        });

        JMenuItem nahItem = new JMenuItem("Nah");
        nahItem.addActionListener((e) -> System.out.println("Nah"));

        fileMenu.add(newGameItem);
        fileMenu.add(exitItem);
        editMenu.add(repaintItem);
        helpMenu.add(nahItem);
    }

    private void initStartingMenu() {
        add(new StartingMenu(this));
        pack();
    }

    //Only displays FPS for now
    private void initStatsFrame() {
        statsFrame = new StatsFrame(this);
        statsFrame.resetLocation();
        statsFrame.setVisible(false);
    }

    synchronized void startGame() {
        getContentPane().removeAll();
        add(new GameBoard());
        revalidate();
        repaint();
        pack();
        setLocationRelativeTo(null);
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                repaint();
                delta--;
            }
            if (running)
                //repaint();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                statsFrame.updateFPS(frames);
                frames = 0;
            }
        }
        stop();
    }

    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
