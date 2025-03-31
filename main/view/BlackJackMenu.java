

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
        startButton.setIcon(loadIcon("img/icons/start.png", 32, 32));

        instructionsButton = createStyledButton(Texts.instructions[language]);
        instructionsButton.setIcon(loadIcon("img/icons/instructions.png", 32, 32));

        optionsButton = createStyledButton(Texts.options[language]);
        optionsButton.setIcon(loadIcon("img/icons/options.png", 32, 32));

        exitButton = createStyledButton(Texts.exit[language]);
        exitButton.setIcon(loadIcon("img/icons/exit.png", 32, 32));



        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("img/blackjack.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        mainTitleLabel = new JLabel(Texts.mainTitle[language]);
        mainTitleLabel.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 60));
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitleLabel.setForeground(new Color(255, 255, 255, 230));

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

        // Use a placeholder position, actual movement is handled in the timer
        mainTitleLabel.setBounds(0, 0, 1, 100); 
        titlePanel.add(mainTitleLabel);

        // ✅ You were missing this:
        contentPanel.add(titlePanel, BorderLayout.CENTER);


        // Use a placeholder position, actual movement is handled in the timer
        mainTitleLabel.setBounds(0, 0, 1, 100); // Initial dummy size, will be updated

        titlePanel.add(mainTitleLabel);

        
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
        // Glassmorphism-style semi-transparent panel to hold buttons
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.3f)); // 30% opacity
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Add the button panel to the glass panel
        glassPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add the glass panel to the main panel
        mainPanel.add(glassPanel, BorderLayout.SOUTH);

        
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
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
                Color gradientStart = getModel().isRollover() ? new Color(255, 240, 100) : new Color(255, 215, 0);
                Color gradientEnd = getModel().isRollover() ? new Color(255, 210, 0) : new Color(240, 180, 0);
    
                GradientPaint gp = new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientEnd);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
    
                // Shadow effect
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 40, 40);
    
                super.paintComponent(g);
                g2.dispose();
            }
        };
    
        button.setFont(new Font("Segoe UI", Font.BOLD, 28));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(320, 80));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);            // Center icon + text block
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(15); // space between icon and text

    
        return button;
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is != null) {
                BufferedImage iconImage = ImageIO.read(is);
                Image scaledImage = iconImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                System.err.println("Icon not found: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error loading icon: " + path + " -> " + e.getMessage());
        }
        return null;
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
            int titleWidth = mainTitleLabel.getPreferredSize().width;
            mainTitleLabel.setBounds(titleX, 0, titleWidth, 100);
            mainTitleLabel.setForeground(new Color(255, 255, 255, 230));
            repaint(); // Full repaint - less efficient but guaranteed to work
        });
        titleTimer.start();
    }
    
    private class BackgroundPanel extends JPanel {
        private int xOffset = 0;
        private Timer animationTimer;
    
        public BackgroundPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
            startAnimation();
        }
    
        private void startAnimation() {
            animationTimer = new Timer(30, e -> {
                xOffset -= 1; // adjust speed here
                if (Math.abs(xOffset) >= getWidth()) {
                    xOffset = 0;
                }
                repaint();
            });
            animationTimer.start();
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
    
            if (backgroundLoaded && backgroundImage != null) {
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    
                // ✅ Draw the image twice
                g2d.drawImage(backgroundImage, xOffset, 0, getWidth(), getHeight(), this);
                g2d.drawImage(backgroundImage, xOffset + getWidth(), 0, getWidth(), getHeight(), this);
            } else {
                // fallback gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 100, 0),
                        getWidth(), getHeight(), new Color(0, 60, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
    
            // overlay
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
    
            g2d.dispose();
        }
    }
    
    


    public static void main(String[] args) {
        new BlackJackMenu().setVisible(true);
    }
    

}
