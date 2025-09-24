import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Labels for game stats
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel missLabel;
    private JLabel accuracyLabel;

    // The start button which will start the game
    private JButton startButton;

    private JPanel topPanel; // Panel that will hold the game stats and buttons
    private GamePanel gamePanel; // The game panel which will hold the actual area for the game
    private WelcomePanel welcomePanel; // Welcome Panel screen

    private int score = 0;

    // Buttons to change the game's level of difficulty
    private JButton easyBtn;
    private JButton mediumBtn;
    private JButton hardBtn;

    private String selectedDifficulty = "Medium"; // Default difficulty

    public GameWindow() {

        // Creating the Game window, as well as setting a title and size
        frame = new JFrame("Click the Circle: Reflex Rush");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allows for program to exit if user closes it
        frame.setSize(800, 550);

        // CardLayout allows for my program to display different panels one at a time. 
        // This is used so I can have a separate welcome screen panel. and a separate game screen panel.
        cardLayout = new CardLayout(); 
        mainPanel = new JPanel(cardLayout); // our mainpanel will be the container that uses CardLayout

        // Welcome Panel
        welcomePanel = new WelcomePanel(this);

        // Game Panel Setup
        scoreLabel = new JLabel("Score: 0");
        timerLabel = new JLabel("Time: 20");
        missLabel = new JLabel("Misses: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");

        startButton = new JButton("Start Game");
        startButton.addActionListener(new StartButtonListener()); //adding the actionlistener to the button
        
        // adding labels of the stats to the top panel
        topPanel = new JPanel(new FlowLayout());
        topPanel.add(scoreLabel);
        topPanel.add(timerLabel);
        topPanel.add(missLabel);
        topPanel.add(accuracyLabel);
        topPanel.add(startButton);

        // creating our GamePanel
        gamePanel = new GamePanel(this, timerLabel, missLabel, accuracyLabel);

        // The gameContainer JPanel is our main gameplay screen, which holds the topPanel and gamePanel
        // Using a BorderLayout, I adjust the panels accordingly for a clean display.
        JPanel gameContainer = new JPanel(new BorderLayout()); 
        gameContainer.add(topPanel, BorderLayout.NORTH);
        gameContainer.add(gamePanel, BorderLayout.CENTER);

        // Add both panels to the card layout
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(gameContainer, "Game");

        // Adding the panel to the frame
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Display the welcome screen first
        // Later in the code, we will be able to switch to the game screen through the Start button listener.
        cardLayout.show(mainPanel, "Welcome");

        // These buttons are to change the level of difficulty of the game (making circles spawn and reset faster)
        easyBtn = new JButton("Easy");
        mediumBtn = new JButton("Medium");
        hardBtn = new JButton("Hard");
        

        /*
         * Each of these buttons will have an actionlistener
         * Once button is pressed, it will set the selectedDifficulty string, and call the updateDifficultyButtonStyles method, which we will see below
         */

        // Easy difficulty button
        easyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = "Easy";
                updateDifficultyButtonStyles();
            }
        });
        
        // Medium difficulty button
        mediumBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = "Medium";
                updateDifficultyButtonStyles();
            }
        });
        
        // Hard difficulty button
        hardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = "Hard";
                updateDifficultyButtonStyles();
            }
        });
        
        // Add each of the difficulty buttons to the top panel
        topPanel.add(new JLabel("Difficulty:"));
        topPanel.add(easyBtn);
        topPanel.add(mediumBtn);
        topPanel.add(hardBtn);
        
    }

    // This method switches the Welcome screen panel to the Game screen panel.
    public void showGameScreen() {
        cardLayout.show(mainPanel, "Game");
    }

    // Update score by incrementing by 1, as well as updating the score label
    public void updateScore() {
        score++;
        scoreLabel.setText("Score: " + score);
    }

    // Resets score back to 0
    // Used for when we start new game
    public void resetScore() {
        score = 0;
        scoreLabel.setText("Score: 0");
    }
    
    // Returns score
    public int getScore() {
        return score;
    }

    /*
     * Once our Start Button is clicked, it will call the resetScore method, as well as call the startGame method in our gamePanel class.
     * This essentially starts our whole game once the button is clicked.
     */
    private class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            resetScore();
            cardLayout.show(mainPanel, "Game");
            gamePanel.startGame();
        }
    }

    // returns the selected difficulty string
    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }

    /*
     * This method simply changes the font of the label difficulty onces pressed.
     * This is so that the user knows what difficulty the game is set at
     */
    private void updateDifficultyButtonStyles() {
        JButton[] buttons = { easyBtn, mediumBtn, hardBtn }; // create array for difficulty buttons
        for (JButton btn : buttons) { // loop through each of them
            if (btn.getText().equals(selectedDifficulty)) { // if that button is the one that is selected, it will change that button's font to green.
                btn.setForeground(Color.GREEN);
            } else {
                btn.setForeground(Color.BLACK); // else, the font will be black.
            }
        }
    }
}