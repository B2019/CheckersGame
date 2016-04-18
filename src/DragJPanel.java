import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.lang.Thread;
import javax.swing.*;

/**
 * DragJPanel.java
 *  Drag panel class for checkers GUI. Handles drag and drop in GUI.
 *
 * @author Francis Poole
 * @version 1.0, 04/01/15
 */
public class DragJPanel extends JPanel implements MouseListener, MouseMotionListener {

    private CheckersGameModel game; //Game to use
    private int square; //Square to move from
    private int square2; //Square to move to
    private final InfoJPanel infoPanel; //Information panel
    private boolean visible; //If draggable piece is visable

    private int checkerX; //draggable pieces X coord
    private int checkerY;//draggable pieces Y coord
    private int squareX;//draggable pieces origin squares X coord
    private int squareY;//draggable pieces origin squares X coord

    private final double[][] starPoints = {
            { 0, 16 }, { 15, 15 }, { 20, 2 }, { 25, 15 },
            { 40, 16 }, { 30, 25 }, { 32, 38 }, { 20, 30 },
            { 8, 38 }, { 10, 25 }, { 0, 16 }
    }; //Kings star points for drawing


    /**
     * Drag and drop panel constructor
     *
     * @param game to use
     * @param infoPanel to use
     */
    public DragJPanel(CheckersGameModel game, InfoJPanel infoPanel) {
        this.game = game;
        this.infoPanel = infoPanel;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    /**
     * Paint a covering square on top of the moving pieces square and draw a checker at the mouse pointer
     *
     * @param g grahics to use
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.visible) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            //Draw square hider
            g2d.setColor(Color.white);

            g2d.fillRect(this.squareX *100, this.squareY *100,100,100);

            //Draw checker
            g2d.setColor(Color.white);
            g2d.fillOval(this.checkerX +10, this.checkerY +10, 80, 80);
            g2d.setColor(Color.black);
            g2d.drawOval(this.checkerX +10, this.checkerY +10, 80, 80);
            g2d.drawOval(this.checkerX +15, this.checkerY +15, 70, 70);

            if (this.game.getBoardNode().getSquare(this.square) == 3) { //Add kings star
                g2d.setColor(Color.gray);
                GeneralPath star = new GeneralPath();
                star.moveTo(this.starPoints[0][0] + this.checkerX + 30, this.starPoints[0][1] + this.checkerY + 30);
                for (int k = 1; k < this.starPoints.length; k++) {
                    star.lineTo(this.starPoints[k][0] + this.checkerX + 30, this.starPoints[k][1] + this.checkerY + 30);
                }
                star.closePath();
                g2d.fill(star);
            }
        }
    }

    /**
     * Picking up the piece
     * <p>
     * Gets selected checker from the board and picks it up if it is selectable
     * <p>
     *
     * @param e Mouse click event
     */
    public void mousePressed(MouseEvent e) {

        if (this.game.getBoardNode().getPlayer() == 1 && this.game.winner() == 0) {
            //Get mouse position
            int x = e.getX();
            int y = e.getY();


            //Get square
            this.squareX = (int)Math.floor(x/100);
            this.squareY = (int)Math.floor(y/100);

            //Check is not black square
            if ((this.squareX + this.squareY) % 2 != 0) {
                this.square = this.squareY * 4 + this.squareX / 2; //Calculate square number
                //Check is white checker and is jumping square if there is one
                if (this.game.getBoardNode().getSquare(this.square) == 1 || this.game.getBoardNode().getSquare(this.square) == 3) {
                    if (this.game.getBoardNode().getJumpingSquare() == -1 || this.game.getBoardNode().getJumpingSquare() == this.square) { //Ensures same piece multijumps
                        //Paint draggable checker
                        this.checkerX = x - 50;
                        this.checkerY = y - 50;
                        this.visible = true;
                    }
                }
            }
        }
    }

    /**
     * Moving the piece
     * <p>
     * Moves selected checker around the board
     * <p>
     *
     * @param e Mouse drag event
     */
    public void mouseDragged(MouseEvent e) {
        if (this.visible) {

            this.checkerX = e.getX()-50;
            this.checkerY = e.getY()-50;

            //Checker wont leave board
            this.checkerX = Math.max(this.checkerX, 0);
            this.checkerY = Math.max(this.checkerY, 0);
            this.checkerX = Math.min(this.checkerX, this.getWidth() - 100);
            this.checkerY = Math.min(this.checkerY, this.getHeight() - 100);

            this.repaint();
        }
    }

    /**
     * Places the piece
     * <p>
     * Attempts to place selected checker at the mouse position, showing error message if it fails. Then checks if game
     * has been won, if not then it get the AI to make a move.
     * <p>
     *
     * @param e Mouse click event
     */
    public void mouseReleased(MouseEvent e) {
        if (this.visible) {
            //Get mouse position
            int x = e.getX();
            int y = e.getY();

            //Get square2 X and Y
            int square2X = (int) Math.floor(x / 100);
            int square2Y = (int) Math.floor(y / 100);
            if ((square2X + square2Y) % 2 != 0 && square2X >= 0 && square2Y >= 0 && square2X <= 7 && square2Y <= 7) { //Is non black square
                this.square2 = square2Y * 4 + square2X / 2; //Calculate square2 number
                if (this.square2 >= 0 && this.square2 <= 31) {
                    String errorMsg = this.game.moveUsersChecker(this.square, this.square2); //Attempt to move user checker, returning error msg
                    this.infoPanel.setError(errorMsg); //Show error message
                }
            }
            this.visible = false;
            this.repaint();
            //Check if user won
            if (this.game.winner() != 0) {
                this.infoPanel.setError("You win! Game has been won by player " + this.game.winner() + "!");
                return;
            }
            if (this.game.getBoardNode().getPlayer() == 2) {
                this.infoPanel.setError("Player 2's turn!");
                Thread thread = new Thread() {
                    @Override
                    public void run(){
                        while (DragJPanel.this.game.getBoardNode().getPlayer() == 2) {
                            do {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                //While jumping keep running AI and updating board
                                DragJPanel.this.game.runAI();
                                DragJPanel.this.repaint();
                            } while (DragJPanel.this.game.getBoardNode().getJumpingSquare() != -1);
                            //Check if AI won
                            if (DragJPanel.this.game.winner() != 0) {
                                DragJPanel.this.infoPanel.setError("You lose! Game has been won by player " + DragJPanel.this.game.winner() + "!");
                                return;
                            }
                        }
                        DragJPanel.this.infoPanel.setError("Player 1's turn!");

                    }

                };
                thread.start();

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

    /**
     * unused mouse events
     * @param e some unused mouse event
     */
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}

}