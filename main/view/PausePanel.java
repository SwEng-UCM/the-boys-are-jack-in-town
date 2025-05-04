package main.view;

import main.controller.AudioManager;
import main.controller.GameManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * The PausePanel class represents the pause menu in the game.
 * It provides options to resume the game, save the game, return to the main menu, or exit the game.
 * The menu also includes a volume slider for adjusting the game's audio volume.
 */
public class PausePanel {
    private final JFrame parent;
    private final GameManager gameManager;
    private final int language;

    /**
     * Constructs a PausePanel instance.
     *
     * @param parent The parent JFrame that owns this pause panel.
     * @param gameManager The GameManager instance for managing game state.
     * @param gui The BlackjackGUI instance for updating the game UI.
     * @param language The current language setting for the game.
     */
    public PausePanel(JFrame parent, GameManager gameManager, BlackjackGUI gui, int language) {
        this.parent = parent;
        this.gameManager = gameManager;
        this.language = language;
    }

    /**
     * Displays the pause menu dialog.
     * The menu includes options to resume the game, save the game, return to the main menu, or exit the game.
     * It also includes a volume slider for adjusting the game's audio volume.
     */
    public void showPauseMenu() {
        gameManager.getGameFlowController().pauseGame();

        JDialog pauseDialog = new JDialog(parent, Texts.PAUSE[language], true);
        pauseDialog.setSize(400, 550);
        pauseDialog.setLocationRelativeTo(parent);
        pauseDialog.setUndecorated(true);
        pauseDialog.setLayout(new BorderLayout());

        JPanel glassPanel = createGlassPanel();

        Font font = new Font("Segoe UI", Font.BOLD, 22);
        Color btnColor = new Color(240, 200, 0);

        JButton resumeBtn = createPauseButton(Texts.RESUME[language], font, btnColor);
        JButton saveBtn = createPauseButton(Texts.saveGame[language], font, btnColor);
        JButton menuBtn = createPauseButton(Texts.guiBackToMain[language], font, btnColor);
        JButton exitBtn = createPauseButton(Texts.exitGame[language], font, btnColor);

        resumeBtn.addActionListener(e -> {
            resumeGame();
            pauseDialog.dispose();
        });

        saveBtn.addActionListener(e -> {
            gameManager.save();
            JOptionPane.showMessageDialog(parent, "Game saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);
        });

        menuBtn.addActionListener(e -> {
            pauseDialog.dispose();
            returnToMainMenu();
        });

        exitBtn.addActionListener(e -> System.exit(0));

        JLabel volumeLabel = new JLabel(Texts.VOLUME[language]);
        volumeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        volumeLabel.setForeground(Color.BLACK);
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider volumeSlider = new JSlider(0, 100, (int) (AudioManager.getInstance().getVolume() * 100));
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setOpaque(false);
        volumeSlider.setForeground(Color.BLACK);
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        volumeSlider.addChangeListener(e -> {
            float volume = volumeSlider.getValue() / 100f;
            AudioManager.getInstance().setVolume(volume);
        });

        glassPanel.add(resumeBtn);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        glassPanel.add(saveBtn);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        glassPanel.add(menuBtn);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        glassPanel.add(exitBtn);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        glassPanel.add(volumeLabel);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        glassPanel.add(volumeSlider);

        pauseDialog.add(glassPanel, BorderLayout.CENTER);
        pauseDialog.setBackground(new Color(0, 0, 0, 0));
        pauseDialog.setVisible(true);
    }

    /**
     * Creates a custom glass panel with a semi-transparent background and rounded corners.
     *
     * @return A JPanel with a custom painted background.
     */
    private JPanel createGlassPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.75f));
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 30, 30);
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Creates a custom-styled button for the pause menu.
     *
     * @param text The text to display on the button.
     * @param font The font to use for the button text.
     * @param bgColor The background color of the button.
     * @return A JButton with custom styling and hover effects.
     */
    private JButton createPauseButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    /**
     * Resumes the game by unpausing the game flow controller.
     */
    private void resumeGame() {
        gameManager.getGameFlowController().resumeGame();
    }

    /**
     * Returns to the main menu by disposing of the current game window
     * and opening the main menu.
     */
    private void returnToMainMenu() {
        new BlackJackMenu().setVisible(true);
        parent.dispose();
    }
}
