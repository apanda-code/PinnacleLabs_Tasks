import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;

public class QuizGameGUI extends JFrame implements ActionListener {
    String playerName;
    String[][] questions = {
            {"What is the capital of France?", "A. Paris", "B. Rome", "C. Berlin", "D. Madrid", "A"},
            {"Which planet is known as the Red Planet?", "A. Earth", "B. Venus", "C. Mars", "D. Jupiter", "C"},
            {"Who wrote 'Hamlet'?", "A. Dickens", "B. Shakespeare", "C. Orwell", "D. Austen", "B"},
            {"What is 5 x 6?", "A. 30", "B. 25", "C. 35", "D. 20", "A"},
            {"What is H2O?", "A. Oxygen", "B. Hydrogen", "C. Water", "D. Acid", "C"},
            {"Which country won the ICC Cricket World Cup in 2011?", "A. Australia", "B. India", "C. England", "D. South Africa", "B"},
            {"Which player has won the most Ballon d'Or awards?", "A. Cristiano Ronaldo", "B. Messi", "C. Pele", "D. Maradona", "B"},
            {"Which is the largest desert in the world?", "A. Gobi", "B. Arabian", "C. Sahara", "D. Antarctica", "D"},
            {"Who was the first President of the United States?", "A. Lincoln", "B. Washington", "C. Jefferson", "D. Adams", "B"},
            {"Who invented the light bulb?", "A. Edison", "B. Tesla", "C. Newton", "D. Graham Bell", "A"}
    };

    int currentQuestion = 0;
    int score = 0;
    int timeLeft = 10;

    JLabel questionLabel, timerLabel, feedbackLabel, resultLabel;
    JButton[] optionButtons = new JButton[4];
    Timer countdownTimer, delayTimer;
    JPanel optionsPanel, centerPanel;

    public QuizGameGUI(String playerName) {
        this.playerName = playerName;

        setTitle("Quiz Game - " + playerName);
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 228, 181)); // peach

        // NORTH panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        questionLabel = new JLabel("Question", JLabel.CENTER);
        questionLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        questionLabel.setForeground(new Color(90, 40, 0));
        topPanel.add(questionLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("Time: 10", JLabel.RIGHT);
        timerLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // CENTER panel (CardLayout)
        centerPanel = new JPanel(new CardLayout());
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        // OPTIONS panel
        optionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));
        optionsPanel.setOpaque(false);

        Color[] buttonColors = {
                new Color(100, 149, 237), // Blue
                new Color(60, 179, 113),  // Green
                new Color(255, 165, 0),   // Orange
                new Color(186, 85, 211)   // Purple
        };

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new RoundedButton("");
            optionButtons[i].setBackground(buttonColors[i]);
            optionButtons[i].setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setForeground(Color.WHITE);
            optionButtons[i].addActionListener(this);
            optionsPanel.add(optionButtons[i]);
        }

        // RESULT label
        resultLabel = new JLabel("", JLabel.CENTER);
        resultLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        resultLabel.setForeground(new Color(90, 40, 0));

        centerPanel.add(optionsPanel, "OPTIONS");
        centerPanel.add(resultLabel, "RESULT");

        // SOUTH feedback label
        feedbackLabel = new JLabel("", JLabel.CENTER);
        feedbackLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        feedbackLabel.setForeground(new Color(70, 40, 0));
        add(feedbackLabel, BorderLayout.SOUTH);

        loadQuestion();
    }

    private void loadQuestion() {
        stopTimers();
        showCard("OPTIONS");

        if (currentQuestion < questions.length) {
            // Wrap long question in 2 lines using HTML
            questionLabel.setText("<html><div style='text-align:center'>Q" + (currentQuestion + 1) + ":<br>" +
                    questions[currentQuestion][0] + "</div></html>");

            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(questions[currentQuestion][i + 1]);
                optionButtons[i].setEnabled(true);
                optionButtons[i].setVisible(true);
            }

            feedbackLabel.setText("");
            startCountdown();
        } else {
            showResult();
        }
    }

    private void startCountdown() {
        timeLeft = 10;
        timerLabel.setText("Time: " + timeLeft);

        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                countdownTimer.stop();
                handleTimeout();
            }
        });
        countdownTimer.start();
    }

    private void stopTimers() {
        if (countdownTimer != null) countdownTimer.stop();
        if (delayTimer != null) delayTimer.stop();
    }

    private void handleTimeout() {
        for (JButton btn : optionButtons) btn.setEnabled(false);
        feedbackLabel.setText("⏰ Time's up! Correct: " + questions[currentQuestion][5]);
        playSound("wrong.wav");

        delayTimer = new Timer(1500, e -> {
            currentQuestion++;
            loadQuestion();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    public void actionPerformed(ActionEvent e) {
        stopTimers();

        JButton clicked = (JButton) e.getSource();
        String selected = clicked.getText().substring(0, 1);
        String correct = questions[currentQuestion][5];

        for (JButton btn : optionButtons) btn.setEnabled(false);

        if (selected.equals(correct)) {
            for (JButton btn : optionButtons) {
                if (btn != clicked) btn.setVisible(false);
            }
            feedbackLabel.setText("✅ Correct!");
            playSound("correct.wav");
            score++;

            delayTimer = new Timer(1500, ev -> {
                currentQuestion++;
                loadQuestion();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();

        } else {
            clicked.setText(clicked.getText() + " ❌");
            playSound("wrong.wav");

            delayTimer = new Timer(1000, ev -> {
                for (JButton btn : optionButtons) btn.setVisible(false);
                for (JButton btn : optionButtons) {
                    if (btn.getText().startsWith(correct)) {
                        btn.setVisible(true);
                        break;
                    }
                }
                feedbackLabel.setText("Correct Answer: " + correct);

                Timer nextQuestionTimer = new Timer(1500, next -> {
                    currentQuestion++;
                    loadQuestion();
                });
                nextQuestionTimer.setRepeats(false);
                nextQuestionTimer.start();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    private void showResult() {
        stopTimers();
        timerLabel.setText("");
        questionLabel.setText("🎉 Game Over!");
        feedbackLabel.setText("");

        // 🎯 Update for 10 questions
        String finalMessage = (score == 10)
                ? "🎉 Congratulations, you got a perfect 10 👌"
                : "Well tried; next time, go for a 10 👍";

        resultLabel.setText("<html><div style='text-align:center'>" +
                playerName + ",<br>Your Score: " + score + " / 10<br><br>" +
                finalMessage + "</div></html>");

        showCard("RESULT");
    }

    private void showCard(String name) {
        CardLayout cl = (CardLayout) centerPanel.getLayout();
        cl.show(centerPanel, name);
    }

    private void playSound(String fileName) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/" + fileName);
            if (audioSrc == null) return;
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Sound error: " + ex.getMessage());
        }
    }

    static class RoundedButton extends JButton {
        public RoundedButton(String label) {
            super(label);
            setContentAreaFilled(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }

        @Override protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
    }
}
