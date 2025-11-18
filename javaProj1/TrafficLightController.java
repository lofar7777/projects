import javax.swing.*;
import java.awt.*;


public class TrafficLightController {

    private TrafficLight light;
    private TrafficLightPanel panel;
    private JLabel statusLabel;
    private JLabel countdownLabel;

    private Timer stateTimer;
    private Timer carTimer;

    private int remaining;

    private final int RED_TIME = 5;
    private final int GREEN_TIME = 5;
    private final int YELLOW_TIME = 2;

    public TrafficLightController(TrafficLight light, TrafficLightPanel panel,
                                  JLabel statusLabel, JLabel countdownLabel) {

        this.light = light;
        this.panel = panel;
        this.statusLabel = statusLabel;
        this.countdownLabel = countdownLabel;

        setRemaining();

        stateTimer = new Timer(1000, e -> {
            remaining--;
            if (remaining <= 0) {
                light.nextState();
                setRemaining();
            }
            updateLabels();
            panel.repaint();
        });

        carTimer = new Timer(40, e -> {
            panel.moveCar();
            panel.repaint();
        });
    }

    private void setRemaining() {
        switch (light.getState()) {
            case RED: remaining = RED_TIME; break;
            case GREEN: remaining = GREEN_TIME; break;
            case YELLOW: remaining = YELLOW_TIME; break;
        }
        updateLabels();
    }

    private void updateLabels() {
        switch (light.getState()) {
            case RED:
                statusLabel.setText("STOP");
                statusLabel.setForeground(Color.RED);
                break;

            case GREEN:
                statusLabel.setText("GO");
                statusLabel.setForeground(Color.GREEN.darker());
                break;

            case YELLOW:
                statusLabel.setText("WAIT");
                statusLabel.setForeground(Color.ORANGE.darker());
                break;
        }

        countdownLabel.setText(Integer.toString(remaining));
    }

    public void start() {
        stateTimer.start();
        carTimer.start();
    }

    public void stop() {
        stateTimer.stop();
        carTimer.stop();
    }

    public void nextManually() {
        light.nextState();
        setRemaining();
        panel.repaint();
    }
}
