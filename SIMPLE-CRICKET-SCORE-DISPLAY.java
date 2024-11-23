// Final Updated program with updated features and is runnable
import javax.swing.*;
import java.awt.*;

public class CSD4 extends JFrame {
    // Variables for tracking score
    private int totalRuns = 0, totalWickets = 0, ballsBowled = 0;
    private int oversLimit = 20 * 6; // Default to 20 overs (in balls)
    private int currentInnings = 1;
    private int firstInningsScore = 0;

    // Team input fields
    private JTextField team1Field, team2Field;

    // Display fields
    private JTextField runsField, wicketsField, oversField, scoreFieldInnings1, scoreFieldInnings2, inningsField;

    // Radio buttons for toss, decision, and overs selection
    private JRadioButton team1Radio, team2Radio, batRadio, ballRadio, twentyOversRadio, fiftyOversRadio;
    private JTextField customOversField; // Input field for custom overs
    private JButton setCustomOversButton; // Button to set custom overs
    private JButton resetButton; // Reset button

    public CSD4() {
        // Setting up the main window
        setTitle("Cricket Score App");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Top Panel for Team Info and Toss Decision
        JPanel topPanel = new JPanel();
        topPanel.setBounds(10, 10, 670, 120);
        topPanel.setBackground(Color.YELLOW);
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel team1Label = new JLabel("TEAM 1:");
        team1Field = new JTextField(10);
        JLabel team2Label = new JLabel("TEAM 2:");
        team2Field = new JTextField(10);

        JLabel tossLabel = new JLabel("Toss Won By:");
        team1Radio = new JRadioButton("Team 1");
        team2Radio = new JRadioButton("Team 2");
        ButtonGroup tossGroup = new ButtonGroup();
        tossGroup.add(team1Radio);
        tossGroup.add(team2Radio);

        JLabel decisionLabel = new JLabel("Chose To:");
        batRadio = new JRadioButton("Bat");
        ballRadio = new JRadioButton("Ball");
        ButtonGroup decisionGroup = new ButtonGroup();
        decisionGroup.add(batRadio);
        decisionGroup.add(ballRadio);

        JLabel oversLabel = new JLabel("Overs:");
        twentyOversRadio = new JRadioButton("20 Overs");
        fiftyOversRadio = new JRadioButton("50 Overs");
        ButtonGroup oversGroup = new ButtonGroup();
        oversGroup.add(twentyOversRadio);
        oversGroup.add(fiftyOversRadio);

        JLabel customOversLabel = new JLabel("Custom Overs:");
        customOversField = new JTextField(5);
        setCustomOversButton = new JButton("Set Overs");

        // Event listener for custom overs input
        setCustomOversButton.addActionListener(e -> {
            String input = customOversField.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int customOvers = Integer.parseInt(input);
                    if (customOvers > 0) {
                        oversLimit = customOvers * 6; // Convert overs to balls
                        twentyOversRadio.setEnabled(false);
                        fiftyOversRadio.setEnabled(false);
                        JOptionPane.showMessageDialog(this, "Overs set to " + customOvers);
                    } else {
                        JOptionPane.showMessageDialog(this, "Please enter a valid number of overs greater than 0.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a numeric value.");
                }
            } else {
                oversLimit = 20 * 6; // Default to 20 overs
                twentyOversRadio.setEnabled(true);
                fiftyOversRadio.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Default 20 overs selected.");
            }
        });

        topPanel.add(team1Label);
        topPanel.add(team1Field);
        topPanel.add(team2Label);
        topPanel.add(team2Field);
        topPanel.add(tossLabel);
        topPanel.add(team1Radio);
        topPanel.add(team2Radio);
        topPanel.add(decisionLabel);
        topPanel.add(batRadio);
        topPanel.add(ballRadio);
        topPanel.add(oversLabel);
        topPanel.add(twentyOversRadio);
        topPanel.add(fiftyOversRadio);
        topPanel.add(customOversLabel);
        topPanel.add(customOversField);
        topPanel.add(setCustomOversButton);

        add(topPanel);

        // Middle Panel for Score Entry (Runs, Wickets, Extras)
        JPanel scorePanel = new JPanel();
        scorePanel.setBounds(10, 150, 300, 300);
        scorePanel.setBackground(Color.GREEN);
        scorePanel.setLayout(new GridLayout(5, 3, 10, 10));

        JButton[] runButtons = new JButton[6];
        for (int i = 1; i <= 6; i++) {
            runButtons[i - 1] = new JButton(String.valueOf(i));
            final int run = i;
            runButtons[i - 1].addActionListener(e -> addRuns(run, true));
            scorePanel.add(runButtons[i - 1]);
        }

        JButton noBallButton = new JButton("No Ball");
        noBallButton.addActionListener(e -> addRuns(1, false));
        JButton wideButton = new JButton("Wide");
        wideButton.addActionListener(e -> addRuns(1, false));
        JButton byeButton = new JButton("Bye");
        byeButton.addActionListener(e -> addRuns(1, false));
        JButton wicketButton = new JButton("Wicket");
        wicketButton.addActionListener(e -> addWicket());
        JButton dotButton = new JButton("Dot");
        dotButton.addActionListener(e -> addDotBall());

        scorePanel.add(noBallButton);
        scorePanel.add(wideButton);
        scorePanel.add(byeButton);
        scorePanel.add(wicketButton);
        scorePanel.add(dotButton);

        add(scorePanel);

        // Right Panel for Displaying Runs, Wickets, and Overs
        JPanel displayPanel = new JPanel();
        displayPanel.setBounds(320, 150, 360, 150);
        displayPanel.setBackground(Color.PINK);
        displayPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel runsLabel = new JLabel("Runs:");
        runsField = new JTextField("0");
        runsField.setEditable(false);

        JLabel wicketsLabel = new JLabel("Wickets:");
        wicketsField = new JTextField("0");
        wicketsField.setEditable(false);

        JLabel oversLabelDisplay = new JLabel("Overs:");
        oversField = new JTextField("0.0");
        oversField.setEditable(false);

        JLabel inningsLabel = new JLabel("Current Innings:");
        inningsField = new JTextField("Innings " + currentInnings);
        inningsField.setEditable(false);

        displayPanel.add(runsLabel);
        displayPanel.add(runsField);
        displayPanel.add(wicketsLabel);
        displayPanel.add(wicketsField);
        displayPanel.add(oversLabelDisplay);
        displayPanel.add(oversField);
        displayPanel.add(inningsLabel);
        displayPanel.add(inningsField);

        add(displayPanel);

        // Bottom Panel for Innings Display and Reset Button
        JPanel inningsPanel = new JPanel();
        inningsPanel.setBounds(10, 460, 670, 80);
        inningsPanel.setBackground(Color.YELLOW);
        inningsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel inningsScoreLabel = new JLabel("SCORE:");
        scoreFieldInnings1 = new JTextField("Innings 1: ", 10);
        scoreFieldInnings2 = new JTextField("Innings 2: ", 10);
        scoreFieldInnings1.setEditable(false);
        scoreFieldInnings2.setEditable(false);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetMatch());

        inningsPanel.add(inningsScoreLabel);
        inningsPanel.add(scoreFieldInnings1);
        inningsPanel.add(scoreFieldInnings2);
        inningsPanel.add(resetButton);

        add(inningsPanel);

        // Event Listener for Overs Selection
        twentyOversRadio.addActionListener(e -> {
            oversLimit = 20 * 6; // 20 overs
            twentyOversRadio.setEnabled(true);
            fiftyOversRadio.setEnabled(true);
        });

        fiftyOversRadio.addActionListener(e -> {
            oversLimit = 50 * 6; // 50 overs
            twentyOversRadio.setEnabled(true);
            fiftyOversRadio.setEnabled(true);
        });

        setVisible(true);
    }

    private void addDotBall() {
        ballsBowled++;
        checkOversOrWickets();
        updateDisplay();
    }

    private void addRuns(int runs, boolean validBall) {
        totalRuns += runs;
        if (validBall) {
            ballsBowled++;
        }
        checkOversOrWickets();
        updateDisplay();
    }

    private void addWicket() {
        totalWickets++;
        ballsBowled++; // Ensure ball count increases when a wicket falls
        checkOversOrWickets();
        updateDisplay();
    }

    private void checkOversOrWickets() {
        if (currentInnings == 1 && (ballsBowled >= oversLimit || totalWickets >= 10)) {
            firstInningsScore = totalRuns;
            scoreFieldInnings1.setText("Innings 1: " + totalRuns + "/" + totalWickets);
            JOptionPane.showMessageDialog(this, "End of First Innings! Target is " + (firstInningsScore + 1));
            resetForSecondInnings();
        } else if (currentInnings == 2) {
            if (ballsBowled >= oversLimit || totalWickets >= 10 || totalRuns > firstInningsScore) {
                scoreFieldInnings2.setText("Innings 2: " + totalRuns + "/" + totalWickets);
                declareResult();
            }
        }
    }

    private void declareResult() {
        String winner;
        if (totalRuns > firstInningsScore) {
            winner = team2Field.getText().isEmpty() ? "Team 2" : team2Field.getText();
            JOptionPane.showMessageDialog(this, winner + " won by " + (10 - totalWickets) + " wickets!");
        } else if (totalRuns == firstInningsScore) {
            JOptionPane.showMessageDialog(this, "It's a tie!");
        } else {
            winner = team1Field.getText().isEmpty() ? "Team 1" : team1Field.getText();
            JOptionPane.showMessageDialog(this, winner + " won by " + (firstInningsScore - totalRuns) + " runs!");
        }
        System.exit(0);
    }

    private void resetForSecondInnings() {
        totalRuns = 0;
        totalWickets = 0;
        ballsBowled = 0;
        currentInnings++;
        inningsField.setText("Innings " + currentInnings);
    }

    private void updateDisplay() {
        runsField.setText(String.valueOf(totalRuns));
        wicketsField.setText(String.valueOf(totalWickets));
        oversField.setText((ballsBowled / 6) + "." + (ballsBowled % 6));
    }

    private void resetMatch() {
        totalRuns = 0;
        totalWickets = 0;
        ballsBowled = 0;
        currentInnings = 1;
        firstInningsScore = 0;
        oversLimit = 20 * 6; // Reset to default 20 overs

        // Reset UI fields
        runsField.setText("0");
        wicketsField.setText("0");
        oversField.setText("0.0");
        inningsField.setText("Innings " + currentInnings);
        scoreFieldInnings1.setText("Innings 1: ");
        scoreFieldInnings2.setText("Innings 2: ");
        twentyOversRadio.setEnabled(true);
        fiftyOversRadio.setEnabled(true);
        customOversField.setText("");
    }
    public static void main(String[] args) {
        new CSD4();
    }
}
