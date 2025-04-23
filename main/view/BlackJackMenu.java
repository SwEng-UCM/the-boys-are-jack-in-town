

package main.view;

import main.controller.GameManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import main.model.EasyDifficulty;
import main.model.MediumDifficulty;
import main.model.HardDifficulty;


import static main.view.Languages.*;

public class BlackJackMenu extends JFrame {
    private JButton startButton, multiplayerButton, instructionsButton, exitButton, optionsButton, loadGameButton;
    private JLabel imageLabel, mainTitleLabel;
    private BufferedImage backgroundImage;
    private boolean backgroundLoaded = false;
    private JComboBox<String> difficultyComboBox;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int gameHeight = (int) screenSize.getHeight();
    private int gameWidth = (int)screenSize.getWidth();
    private JPanel titlePanel;

    
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
        
        SwingUtilities.invokeLater(() -> {
            titleX = getWidth(); // Ensure width is valid after window is visible
            startTitleAnimation();
        });
    
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                titleX = getWidth(); // Reset to full width on resize
            }
        });
        
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

        multiplayerButton = createStyledButton("Multiplayer");
    
        instructionsButton = createStyledButton(Texts.instructions[language]);
        instructionsButton.setIcon(loadIcon("img/icons/instructions.png", 32, 32));
    
        optionsButton = createStyledButton(Texts.options[language]);
        optionsButton.setIcon(loadIcon("img/icons/options.png", 32, 32));
    
        loadGameButton = createStyledButton("Load Game");
    
        exitButton = createStyledButton(Texts.exit[language]);
        exitButton.setIcon(loadIcon("img/icons/exit.png", 32, 32));
    
        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("img/blackjack.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance((int) (gameWidth*0.2), (int)(gameHeight*0.2), Image.SCALE_SMOOTH);
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
    
        // Content panel (using BorderLayout)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
    
        // Image panel (adjust size to screen)
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setOpaque(false);
        imagePanel.add(imageLabel);
    
        // Title panel (null layout to position title directly)
        titlePanel = new JPanel(null);
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(gameWidth, (int)(gameHeight * 0.2)));

       // mainTitleLabel.setBounds(gameWidth, 0, mainTitleLabel.getPreferredSize().width, (int)(gameHeight * 0.15));

        mainTitleLabel.setBounds(50, 0, 600, 80); // Force it to a visible spot and fixed size
       
        titlePanel.add(mainTitleLabel);
        titlePanel.setComponentZOrder(mainTitleLabel, 0);

        // ✅ Stack the title and image panels vertically
        JPanel stackedTopPanel = new JPanel();
        stackedTopPanel.setLayout(new BoxLayout(stackedTopPanel, BoxLayout.Y_AXIS));
        stackedTopPanel.setOpaque(false);
        stackedTopPanel.add(imagePanel);
        stackedTopPanel.add(titlePanel);

        // ✅ Now add that to the NORTH position
        contentPanel.add(stackedTopPanel, BorderLayout.NORTH);


    
        //contentPanel.add(titlePanel, BorderLayout.NORTH);
    
        // Button panel (using GridBagLayout for buttons)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 2)); // Increased padding
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 5, 0);  // Adjusted insets for more spacing between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        buttonPanel.add(startButton, gbc);
        buttonPanel.add(multiplayerButton, gbc);
        buttonPanel.add(loadGameButton, gbc);
        buttonPanel.add(instructionsButton, gbc);
        buttonPanel.add(optionsButton, gbc);
        buttonPanel.add(exitButton, gbc);
    
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
        lowerPanel.setOpaque(false);
        lowerPanel.add(buttonPanel);
    
        // Create glassPanel with space below
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.3f));
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        glassPanel.add(lowerPanel, BorderLayout.CENTER);
    
        
        // Add everything to the main panel
        //contentPanel.add(imagePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add spacerPanel and then glassPanel to the south
        mainPanel.add(glassPanel, BorderLayout.SOUTH);   // Then add glassPanel after spacer
    
        add(mainPanel);
    
        // Refresh UI to make sure everything is rendered properly
        revalidate();
        repaint();
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
        });

        loadGameButton.addActionListener(e -> {
            new LoadGamePanel(this);
            dispose();
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
    
        // Make the button size based on gameWidth and gameHeight
        int buttonWidth = (int) (gameWidth * 0.2);  // Set button width to 20% of screen width
        int buttonHeight = (int) (gameHeight * 0.08); // Set button height to 10% of screen height
    
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setFont(new Font("Segoe UI", Font.BOLD, gameHeight/30));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        button.setHorizontalAlignment(SwingConstants.CENTER);  // Center icon + text block
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
        SwingUtilities.invokeLater(() -> {
            titleX = getWidth()/2;
            titleTimer = new Timer(16, e -> {
                float fontSize = gameHeight * 0.05f;
                mainTitleLabel.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, (int) fontSize));
            
                titleX -= 3;
                if (titleX + mainTitleLabel.getPreferredSize().width < 0) {
                    titleX = getWidth();
                }
            
                if (titlePanel.getHeight() > 0) {
                    int titleHeight = mainTitleLabel.getPreferredSize().height;
                    int panelHeight = titlePanel.getHeight();
                    int y = (int) ((panelHeight - titleHeight) / 4);
            
                    mainTitleLabel.setBounds(titleX, y, mainTitleLabel.getPreferredSize().width, titleHeight);
                }
            
                mainTitleLabel.repaint();
            });
            
                titleTimer.start();
                System.out.println("Animation started at width: " + getWidth());

        });
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
