package main.controller;

import main.model.Dealer;
import main.model.Player;
import main.view.View;

public class Main {

    public static void main(String[] args) {
        View view = new View();
        Dealer dealer = new Dealer();
        Player player = new Player();

        view.showMessage("Starting a new game...");
        dealer.startNewGame();

        System.out.println("/////////////");
       // System.out.println(dealer.getPlayer().calculateScore()+ " - "+ dealer.getDealer().calculateScore());

        dealer.decideWinner(dealer.getDealer().calculateScore(), dealer.getPlayer().calculateScore());



    }
}