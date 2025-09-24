import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomePanel extends JPanel {
    
    // Instance Variables
    private JLabel titleLabel; // Label for title of game
    private JTextArea instructions; // Text area to explain game instructions
    private JButton letsPlayBtn; // Button to go to GamePanel


    public WelcomePanel(GameWindow parent) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        // Title of game "Reflex Rush"
        titleLabel = new JLabel("Click the Circle: Reflex Rush");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); //setting font and size

        // Instructions
        instructions = new JTextArea(
            "Welcome to Reflex Rush!\n\n"
          + "Objective:\n"
          + "Click the RED circle as quickly and accurately as you can.\n"
          + "Avoid clicking the gray circles or missing entirely.\n"
          + "You have 15 seconds to score as much as possible!\n\n"
          + "Click 'Let's Play!' to continue to the game."
          + "\n\nDifficulty Levels (rate at which set of circles appear):\n"
          + "Easy: 2.5 Seconds\n"
          + "Medium: 1.5 Seconds\n"
          + "Hard: 1.0 Seconds"
        );
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 16)); //sets font and size
        instructions.setEditable(false); // makes text non-editable
        instructions.setFocusable(false);
        instructions.setOpaque(false); // makes background transparent


        // Let's Play Button
        letsPlayBtn = new JButton("Let's Play!");
        letsPlayBtn.setFocusPainted(false);
        
        // Action Listener for the Lets Play button
        // Once the button is clicked, it will call the showGameScreen method from the GameWindow class, which will switch the panel to the GamePanel
        letsPlayBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showGameScreen();
            }
        });

        // adding to the BorderLayout
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(instructions, BorderLayout.CENTER);
        this.add(letsPlayBtn, BorderLayout.SOUTH);
    }
}