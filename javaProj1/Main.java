import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            TrafficLight light = new TrafficLight();
            TrafficLightPanel panel = new TrafficLightPanel(light);

            JLabel statusLabel = new JLabel("STOP", SwingConstants.CENTER);
            statusLabel.setFont(new Font("Arial", Font.BOLD, 28));
            statusLabel.setForeground(Color.RED);

            JLabel countdownLabel = new JLabel("5", SwingConstants.CENTER);
            countdownLabel.setFont(new Font("Arial", Font.BOLD, 22));

            TrafficLightController controller =
                    new TrafficLightController(light, panel, statusLabel, countdownLabel);

            JFrame frame = new JFrame("Traffic Light Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel top = new JPanel(new BorderLayout());
            top.add(statusLabel, BorderLayout.CENTER);

            JPanel right = new JPanel();
            right.add(new JLabel("Time:"));
            right.add(countdownLabel);
            top.add(right, BorderLayout.EAST);

            JPanel buttons = new JPanel();
            JButton start = new JButton("Start");
            JButton stop = new JButton("Stop");
            JButton next = new JButton("Next");
            JButton reset = new JButton("Reset Car");

            start.addActionListener(e -> controller.start());
            stop.addActionListener(e -> controller.stop());
            next.addActionListener(e -> controller.nextManually());
            reset.addActionListener(e -> panel.resetCar());

            buttons.add(start);
            buttons.add(stop);
            buttons.add(next);
            buttons.add(reset);

            frame.add(top, BorderLayout.NORTH);
            frame.add(panel, BorderLayout.CENTER);
            frame.add(buttons, BorderLayout.SOUTH);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
