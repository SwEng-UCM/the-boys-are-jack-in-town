package main.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import main.model.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Memento Class: Captures and restores the complete game state.
 * Includes player data, dealer data, deck state, and difficulty level.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Player> players;
    private Player dealer;
    private Deck deck;
    private List<Integer> playerBalances;
    private List<Integer> currentBets;
    private List<Integer> playerScores;
    private List<List<Card>> playerHands;
    private List<Card> dealerHand;
    private List<Card> deckCards;
    private int dealerBalance;
    private int dealerBet;
    private int dealerScore;
    private int currentPlayerIndex;
    private boolean gameOver;
    private String currentDifficulty;

    // === Constructors ===

    // For saving the game state
    public GameState(GameManager gm) {
        this.players = new ArrayList<>(gm.getPlayers());
        this.dealer = gm.getDealer();
        this.deck = gm.getDeck();
        this.playerBalances = new ArrayList<>(gm.getPlayerBalances());
        this.currentBets = new ArrayList<>(gm.getPlayerBets());
        this.playerScores = new ArrayList<>(gm.getPlayerScores());
        this.playerHands = new ArrayList<>(gm.getPlayerHands());
        this.dealerHand = new ArrayList<>(gm.getDealerHand());
        this.deckCards = new ArrayList<>(gm.getFilteredDeck());
        this.dealerBalance = gm.getDealerBalance();
        this.dealerBet = gm.getDealerBet();
        this.dealerScore = gm.getDealerScore();
        this.currentPlayerIndex = gm.getCurrentPlayerIndex();
        this.gameOver = gm.isGameOver();
        this.currentDifficulty = gm.getDifficultyStrategy().getDifficultyName();
    }

    // For loading from a JSON file
    public GameState(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile);

        this.players = parsePlayers(root.get("players"));
        this.dealer = parseDealer(root.get("dealer"));
        this.deck = new Deck();
        this.deck.getCards().addAll(parseCards(root.get("deck").get("allCards")));
        this.playerHands = extractHands(players);
        this.playerBalances = extractBalances(players);
        this.currentBets = extractBets(players);
        this.playerScores = calculateScores(playerHands);
        this.dealerHand = parseCards(root.get("dealer").get("hand"));
        this.dealerBalance = root.get("dealer").get("balance").asInt();
        this.dealerBet = root.get("dealer").get("currentBet").asInt();
        this.dealerScore = 0; // optional: calculate if needed
        this.currentPlayerIndex = root.get("currentPlayerIndex").asInt();
        this.currentDifficulty = "Medium"; // Default if not saved
    }

    public GameState(){}

    // === Restore Methods ===

    public void restoreFullState(GameManager manager) {
        manager.getPlayers().clear();
        manager.getPlayers().addAll(players);
        manager.getDealer().getHand().clear();
        manager.getDealer().getHand().addAll(dealerHand);
        manager.setCurrentPlayerIndex(currentPlayerIndex);
        manager.setGameOver(gameOver);
        manager.getDeck().getCards().clear();
        manager.getDeck().getCards().addAll(deck.getCards());
        restoreDifficulty(manager);
    }

    public void restoreDifficulty(GameManager manager) {
        switch (currentDifficulty) {
            case "Easy" -> manager.setDifficultyStrategy(new EasyDifficulty());
            case "Hard" -> manager.setDifficultyStrategy(new HardDifficulty());
            default -> manager.setDifficultyStrategy(new MediumDifficulty());
        }
    }

    // === Save Method ===

    public void saveToFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, this);
    }

    // === Helper Methods ===

    private List<Player> parsePlayers(JsonNode playersNode) {
        List<Player> players = new ArrayList<>();
        for (JsonNode node : playersNode) {
            String name = node.get("name").asText();
            int balance = node.get("balance").asInt();
            players.add(new Player(name, balance));
        }
        return players;
    }

    private Player parseDealer(JsonNode dealerNode) {
        String name = dealerNode.get("name").asText();
        int balance = dealerNode.get("balance").asInt();
        return new Player(name, balance);
    }

    private List<Card> parseCards(JsonNode cardsNode) {
        List<Card> cards = new ArrayList<>();
        for (JsonNode cardNode : cardsNode) {
            String rank = cardNode.get("rank").asText();
            String suit = cardNode.get("suit").asText();
            cards.add(new Card(rank, suit, false));
        }
        return cards;
    }

    private List<List<Card>> extractHands(List<Player> players) {
        List<List<Card>> hands = new ArrayList<>();
        for (Player player : players) {
            hands.add(new ArrayList<>(player.getHand()));
        }
        return hands;
    }

    private List<Integer> extractBalances(List<Player> players) {
        List<Integer> balances = new ArrayList<>();
        for (Player player : players) {
            balances.add(player.getBalance());
        }
        return balances;
    }

    private List<Integer> extractBets(List<Player> players) {
        List<Integer> bets = new ArrayList<>();
        for (Player player : players) {
            bets.add(player.getCurrentBet());
        }
        return bets;
    }

    private List<Integer> calculateScores(List<List<Card>> hands) {
        List<Integer> scores = new ArrayList<>();
        for (List<Card> hand : hands) {
            int score = hand.stream().mapToInt(Card::getValue).sum();
            scores.add(score);
        }
        return scores;
    }

    // === Getters ===

    public List<Player> getPlayers() { return players; }
    public Player getDealer() { return dealer; }
    public Deck getDeck() { return deck; }
    public List<Integer> getPlayerBalances() { return playerBalances; }
    public List<Integer> getCurrentBets() { return currentBets; }
    public List<Integer> getPlayerScores() { return playerScores; }
    public List<List<Card>> getPlayerHands() { return playerHands; }
    public List<Card> getDealerHand() { return dealerHand; }
    public List<Card> getDeckCards() { return deckCards; }
    public int getDealerBalance() { return dealerBalance; }
    public int getDealerBet() { return dealerBet; }
    public int getDealerScore() { return dealerScore; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public boolean isGameOver() { return gameOver; }
    public String getCurrentDifficulty() { return currentDifficulty; }

    // === toString ===

    @Override
    public String toString() {
        return String.format("""
                === Game State ===
                Players: %s
                Dealer: %s
                Deck Size: %d
                Player Balances: %s
                Current Bets: %s
                Player Scores: %s
                Dealer Balance: %d
                Dealer Bet: %d
                Dealer Hand: %s
                Current Difficulty: %s
                """, players, dealer, deck.getCards().size(),
                playerBalances, currentBets, playerScores,
                dealerBalance, dealerBet, dealerHand, currentDifficulty);
    }

    // CARETAKER asks GameState (Memento) to restore GameManager (Originator)
    public void restore(GameManager manager) {
        manager.setPlayers(new ArrayList<>(players));
        manager.setDealer(dealer);
        manager.setDeck(deck);
        manager.setCurrentPlayerIndex(currentPlayerIndex);
        manager.setGameOver(false);

        BettingManager bettingManager = new BettingManager(players, players.get(0).getBalance(), dealerBalance);
        bettingManager.placeDealerBet(dealerBet);
        manager.setBettingManager(bettingManager);
    }

}
