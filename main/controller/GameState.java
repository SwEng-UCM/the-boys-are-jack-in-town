package main.controller;

import java.io.File;
import java.io.IOException;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import main.model.*;


// MOMENTO CLass
/**
 * Represents the complete game state that can be saved and loaded.
 * Includes player data, dealer data, deck state, and difficulty level.
 */

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    
    // Game entities
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
    private String currentDifficulty; 
    private boolean gameOver;


    public GameState(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonData = mapper.readValue(jsonFile, Map.class);
        Map<String, Object> gameData = (Map<String, Object>) jsonData;
        JsonNode root = mapper.readTree(new File(jsonFile.getPath()));

        List<Player> players = new ArrayList<>();
        List<List<Card>> playerHands = new ArrayList<>();
        List<Integer> playerBalances = new ArrayList<>();
        List<Card> deck = new ArrayList<>();
        List<Integer> playerBets = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();
        List<Integer> playerScores = new ArrayList<>();

        JsonNode playersNode = root.get("players");
        for (JsonNode playerNode : playersNode) {
            String name = playerNode.get("name").asText();
            int balance = playerNode.get("balance").asInt();
            int bet = playerNode.get("currentBet").asInt();
            players.add(new Player(name, balance));
            playerBets.add(bet);
            playerBalances.add(balance);

            JsonNode handNode = playerNode.get("hand");
            List<Card> hand = new ArrayList<>();
            int score = 0;
            for (JsonNode cardNode : handNode) {
                String rank = cardNode.get("rank").asText();
                String suit = cardNode.get("suit").asText();
                int value = cardNode.get("value").asInt();
                score += value;
                hand.add(new Card(rank, suit, false));
            }
            playerHands.add(hand);
            playerScores.add(score);
        }
        JsonNode deckNode = root.get("deck");
        JsonNode allCardsNode = deckNode.get("allCards");
        for(JsonNode cardNode : allCardsNode) {
            String rank = cardNode.get("rank").asText();
            String suit = cardNode.get("suit").asText();
            int value = cardNode.get("value").asInt();
            Card card = new Card(rank, suit, false);

            deck.add(card);
        }
        JsonNode dealerNode = root.get("dealer");
        JsonNode dealerHandNode = dealerNode.get("hand");
        for(JsonNode cardNode : dealerHandNode) {
            String rank = cardNode.get("rank").asText();
            String suit = cardNode.get("suit").asText();
            int value = cardNode.get("value").asInt();
            Card card = new Card(rank, suit, false);

            dealerHand.add(card);
        }
        int cpi = root.get("currentPlayerIndex").asInt();

        this.players = players;
        this.playerHands = playerHands;
        this.deck = new Deck(deck);
        this.dealer = new Player(dealerNode.get("name").toString(), dealerNode.get("balance").asInt());
        this.dealerBalance = dealerNode.get("balance").asInt();
        this.dealerBet = dealerNode.get("currentBet").asInt();
        this.dealerHand = dealerHand;
        this.currentBets = playerBets;
        this.playerBalances = playerBalances;
        this.playerScores = playerScores;
        // this.currentPlayerIndex = cpi;
       this.currentDifficulty = "Medium";

        System.out.println(this.toString());
    }

    // No arg constructor for saving.
    public GameState(GameManager gm) {
        System.out.println("Saving");
        this.currentPlayerIndex = gm.getCurrentPlayerIndex();
        this.playerBalances = gm.getPlayerBalances();
        this.playerScores = gm.getPlayerScores();
        this.players = gm.getPlayers();
        this.dealer = gm.getDealer();
        this.deck = gm.getDeck();
        this.dealerBalance = gm.getDealerBalance();
        this.dealerBet = gm.getDealerBet();
        this.dealerScore = gm.getDealerScore();
        this.currentBets = gm.getPlayerBets();
        this.playerHands = gm.getPlayerHands();
        this.dealerHand = gm.getDealerHand();
        this.deckCards = gm.getFilteredDeck();

        this.currentDifficulty = gm.getDifficultyStrategy().getDifficultyName();

    }
    /**
     * Restores difficulty setting to GameManager
     */
    public void restoreDifficulty(GameManager manager) {
        switch(this.currentDifficulty) {
            case "Easy":
                manager.setDifficultyStrategy(new EasyDifficulty());
                break;
            case "Medium":
                manager.setDifficultyStrategy(new MediumDifficulty());
                break;
            case "Hard":
                manager.setDifficultyStrategy(new HardDifficulty());
                break;
            default:
                manager.setDifficultyStrategy(new MediumDifficulty());
        }
    }

    /**
     * Restores complete game state to GameManager
     */
    public void restoreFullState(GameManager manager) {
        // Restore core entities
        manager.getPlayers().clear();
        manager.getPlayers().addAll(this.players);
        manager.getDealer().getHand().clear();
        manager.getDealer().getHand().addAll(this.dealer.getHand());
        
        // Restore deck
        manager.getDeck().getCards().clear();
        manager.getDeck().getCards().addAll(this.deck.getCards());
        // Restore game progress
        manager.setCurrentPlayerIndex(this.currentPlayerIndex);
        manager.setGameOver(this.gameOver);
        
        
        
        // Restore difficulty
        restoreDifficulty(manager);

    }


    // Getters
    public String getCurrentDifficulty() { return currentDifficulty; }
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
      
    public boolean isGameOver() { return gameOver; }

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
    public GameState(ArrayList<Player> players, Player dealer, Deck deck, int currentPlayerIndex, boolean gameOver) {
        this.players = new ArrayList<>(players); // Deep copy if necessary
        this.dealer = new Player(dealer); // Ensure Player has a copy constructor
        this.deck = new Deck(deck); // Ensure Deck has a copy constructor
        this.currentPlayerIndex = currentPlayerIndex;
        this.gameOver = gameOver;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Game State ===\n");
        sb.append("Player Balances: ").append(getPlayerBalances()).append("\n");
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


