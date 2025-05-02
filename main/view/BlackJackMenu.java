package main.view;

import main.controller.BlackjackClient;
import main.controller.GameManager;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * The main menu window for the Blackjack game.
 * Displays buttons for starting the game, loading, multiplayer, and accessing options.
 * Handles basic language switching and animated background/title.
 */
public class BlackJackMenu extends JFrame {
    private JButton startButton, instructionsButton, exitButton, optionsButton, loadGameButton, multiplayerButton;
    private JLabel imageLabel, mainTitleLabel;
    private BufferedImage backgroundImage;
    private boolean backgroundLoaded = false;
    private JComboBox<String> difficultyComboBox;
    private JPanel topRightBar;
    private int titleX = 0;
    private Timer titleTimer;
    public static int language = 0;

    /**
     * Constructs and initializes the Blackjack main menu GUI.
     */
    public BlackJackMenu() {
        setTitle(Texts.guiTitle[language]);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Loads the animated background image from resources.
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("resources/img/backgroundimage.png");
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
          
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                titleX = getWidth(); // Reset to full width on resize
            }
        });
        
        // Custom image icon
        ImageIcon icon = new ImageIcon("resources/img/black.png");
        setIconImage(icon.getImage());

        initializeComponents();
        layoutComponents();
        attachEventListeners();
    }

    /**
     * Initializes all GUI components: buttons, title, icons, labels, and language box.
     */
    private void initializeComponents() {
        startButton = createStyledButton(Texts.startGame[language]);
        startButton.setIcon(loadIcon("resources/icons/start.png", 32, 32));

        multiplayerButton = createStyledButton(Texts.multiplayer[language]);
        multiplayerButton.setIcon(loadIcon("resources/icons/multiplayer.png", 32, 32));
    
        instructionsButton = createStyledButton(Texts.instructions[language]);
        instructionsButton.setIcon(loadIcon("resources/icons/instructions.png", 32, 32));
    
        optionsButton = createStyledButton(Texts.options[language]);
        optionsButton.setIcon(loadIcon("resources/icons/options.png", 32, 32));
    
        loadGameButton = createStyledButton(Texts.loadGame[language]);
        loadGameButton.setIcon(loadIcon("resources/icons/loadgame.png", 32, 32));

        exitButton = createStyledButton(Texts.exit[language]);
        exitButton.setIcon(loadIcon("resources/icons/exit.png", 32, 32));
    
        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("resources/img/blackjack.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(400, 219, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
    
        mainTitleLabel = new JLabel(Texts.mainTitle[language]);
        mainTitleLabel.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 60));
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitleLabel.setForeground(new Color(255, 255, 255, 230));
        mainTitleLabel.setOpaque(false);
        mainTitleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel profileLabel = new JLabel("👤 Guest");
        profileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        profileLabel.setForeground(Color.WHITE);

        String[] languages = {"🇬🇧", "🇪🇸", "🇫🇷", "🇮🇪", "🇭🇺", "🇸🇦"};
        JComboBox<String> languageBox = new JComboBox<>(languages);
        languageBox.setSelectedIndex(language); // sync with current language
        languageBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        languageBox.addActionListener(e -> {
            language = languageBox.getSelectedIndex();
            refreshMenu(); // Refresh menu to apply new language
        });

        topRightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topRightBar.setOpaque(false);
        topRightBar.add(profileLabel);
        topRightBar.add(languageBox);

        SwingUtilities.invokeLater(() -> {
            titleX = getWidth(); // Ensure width is valid after window is visible
            startTitleAnimation();
        });

        startTitleAnimation();
    }
    

    /**
     * Lays out all components into the main window using custom panels.
     */
    private void layoutComponents() {
        BackgroundPanel mainPanel = new BackgroundPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 50, 20));
    
        // 🌟 Title panel - now directly added to NORTH
        JPanel titlePanel = new JPanel(null);
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(getWidth(), 100));
        mainTitleLabel.setBounds(0, 0, 1, 100);
        titlePanel.add(mainTitleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH); // <<=== Moved here
    
        // Center content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
    
        // Image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, -20, 0));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.NORTH);
    
        // Button panel setup
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        buttonPanel.add(startButton, gbc);
        buttonPanel.add(loadGameButton, gbc);
        buttonPanel.add(multiplayerButton, gbc);
        buttonPanel.add(instructionsButton, gbc);
        buttonPanel.add(optionsButton, gbc);
        buttonPanel.add(exitButton, gbc);
    
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
        lowerPanel.setOpaque(false);
        lowerPanel.add(buttonPanel);
    
        // Glassmorphism-style panel
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
        glassPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        glassPanel.add(lowerPanel, BorderLayout.CENTER);
    
        contentPanel.add(glassPanel, BorderLayout.SOUTH);
    
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    
    /**
     * Binds action listeners to each interactive button.
     */
    private void attachEventListeners() {
        startButton.addActionListener(e -> {
            GameManager gameManager = GameManager.getInstance();
        
            // 1. Create the GUI
            BlackjackGUI gui = new BlackjackGUI(gameManager);
        
            // 2. Set the GUI FIRST (re-inits controllers)
            gameManager.setGui(gui);
        
            // 3. THEN call startNewGame()
            gameManager.getGameFlowController().startNewGame();
        
            // 4. Show the GUI
            gui.setVisible(true);
            dispose();
        });
        
        
        

        instructionsButton.addActionListener(e -> {
            new InstructionsDialog(this).setVisible(true);
        });
        

        multiplayerButton.addActionListener(e -> MultiplayerSetUpDialog.show(this));

        exitButton.addActionListener(e -> System.exit(0));

        optionsButton.addActionListener(e -> {
            new OptionsPanel(this).setVisible(true);
        });

        loadGameButton.addActionListener(e -> {
            new LoadGamePanel(this);
            dispose();
        });
    }

    /**
     * Creates a styled button with hover gradient and optional icon.
     * @param text Button label text
     * @return A customized JButton
     */
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
    
                g2.setColor(new Color(0, 0, 0, 40)); // Shadow
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
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setIconTextGap(15);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // ✅ Added line
    
        return button;
    }
    

    /**
     * Loads and scales an icon image from the resources directory.
     * @param path Image path relative to classpath
     * @param width Desired icon width
     * @param height Desired icon height
     * @return A scaled ImageIcon, or null if loading fails
     */
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
    
    /**
     * Refreshes all menu texts based on the selected language.
     * Repaints the UI with updated translations.
     */
    public void refreshMenu() {
        setTitle(Texts.guiTitle[language]); // ✅ Correct window banner title
    
        // Update button texts
        startButton.setText(Texts.startGame[language]);
        loadGameButton.setText(Texts.loadGame[language]);
        instructionsButton.setText(Texts.instructions[language]);
        exitButton.setText(Texts.exit[language]);
        optionsButton.setText(Texts.options[language]);
        multiplayerButton.setText(Texts.multiplayer[language]);
    
        // Optionally update main title if desired:
        mainTitleLabel.setText(Texts.mainTitle[language]);
    
        // Repaint UI
        revalidate();
        repaint();
    }
    

    /**
     * Starts the scrolling animation of the title label across the screen.
     */
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
    
    /**
     * Inner class responsible for painting a horizontally scrolling background.
     */
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
}
