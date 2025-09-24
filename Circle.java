import java.awt.*;

public class Circle {
    // coordinates of circle
    private int x;
    private int y;

    // diameter of circle
    private int size;

    // color of circle
    private Color color;

    // whether circle is the desired target -- (true = target, false = not target)
    private boolean isTarget;
    
    // constructor for circle class
    public Circle(int x, int y, int size, boolean isTarget) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.isTarget = isTarget;

        // if the circle is the target, the color of it will be set to red. Else, the color of the circle will be grey.
        if (isTarget) {
            this.color = Color.RED;
        } else {
            this.color = Color.GRAY;
        }
    }

    // Draws circle
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }

    // px and py are the coords taken from where user clicks in circle
    public boolean containsPoint(int px, int py) {

        // gets center of circle, given that the x and y coords are located
        // at the top left of the circle
        int centerX = x + size / 2;
        int centerY = y + size / 2;

        // radius of circle
        int radius = size / 2;

        // Calculating distance from the point, to the center, horizontally and vertically
        int dx = px - centerX;
        int dy = py - centerY;

        // if the distance from the point clicked to the center of the circle
        // is less than or equal to the radius of the circle, then the circle contains the Point,
        // returning true. Otherwise, it will return false, meaning user did not click circle.
        return dx * dx + dy * dy <= radius * radius;

    }
    
    public boolean isTarget() {
        return isTarget;
    }
}
