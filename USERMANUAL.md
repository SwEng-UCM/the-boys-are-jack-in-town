# 📘 Blackjack 2.0 – User Manual
 
Welcome to **Blackjack 2.0**, a modern twist on the classic casino game!  
This guide will walk you through how to play, configure, and fully enjoy the game.

---

## 🎮 How to Play

1. **Start the Game**
   - Run the game via the provided JAR file or launch from your IDE.
   - Click "New Game" from the main menu.

2. **Place Your Bet**
   - Use the bet text box to enter your bet.
   - Your current balance is shown on screen.
   - Click "Confirm Bet" to begin the round.

3. **Gameplay Controls**
   - **Hit** – Draw a card.
   - **Stand** – Stop drawing and let the dealer play.
   - **Undo** – Revert your last action (if enabled).
   - The dealer plays after all players stand.

4. **Winning**
   - Get a hand total closer to 21 than the dealer without busting (going over 21).
   - Blackjack (Ace + 10/Face card) wins instantly.
   - Note that the value of an Ace adjusts dynamically between 11 and 1 depending on what is better for you!

5. **Achievements**
   - Play to unlock visual badges (e.g., First Win, First Blackjack, First Loss).

6. **Options Menu**
   - Change language (English, Spanish, etc.)
   - Select AI dealer difficulty: Easy, Medium, or Hard.
  
7. **Online Multiplayer**
   - Choose Multiplayer from the Main Menu.
   - Act as a client or host.
   - Use the hosts IP address and chosen port to join a game.
   - Once all players have joined they can play like usual against the dealer and each other!
   - Only the host may restart the game.

8. **Save and Load**
   - You can save your current game (all cards, bets, balances etc.) in the Pause menu.
   - From there you can load your game using the Load button on the Main Menu and selecting the JSON file "game_state".
   - Note that you MUST save a game first in order to be able to load a game.
  
9. **Pause Menu**
    - Pause and resume the game from the Pause Menu.
    - You can also use the volume slider to control the volume of the background music.
    - The Pause Menu is also where you save games to later load them.

10. **Wild Cards**
    
      *Blackjack Bomb*
    - Effect: Instantly busts any hand it is drawn into—player or dealer.
    - Strategy Tip: Avoid at all costs! If drawn, it overrides your score regardless of previous cards.
    - Visual: Explosive design with white theme.
      
      *Split Ace*
    - Effect: Automatically splits a pair of Aces if you draw one and already hold another Ace.
    - Strategy Tip: This lets you play two powerful hands simultaneously. Take advantage of it to double your chances of winning.
    - Visual: Twin Aces
      
       *Joker Wild*
    - Effect: Acts as any card with a value of your choice between 1 and 11.
    - Strategy Tip: Perfect for filling gaps or reaching a soft 21. You'll be prompted to choose the value when drawn.
    - Visual: A playful, color-shifting Joker card.

      
---

## ⚙️ Project Configuration

To configure and run this project locally:

1. **Clone the repository**
   ```bash
   git clone https://github.com/SwEng-UCM/the-boys-are-jack-in-town.git
   ```
   ```bash
   cd the-boys-are-jack-in-town
   ```
2. **Open the project in VS Code, IntelliJ, Eclipse, or any Java IDE**
3. **Ensure Java 17+ is installed**
   ```bash
    java -version
4. Locate and open the file: `BlackjackGUI.java` in `/src/main/java/blackjack/`

---

## 🚀 Deployment Instructions

1. **Package the project as a JAR file using your IDE or:**
    ```bash
   jar cfe BlackjackGame.jar blackjack.BlackjackGUI -C out .
    ```
2. **To run the JAR:**
    ```bash
   java -jar BlackjackGame.jar
    ```

--- 

## 🙋 FAQ

**Q: What happens if I bust?**  
You lose the round and your bet.

**Q: What does 'Undo' do?**  
It reverts your last action if available.

**Q: Can I play in Spanish?**  
Yes! Use the Options menu to change the game language.

---

## 🧑‍💻 Support

Have an issue or suggestion?  
Open a ticket on our GitHub Issues page:  
[https://github.com/SwEng-UCM/the-boys-are-jack-in-town/issues](https://github.com/SwEng-UCM/the-boys-are-jack-in-town/issues)

