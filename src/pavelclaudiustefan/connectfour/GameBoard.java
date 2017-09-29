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
    private final static int COLUMNS = 7;
    private final static int ROWS = 6;
    //TODO - Use ROWS and COLUMNS instead of numbers
    private double yOffset = HEIGHT / 13;
    private double cellHeight = (HEIGHT / 13) * 2;
    private double cellWidth = WIDTH / 7;
    private double cellPaddingY = (cellHeight / 100) * 15;
    private double cellPaddingX = (cellWidth / 100) * 15;
    private double cellCircleHeight = (cellHeight / 100) * 70;
    private double cellCircleWidth = (cellWidth / 100) * 70;

    private Disc[][] discs;
    private int[] lowestPosition;
    private int lastDroppedDiscX;
    private int lastDroppedDiscY;
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;

    private int selectionTriangleColumn;
    private int playerID;
    private boolean running;
    private boolean draw;

    GameBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        selectionTriangleColumn = 0;
        playerID = 1;
        running = true;
        draw = false;
        setBackground(Color.CYAN);
        initDiscs();
        initMouseListener();
    }

    private void initDiscs() {
        discs = new Disc[ROWS][COLUMNS];
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                discs[y][x] = new Disc();
            }
        }
        lowestPosition = new int[7];
        for (int i = 0; i < COLUMNS; i++) {
            lowestPosition[i] = 5;
        }
    }

    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (running) {
                    if (isDiscDroppable()) {
                        dropNewDisc();
                        if (isGameOver())
                            endGame();
                        else {
                            playerID *= -1;
                            //TODO START - TEMPORAR - Random placing of red discs
                            dropNewDisc();
                            playerID *= -1;
                            //TODO END
                        }
                    }
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                selectionTriangleColumn = e.getX() / (WIDTH / COLUMNS);
            }
        });
    }

    private boolean isDiscDroppable() {
        return lowestPosition[selectionTriangleColumn] >= 0;
    }

    private void dropNewDisc() {
        if (playerID == 1) {
            lastDroppedDiscY = lowestPosition[selectionTriangleColumn]--;
            lastDroppedDiscX = selectionTriangleColumn;
            discs[lastDroppedDiscY][lastDroppedDiscX].setPlayerID(playerID);
        }

        //TODO START - TEMPORAR - Random placing of red discs
        if (playerID == -1) {
            int temp = (int)(Math.random()*100)%7;
            System.out.println(temp);
            lastDroppedDiscY = lowestPosition[temp]--;
            lastDroppedDiscX = temp;
            discs[lastDroppedDiscY][lastDroppedDiscX].setPlayerID(playerID);
        }
        //TODO END
    }

    private boolean isGameOver() {
        if (isWinningLineHorizontal())
            return true;

        if (isWinningLineVertical())
            return true;

        if (isWinningLineMainDiagonal())
            return true;

        if (isWinningLineSecondaryDiagonal())
            return true;

        if (isDraw()) {
            draw = true;
            return true;
        }


        return false;
    }

    private boolean isWinningLineHorizontal() {
        if (lastDroppedDiscX < 3)
            firstX = 0;
        else
            firstX = lastDroppedDiscX - 3;
        if (lastDroppedDiscX > 3)
            lastX = 6;
        else
            lastX = lastDroppedDiscX + 3;
        firstY = lastY = lastDroppedDiscY;

        if (lastX - firstX > 2) {
            int nrOfDiscs = 0;
            for (int i = firstX; i <= lastX; i++) {
                if (discs[lastDroppedDiscY][i].getPlayerID() == playerID) {
                    nrOfDiscs++;
                }
                else if (nrOfDiscs < 4) {
                    nrOfDiscs = 0;
                    firstX = i + 1;
                }
            }
            if (nrOfDiscs > 3) {
                lastX = firstX + nrOfDiscs - 1;
                return true;
            }
        }
        return false;
    }

    private boolean isWinningLineVertical() {
        firstX = lastX = lastDroppedDiscX;
        firstY = lastDroppedDiscY;
        lastY = firstY + 3;

        if (lastY < 6) {
            int nrOfDiscs = 0;
            for (int i = firstY; i <= lastY; i++) {
                if (discs[i][lastDroppedDiscX].getPlayerID() == playerID) {
                    nrOfDiscs++;
                }
            }
            if (nrOfDiscs > 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isWinningLineMainDiagonal() {
        if (lastDroppedDiscX > 2 && lastDroppedDiscY > 2) {
            firstX = lastDroppedDiscX - 3;
            firstY = lastDroppedDiscY - 3;
        } else {
            int offset = Math.min(lastDroppedDiscX, lastDroppedDiscY);
            firstX = lastDroppedDiscX - offset;
            firstY = lastDroppedDiscY - offset;
        }
        if (lastDroppedDiscX < 4 && lastDroppedDiscY < 3) {
            lastX = lastDroppedDiscX + 3;
            lastY = lastDroppedDiscY + 3;
        } else {
            int offset = Math.min(6 - lastDroppedDiscX, 5 - lastDroppedDiscY);
            lastX = lastDroppedDiscX + offset;
            lastY = lastDroppedDiscY + offset;
        }

        if (lastX - firstX > 2) {
            int nrOfDiscs = 0;
            for (int i = 0; i <= lastX - firstX; i++) {
                if (discs[firstY+i][firstX+i].getPlayerID() == playerID) {
                    nrOfDiscs++;
                } else if (nrOfDiscs < 4) {
                    nrOfDiscs = 0;
                    firstX = firstX + i + 1;
                    firstY = firstY + i + 1;
                }
            }
            if (nrOfDiscs > 3) {
                lastX = firstX + nrOfDiscs - 1;
                lastY = firstY + nrOfDiscs - 1;
                return true;
            }
        }
        return false;
    }

    private boolean isWinningLineSecondaryDiagonal() {
        if (lastDroppedDiscX > 2 && lastDroppedDiscY < 3) {
            firstX = lastDroppedDiscX - 3;
            firstY = lastDroppedDiscY + 3;
        } else {
            int offset = Math.min(lastDroppedDiscX, 5 - lastDroppedDiscY);
            firstX = lastDroppedDiscX - offset;
            firstY = lastDroppedDiscY + offset;
        }
        if (lastDroppedDiscX < 4 && lastDroppedDiscY > 3) {
            lastX = lastDroppedDiscX + 3;
            lastY = lastDroppedDiscY - 3;
        } else {
            int offset = Math.min(6 - lastDroppedDiscX, lastDroppedDiscY);
            lastX = lastDroppedDiscX + offset;
            lastY = lastDroppedDiscY - offset;
        }

        if (lastX - firstX > 2) {
            int nrOfDiscs = 0;
            for (int i = 0; i <= lastX - firstX; i++) {
                if (discs[firstY-i][firstX+i].getPlayerID() == playerID) {
                    nrOfDiscs++;
                } else if (nrOfDiscs < 4) {
                    nrOfDiscs = 0;
                    firstX = firstX + i + 1;
                    firstY = firstY + i - 1;
                }
            }
            if (nrOfDiscs > 3) {
                lastX = firstX + nrOfDiscs - 1;
                lastY = firstY - nrOfDiscs + 1;
                return true;
            }
        }
        return false;
    }

    private boolean isDraw() {
        //If every cell is filled but there is no winner -> Draw
        boolean hasNoEmptyCells = true;
        for (int i = 0; i < COLUMNS; i++) {
            if (lowestPosition[i] >= 0)
                hasNoEmptyCells = false;
        }
        return hasNoEmptyCells;
    }

    private void endGame() {
        running = false;
        if (draw) {
            System.out.println("Draw!");
        } else
            System.out.println("Player " + playerID + " won!");
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

        if (!running)
            drawWinningRowLine(g2d);
    }

    private void drawDiscs(Graphics2D g2d) {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
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

    private void drawWinningRowLine(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine((int)(firstX * cellWidth + (cellWidth / 2)), (int)(firstY * cellHeight + yOffset + (cellHeight / 2)), (int)(lastX * cellWidth + (cellWidth / 2)), (int)(lastY * cellHeight + yOffset + (cellHeight / 2)));
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
