package main.view;

import javax.swing.*;
import java.awt.*;

public class Texts {
    Texts(){}


    public static String[] startGame =
            {"Start Game!","Empieza el juego!", "Tús a chur leis an gcluiche!", "Kezdjük a játékot!", "ابدأ اللعبة", "Commencer le jeu!"};

    public static String[] instructions =
            {"Instructions","Instrucciones", "Treoir",
                    "Szabályok", " تعليمات", "Instructions"};

    public static String[] exit =
            {"Exit","Salida", "Slí Amach",
                    "Kilépés", "خروج", "Quitter"};

    public static String[] options =
            {"Options", "Opciónes", "Rogha",
                    "Opciók", "خيارات", "Options"};


    public static String[] guiTitle =
            {"Blackjack Game","El juego de BlackJack", "cluiche BlackJack",
                    "Huszonegy", "لعبة البلاك جاك","Jeu de Blackjack"};

    public static String[] guiHit =
            {"Hit","Robar", "tháinig sé",
                            "Lapkérés", "سحب", "Tirer"};

    public static String[] guiStand =
            {"Stand","Plantar", " folamh le",
                    "\n" +
                            "Állj", "توقف", "Rester"};

    public static String[] guiNewGame =
            {"New Game","Juego nuevo", "cluiche nua",
                            "Új játék", "لعبة جديدة" , "Nouvelle Partie"};

    public static String[] guiDealerScore =
            {"Dealer's Score ","El scor del distribudor", "scor le déanta",
                    "Osztó pontszáma", "نتيجة الموزع", "Score du Croupier"};

    public static String[] guiPlayerScore =
            {"Player's Score ","Puntuación del jugador", "scor le imreoir",
                    "Játékos pontszáma", "نتيجة اللاعب", "Score du Joueur"};

    public static String[] guiWelcome =
            {"Welcome to BlackJack!","Bienvenido a BlackJack", "failte go dti BlackJack",
                    "Üdv a játékban", "مرحبًا بك في بلاك جاك!", "Bienvenue au Blackjack!"};

    public static String[] guiBackToMain =
            {"Back to Main Menu.","Salida al menú principal", "ar ais go dtí an príomh-roghchlár",
                    "Vissza a főmenübe", "العودة إلى القائمة الرئيسية", "Retour au menu principal"};

    public static String[] gameManagerGameOn  =
            {"Game On! Your turn.",
                    "¡Juego encendido! Tu turno.", "Cluiche Ar! Do sheal.",
                            "Game On! Te jössz.", "اللعبة بدأت! دورك الآن.", "C'est parti ! À vous."};

    public static String[] dealerBlackjack =
            {"Dealer has Blackjack! Dealer wins!", "¡El repartidor tiene Blackjack! ¡El repartidor gana!", "Tá Blackjack ag an dáileoir! Buaileann an dáileoir!", "Az osztónak Blackjackje van! Az osztó nyer!", "الموزع لديه بلاك جاك! الموزع يفوز!", "Le croupier a un Blackjack ! Il gagne !"};

    public static String[] playerBlackjack =
            {"Player has Blackjack! Player wins!", "¡El jugador tiene Blackjack! ¡El jugador gana!", "Tá Blackjack ag an imreoir! Buaileann an t-imreoir!", "A játékosnak Blackjackje van! A játékos nyer!", "اللاعب لديه بلاك جاك! اللاعب يفوز!", "Le joueur a un Blackjack ! Il gagne !"};

    public static String[] playerBusts =
            {"Player busts! Dealer wins!", "¡El jugador se pasa! ¡El repartidor gana!", "Tá an t-imreoir imithe thar fóir! Buaileann an dáileoir!", "A játékos túllépte a 21-et! A kereskedő nyer!", "تجاوز اللاعب 21! الموزع يفوز!", "Le joueur dépasse 21 ! Le croupier gagne !"};

    public static String[] dealerBusts =
            {"Dealer busts! Player wins!", "¡El repartidor se pasa! ¡El jugador gana!", "Tá an dáileoir imithe thar fóir! Buaileann an t-imreoir!", "A kereskedő túllépte a 21-et! A játékos nyer!", "تجاوز الموزع 21! اللاعب يفوز!", "Le croupier dépasse 21 ! Le joueur gagne !"};

    public static String[] playerWins =
            {"Player wins!", "¡El jugador gana!", "Buaileann an t-imreoir!", "A játékos nyer!", "اللاعب يفوز!","Le joueur gagne !"};

    public static String[] dealerWins =
            {"Dealer wins!", "¡El repartidor gana!", "Buaileann an dáileoir!", "Az osztó nyer!", "الموزع يفوز!",  "Le croupier gagne !"};
            

    public static String[] tie =
            {"It's a tie!", "¡Es un empate!", "Tá sé cothrom!", "Döntetlen!", "إنها تعادل!", "Égalité !"};

        public static String[] enterBet =
                {"Enter Bet:", "Ingrese apuesta:", "Iontráil geall:", "Tegye meg a tétet:", "أدخل الرهان:", "Saisir mise :"};

        public static String[] placeBet =
                {"Place Bet", "Realizar apuesta", "Cuir geall", "Fogadás", "ضع الرهان", "Placer mise"};

        public static String[] balance =
                {"Balance:", "Saldo:", "Iarmhéid:", "Egyenleg:", "الرصيد:", "Solde :"};

        public static String[] bet =
                {"Bet:", "Apuesta:", "Geall:", "Tét:", "الرهان:", "Mise :"};

                public static String[] dealerBalance =
                {"Dealer Balance:", "Saldo del crupier:", "Iarmhéid an déileálaí:", "Osztó egyenlege:", "رصيد الموزع:", "Solde du croupier:"};
        
        public static String[] dealerBet =
                {"Dealer Bet:", "Apuesta del crupier:", "Geall an déileálaí:", "Osztó tétje:", "رهان الموزع:", "Mise du croupier:"};
        public static String[][] instructionsPopup = {
                { // English
                            "Instructions:",
                            "1. Click 'Hit' to draw a card.",
                            "2. Click 'Stand' to end your turn.",
                            "3. Try to get as close to 21 without going over."
                },
                { // Spanish 🇪🇸
                            "Instrucciones:",
                            "1. Haz clic en 'Robar' para sacar una carta.",
                            "2. Haz clic en 'Plantarse' para terminar tu turno.",
                            "3. Intenta acercarte a 21 sin pasarte."
                },
                { // Irish (Gaeilge) 🇮🇪
                            "Treoracha:",
                            "1. Cliceáil 'Buail' chun cárta a tharraingt.",
                            "2. Cliceáil 'Fanacht' chun do sheal a chríochnú.",
                            "3. Déan iarracht teacht chomh gar do 21 agus is féidir gan dul thar fóir."
                },
                { // Hungarian 🇭🇺
                            "Utasítások:",
                            "1. Kattints a 'Lapkérés' gombra egy lap húzásához.",
                            "2. Kattints az 'Állj' gombra a köröd befejezéséhez.",
                            "3. Próbálj meg minél közelebb kerülni a 21-hez anélkül, hogy túllépnéd."
                },
                { // Arabic 🇸🇦
                            "التعليمات:",
                            "1. اضغط على 'سحب' لسحب بطاقة.",
                            "2. اضغط على 'توقف' لإنهاء دورك.",
                            "3. حاول الاقتراب من 21 دون تجاوزها."
                },
                { // French 🇫🇷
                            "Instructions :",
                            "1. Cliquez sur 'Tirer' pour piocher une carte.",
                            "2. Cliquez sur 'Rester' pour terminer votre tour.",
                            "3. Essayez d'atteindre 21 sans le dépasser."
                }
                };

        public static String[] betError = {
                "Invalid bet amount or insufficient balance.",  // English 🇬🇧
                "Cantidad de apuesta no válida o saldo insuficiente.",  // Spanish 🇪🇸
                "Méid geall neamhbhailí nó iarmhéid neamhleor.",  // Irish 🇮🇪
                "Érvénytelen tétösszeg vagy elégtelen egyenleg.",  // Hungarian 🇭🇺
                "مبلغ الرهان غير صالح أو رصيد غير كافٍ.",  // Arabic 🇸🇦
                "Montant de mise invalide ou solde insuffisant."  // French 🇫🇷
        };
        public static String[] error = {
                "Error",  // English 🇬🇧
                "Error",  // Spanish 🇪🇸 (same spelling)
                "Earráid",  // Irish 🇮🇪
                "Hiba",  // Hungarian 🇭🇺
                "خطأ",  // Arabic 🇸🇦
                "Erreur"  // French 🇫🇷
        };
        public static String[] invalidInput = {
                "Please enter a valid number.",  // English 🇬🇧
                "Por favor, ingrese un número válido.",  // Spanish 🇪🇸
                "Cuir isteach uimhir bhailí, le do thoil.",  // Irish 🇮🇪
                "Kérjük, adjon meg egy érvényes számot.",  // Hungarian 🇭🇺
                "يرجى إدخال رقم صالح.",  // Arabic 🇸🇦
                "Veuillez entrer un nombre valide."  // French 🇫🇷
        };
        public static String[] invalidInputTitle = {
                "Invalid Input",  // English 🇬🇧
                "Entrada no válida",  // Spanish 🇪🇸
                "Ionchur neamhbhailí",  // Irish 🇮🇪
                "Érvénytelen bevitel",  // Hungarian 🇭🇺
                "إدخال غير صالح",  // Arabic 🇸🇦
                "Entrée invalide"  // French 🇫🇷
 };
            
            
            
                    
                    
        



        




}

    // instructions main menu
    // options menu
    // specialcards...
