import javax.swing.*;
import java.awt.*;

public class TrafficLightPanel extends JPanel {

    private TrafficLight light;
    private int carX = 20;
    private final int carY = 320;
    private final int stopLineX = 230;

    public TrafficLightPanel(TrafficLight light) {
        this.light = light;
        setPreferredSize(new Dimension(600, 420));
        setBackground(new Color(200, 220, 255));
    }

    public void moveCar() {
        if (light.getState() == TrafficLight.State.GREEN) {
            carX += 6;
            if (carX > getWidth()) carX = -90;
        } else {
            int carFront = carX + 80;
            if (carFront < stopLineX - 2) carX += 2;
        }
    }

    public void resetCar() {
        carX = 20;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(60, 60, 60));
        g.fillRect(0, 350 - 30, getWidth(), 80);

        // Lane lines
        g.setColor(Color.WHITE);
        for (int x = 0; x < getWidth(); x += 40)
            g.fillRect(x, 350 + 5, 25, 4);

        // Stop line
        g.fillRect(stopLineX, 350 - 40, 4, 60);

        // Traffic light box
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(260, 40, 100, 220, 20, 20);

        TrafficLight.State s = light.getState();

        drawLight(g, 310, 90, s == TrafficLight.State.RED ? Color.RED : Color.GRAY);
        drawLight(g, 310, 160, s == TrafficLight.State.YELLOW ? Color.YELLOW : Color.GRAY);
        drawLight(g, 310, 230, s == TrafficLight.State.GREEN ? Color.GREEN : Color.GRAY);

        drawCar(g, carX, carY);
    }

    private void drawLight(Graphics g, int x, int y, Color c) {
        g.setColor(Color.BLACK);
        g.fillOval(x - 30, y - 30, 60, 60);
        g.setColor(c);
        g.fillOval(x - 25, y - 25, 50, 50);
    }

    private void drawCar(Graphics g, int x, int y) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y - 30, 80, 30);
        g.fillRect(x + 10, y - 50, 60, 20);

        g.setColor(Color.BLACK);
        g.fillOval(x + 5, y - 10, 20, 20);
        g.fillOval(x + 55, y - 10, 20, 20);
    }
}
