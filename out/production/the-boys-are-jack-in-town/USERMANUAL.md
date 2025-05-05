# 📘 Blackjack 2.0 – User Manual
 
Welcome to **Blackjack 2.0**, a modern twist on the classic casino game!  
This guide will walk you through how to play, configure, and fully enjoy the game.

---

## 🎮 How to Play

1. **Start the Game**
   - Run the game via the provided JAR file or launch from your IDE.
   - Click "Play" from the main menu.

2. **Place Your Bet**
   - Use the slider to select your bet.
   - Your current balance is shown on screen.
   - Click "Confirm Bet" to begin the round.

3. **Gameplay Controls**
   - **Hit** – Draw a card.
   - **Stand** – Stop drawing and let the dealer play.
   - **Undo** – Revert your last action (if enabled).
   - The dealer plays after you stand.

4. **Winning**
   - Get a hand total closer to 21 than the dealer without busting (going over 21).
   - Blackjack (Ace + 10/Face card) wins instantly.

5. **Achievements**
   - Play to unlock visual badges (e.g., First Win, First Blackjack, First Loss).

6. **Options Menu**
   - Change language (English, Spanish, etc.)
   - Select AI dealer difficulty: Easy, Medium, or Hard.

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

