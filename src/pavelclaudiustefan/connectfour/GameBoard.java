package pavelclaudiustefan.connectfour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class GameBoard extends JPanel {

    private final static int WIDTH = 350, HEIGHT = 325;
    private int selectionTriangleColumn;

    GameBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        selectionTriangleColumn = 0;
        setBackground(Color.CYAN);
        initMouseListener();
    }

    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //Drop disc
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                selectionTriangleColumn = e.getX() / (WIDTH / 7);
            }
        });
    }

    void tick() {
        //check if there is a winner
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        drawBoardArea(g2d);
    }

    private void drawBoardArea(Graphics2D g2d) {
        double yOffset = HEIGHT / 13;
        double cellHeight = (HEIGHT / 13) * 2;
        double cellWidth = WIDTH / 7;
        double cellPaddingY = (cellHeight / 100) * 15;
        double cellPaddingX = (cellWidth / 100) * 15;
        double cellCircleHeight = (cellHeight / 100) * 70;
        double cellCircleWidth = (cellWidth / 100) * 70;

        Area area = new Area(new Rectangle2D.Double(0, yOffset, WIDTH, HEIGHT));
        for (double y = yOffset; y < HEIGHT; y += cellHeight) {
            for (double x = 0; x < WIDTH; x += cellWidth ) {
                Area disc = new Area(new Ellipse2D.Double(x+cellPaddingX, y+cellPaddingY, cellCircleWidth, cellCircleHeight));
                area.subtract(disc);
            }
        }
        g2d.fill(area);

        int x1 = (int)(selectionTriangleColumn * cellWidth);
        int x2 = (int)(x1 + cellWidth);
        int x3 = (int)(x1 + (cellWidth / 2));
        Area selectionTriangle = new Area(new Polygon(new int[] {x1, x2, x3}, new int[] {0, 0, (int)yOffset}, 3));
        g2d.fill(selectionTriangle);
    }
}
