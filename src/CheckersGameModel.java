/** TODO
 * Read through and check commenting
 * Make into Model-View-Controller
 * Center board and make sort out window size problems
 * Import help text from a file
 * check overides
 * on player 2 cant move then stop from moving player 1!
 * add difficulty values
 * make getters and setters
 */



import java.util.Arrays;
import java.util.Vector;
import java.util.Stack;

/**
 * CheckersGameModel.java
 * Game model class for checkers. Contains board node that is in play and handles user moves along with AI moves (using
 * the get successor function).
 *
 * @author  Francis Poole
 * @version 1.0, 04/01/15
 */
public class CheckersGameModel {

    private CheckersBoardNode boardNode; //Games current board node
    private int levelLimit; //Depth the AI's search tree can go to (difficulty)

    /**
     * Set up a new game with player 1 as starting player and difficulty of 4
     */
    public CheckersGameModel() {
        this.boardNode = new CheckersBoardNode();
        this.boardNode.setPlayer(1);
        this.levelLimit = 4;
    }

    /**
     * Get board node's successors by looping through all squares in board. Only if there are no jump successors in the board
     * will it check for move successors. This ensures takes. If player cannot move it skips the
     * players turn.
     *
     * @param   parentBoardNode The board node from which to get successors
     * @return  Successors of the parentBoardNode
     */
    public Vector<CheckersBoardNode> getSuccessors(CheckersBoardNode parentBoardNode) {
        Vector<CheckersBoardNode> successors = new Vector<CheckersBoardNode>();

        //Jumps
        for (int square = 0; square <= 31; square++) { //Loop through all squares
            successors.addAll(this.getSquaresJumpSuccessors(parentBoardNode, square));
        }
        //Moves
        if (successors.isEmpty()) {
            for (int square = 0; square <= 31; square++) { //Loop through all squares
                successors.addAll(this.getSquaresMoveSuccessors(parentBoardNode, square));
            }
        }
        if (successors.size() == 0) { //Player cannot move
            if (parentBoardNode.getPlayer() == 1) {
                parentBoardNode.setPlayer(2);
            } else {
                parentBoardNode.setPlayer(1);
            }
            successors = this.getSuccessors(parentBoardNode);
        }
        return successors;
    }

    /**
     * Get square's jump successors by checking possible jumps that a piece at some square can take, returning any that are found.
     *
     * @param   parentBoardNode The board node from which to get jump successors
     * @param   square The square in the board to check for jump successors
     * @return  Jump successors of the parentBoardNode at square
     */
    public Vector<CheckersBoardNode> getSquaresJumpSuccessors(CheckersBoardNode parentBoardNode, int square) {
        Vector<CheckersBoardNode> successors = new Vector<CheckersBoardNode>();
        CheckersBoardNode successor;
        int squareJumped;
        int square2;
        if (parentBoardNode.getSquare(square) == parentBoardNode.getPlayer() || parentBoardNode.getSquare(square) == parentBoardNode.getPlayer() + 2) { //If square contains players checker
            //Jump NW
            squareJumped = square - 4 - square % 8 / 4;
            square2 = squareJumped - 4 - squareJumped % 8 / 4;
            if (square % 8 != 4 && square % 8 != 0 &&
                    squareJumped >= 0 && squareJumped <= 31 && square2 >= 0 && square2 <= 31) { //Check is inside board
                successor = this.getJumpSuccessor(square, squareJumped, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }

            //Jump NE
            squareJumped = square - 3 - square % 8 / 4;
            square2 = squareJumped - 3 - squareJumped % 8 / 4;
            if (square % 8 != 3 && square % 8 != 7 && squareJumped >= 0 && squareJumped <= 31 && square2 >= 0 && square2 <= 31) {
                successor = this.getJumpSuccessor(square, squareJumped, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }

            //Jump SE
            squareJumped = square + 5 - square % 8 / 4;
            square2 = squareJumped + 5 - squareJumped % 8 / 4;
            if (square % 8 != 3 && square % 8 != 7 && squareJumped >= 0 && squareJumped <= 31 && square2 >= 0 && square2 <= 31) {
                successor = this.getJumpSuccessor(square, squareJumped, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }

            //Jump SW
            squareJumped = square + 4 - square % 8 / 4;
            square2 = squareJumped + 4 - squareJumped % 8 / 4;
            if (square % 8 != 4 && square % 8 != 0 && squareJumped >= 0 && squareJumped <= 31 && square2 >= 0 && square2 <= 31) {
                successor = this.getJumpSuccessor(square, squareJumped, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }
        }
        return successors;
    }

    /**
     * Get square's move successors by checking possible moves that a piece at some square can take, returning any that are found.
     * <p>
     *
     * @param  parentBoardNode The board node from which to get move successors
     * @param  square The square in the board to check for move successors
     * @return Move successors of the parentBoardNode at square
     */
    public Vector<CheckersBoardNode> getSquaresMoveSuccessors(CheckersBoardNode parentBoardNode, int square) {
        Vector<CheckersBoardNode> successors = new Vector<CheckersBoardNode>();
        CheckersBoardNode successor;
        int square2;

        if (parentBoardNode.getSquare(square) == parentBoardNode.getPlayer() && parentBoardNode.getPlayer() == 2 || parentBoardNode.getSquare(square) == parentBoardNode.getPlayer() + 2) { //If square contains player 1's king checker or any player 2 checker
            //Move NW
            square2 = square - 4 - square % 8 / 4;
            if (square % 8 != 4 && square2 >= 0 && square2 <= 31) {
                successor = this.getMoveSuccessor(square, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }
            //Move NE
            square2 = square - 3 - square % 8 / 4;
            if (square % 8 != 3 && square2 >= 0 && square2 <= 31) {
                successor = this.getMoveSuccessor(square, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }
        }
        if (parentBoardNode.getSquare(square) == parentBoardNode.getPlayer() && parentBoardNode.getPlayer() == 1 || parentBoardNode.getSquare(square) == parentBoardNode.getPlayer() + 2) { //If square contains player 2's king checker or any player 1 checker
            //Move SE
            square2 = square + 5 - square % 8 / 4;
            if (square % 8 != 3 && square2 >= 0 && square2 <= 31) {
                successor = this.getMoveSuccessor(square, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }
            //SW
            square2 = square + 4 - square % 8 / 4;
            if (square % 8 != 4 && square2 >= 0 && square2 <= 31) {
                successor = this.getMoveSuccessor(square, square2, parentBoardNode);
                if (successor != null) {
                    successors.add(successor);
                }
            }
        }
        return successors;
    }

    /**
     * Get square's jump successor
     * <p>
     * Checks for a possible jump that some piece at some square can take to some other square over some jumped square,
     * returning it if found. Also flags the successor if there is a possible multi jump detected as well as ensures
     * multi jumps are performed by the same piece
     * <p>
     *
     * @param  square The square in the board to check for the jump successor
     * @param  squareJumped The square in the board that is jumped
     * @param  square2 The square in the board that is jumped to
     * @param  parentBoardNode The board node from which to get the successor
     * @return Jump successor of the parentBoardNode at square, given some squareJumped and square2
     */
    public CheckersBoardNode getJumpSuccessor(int square, int squareJumped, int square2, CheckersBoardNode parentBoardNode) {

        if (parentBoardNode.getSquare(square2) == 0 //Square2 is empty
                && parentBoardNode.getSquare(squareJumped) != 0 //Jumped square is not empty
                && parentBoardNode.getSquare(squareJumped) != parentBoardNode.getPlayer() && parentBoardNode.getSquare(squareJumped) != parentBoardNode.getPlayer() + 2) { //Jumped square is not same player

            if (parentBoardNode.getJumpingSquare() == -1 || parentBoardNode.getJumpingSquare() == square) {
                CheckersBoardNode successor = new CheckersBoardNode(parentBoardNode); //Create successor

                successor.setSquare(squareJumped, 0);
                //Check if kinged
                if (parentBoardNode.getPlayer() == 1 && square2 >= 28) {
                    successor.setSquare(square2, 3);
                } else if (parentBoardNode.getPlayer() == 2 && square2 <= 3) {
                    successor.setSquare(square2, 4);
                } else {
                    //Not kinged
                    successor.setSquare(square2, parentBoardNode.getSquare(square));
                }
                successor.setSquare(square, 0);

                //Check for multijump!
                successor.setPlayer(parentBoardNode.getPlayer()); //Temporarly set successors player to same as its parent
                Vector<CheckersBoardNode> successors2 = this.getSquaresJumpSuccessors(successor, square2); //Get possible second jump successors of the successor

                if (successors2.size() > 0) {
                    //Board is a jumping board!
                    successor.setJumpingSquare(square2);
                } else if (successor.getPlayer() == 1) {
                    //Change player back to 2
                    successor.setPlayer(2);
                } else if (successor.getPlayer() == 2) {
                    //Change player back to 1
                    successor.setPlayer(1);
                }
                return successor;
            }
        }
        return null;
    }

    /**
     * Get square's move successor
     * <p>
     * Checks for a possible move that some piece at some square can take to some other square, returning it if found.
     * <p>
     *
     * @param  square The square in the board to check for the move successor
     * @param  square2 The square in the board that is moved to
     * @param  parentBoardNode The board node from which to get the successor
     * @return Move successor of the parentBoardNode at square, given some square2
     */
    private CheckersBoardNode getMoveSuccessor(int square, int square2, CheckersBoardNode parentBoardNode) {
        if (parentBoardNode.getSquare(square2) == 0) { //Square is empty
            CheckersBoardNode successor = new CheckersBoardNode(parentBoardNode);
            if (parentBoardNode.getPlayer() == 1 && square2 >= 28) { //Check if king
                successor.setSquare(square2, 3);
            } else if (parentBoardNode.getPlayer() == 2 && square2 <= 3){
                successor.setSquare(square2, 4);
            } else {
                successor.setSquare(square2, parentBoardNode.getSquare(square));
            }
            successor.setSquare(square, 0);
            successor.setParentNode(parentBoardNode);
            return successor;
        }
        return null;
    }

    /**
     * Attempt to move/jump the users checker on the games board
     * <p>
     * Attempts to move/jump the users checker from one square to another, checking first if the move/jump is valid
     * using the get successors function above, returning an error message if it isn't.
     * <p>
     *
     * @param  square The square in the board to check for the jump successor
     * @param  square2 The square in the board that is jumped to
     * @return Error message if there is one
     */
    public String moveUsersChecker(int square, int square2) {
        if (this.boardNode.getSquare(square2) != 0) {
            return "Must move to empty space!";
        }
        //Test against successors
        Vector<CheckersBoardNode> successors = this.getSuccessors(this.boardNode);
        for (int i = 0; i < successors.size(); i++) {
            //Compare boards
            if (successors.get(i).getSquare(square) == 0 &&
                    (successors.get(i).getSquare(square2) == 1 || successors.get(i).getSquare(square2) == 3)) {
                //Move
                this.boardNode = successors.get(i);

                //Check successor is not a multi jump move
                if (successors.get(i).getJumpingSquare() == -1) {
                    successors.get(i).setPlayer(2); //Set to player 2s turn
                }
                return null;
            }
        }
        //Generate Error message
        if (successors.get(0).getBlackCheckers() != this.boardNode.getBlackCheckers()) {
            return "Must take!";
        } else if (this.boardNode.getSquare(square) == 1 && square2 < square) {
            return "Non-king checkers can only move down the board!";
        } else {
            return "Invalid move!";
        }
    }

    /**
     * Get the games current board node
     *
     * @return The games boardNode
     */
    public CheckersBoardNode getBoardNode() {
        return this.boardNode;
    }

    /**
     * Runs the AI's turn
     * <p>
     * Uses a stack to implement a search tree of possible moves up to some level. Minimax has been implemented to
     * push the best possible move the AI can do to the top. Alpha beta pruning is also implemented to allow for larger
     * trees to be built.
     * <p>
     * More detail on exact implementation can be seen in the comments below
     */
    public void runAI() {
        //Minimax search
        this.boardNode.setLevel(0); //Set root node to level 0
        Stack<CheckersBoardNode> stack = new Stack<CheckersBoardNode>(); //Search space
        Vector<int[]> seenBoards = new Vector<int[]>(); //Seen boards

        stack.add(this.boardNode);   //Add first board to stack

        while (stack.size() > 0) {
            if (stack.peek().getLevel() < this.levelLimit) { //Not reached level
                CheckersBoardNode poppedBoardNode = stack.pop();

                if (!poppedBoardNode.isExpanded()) { //Not been expanded

                    Vector<CheckersBoardNode> successors = this.getSuccessors(poppedBoardNode); //Get successor boards
                    if (successors.size() > 0) {//Has successor
                        poppedBoardNode.setExpanded(true);
                        stack.push(poppedBoardNode);
                        for (int j = 0; j < successors.size(); j++) { //Loop through successors

                            //Check if board has been seen before
                            boolean seen = false;
                            for (int k = 0; k < seenBoards.size(); k++) { //Loops through seen boards
                                if (Arrays.equals(seenBoards.get(k), successors.get(j).getBoard())) { //Check if boards are equal
                                    seen = true;
                                    break;
                                }
                            }
                            if (!seen) {
                                successors.get(j).setLevel(poppedBoardNode.getLevel() + 1); //Set successors levels
                                stack.push(successors.get(j)); //Push board onto stack
                                seenBoards.add(successors.get(j).getBoard()); //Add board to seen boards
                            }
                        }
                    } else { //No successors
                        CheckersBoardNode parentBoard = poppedBoardNode.getParentNode();
                        while (parentBoard != poppedBoardNode.getParentNode()) {
                            parentBoard = parentBoard.getParentNode();
                        }
                    }
                } else { //Already been expanded
                    int score = poppedBoardNode.getScore();
                    if (poppedBoardNode.getLevel()%2 == 0) {
                        //AI move
                        //push back min
                        poppedBoardNode.getParentNode().backupMin(score, poppedBoardNode);
                        //Beta pruning
                        if (poppedBoardNode.getLevel() >= 3 && //Is at or below level 3
                                poppedBoardNode.getParentNode().getParentNode().getScore() <= poppedBoardNode.getScore()) { //Can be pruned
                            //Pop of rest of level from stack
                            while (stack.peek().getLevel() == poppedBoardNode.getLevel()) {
                                stack.pop();
                            }
                        }
                    } else {
                        //User move
                        //push back max
                        poppedBoardNode.getParentNode().backupMax(score, poppedBoardNode);
                        //Alpha pruning
                        if (poppedBoardNode.getLevel() >= 2 && //Is at or below level 2
                                poppedBoardNode.getParentNode().getParentNode().getScore() >= poppedBoardNode.getScore()) { //Can be pruned
                            //Pop of rest of level from stack
                            while (stack.peek().getLevel() == poppedBoardNode.getLevel()) {
                                stack.pop();
                            }
                        }
                    }
                }
            } else { //Level limit reached (reached a terminal state)
                CheckersBoardNode terminalState = stack.pop();

                int score = terminalState.getStaticEvaluation();
                if (terminalState.getLevel()%2 == 0) {
                    //AI move
                    //push back min
                    terminalState.getParentNode().backupMin(score, terminalState);
                    //Beta pruning
                    if (terminalState.getLevel() >= 3 && //Is at or below level 3
                            terminalState.getParentNode().getParentNode().getScore() <= terminalState.getScore()) {
                        //Pop off rest of level from stack
                        while (stack.peek().getLevel() == terminalState.getLevel()) {
                            stack.pop();
                        }
                    }
                } else {
                    //User move
                    //push back max
                    terminalState.getParentNode().backupMax(score, terminalState);
                    //Alpha pruning
                    if (terminalState.getLevel() >= 2 && //Is at or below level 2
                            terminalState.getParentNode().getParentNode().getScore() >= terminalState.getScore()) {
                        //Pop off rest of level from stack
                        while (stack.peek().getLevel() == terminalState.getLevel()) {
                            stack.pop();
                        }
                    }
                }
            }
        }
        //Reached back to root
        //get best successor
        this.boardNode = this.boardNode.getChildNode(); //Make best move
        if (this.boardNode.getJumpingSquare() != -1) {
            this.boardNode.setPlayer(2); //Set to player 2s turn
        }
    }

    /**
     * Checks if there is a winner, returning the winners number if there is.

     * @return Winner of the game if there is one, otherwise returns 0
     */
    public int winner() {
        if (this.boardNode.getWhiteCheckers() == 0) {
            return 2;
        } else if (this.boardNode.getBlackCheckers() == 0) {
            return 1;
        }
        return 0;

    }

    /**
     * Sets the AI's search tree level limit. The higher this is the better the AI.
     *
     * @param  levelLimit The depth the AI's search tree will go to before pruning
     */
    public void setLevelLimit(int levelLimit) {
        this.levelLimit = levelLimit;
    }
}