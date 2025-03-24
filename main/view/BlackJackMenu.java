package main.view;

import main.controller.GameManager;

import javax.swing.*;
import java.awt.*;

import static main.view.Languages.*;

public class BlackJackMenu extends JFrame {
    private JButton startButton, instructionsButton, exitButton, optionsButton;
    private JLabel imageLabel, mainTitleLabel;

    private int titleX = 0;
    private Timer titleTimer;

    public static int language = 0;

    public BlackJackMenu() {
        setTitle(Texts.startGame[language]);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Custom image icon
        ImageIcon icon = new ImageIcon("img/black.png");
        setIconImage(icon.getImage());

        initializeComponents();
        layoutComponents();
        attachEventListeners();
    }

    private void initializeComponents() {
        startButton = createStyledButton(Texts.startGame[language]); // "Start Game"
        instructionsButton = createStyledButton(Texts.instructions[language]);
        exitButton = createStyledButton(Texts.exit[language]);
        optionsButton =  createStyledButton(Texts.options[language]);

        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("img/blackjack.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        //
        mainTitleLabel = new JLabel(Texts.mainTitle[language]);
        mainTitleLabel.setFont(new Font("Serif", Font.ITALIC, 56));
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitleLabel.setForeground(Color.WHITE);

        startTitleAnimation();
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 139, 34));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(34, 139, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 5, 0); // Add some space between buttons
        buttonPanel.add(startButton, gbc);
        buttonPanel.add(instructionsButton, gbc);
        buttonPanel.add(optionsButton, gbc);
        buttonPanel.add(exitButton, gbc);

        // Moving Title Panel
        JPanel titlePanel = new JPanel(null);
        titlePanel.setBackground(new Color(34, 139, 34));
        titlePanel.setPreferredSize(new Dimension(getWidth(), 50));
        mainTitleLabel.setBounds(titleX, 75, 1000, 75);
        titlePanel.add(mainTitleLabel);

        mainPanel.add(imageLabel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        add(mainPanel);
    }

    private void attachEventListeners() {
        startButton.addActionListener(e -> {
            GameManager gameManager = GameManager.getInstance();
            BlackjackGUI gui = new BlackjackGUI(gameManager);
            gui.setVisible(true);
            dispose(); // Close the menu window
        });

        instructionsButton.addActionListener(e -> {
            String message = Texts.instructionsPopup[language][0] + "\n" +
                             "1. " + Texts.instructionsPopup[language][1] + "\n" +
                             "2. " + Texts.instructionsPopup[language][2] + "\n" +
                             "3. " + Texts.instructionsPopup[language][3];
        
            JOptionPane.showMessageDialog(this, 
                message, 
                Texts.instructions[language], 
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        
        

        exitButton.addActionListener(e -> System.exit(0));

        optionsButton.addActionListener(e -> {
            new OptionsPanel(this).setVisible(true);
            // add option logic
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 28));
        button.setBackground(new Color(255, 215, 0)); // Gold
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 75)); // Set preferred size to make buttons smaller
        return button;
    }

    public void refreshMenu() {
        setTitle(Texts.startGame[language]); // Update window title

        // Update button texts
        startButton.setText(Texts.startGame[language]);
        instructionsButton.setText(Texts.instructions[language]);
        exitButton.setText(Texts.exit[language]);
        optionsButton.setText(Texts.options[language]);

        // Repaint UI
        revalidate();
        repaint();
    }

    private void startTitleAnimation() {
        titleX = getWidth(); // Start from the right side of the window
        titleTimer = new Timer(10, e -> {
            titleX -= 2; // Move left

            // If it goes off-screen, reset to right side
            if ((titleX + mainTitleLabel.getWidth() - 100) < 0) {
                titleX = getWidth();
            }

            mainTitleLabel.setLocation(titleX, mainTitleLabel.getY());
        });
        titleTimer.start();
    }

}