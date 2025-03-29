

package main.view;

import main.controller.GameManager;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import static main.view.Languages.*;

public class BlackJackMenu extends JFrame {
    private JButton startButton, instructionsButton, exitButton, optionsButton;
    private JLabel imageLabel, mainTitleLabel;
    private BufferedImage backgroundImage;
    private boolean backgroundLoaded = false;
    
    private int titleX = 0;
    private Timer titleTimer;
    public static int language = 0;

    public BlackJackMenu() {
        setTitle(Texts.startGame[language]);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Load background image
        // Change the image loading to be more robust:
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("img/backgroundimage.png");
            if (is != null) {
                backgroundImage = ImageIO.read(is);
                backgroundLoaded = true;
                is.close();
            } else {
                System.err.println("Background image not found in resources");
            }
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundLoaded = false;
        }
        
        // Custom image icon
        ImageIcon icon = new ImageIcon("img/black.png");
        setIconImage(icon.getImage());

        initializeComponents();
        layoutComponents();
        attachEventListeners();
    }

    private void initializeComponents() {
        startButton = createStyledButton(Texts.startGame[language]);
        instructionsButton = createStyledButton(Texts.instructions[language]);
        exitButton = createStyledButton(Texts.exit[language]);
        optionsButton = createStyledButton(Texts.options[language]);

        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("img/blackjack.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        mainTitleLabel = new JLabel(Texts.mainTitle[language]);
        mainTitleLabel.setFont(new Font("Serif", Font.ITALIC, 56));
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitleLabel.setForeground(Color.WHITE);

        startTitleAnimation();
    }

    private void layoutComponents() {
        BackgroundPanel mainPanel = new BackgroundPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 50, 20));
    
        // Center content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        // Image panel with proper centering
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setOpaque(false);
        imagePanel.add(imageLabel);
        
        // Title animation panel
        JPanel titlePanel = new JPanel(null);
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(getWidth(), 100));
        mainTitleLabel.setBounds(titleX, 0, 1000, 100);
        titlePanel.add(mainTitleLabel);
        
        contentPanel.add(imagePanel, BorderLayout.NORTH);
        contentPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Button panel with better spacing
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        buttonPanel.add(startButton, gbc);
        buttonPanel.add(instructionsButton, gbc);
        buttonPanel.add(optionsButton, gbc);
        buttonPanel.add(exitButton, gbc);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
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
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    g.setColor(new Color(255, 230, 0)); // Lighter gold on hover
                } else {
                    g.setColor(getBackground());
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 28));
        button.setBackground(new Color(255, 215, 0)); // Gold
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setPreferredSize(new Dimension(300, 75));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
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
        titleX = getWidth();
        titleTimer = new Timer(16, e -> {
            titleX -= 2;
            if (titleX + mainTitleLabel.getWidth() < 0) {
                titleX = getWidth();
            }
            mainTitleLabel.setLocation(titleX, mainTitleLabel.getY());
            repaint(); // Full repaint - less efficient but guaranteed to work
        });
        titleTimer.start();
    }
    private class BackgroundPanel extends JPanel {
        public BackgroundPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            
            // Draw background image with better quality
            if (backgroundLoaded) {
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Create a nice gradient fallback
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 100, 0),
                              getWidth(), getHeight(), new Color(0, 60, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            
            // Add dark overlay for better text visibility
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

}
