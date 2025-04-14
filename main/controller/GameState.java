package main.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import main.model.Card;
import main.model.Deck;
import main.model.Player;


// MOMENTO CLass
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Player> players;
    private Player dealer;
    private Deck deck;
    private int currentPlayerIndex;
    private int numPlayers;
    private List<Integer> playerBalances;
    private List<Integer> currentBets;
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


        this.players = ((List<?>) gameData.get("players")).stream()
                .map(p -> mapper.convertValue(p, Player.class))
                .collect(Collectors.toList());
        this.dealer = mapper.convertValue(gameData.get("dealer"), Player.class);
        this.deck = mapper.convertValue(gameData.get("deck"), Deck.class);

        this.currentPlayerIndex = (int) gameData.get("currentPlayerIndex");
        this.numPlayers = (int) gameData.get("num_players");
        this.playerBalances = (List<Integer>) gameData.get("playerBalances");
        this.currentBets = (List<Integer>) gameData.get("currentBets");
        this.dealerBalance = (int) gameData.get("dealerBalance");
        this.dealerBet = (int) gameData.get("dealerBet");
        this.dealerScore = (int) gameData.get("dealerScore");
        this.playerScores = (List<Integer>) gameData.get("playerScores");

        List<List<Map<String, Object>>> rawPlayerHands = mapper.convertValue(gameData.get("playerHand"), List.class);
        this.playerHands = convertJsonToCardLists(rawPlayerHands);

        List<Map<String, Object>> rawDealerHand = mapper.convertValue(gameData.get("dealerHand"), List.class);
        this.dealerHand = convertJsonToCards(rawDealerHand);

        Map<String, Object> deckMap = (Map<String, Object>) gameData.get("deck");
        List<Map<String, Object>> cards = (List<Map<String, Object>>) deckMap.get("cards");
        this.deckCards = convertJsonToCards(cards);
    }

    // No arg constructor for saving.
    public GameState(){

    }

    private List<Card> convertJsonToCards(List<Map<String, Object>> jsonCards) {
        return jsonCards.stream()
                .map(cardMap -> new Card(
                        (String) cardMap.get("rank"),
                        (String) cardMap.get("suit"),
                        cardMap.get("hidden") != null && (boolean) cardMap.get("hidden")
                ))
                .collect(Collectors.toList());
    }

    private List<List<Card>> convertJsonToCardLists(List<List<Map<String, Object>>> jsonCardLists) {
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
    public List<Integer> getPlayerBalances() {
        return playerBalances;
    }
    public List<Integer> getCurrentBets() {
        return currentBets;
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
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // Setters
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setPlayerBalances(List<Integer> playerBalances) {
        this.playerBalances = playerBalances;
    }

    public void setCurrentBets(List<Integer> currentBets) {
        this.currentBets = currentBets;
    }

    public void setDealerBalance(int dealerBalance) {
        this.dealerBalance = dealerBalance;
    }

    public void setDealerBet(int dealerBet) {
        this.dealerBet = dealerBet;
    }

    public void setDealerScore(int dealerScore) {
        this.dealerScore = dealerScore;
    }

    public void setPlayerScores(List<Integer> playerScores) {
        this.playerScores = playerScores;
    }

    public void setPlayerHands(List<List<Card>> playerHands) {
        this.playerHands = playerHands;
    }

    public void setDealerHand(List<Card> dealerHand) {
        this.dealerHand = dealerHand;
    }

    public void setDeckCards(List<Card> deckCards) {
        this.deckCards = deckCards;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void saveToFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, this);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Game State ===\n");
        sb.append("Number of Players: ").append(getNumPlayers()).append("\n");
        sb.append("Player Balance: ").append(getPlayerBalances()).append("\n");
        sb.append("Current Bet: ").append(getCurrentBets()).append("\n");
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

