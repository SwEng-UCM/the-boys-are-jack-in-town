package main.view;

import main.controller.AudioManager;
import main.controller.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PausePanel {

    private final JFrame parent;
    private final GameManager gameManager;
    private final int language;
    private  BlackjackGUI blackjackGUI;

    public PausePanel(JFrame parent, GameManager gameManager, BlackjackGUI gui, int language) {
        this.parent = parent;
        this.blackjackGUI = gui;
        this.gameManager = gameManager;
        this.language = language;
    }

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

//    private JPanel createGlassPanel() {
//        return new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                g2.setComposite(AlphaComposite.SrcOver.derive(0.75f));
//                g2.setColor(Color.WHITE);
//                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
//                g2.setColor(new Color(0, 0, 0, 50));
//                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 30, 30);
//                g2.dispose();
//            }
//         {
//            setLayout(new BorderLayout());
//            setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
//            setOpaque(false);
//        }};
//    }

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

    private JButton createPauseButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

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

    private void resumeGame() {
        gameManager.getGameFlowController().resumeGame();
    }

    private void returnToMainMenu() {
        new BlackJackMenu().setVisible(true);
        parent.dispose();
    }
}
