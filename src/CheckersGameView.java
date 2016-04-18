import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * CheckersGameView.java
 * <p>GUI view class for checkers game. Creates JApplet and adds info, board and drag panels to it along with the menu.</p>
 *
 * @author Francis Poole
 * @version 1.0, 04/01/15
 */
public class CheckersGameView extends JApplet {

    private CheckersGameModel gameObject; //Game object
    private DragJPanel dragPanel; //Drag and drop panel
    private BoardJPanel boardPanel; //Board panel
    private InfoJPanel infoPanel; //Information panel

    /**
     * Initalise Applet
     */
    @Override
    public void init() {
        this.gameObject = new CheckersGameModel();
        JLayeredPane layeredPane = new JLayeredPane();
        this.boardPanel = new BoardJPanel(this.gameObject);
        this.infoPanel = new InfoJPanel();
        this.dragPanel = new DragJPanel(this.gameObject, this.infoPanel);

        this.initMenu();

        this.setPreferredSize(new Dimension(800, 900));
        layeredPane.setBounds(0, 0, 800, 800);
        this.dragPanel.setBounds(0, 0, 800, 800);
        this.boardPanel.setBounds(0, 0, 800, 800);
        this.infoPanel.setPreferredSize(new Dimension(800, 60));

        this.dragPanel.setOpaque(false);
        this.boardPanel.setOpaque(false);
        this.add(layeredPane,BorderLayout.CENTER);
        this.add(this.infoPanel, BorderLayout.PAGE_END);
        layeredPane.add(this.dragPanel);
        layeredPane.add(this.boardPanel);
    }

    /**
     * Initalise the menu bar
     */
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.setMnemonic(KeyEvent.VK_D);
        JMenu helpMenu = new JMenu("Help");
        fileMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);

        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                CheckersGameView.this.gameObject = new CheckersGameModel();
                CheckersGameView.this.boardPanel.setGame(CheckersGameView.this.gameObject);
                CheckersGameView.this.dragPanel.setGame(CheckersGameView.this.gameObject);
                CheckersGameView.this.boardPanel.repaint();
                CheckersGameView.this.infoPanel.setError("Welcome to checkers! Player 1's turn");
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JRadioButtonMenuItem easyMenuItem = new JRadioButtonMenuItem("Easy");
        JRadioButtonMenuItem mediumMenuItem = new JRadioButtonMenuItem("Medium");
        JRadioButtonMenuItem hardMenuItem = new JRadioButtonMenuItem("Hard (Slow)");

        easyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                CheckersGameView.this.gameObject.setLevelLimit(4);
            }
        });
        mediumMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                CheckersGameView.this.gameObject.setLevelLimit(8);
            }
        });
        hardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                CheckersGameView.this.gameObject.setLevelLimit(10);
            }
        });

        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(new JFrame(), "Rules of Draughts (Checkers in the US).\n" +
                                "Adapted from 'http://www.draughts.org/how-to-play-draughts.html'\n" +
                                "\n" +
                                "In the US, the game of Draughts is referred to as Checkers, a game which has\n" +
                                "become very popular with players of all ages and from all different lifestyles.\n" +
                                "It has become known as a family game, but there are some players who take the\n" +
                                "game very seriously.\n" +
                                "\n" +
                                "The game is played on a 64 square checkerboard with eight rows of alternating\n" +
                                "dark and light colored squares.\n" +
                                "\n" +
                                "There are two players and each player will begin the game with 12 draughts, each\n" +
                                "player having their own colour. The players will place their draughts on the three\n" +
                                "rows of white squares which are closest to them. The players will then begin playing,\n" +
                                "making one move at a time by clicking and dragging one of their pieces to a valid square.\n" +
                                "\n" +
                                "The object of the game is to take all the opponents pieces. The normal draughts can only\n" +
                                "move in a forward diagonal direction into a square without another piece in it. If an\n" +
                                "opponent’s piece is in an adjacent square, the player can jump it and capture it, removing the\n" +
                                "piece from the board. They can only do this if the next square is empty. Players can never\n" +
                                "jump over their own piece.\n" +
                                "\n" +
                                "When a player makes their way all the way across the board to the other player’s side,\n" +
                                "their piece will be turned into a “King.” When this happens, a star will appear on\n" +
                                "the piece. Once a piece is made into a king, it will be able to move forwards and backwards,\n" +
                                "giving it more chance to capture the opponents pieces.\n" +
                                "\n" +
                                "Pieces can jump as many times as they are able to with regards to the necessary squares\n" +
                                "being unoccupied. However, they cannot jump over pieces which are the same color as them.\n" +
                                "\n" +
                                "As previously mentioned, the game will come to an end once a player has no pieces left. A\n" +
                                "new game can then be started from the file menu.\n" +
                                "\n" +
                                "If you wish to increase the difficulty of the game you can set it to easy, medium or hard at\n" +
                                "any point. Please note that early AI moves in hard difficulty can take up to 10 seconds to\n" +
                                "process.\n" +
                                "INFO ABOUT DATE MADE AND CREATOR NAME ETC",
                        "Help",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });


        ButtonGroup difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyMenuItem);
        difficultyGroup.add(mediumMenuItem);
        difficultyGroup.add(hardMenuItem);
        easyMenuItem.setSelected(true);

        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(difficultyMenu);
        menuBar.add(helpMenu);

        fileMenu.add(newMenuItem);
        fileMenu.add(exitMenuItem);
        difficultyMenu.add(easyMenuItem);
        difficultyMenu.add(mediumMenuItem);
        difficultyMenu.add(hardMenuItem);
        helpMenu.add(helpMenuItem);
    }
}