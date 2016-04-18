import javax.swing.*;
import java.awt.*;

/**
 * InfoJPanel.java
 *  Information panel class for checkers GUI. Creates an error label whose text can be changed to show errors in user
 *  moves or which player's turn it is.
 *
 * @author Francis Poole
 * @version 1.0, 04/01/15
 */
public class InfoJPanel extends JPanel {

    private final JLabel errorLabel; //Label that shows error message

    /**
     * Info panel constructor
     */
    public InfoJPanel() {
        this.setBackground(Color.white);
        this.errorLabel = new JLabel("Welcome to checkers! Player 1's turn");
        this.errorLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 30));
        this.add(this.errorLabel);
    }

    /**
     * Sets error message
     *
     * @param errorMsg to set
     */
    public void setError(String errorMsg) {
        this.errorLabel.setText(errorMsg);
    }
}
