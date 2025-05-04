package main.controller;

import java.util.ArrayList;
import java.util.List;
import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.model.DifficultyStrategy;
import main.view.BlackjackGUI;
import main.view.Texts;
import static main.view.BlackJackMenu.language;

/**
 * The DealerManager class is responsible for managing the dealer's actions and interactions
 * in the game. It handles the dealer's turn, decision-making based on the difficulty strategy,
 * and updates the game state for players. It also provides utility methods to retrieve dealer
 * information such as balance, score, and hand.
 */
public class DealerManager {
    private Player dealer;
    private List<Player> players;
    private Deck deck;
    private DifficultyStrategy difficultyStrategy;
    private BlackjackGUI gui;
    private BettingManager bettingManager;
    private GameManager gameManager;

    /**
     * Constructs a DealerManager instance with the specified dealer, players, deck, difficulty strategy,
     * GUI, betting manager, and game manager.
     *
     * @param dealer The dealer player object.
     * @param players The list of players in the game.
     * @param deck The deck of cards used in the game.
     * @param difficultyStrategy The strategy used to determine the dealer's actions.
     * @param gui The BlackjackGUI instance for updating the game state visually.
     * @param bettingManager The BettingManager instance for handling bets and payouts.
     * @param gameManager The GameManager instance for managing the overall game state.
     */
    public DealerManager(Player dealer, List<Player> players, Deck deck, 
    DifficultyStrategy difficultyStrategy, 
    BlackjackGUI gui, BettingManager bettingManager, GameManager gameManager) {
        this.dealer = dealer;
        this.players = players;
        this.deck = deck;
        this.difficultyStrategy = difficultyStrategy;
        this.gui = gui;
        this.bettingManager = bettingManager;
        this.gameManager = gameManager;
    }

    /**
     * Executes the dealer's turn. The dealer's actions are determined by the difficulty strategy.
     * The dealer will continue to draw cards until the strategy indicates they should stop or they bust.
     * Updates the game state for all players after each action.
     */
    public void dealerTurn() {
        // Reveal dealer's hidden card first
        // Get initial reference player based on difficulty strategy
        Player referencePlayer = players.get(0); // Or choose the strongest player
        for (Player p : players) {
            if (p.calculateScore() <= 21 && p.calculateScore() > referencePlayer.calculateScore()) {
                referencePlayer = p;
            }
        }

        // Dealer decision loop
        while (true) {
            // Check if dealer must stop
            if (!difficultyStrategy.shouldDealerHit(dealer, referencePlayer)) {
                break;
            }
    
            // Deal new card with animation delay
            Card newCard = deck.dealCard();
            dealer.receiveCard(newCard);
            
            // Update game state for all players
            if (gameManager.isMultiplayerMode()) {
                gameManager.broadcastGameState();
            } else {
                gui.updateGameState((ArrayList<Player>) players, dealer, false, false);
            }
    
            // Immediate bust check
            if (dealer.calculateScore() > 21) {
                checkDealerBust();
                return;
            }
        }
        
        // Final determination after dealer stands
        gameManager.determineWinners();
    }

    /**
     * Checks if the dealer has busted (score > 21). If the dealer busts, updates the game message
     * and awards winnings to players who have not busted.
     */
    private void checkDealerBust() {
        if (dealer.calculateScore() > 21) {
            gui.updateGameMessage(Texts.dealerBusts[language]);
            for (Player player : players) {
                if (player.calculateScore() <= 21) {
                    if (bettingManager.getPlayerBet(player.getName()) > 0) { // <- check here
                        bettingManager.playerWins(player.getName());
                    }
                }
            }
        }
    }

    /**
     * Retrieves the dealer's current balance.
     *
     * @return The dealer's balance as managed by the BettingManager.
     */
    public int getDealerBalance() {
        return bettingManager.getDealerBalance();
    }

    /**
     * Retrieves the dealer's current bet.
     *
     * @return The dealer's bet as managed by the BettingManager.
     */
    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    /**
     * Retrieves the dealer's current hand of cards.
     *
     * @return A list of cards representing the dealer's hand.
     */
    public int getDealerScore() {
        return dealer.calculateScore();
    }

    /**
     * Retrieves the dealer's current hand of cards.
     *
     * @return A list of cards representing the dealer's hand.
     */
    public List<Card> getDealerHand(){
        List<Card> dealerHand = new ArrayList<>();
        for(Card c: dealer.getHand()) {
            dealerHand.add(c);
        }
        return dealerHand;
    }

    /**
     * Retrieves the dealer's current hand of cards.
     *
     * @return A list of cards representing the dealer's hand.
     */
    public Player getDealer() {
        return this.dealer;
    }

    /**
     * Sets the dealer to a new player instance.
     *
     * @param dealer The new dealer player object.
     */
    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }

    /**
     * Retrieves the visible cards of the dealer's hand and the count of hidden cards.
     *
     * @param gameOver Whether the game is over. If true, all cards are visible.
     * @return A DealerCardInfo object containing the visible cards and the count of hidden cards.
     */
    public static class DealerCardInfo {
        public final List<Card> visibleCards;
        public final int hiddenCardCount;
    
        /**
         * A nested class representing the dealer's visible cards and the count of hidden cards.
         */
        public DealerCardInfo(List<Card> visibleCards, int hiddenCardCount) {
            this.visibleCards = visibleCards;
            this.hiddenCardCount = hiddenCardCount;
        }
    }

    /**
     * Retrieves the dealer's score in a formatted string. If the game is over, the full score is shown.
     * Otherwise, only the visible card's score is shown with a placeholder for hidden cards.
     *
     * @return A formatted string representing the dealer's score.
     */
    public DealerCardInfo getVisibleDealerCards(boolean gameOver) {
        List<Card> hand = dealer.getHand();
        List<Card> visibleCards = new ArrayList<>();
        int hiddenCardCount = 0;

        if (gameOver) {
            visibleCards.addAll(hand);
        } else {
            if (!hand.isEmpty()) {
                visibleCards.add(hand.get(0));
                hiddenCardCount = hand.size() - 1;
            }
        }
        return new DealerCardInfo(visibleCards, hiddenCardCount);
    }
    
    /**
     * Retrieves the dealer's score in a formatted string. If the game is over, the full score is shown.
     * Otherwise, only the visible card's score is shown with a placeholder for hidden cards.
     *
     * @return A formatted string representing the dealer's score.
     */
    public String getFormattedDealerScore() {
        if (gameManager.getGameFlowController().isGameOver()) {
            return Texts.guiDealerScore[language] + ": " + dealer.calculateScore();
        } else {
            List<Card> hand = dealer.getHand();
            if (!hand.isEmpty()) {
                int visibleScore = hand.get(0).getValue();
                return Texts.guiDealerScore[language] + ": " + visibleScore + " + ?";
            } else {
                return Texts.guiDealerScore[language] + ": ?";
            }
        }
    }
}
