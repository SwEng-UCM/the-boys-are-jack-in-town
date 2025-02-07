package main.controller;

import main.model.Deck;

public class Main {

    public static void main(String[] args) {
       System.out.println("Hello World");

       Deck standard = new Deck();
       standard.prettyPrint();
    }
}