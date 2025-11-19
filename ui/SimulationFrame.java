package lidia_assignement2.ui;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {
    public JTextField clientsField = new JTextField(5);
    public JTextField queuesField = new JTextField(5);
    public JTextField timeLimitField = new JTextField(5);
    public JTextField minArrivalField = new JTextField(5);
    public JTextField maxArrivalField = new JTextField(5);
    public JTextField minServiceField = new JTextField(5);
    public JTextField maxServiceField = new JTextField(5);
    public JButton startButton = new JButton("Start Simulation");
    public JTextArea logArea = new JTextArea(20, 50);

    public SimulationFrame() {
        setTitle("Queue Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Number of clients:"));
        inputPanel.add(clientsField);
        inputPanel.add(new JLabel("Number of queues:"));
        inputPanel.add(queuesField);
        inputPanel.add(new JLabel("Time limit:"));
        inputPanel.add(timeLimitField);
        inputPanel.add(new JLabel("Min arrival time:"));
        inputPanel.add(minArrivalField);
        inputPanel.add(new JLabel("Max arrival time:"));
        inputPanel.add(maxArrivalField);
        inputPanel.add(new JLabel("Min service time:"));
        inputPanel.add(minServiceField);
        inputPanel.add(new JLabel("Max service time:"));
        inputPanel.add(maxServiceField);

        add(inputPanel, BorderLayout.NORTH);

        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
}
