import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * BoardJPanel.java
 *  Board panel class for checkers GUI. Shows state of board in game class, filling it with the correct pieces.
 *
 * @author Francis Poole
 * @version 1.0, 04/01/15
 */
public class BoardJPanel extends JPanel {

    private final int gridSize; //Grid size of board
    private CheckersGameModel game; //Game to show
    private final double[][] starPoints = {
            { 0, 16 }, { 15, 15 }, { 20, 2 }, { 25, 15 },
            { 40, 16 }, { 30, 25 }, { 32, 38 }, { 20, 30 },
            { 8, 38 }, { 10, 25 }, { 0, 16 }
    }; //Kings star points for drawing

    /**
     * Board panel constructor
     *
     * @param game to show in board
     */
    public BoardJPanel(CheckersGameModel game) {
        this.game = game;
        this.gridSize = 8;
    }

    /**
     * Paint board
     *
     * @param g graphics of gui
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        CheckersBoardNode board = this.game.getBoardNode();
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                //Check if white
                if ((i + j) % 2 != 0) {
                    //Draw white square
                    g2d.setColor(Color.white);
                    g2d.fillRect(j * 100, i * 100, j * 100 + 100, i * 100 + 100);

                    int squareNo = i * 4 + j / 2;

                    if (board.getSquare(squareNo) == 1) {
                        //White Checker
                        g2d.setColor(Color.white);
                        g2d.fillOval(j*100+10,i*100+10,80,80);
                        g2d.setColor(Color.black);
                        g2d.drawOval(j*100+10,i*100+10,80,80);
                        g2d.drawOval(j*100+15,i*100+15,70,70);

                    } else if (board.getSquare(squareNo) == 2) {
                        //Black Checker
                        g2d.setColor(Color.gray);
                        g2d.fillOval(j*100+10,i*100+10,80,80);
                        g2d.setColor(Color.black);
                        g2d.drawOval(j * 100 + 10, i * 100 + 10, 80, 80);
                        g2d.drawOval(j * 100 + 15, i * 100 + 15, 70, 70);

                    } else if (board.getSquare(squareNo) == 3) {

                        //White Checker King
                        g2d.setColor(Color.white);
                        g2d.fillOval(j*100+10,i*100+10,80,80);
                        g2d.setColor(Color.black);
                        g2d.drawOval(j*100+10,i*100+10,80,80);
                        g2d.drawOval(j*100+15,i*100+15,70,70);

                        //Kings star
                        g2d.setColor(Color.gray);
                        GeneralPath star = new GeneralPath();
                        star.moveTo(this.starPoints[0][0]+ j*100 +30, this.starPoints[0][1]+ i*100 +30);
                        for (int k = 1; k < this.starPoints.length; k++) {
                            star.lineTo(this.starPoints[k][0]+ j*100 +30, this.starPoints[k][1]+ i*100 +30);
                        }
                        star.closePath();
                        g2d.fill(star);


                    } else if (board.getSquare(squareNo) == 4) {
                        //Black Checker King
                        g2d.setColor(Color.gray);
                        g2d.fillOval(j*100+10,i*100+10,80,80);
                        g2d.setColor(Color.black);
                        g2d.drawOval(j * 100 + 10, i * 100 + 10, 80, 80);
                        g2d.drawOval(j * 100 + 15, i * 100 + 15, 70, 70);

                        //Kings star
                        g2d.setColor(Color.white);
                        GeneralPath star = new GeneralPath();
                        star.moveTo(this.starPoints[0][0]+ j*100 +30, this.starPoints[0][1]+ i*100 +30);
                        for (int k = 1; k < this.starPoints.length; k++) {
                            star.lineTo(this.starPoints[k][0] + j * 100 + 30, this.starPoints[k][1] + i * 100 + 30);
                        }
                        star.closePath();
                        g2d.fill(star);
                    }
                } else {
                    //Black square
                    g2d.setColor(Color.black);
                    g2d.fillRect(j*100,i*100, j*100 +100, i*100 +100);
                }
            }
        }
    }

    /**
     * Set game
     *
     * @param game to set to
     */
    public void setGame(CheckersGameModel game) {
        this.game = game;
    }
}