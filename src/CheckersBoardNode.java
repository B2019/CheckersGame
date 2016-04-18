/**
 * CheckersBoardNode.java
 *  Board node class for checkers. Contains board and parent board, handling all actions related to them.
 *
 * @author Francis Poole
 * @version 1.0, 04/01/15
 */
public class CheckersBoardNode {

    private final int[] board; //0 = empty, 1 = w, 2 = b, 3 = W, 4 = B
    private CheckersBoardNode parentNode; //Nodes parent in the AI's search tree
    private int player; //Players turn for the board
    private int jumpingSquare; //Multi move jumping square
    private int level; //Level in AI search tree
    private int score; //Boards score
    private boolean scored; //Whether the board has been scored
    private CheckersBoardNode childNode; //Child node to get to best terminal node
    private boolean expanded; //Whether the node has been expanded in the search tree

    /**
     * Checkers board node constructor
     * <p>
     * Sets up a new checkers board node
     * <p>
     */
    public CheckersBoardNode()
    {
        this.board = new int[32];
        this.player = 1;
        this.initBoard();
        this.jumpingSquare = -1;
        this.level = 0;
        this.expanded = false;
        this.scored = false;
    }

    /**
     * Checkers board node constructor 2
     * <p>
     * Sets up a new checkers board node with some given parent node
     * <p>
     *
     * @param parentNode Node to add as parent
     */
    public CheckersBoardNode(CheckersBoardNode parentNode)
    {
        this.player = 1;
        this.parentNode = parentNode;
        this.board = new int[32];
        for (int i = 0; i <= 31; i++) {
            this.board[i] = parentNode.getSquare(i);
        }
        this.jumpingSquare = -1;
        this.level = 0;
        this.expanded = false;
        this.scored = false;


    }

    /**
     * Initalise board's pieces
     * <p>
     * Fills board with 12 white and 12 black pieces in the correct positions
     * <p>
     */
    public void initBoard() {
        //Setup white checkers
        for(int i = 0; i <= 11; i++ ){
            this.board[i] = 1;
        }
        //Setup black checkers
        for(int i = 20; i <= 31; i++ ){
            this.board[i] = 2;
        }
    }

    /**
     * Get nodes player
     *
     * @return Player for the board
     */
    public int getPlayer() {
        return this.player;
    }

    /**
     * Set nodes player to player
     *
     * @param player to be set to
     */
    public void setPlayer(int player) {
        this.player = player;
    }

    /**
     * Get contents of square in nodes board
     *
     * @param square to get from
     * @return Piece in square
     */
    public int getSquare(int square) {
        return this.board[square];
    }

    /**
     * Get nodes player
     *
     * @param square to be set
     * @param checker to set square to
     */
    public void setSquare(int square, int checker) {
        //Check if turned to king
        if (square >= 30 && checker == 1) {
            checker = 3;
        } else if (square <= 3 && checker == 2) {
            checker = 4;
        }
        //set square to checker
        this.board[square] = checker;
    }

    /**
     * Get nodes parent node
     *
     * @return Parent node
     */
    public CheckersBoardNode getParentNode() {
        return this.parentNode;
    }

    /**
     * Set nodes parent node
     *
     * @param parentNode to be set
     */
    public void setParentNode(CheckersBoardNode parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * Get nodes board
     *
     * @return Board
     */
    public int[] getBoard() {
        return this.board;
    }

    /**
     * Print out board
     *
     * @depricated
     */
    public void printBoard() {
        for (int i = 0; i <= 31; i++) {
            if (i % 4 == 0 && i > 0) {
                System.out.println("|");// print end slash
            }
            if (i % 8 < 4) {
                System.out.print("|.");
            }
            if (this.board[i] == 0) {
                System.out.print("| ");
            } else if (this.board[i] == 1) {
                System.out.print("|w");
            } else if (this.board[i] == 2) {
                System.out.print("|b");
            } else if (this.board[i] == 3) {
                System.out.print("|W");
            } else if (this.board[i] == 4) {
                System.out.print("|B");
            } else {
                System.out.println("Error: Incorrect checker number!");
            }
            if (i % 8 > 3) {
                System.out.print("|.");
            }
        }
        System.out.println("|");
        System.out.println("");
    }

    /**
     * Get number of white checkers on board
     *
     * @return number of white checkers on board
     */
    public int getWhiteCheckers() {
        int whiteCheckers = 0;
        for (int i = 0; i <= 31; i++) {
            if (this.board[i] == 1 || this.board[i] == 3) {
                whiteCheckers++;
            }
        }
        return whiteCheckers;
    }

    /**
     * Get number of black checkers on board
     *
     * @return number of black checkers on board
     */
    public int getBlackCheckers() {
        int blackCheckers = 0;
        for (int i = 0; i <= 31; i++) {
            if (this.board[i] == 2 || this.board[i] == 4) {
                blackCheckers++;
            }
        }
        return blackCheckers;
    }

    /**
     * Get node's jumping square
     *
     * @return jumping square
     */
    public int getJumpingSquare() {
        return this.jumpingSquare;
    }

    /**
     * Set node's jumping square
     *
     * @param jumpingSquare value to be set to
     */
    public void setJumpingSquare(int jumpingSquare) {
        this.jumpingSquare = jumpingSquare;
    }

    /**
     * Get static evaluation of node
     *
     * @return A static evaluation of the node
     */
    public int getStaticEvaluation() {
        return this.getBlackCheckers() - this.getWhiteCheckers();
    }

    /**
     * Get node's level in the search tree
     *
     * @return level in search tree
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Set node's level in the search tree
     *
     * @param level node is in search tree
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Backup score and node if the score is smaller than the current score
     *
     * @param score to backup
     * @param childNode to backup
     */
    public void backupMin (int score, CheckersBoardNode childNode) {
        if (!this.scored) {
            this.score = score;
            this.scored = true;
            this.childNode = childNode;
        } else {
            if (this.score > score) {
                this.score = score;
                this.childNode = childNode;
            }
        }
    }

    /**
     * Backup score and node if the score is larger than the current score
     *
     * @param score to backup
     * @param childNode to backup
     */
    public void backupMax (int score, CheckersBoardNode childNode) {
        if (!this.scored) {
            this.score = score;
            this.scored = true;
            this.childNode = childNode;
        } else {
            if (this.score < score) {
                this.score = score;
                this.childNode = childNode;
            }
        }
    }

    /**
     * Get node's child
     *
     * @return child
     */
    public CheckersBoardNode getChildNode() {
        return this.childNode;
    }

    /**
     * Change if node is expanded or not
     *
     * @param expanded Whether node is expanded or not
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * Get if node is exanded or not
     *
     * @return if node is expanded
     */
    public boolean isExpanded() {
        return this.expanded;
    }

    /**
     * Get node's score
     *
     * @return nodes score
     */
    public int getScore() {
        return this.score;
    }
}
