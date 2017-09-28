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
    private double yOffset = HEIGHT / 13;
    private double cellHeight = (HEIGHT / 13) * 2;
    private double cellWidth = WIDTH / 7;
    private double cellPaddingY = (cellHeight / 100) * 15;
    private double cellPaddingX = (cellWidth / 100) * 15;
    private double cellCircleHeight = (cellHeight / 100) * 70;
    private double cellCircleWidth = (cellWidth / 100) * 70;

    private Disc[][] discs;
    private int[] lowestPosition;

    private int selectionTriangleColumn;
    private int playerID;

    GameBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        selectionTriangleColumn = 0;
        playerID = 1;
        setBackground(Color.CYAN);
        initDisc();
        initMouseListener();
    }

    private void initDisc() {
        discs = new Disc[6][7];
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 7; x++) {
                discs[y][x] = new Disc();
            }
        }
        lowestPosition = new int[7];
        for (int i = 0; i < 7; i++) {
            lowestPosition[i] = 5;
        }
    }

    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isDiscDroppable()) {
                    dropNewDisc();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                selectionTriangleColumn = e.getX() / (WIDTH / 7);
            }
        });
    }

    private boolean isDiscDroppable() {
        return lowestPosition[selectionTriangleColumn] >= 0;
    }

    private void dropNewDisc() {
        int y = lowestPosition[selectionTriangleColumn]--;
        int x = selectionTriangleColumn;
        discs[y][x].setPlayerID(playerID);
        playerID *= -1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        Color defaultColor = g2d.getColor();

        drawDiscs(g2d);

        g2d.setColor(defaultColor);
        drawBoardArea(g2d);

        g2d.setColor(playerColor(playerID));
        drawSelectionTriangle(g2d);
    }

    private void drawDiscs(Graphics2D g2d) {
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 7; x++) {
                if (discs[y][x].getPlayerID() != 0) {
                    double discY = y * cellHeight + yOffset + cellPaddingY;
                    double discX = x * cellWidth + cellPaddingX;

                    Area disc = new Area(new Ellipse2D.Double(discX, discY, cellCircleWidth, cellCircleHeight));
                    g2d.setColor(playerColor(discs[y][x].getPlayerID()));
                    g2d.fill(disc);

                    Area discBorder = new Area(new Ellipse2D.Double(discX-2, discY-2, cellCircleWidth+4, cellCircleHeight+4));
                    discBorder.subtract(new Area(new Ellipse2D.Double(discX+4, discY+4, cellCircleWidth-8, cellCircleHeight-8)));
                    g2d.setColor(g2d.getColor().darker());
                    g2d.fill(discBorder);
                }
            }
        }
    }

    private void drawBoardArea(Graphics2D g2d) {
        Area area = new Area(new Rectangle2D.Double(0, yOffset, WIDTH, HEIGHT));
        for (double y = yOffset; y < HEIGHT; y += cellHeight) {
            for (double x = 0; x < WIDTH; x += cellWidth ) {
                Area disc = new Area(new Ellipse2D.Double(x+cellPaddingX, y+cellPaddingY, cellCircleWidth, cellCircleHeight));
                area.subtract(disc);
            }
        }
        g2d.fill(area);
    }

    private void drawSelectionTriangle(Graphics2D g2d) {
        int x1 = (int)(selectionTriangleColumn * cellWidth);
        int x2 = (int)(x1 + cellWidth);
        int x3 = (int)(x1 + (cellWidth / 2));

        Area selectionTriangle = new Area(new Polygon(new int[] {x1, x2, x3}, new int[] {0, 0, (int)yOffset}, 3));
        g2d.fill(selectionTriangle);

        Area selectionTriangleBorder = new Area(new Polygon(new int[] {x1, x2, x3}, new int[] {0, 0, (int)yOffset}, 3));
        selectionTriangleBorder.subtract(new Area(new Polygon(new int[] {x1+5, x2-5, x3}, new int[] {2, 2, (int)yOffset - 3}, 3)));
        g2d.setColor(g2d.getColor().darker());
        g2d.fill(selectionTriangleBorder);
    }

    private Color playerColor(int id) {
        if (id == 1)
            return Color.YELLOW;
        else if (id == -1)
            return Color.RED;
        else
            return Color.BLACK;
    }
}
