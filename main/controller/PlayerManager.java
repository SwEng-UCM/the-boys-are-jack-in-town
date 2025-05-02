package main.controller;

import java.util.ArrayList;
import java.util.List;
import main.model.Card;
import main.model.Deck;
import main.model.Player;

public class PlayerManager {
    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    public Player getCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null; // In case there are no players or we've reached the dealer turn
    }

    public boolean isCurrentPlayerStillInRound() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return false;
        }
        // Player is out of the round if they stood or busted
        return !currentPlayer.hasStood() && currentPlayer.calculateScore() <= 21;
    }

    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }
    
    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public boolean isCurrentPlayer(Player player) {
        return !players.isEmpty() &&
               currentPlayerIndex >= 0 &&
               currentPlayerIndex < players.size() &&
               players.get(currentPlayerIndex).equals(player);
    }
    
    public boolean isCurrentPlayer(String playerName) {
        if (players.isEmpty() || currentPlayerIndex < 0 || currentPlayerIndex >= players.size()) {
            return false;
        }
        Player currentPlayer = players.get(currentPlayerIndex);
        
        return currentPlayer != null && 
               currentPlayer.getName().equalsIgnoreCase(playerName);
    }

    
    public int getPlayerScore(Player currentPlayer) {
        return currentPlayer.calculateScore();
    }

    public int getPlayerBet(Player currentPlayer) {
        return currentPlayer.getCurrentBet();
    }

    public void advanceToNextPlayer() {
        currentPlayerIndex++;
    }

    public Player getPlayerByName(String name) {
        return players.stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public String getPlayerHand(Player player) {
        return player.getHand().toString();
    }

    public int getPlayerBalance(Player player) {
        return player.getBalance();
    }

    public boolean hasNextPlayer() {
        return currentPlayerIndex < players.size()-1;
    }

    public List<Integer> getPlayerScores() {
        List<Integer> pScores = new ArrayList<>();
        for(Player p : players) {
            pScores.add(p.calculateScore());
        }
        return pScores;
    }

    public List<Integer> getPlayerBalances(){
        List<Integer> pBalances = new ArrayList<>();
        for(Player p : players) {
            pBalances.add(p.getBalance());
        }
        return pBalances;
    }

    public List<Integer> getPlayerBets(){
        List<Integer> pBets = new ArrayList<>();
        for(Player p : players) {
            pBets.add(p.getCurrentBet());
        }
        return pBets;
    }

    public List<List<Card>> getPlayerHands() {
        List<List<Card>> playerHands = new ArrayList<>();
        for(Player p : players) {
            List<Card> playerHand = p.getHand();
            playerHands.add(playerHand);
        }
        return playerHands;
    }

    public void addPlayer(String name, int initialBalance) {
        players.add(new Player(name, initialBalance));
    }

    public static class PlayerInfo {
        public final String name;
        public final int score;
        public final int bet;
        public final int balance;
        public final List<Card> hand;
    
        public PlayerInfo(String name, int score, int bet, int balance, List<Card> hand) {
            this.name = name;
            this.score = score;
            this.bet = bet;
            this.balance = balance;
            this.hand = hand;
        }
    }
    
    public List<PlayerInfo> getAllPlayerInfo() {
        List<PlayerInfo> infoList = new ArrayList<>();
        for (Player p : players) {
            infoList.add(new PlayerInfo(
                p.getName(),
                p.calculateScore(),
                p.getCurrentBet(),
                p.getBalance(),
                new ArrayList<>(p.getHand()) // defensive copy
            ));
        }
        return infoList;
    }
    
    public void setPlayerScores(List<Integer> scores) {
        for (int i = 0; i < Math.min(scores.size(), players.size()); i++) {
            players.get(i).setScore(scores.get(i));
        }
    }
    

}
