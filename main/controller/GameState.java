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
 * The GameState class is part of the Memento design pattern and captures the complete state of the game at a specific moment.
 * This class includes player data, dealer data, deck state, and the current game settings like difficulty level and player balances.
 * It is used to save the game state to a file and restore it later, allowing the game to be resumed from the saved state.
 * 
 * <p>The GameState class can be used for two purposes:
 * <ul>
 *   <li>Saving the game state by creating a snapshot of the current game state.</li>
 *   <li>Loading the game state from a JSON file to restore the game to a previous state.</li>
 * </ul>
 * 
 * It interacts with the GameManager to both save and restore the game data.
 */

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Player> players; // List of players in the game
    private Player dealer; // The dealer in the game
    private Deck deck; // The deck of cards used in the game
    private List<Integer> playerBalances; // Balances for each player
    private List<Integer> currentBets; // Current bets placed by each player
    private List<Integer> playerScores; // Scores for each player based on their hands
    private List<List<Card>> playerHands; // The hands of each player
    private List<Card> dealerHand; // The dealer's hand
    private List<Card> deckCards; // The cards remaining in the deck
    private int dealerBalance; // The dealer's balance
    private int dealerBet; // The current bet placed by the dealer
    private int dealerScore; // The dealer's score
    private int currentPlayerIndex; // Index of the current player whose turn it is
    private boolean gameOver; // Whether the game is over or not
    private String currentDifficulty; // The current difficulty level of the game

    // === Constructors ===

    /**
     * Constructs a GameState from the current state of the GameManager.
     * This captures all necessary game data, including players, dealer, deck, and game settings.
     * 
     * @param gm the GameManager instance holding the current game state
     */

    public GameState(GameManager gm) {
        this.players = new ArrayList<>(gm.getPlayerManager().getPlayers());
        this.dealer = gm.getDealerManager().getDealer();
        this.deck = gm.getDeck();
        this.playerBalances = new ArrayList<>(gm.getPlayerManager().getPlayerBalances());
        this.currentBets = new ArrayList<>(gm.getPlayerManager().getPlayerBets());
        this.playerScores = new ArrayList<>(gm.getPlayerManager().getPlayerScores());
        this.playerHands = new ArrayList<>(gm.getPlayerManager().getPlayerHands());
        this.dealerHand = new ArrayList<>(gm.getDealerManager().getDealerHand());
        this.deckCards = new ArrayList<>(gm.getFilteredDeck());
        this.dealerBalance = gm.getDealerManager().getDealerBalance();
        this.dealerBet = gm.getDealerManager().getDealerBet();
        this.dealerScore = gm.getDealerManager().getDealerScore();
        this.currentPlayerIndex = gm.getPlayerManager().getCurrentPlayerIndex();
        this.gameOver = gm.getGameFlowController().isGameOver();
        this.currentDifficulty = gm.getDifficultyStrategy().getDifficultyName();
    }
    /**
     * Constructs a GameState by loading data from a JSON file.
     * The JSON file must be formatted according to the expected game data structure.
     * 
     * @param jsonFile the JSON file containing the saved game state
     * @throws IOException if there is an error reading the file
     */
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
        this.dealerScore = 0; 
        this.currentPlayerIndex = root.get("currentPlayerIndex").asInt();
        this.currentDifficulty = "Medium"; // Default if not saved
    }

    /**
     * Default constructor for creating an empty GameState.
     * This constructor is used when initializing a new game or when loading a game.
     */
    public GameState(){}

    // === Restore Methods ===

    /**
     * Restores the full state of the game, including players, dealer, deck, and other settings.
     * This method is typically called by the GameManager to apply the loaded game state.
     * 
     * @param manager the GameManager instance to restore the game state to
     */
    public void restoreFullState(GameManager manager) {
        manager.getPlayerManager().getPlayers().clear();
        manager.getPlayerManager().getPlayers().addAll(players);
        manager.getDealerManager().getDealer().getHand().clear();
        manager.getDealerManager().getDealer().getHand().addAll(dealerHand);
        manager.getPlayerManager().setCurrentPlayerIndex(currentPlayerIndex);
        manager.getGameFlowController().setGameOver(gameOver);
        manager.getDeck().getCards().clear();
        manager.getDeck().getCards().addAll(deck.getCards());
        restoreDifficulty(manager);
    }

    /**
     * Restores the difficulty level of the game based on the saved state.
     * 
     * @param manager the GameManager instance to apply the difficulty to
     */
    public void restoreDifficulty(GameManager manager) {
        switch (currentDifficulty) {
            case "Easy" -> manager.setDifficultyStrategy(new EasyDifficulty());
            case "Hard" -> manager.setDifficultyStrategy(new HardDifficulty());
            default -> manager.setDifficultyStrategy(new MediumDifficulty());
        }
    }

    // === Save Method ===

    /**
     * Saves the current game state to a file in JSON format.
     * This method uses Jackson to serialize the GameState object into a JSON file.
     * 
     * @param file the file to save the game state to
     * @throws IOException if there is an error writing to the file
     */
    public void saveToFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, this);
    }

    // === Helper Methods ===

    /**
     * Parses the list of players from a JSON node.
     * 
     * @param playersNode the JSON node containing player data
     * @return a list of Player objects
     */
    private List<Player> parsePlayers(JsonNode playersNode) {
        List<Player> players = new ArrayList<>();
        for (JsonNode node : playersNode) {
            String name = node.get("name").asText();
            int balance = node.get("balance").asInt();
            players.add(new Player(name, balance));
        }
        return players;
    }

    /**
     * Parses the dealer data from a JSON node.
     * 
     * @param dealerNode the JSON node containing dealer data
     * @return a Player object representing the dealer
     */
    private Player parseDealer(JsonNode dealerNode) {
        String name = dealerNode.get("name").asText();
        int balance = dealerNode.get("balance").asInt();
        return new Player(name, balance);
    }

    /**
     * Parses a list of cards from a JSON node.
     * 
     * @param cardsNode the JSON node containing card data
     * @return a list of Card objects
     */
    private List<Card> parseCards(JsonNode cardsNode) {
        List<Card> cards = new ArrayList<>();
        for (JsonNode cardNode : cardsNode) {
            String rank = cardNode.get("rank").asText();
            String suit = cardNode.get("suit").asText();
            cards.add(new Card(rank, suit, false));
        }
        return cards;
    }

    /**
     * Extracts the hands of all players.
     * 
     * @param players the list of players
     * @return a list of hands (lists of cards) for each player
     */
    private List<List<Card>> extractHands(List<Player> players) {
        List<List<Card>> hands = new ArrayList<>();
        for (Player player : players) {
            hands.add(new ArrayList<>(player.getHand()));
        }
        return hands;
    }

    /**
     * Extracts the balances for all players.
     * 
     * @param players the list of players
     * @return a list of balances for each player
     */
    private List<Integer> extractBalances(List<Player> players) {
        List<Integer> balances = new ArrayList<>();
        for (Player player : players) {
            balances.add(player.getBalance());
        }
        return balances;
    }

    /**
     * Extracts the current bets for all players.
     * 
     * @param players the list of players
     * @return a list of bets for each player
     */
    private List<Integer> extractBets(List<Player> players) {
        List<Integer> bets = new ArrayList<>();
        for (Player player : players) {
            bets.add(player.getCurrentBet());
        }
        return bets;
    }

     /**
     * Calculates the scores for each player's hand.
     * The score is the sum of the values of the cards in the hand.
     *
     * @param hands a list of hands, where each hand is a list of cards
     * @return a list of scores, one for each player's hand
     */
    private List<Integer> calculateScores(List<List<Card>> hands) {
        List<Integer> scores = new ArrayList<>();
        for (List<Card> hand : hands) {
            int score = hand.stream().mapToInt(Card::getValue).sum();
            scores.add(score);
        }
        return scores;
    }

    // === Getters ===

     /**
     * Retrieves the list of players in the game.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() { return players; }

    /**
     * Retrieves the dealer of the game.
     *
     * @return the dealer
     */
    public Player getDealer() { return dealer; }

    /**
     * Retrieves the deck of cards used in the game.
     *
     * @return the deck of cards
     */
    public Deck getDeck() { return deck; }

    /**
     * Retrieves the list of player balances.
     *
     * @return the list of player balances
     */
    public List<Integer> getPlayerBalances() { return playerBalances; }

    /**
     * Retrieves the list of current bets placed by players.
     *
     * @return the list of current bets
     */
    public List<Integer> getCurrentBets() { return currentBets; }

    /**
     * Retrieves the list of player scores.
     *
     * @return the list of player scores
     */
    public List<Integer> getPlayerScores() { return playerScores; }

    /**
     * Retrieves the list of hands held by each player.
     *
     * @return the list of player hands
     */
    public List<List<Card>> getPlayerHands() { return playerHands; }

    /**
     * Retrieves the dealer's hand.
     *
     * @return the dealer's hand
     */
    public List<Card> getDealerHand() { return dealerHand; }

    /**
     * Retrieves the list of cards in the deck.
     *
     * @return the list of deck cards
     */
    public List<Card> getDeckCards() { return deckCards; }

    /**
     * Retrieves the dealer's balance.
     *
     * @return the dealer's balance
     */
    public int getDealerBalance() { return dealerBalance; }

    /**
     * Retrieves the dealer's current bet.
     *
     * @return the dealer's current bet
     */
    public int getDealerBet() { return dealerBet; }

    /**
     * Retrieves the dealer's score.
     *
     * @return the dealer's score
     */
    public int getDealerScore() { return dealerScore; }

    /**
     * Retrieves the index of the current player.
     *
     * @return the index of the current player
     */
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() { return gameOver; }

    /**
     * Retrieves the current difficulty level of the game.
     *
     * @return the current difficulty
     */
    public String getCurrentDifficulty() { return currentDifficulty; }

    /**
     * Restores the game state to the given GameManager, including players, dealer, deck, and difficulty.
     * Initializes the betting manager with the current balances and bets.
     *
     * @param manager the GameManager instance to restore the state to
     */
    public void restore(GameManager manager) {
        manager.getPlayerManager().setPlayers(new ArrayList<>(players));
        manager.getDealerManager().setDealer(dealer);
        manager.setDeck(deck);
        manager.getPlayerManager().setCurrentPlayerIndex(currentPlayerIndex);
        manager.getGameFlowController().setGameOver(false);

        BettingManager bettingManager = new BettingManager(players, players.get(0).getBalance(), dealerBalance);
        bettingManager.placeDealerBet(dealerBet);
        manager.setBettingManager(bettingManager);
    }

}
