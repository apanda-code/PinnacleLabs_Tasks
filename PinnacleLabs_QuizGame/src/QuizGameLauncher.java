import javax.swing.*;
import java.awt.*;

public class QuizGameLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartScreen::new);
    }
}


class StartScreen extends JFrame {
    StartScreen() {
        setTitle("Welcome to the Quiz Game");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 228, 181)); // peach

        JLabel welcomeLabel = new JLabel("Enter your name to start:", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        nameField.setHorizontalAlignment(JTextField.CENTER);

        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                dispose();
                new QuizGameGUI(playerName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter your name.");
            }
        });

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(nameField, BorderLayout.CENTER);
        center.add(startButton, BorderLayout.SOUTH);
        center.setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));

        add(welcomeLabel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        setVisible(true);
    }
}
