import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class GamePanel extends JPanel {
    // Dimension of game panel
    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    private final int CIRCLE_SIZE = 50; // size of circle (diameter)

    private Timer gameTimer; // timer that tracks how long game will run for
    private Timer circleTimer; // timer for how long circles on screen appear for

    private int timeLeft = 20;  // to track time left
    private int misses = 0; // to track num of misses
    private int totalAttempts = 0;  // to track total attempts (misses and hits together)

    private JLabel timerLabel;  // label to display time
    private JLabel missLabel;   // to display misses
    private JLabel accuracyLabel;   // to display stastics of ratio between hits/total hits

    private Random rand = new Random(); // used to randomize placement of circles onto the panel, as well as deciding which circle is the "target"

    private GameWindow parent; // used to reference to the parent class, so that we can update statistics from the gamepanel

    // An arraylist of circles used for game, as well as the number of circles per round
    private ArrayList<Circle> circles = new ArrayList<>();
    private int numCircles = 5;

    private boolean recentlyClicked = false; // boolean to prevent score being counted if clicked multiple times before switching
    private boolean gameRunning = false; // boolean to keep track is game is still running

    // Constructor
    public GamePanel(GameWindow parent, JLabel timerLabel, JLabel missLabel, JLabel accuracyLabel) {
        this.parent = parent;
        this.timerLabel = timerLabel;
        this.missLabel = missLabel;
        this.accuracyLabel = accuracyLabel;

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.WHITE);

        this.addMouseListener(new ClickListener()); // adding mouselistener
    }
    


    public void startGame() {
        // if game is starting/restarting, (by pressing the start game button), any existing timers will stop.
        if (circleTimer != null){
            circleTimer.stop();
        }

        if (gameTimer != null){
            gameTimer.stop();
        }

        // all below resets all stats for when a new game is started:
        gameRunning = true;
        timeLeft = 20;
        misses = 0;
        totalAttempts = 0;
        timerLabel.setText("Time: " + timeLeft);
        missLabel.setText("Misses: 0");
        accuracyLabel.setText("Accuracy: 100%");

        /*
         * The circle Timer runs based on the selected difficulty time, and essentially checks if user clicks outside the red circle, and increases miss count if so.
         * As well, it will spawn new circles once the timer (for the circle timer) has ran out and if game if the gameTimer is still running.
         * (scroll down below to see CircleTimerListener method that implements ActionListener)
         */
        
        // Get difficulty from GameWindow
        String difficulty = parent.getSelectedDifficulty();
        int delay;

        if (difficulty.equals("Easy")) {
            delay = 2500;
        } else if (difficulty.equals("Hard")) {
            delay = 1000;
        } else {
            delay = 1500; // Medium or default
        }

        
        // The Circle Timer will run its actionPerformed method repeatedly based on the level of difficulty chosen (explained later into the code)
        circleTimer = new Timer(delay, new CircleTimerListener());
        circleTimer.setRepeats(true);
        circleTimer.start();

        /*
         * The gameTimer on the otherhand continuously decreases the time that is left, by continuously decreasing the
         * timeLeft variable by 1 second, as well as updating the timerLabel accordingly.
         * This timer as well ends the game once the timer reaches 0.
         */

        gameTimer = new Timer(1000, new GameTimerListener());
        gameTimer.setRepeats(true);
        gameTimer.start();

        GamePanel.this.spawnCircles();
        GamePanel.this.repaint();
    }
    
    /*
     * This method essentially updates the misses and accuracy labels.
     * This method is called every time a user clicks, a circle is missed, or a new attempt is registered
     */
    private void updateStats() {
        missLabel.setText("Misses: " + misses);
        int score = parent.getScore();
        int accuracy;
        if (totalAttempts > 0) {
            accuracy = (int) ((score * 100.0f) / totalAttempts);
        } else {
            accuracy = 100;
        }
        accuracyLabel.setText("Accuracy: " + accuracy + "%");
    }
    
    /*
     * This method is issued once the timer for the game has reached 0, and essentially
     * stops the game, and shows final statistics/score.
     */
    private void endGame() {
        gameRunning = false;
        if (circleTimer != null) {
            circleTimer.stop();
        }

        if (gameTimer != null){
            gameTimer.stop();
        }

        circles.clear();
        repaint();


        JOptionPane.showMessageDialog(this, "Time's up! Final Score: " + parent.getScore() +
                " | Misses: " + misses +
                " | Accuracy: " + accuracyLabel.getText().replace("Accuracy: ", ""));

    }

    
    //Method for spawning in new circles
    private void spawnCircles() {
        circles.clear(); // clears arraylist

        // Randomly chooses an index value, from 0 to numCircles-1 to be chosen to be the target circle
        int targetIndex = rand.nextInt(numCircles);

        // Loops through each circle and chooses random coordinates to plot onto panel.
        // We substract the size of the circle to ensure that circle is plotted within the boundaries of the frame
        for (int i = 0; i < numCircles; i++) {
            int x = rand.nextInt(WIDTH - CIRCLE_SIZE);
            int y = rand.nextInt(HEIGHT - CIRCLE_SIZE);

            boolean isTarget = (i == targetIndex); // Selects a circle at random index 
            circles.add(new Circle(x, y, CIRCLE_SIZE, isTarget)); // adds circle to arraylist
        }
    }

    // Draws every circle in arraylist
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Circle c : circles) {
            c.draw(g);
        }
    }

    /*
     * The CircleTimerListener will runs every few seconds, based on the selected difficulty, and will essentially
     * check if the user had clicked during the round of the spawned circles. If user hadn't clicked during that round and
     * while the game was running,it will update stats accordingly, as well as repaint the screen.
     */
    private class CircleTimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (!recentlyClicked && gameRunning) {
                misses++;
                totalAttempts++;
                updateStats();
                spawnCircles();
                repaint();
            }
            recentlyClicked = false;
        }
    }

    // GameTimer that was explained above
    // This timer is soley responsible for ending the game once the timer hits 0.
    private class GameTimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                endGame();
            }
        }
    }

    // The ClickListener implements the MouseListener, and is essential for the interactive portion of this program
    public class ClickListener implements MouseListener {
        
        public void mouseClicked(MouseEvent e) { 
            // Does not register click if game is not running
            if (!gameRunning) { 
                return;
            }

            // Retrieves coordinates where user clicked
            int x = e.getX();
            int y = e.getY();

            boolean hit = false;

            // Loops through each circle, and checks if coordinates match with target circle.
            // Statistics will update accordingly, regardless of whether user hit or miss target (total attempts and misses)
            // If user does hit target, the score will update by 1, and will then spawn a new set of circles.
            for (Circle c : circles) {
                if (c.containsPoint(x, y)) {
                    hit = true;
                    totalAttempts++;
                    if (c.isTarget()) {
                        parent.updateScore();
                        recentlyClicked = true;
                        spawnCircles();
                        repaint();
                    } else {
                        misses++;
                    }
                    updateStats();
                    break;
                }
            }
            if (!hit) {
                misses++;
                totalAttempts++;
                updateStats();
            }
        }

        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}