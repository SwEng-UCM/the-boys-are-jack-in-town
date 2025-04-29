package main.controller;

import static main.view.BlackJackMenu.language;
import java.util.ArrayList;
import java.util.List;
import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.model.DifficultyStrategy;
import main.view.BlackjackGUI;
import main.view.Texts;

public class DealerManager {
    private Player dealer;
    private List<Player> players;
    private Deck deck;
    private DifficultyStrategy difficultyStrategy;
    private BlackjackGUI gui;
    private BettingManager bettingManager;
    private GameManager gameManager;

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

public void dealerTurn() {
    Player referencePlayer = players.get(0); // Or choose the strongest player
    for (Player p : players) {
        if (p.calculateScore() <= 21 && p.calculateScore() > referencePlayer.calculateScore()) {
            referencePlayer = p;
        }
    }

    while (difficultyStrategy.shouldDealerHit(dealer, referencePlayer)) {
        dealer.receiveCard(deck.dealCard());
    }
    checkDealerBust();
    gameManager.determineWinners();
}

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


    
    public int getDealerBalance() {
        return bettingManager.getDealerBalance();
    }

    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    public int getDealerScore() {
        return dealer.calculateScore();
    }

    
    public List<Card> getDealerHand(){
        List<Card> dealerHand = new ArrayList<>();
        for(Card c: dealer.getHand()) {
            dealerHand.add(c);
        }
        return dealerHand;
    }

    public Player getDealer() {
        return this.dealer;
    }

    
    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }


}
