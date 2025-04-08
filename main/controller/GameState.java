package main.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.Card;
import main.model.Deck;
import main.model.Player;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameState newGameState;
    private GameState loadedGameState;

    private List<Player> players;
    private Player dealer;
    private Deck deck;
    private int numPlayers;
    private int playerBalance;
    private int currentBet;
    private int dealerBalance;
    private int dealerBet;
    private int dealerScore;
    private List<Integer> playerScores;
    private List<List<Card>> playerHands;
    private List<Card> dealerHand;
    private List<Card> deckCards;

    // Add constructor for JSON deserialization
    public GameState(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonData = mapper.readValue(jsonFile, Map.class);
        Map<String, Object> gameData = (Map<String, Object>) jsonData.get("game");

        this.numPlayers = (int) gameData.get("num_players");
        this.playerBalance = (int) gameData.get("playerBalance");
        this.currentBet = (int) gameData.get("currentBet");
        this.dealerBalance = (int) gameData.get("dealerBalance");
        this.dealerBet = (int) gameData.get("dealerBet");
        this.dealerScore = (int) gameData.get("dealerScore");
        this.playerScores = (List<Integer>) gameData.get("playerScores");

        // Convert JSON card data to Card objects
        this.playerHands = convertJsonToCardLists((List<List<Map<String, String>>>) gameData.get("playerHand"));
        this.dealerHand = convertJsonToCards((List<Map<String, String>>) gameData.get("dealerHand"));
        this.deckCards = convertJsonToCards((List<Map<String, String>>) gameData.get("deck"));
    }

    private List<Card> convertJsonToCards(List<Map<String, String>> jsonCards) {
        return jsonCards.stream()
                .map(cardMap -> new Card(cardMap.get("suit"), cardMap.get("rank"), false))
                .collect(Collectors.toList());
    }

    private List<List<Card>> convertJsonToCardLists(List<List<Map<String, String>>> jsonCardLists) {
        return jsonCardLists.stream()
                .map(this::convertJsonToCards)
                .collect(Collectors.toList());
    }

    // Getters
    public List<Player> getPlayers() { return players; }
    public Player getDealer() { return dealer; }
    public Deck getDeck() { return deck; }

    public int getNumPlayers() {
        return numPlayers;
    }
    public int getPlayerBalance() {
        return playerBalance;
    }
    public int getCurrentBet() {
        return currentBet;
    }
    public int getDealerBalance() {
        return dealerBalance;
    }
    public int getDealerBet() {
        return dealerBet;
    }
    public int getDealerScore() {
        return dealerScore;
    }
    public List<Integer> getPlayerScores() {
        return playerScores;
    }
    public List<List<Card>> getPlayerHands() {
        return playerHands;
    }
    public List<Card> getDealerHand() {
        return dealerHand;
    }
    public List<Card> getDeckCards() {
        return deckCards;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Game State ===\n");
        sb.append("Number of Players: ").append(getNumPlayers()).append("\n");
        sb.append("Player Balance: ").append(getPlayerBalance()).append("\n");
        sb.append("Current Bet: ").append(getCurrentBet()).append("\n");
        sb.append("Dealer Balance: ").append(getDealerBalance()).append("\n");
        sb.append("Dealer Bet: ").append(getDealerBet()).append("\n");
        sb.append("Dealer Score: ").append(getDealerScore()).append("\n");

        sb.append("\n-- Player Scores --\n");
        List<Integer> scores = getPlayerScores();
        if (scores != null) {
            for (int i = 0; i < scores.size(); i++) {
                sb.append("Player ").append(i + 1).append(": ").append(scores.get(i)).append("\n");
            }
        }

        sb.append("\n-- Player Hands --\n");
        List<List<Card>> hands = getPlayerHands();
        if (hands != null) {
            for (int i = 0; i < hands.size(); i++) {
                sb.append("Player ").append(i + 1).append(": ").append(hands.get(i)).append("\n");
            }
        }

        sb.append("\n-- Dealer Hand --\n");
        sb.append(getDealerHand()).append("\n");

        sb.append("\n-- Deck Cards --\n");
        sb.append(getDeckCards()).append("\n");

        sb.append("\n-- Players --\n");
        if (getPlayers() != null) {
            for (Player p : getPlayers()) {
                sb.append(p).append("\n");
            }
        }

        sb.append("\n-- Dealer --\n");
        sb.append(getDealer()).append("\n");

        sb.append("\n-- Deck --\n");
        sb.append(getDeck()).append("\n");

        return sb.toString();
    }

}

