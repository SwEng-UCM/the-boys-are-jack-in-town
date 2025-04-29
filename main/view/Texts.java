package main.view;


/**
 * The {@code Texts} class contains all the text strings used throughout the 
 * Blackjack game in multiple languages. These strings are used for various 
 * UI elements, messages, prompts, and game actions. The class provides easy 
 * access to text in different languages (English, Spanish, Irish, Hungarian, 
 * Arabic, and French), making the game multilingual.
 * 
 * This class contains a variety of public static arrays of strings for the 
 * game's messages, including titles, instructions, game actions, error messages, 
 * and more. These arrays are indexed by language, allowing for localization of 
 * the game.
 */

public class Texts {

     /**
     * Constructor for the {@code Texts} class. The constructor is private because 
     * the class is designed to be accessed statically and does not need to be instantiated.
     */
    Texts() {
    }
     /**
     * Array of strings for the "Start Game" text in multiple languages.
     */
    public static String[] startGame =
            {"Start Game!", "Empieza el juego!", "Tús a chur leis an gcluiche!", "Kezdjük a játékot!", "ابدأ اللعبة", "Commencer le jeu!"};

    /**
     * Array of strings for the "Instructions" text in multiple languages.
     */
    public static String[] instructions =
            {"Instructions", "Instrucciones", "Treoir",
                    "Szabályok", " تعليمات", "Instructions"};

    /**
     * Array of strings for the "Exit" text in multiple languages.
     */
    public static String[] exit =
            {"Exit", "Salida", "Slí Amach",
                    "Kilépés", "خروج", "Quitter"};

    /**
     * Array of strings for the "Options" text in multiple languages.
     */
    public static String[] options =
            {"Options", "Opciónes", "Rogha",
                    "Opciók", "خيارات", "Options"};

    /**
     * Array of strings for the title of the game "Blackjack Game" in multiple languages.
     */
    public static String[] guiTitle =
            {"Blackjack Game", "El juego de BlackJack", "cluiche BlackJack",
                    "Huszonegy", "لعبة البلاك جاك", "Jeu de Blackjack"};

    /**
     * Array of strings for the "Hit" button in multiple languages.
     */
    public static String[] guiHit =
            {"Hit", "Robar", "tháinig sé",
                    "Lapkérés", "سحب", "Tirer"};

    /**
     * Array of strings for the "Stand" button in multiple languages.
     */
    public static String[] guiStand =
            {"Stand", "Plantar", " folamh le",
                    "\n" +
                            "Állj", "توقف", "Rester"};

    /**
     * Array of strings for the "New Game" button text in multiple languages.
     */
    public static String[] guiNewGame =
            {"New Game", "Juego nuevo", "cluiche nua",
                    "Új játék", "لعبة جديدة", "Nouvelle Partie"};

    /**
     * Array of strings for the "Dealer's Score" text in multiple languages.
     */
    public static String[] guiDealerScore =
            {"Dealer's Score ", "El scor del distribudor", "scor le déanta",
                    "Osztó pontszáma", "نتيجة الموزع", "Score du Croupier"};

    /**
     * Array of strings for the "Player's Score" text in multiple languages.
     */
    public static String[] guiPlayerScore =
            {"Player's Score ", "Puntuación del jugador", "scor le imreoir",
                    "Játékos pontszáma", "نتيجة اللاعب", "Score du Joueur"};

    /**
     * Array of strings for the "Welcome to Blackjack" text in multiple languages.
     */
    public static String[] guiWelcome =
            {"Welcome to BlackJack!", "Bienvenido a BlackJack", "failte go dti BlackJack",
                    "Üdv a játékban", "مرحبًا بك في بلاك جاك!", "Bienvenue au Blackjack!"};

    /**
     * Array of strings for the "Back to Main Menu" text in multiple languages.
     */
    public static String[] guiBackToMain =
            {"Back to Main Menu.", "Salida al menú principal", "ar ais go dtí an príomh-roghchlár",
                    "Vissza a főmenübe", "العودة إلى القائمة الرئيسية", "Retour au menu principal"};

    /**
     * Array of strings for the game "Game On" message in multiple languages.
     */
    public static String[] gameManagerGameOn =
            {"Game On! Your turn.",
                    "¡Juego encendido! Tu turno.", "Cluiche Ar! Do sheal.",
                    "Game On! Te jössz!", "اللعبة بدأت! دورك الآن.", "C'est parti ! À vous."};

    /**
     * Array of strings for the "Dealer has Blackjack" message in multiple languages.
     */
    public static String[] dealerBlackjack =
            {"Dealer has Blackjack! Dealer wins!", "¡El repartidor tiene Blackjack! ¡El repartidor gana!", "Tá Blackjack ag an dáileoir! Buaileann an dáileoir!", "Az osztónak Blackjackje van! Az osztó nyer!", "الموزع لديه بلاك جاك! الموزع يفوز!", "Le croupier a un Blackjack ! Il gagne !"};

    /**
     * Array of strings for the "Player has Blackjack" message in multiple languages.
     */
    public static String[] playerBlackjack =
            {"Player has Blackjack! Player wins!", "¡El jugador tiene Blackjack! ¡El jugador gana!", "Tá Blackjack ag an imreoir! Buaileann an t-imreoir!", "A játékosnak Blackjackje van! A játékos nyer!", "اللاعب لديه بلاك جاك! اللاعب يفوز!", "Le joueur a un Blackjack ! Il gagne !"};

    /**
     * Array of strings for the "Player busts" message in multiple languages.
     */
    public static String[] playerBusts =
            {"Player busts! Dealer wins!", "¡El jugador se pasa! ¡El repartidor gana!", "Tá an t-imreoir imithe thar fóir! Buaileann an dáileoir!", "A játékos túllépte a 21-et! A kereskedő nyer!", "تجاوز اللاعب 21! الموزع يفوز!", "Le joueur dépasse 21 ! Le croupier gagne !"};

    /**
     * Array of strings for the "Dealer busts" message in multiple languages.
     */
    public static String[] dealerBusts =
            {"Dealer busts! Player wins!", "¡El repartidor se pasa! ¡El jugador gana!", "Tá an dáileoir imithe thar fóir! Buaileann an t-imreoir!", "A kereskedő túllépte a 21-et! A játékos nyer!", "تجاوز الموزع 21! اللاعب يفوز!", "Le croupier dépasse 21 ! Le joueur gagne !"};

    /**
     * Array of strings for the "Player wins" message in multiple languages.
     */
    public static String[] playerWins =
            {"Player wins!", "¡El jugador gana!", "Buaileann an t-imreoir!", "A játékos nyer!", "اللاعب يفوز!", "Le joueur gagne !"};

    /**
     * Array of strings for the "Dealer wins" message in multiple languages.
     */
    public static String[] dealerWins =
            {"Dealer wins!", "¡El repartidor gana!", "Buaileann an dáileoir!", "Az osztó nyer!", "الموزع يفوز!", "Le croupier gagne !"};

    /**
     * Array of strings for the "It's a tie" message in multiple languages.
     */
    public static String[] tie =
            {"It's a tie!", "¡Es un empate!", "Tá sé cothrom!", "Döntetlen!", "إنها تعادل!", "Égalité !"};

    /**
     * Array of strings for the "Enter Bet" prompt in multiple languages.
     */
    public static String[] enterBet =
            {"Enter Bet:", "Ingrese apuesta:", "Iontráil geall:", "Tegye meg a tétet:", "أدخل الرهان:", "Saisir mise :"};

    /**
     * Array of strings for the "Place Bet" button text in multiple languages.
     */
    public static String[] placeBet =
            {"Place Bet", "Realizar apuesta", "Cuir geall", "Fogadás", "ضع الرهان", "Placer mise"};

    /**
     * Array of strings for the "Balance" text in multiple languages.
     */
    public static String[] balance =
            {"Balance:", "Saldo:", "Iarmhéid:", "Egyenleg:", "الرصيد:", "Solde :"};

    /**
     * Array of strings for the "Bet" text in multiple languages.
     */
    public static String[] bet =
            {"Bet:", "Apuesta:", "Geall:", "Tét:", "الرهان:", "Mise :"};

    /**
     * Array of strings for the "Dealer Balance" text in multiple languages.
     */
    public static String[] dealerBalance =
            {"Dealer Balance:", "Saldo del crupier:", "Iarmhéid an déileálaí:", "Osztó egyenlege:", "رصيد الموزع:", "Solde du croupier:"};

     /**
     * Labels for the dealer's bet in multiple languages.
     */
    public static String[] dealerBet =
            {"Dealer Bet:", "Apuesta del crupier:", "Geall an déileálaí:", "Osztó tétje:", "رهان الموزع:", "Mise du croupier:"};

    /**
     * Instructions for the game displayed in multiple languages.
     * The array index corresponds to the language:
     * - 0: English
     * - 1: Spanish
     * - 2: Irish
     * - 3: Hungarian
     * - 4: Arabic
     * - 5: French
     */
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

    /**
     * Error message displayed when the bet amount is invalid or the balance is insufficient, in multiple languages.
     */
    public static String[] betError = {
            "Invalid bet amount or insufficient balance.",  // English 🇬🇧
            "Cantidad de apuesta no válida o saldo insuficiente.",  // Spanish 🇪🇸
            "Méid geall neamhbhailí nó iarmhéid neamhleor.",  // Irish 🇮🇪
            "Érvénytelen tétösszeg vagy elégtelen egyenleg.",  // Hungarian 🇭🇺
            "مبلغ الرهان غير صالح أو رصيد غير كافٍ.",  // Arabic 🇸🇦
            "Montant de mise invalide ou solde insuffisant."  // French 🇫🇷
    };

    /**
     * General error message displayed in multiple languages.
     */
    public static String[] error = {
            "Error",  // English 🇬🇧
            "Error",  // Spanish 🇪🇸 (same spelling)
            "Earráid",  // Irish 🇮🇪
            "Hiba",  // Hungarian 🇭🇺
            "خطأ",  // Arabic 🇸🇦
            "Erreur"  // French 🇫🇷
    };

    /**
     * Error message displayed when invalid input is provided, in multiple languages.
     */
    public static String[] invalidInput = {
            "Please enter a valid number.",  // English 🇬🇧
            "Por favor, ingrese un número válido.",  // Spanish 🇪🇸
            "Cuir isteach uimhir bhailí, le do thoil.",  // Irish 🇮🇪
            "Kérjük, adjon meg egy érvényes számot.",  // Hungarian 🇭🇺
            "يرجى إدخال رقم صالح.",  // Arabic 🇸🇦
            "Veuillez entrer un nombre valide."  // French 🇫🇷
    };

    /**
     * Title for the invalid input error dialog, in multiple languages.
     */
    public static String[] invalidInputTitle = {
            "Invalid Input",  // English 🇬🇧
            "Entrada no válida",  // Spanish 🇪🇸
            "Ionchur neamhbhailí",  // Irish 🇮🇪
            "Érvénytelen bevitel",  // Hungarian 🇭🇺
            "إدخال غير صالح",  // Arabic 🇸🇦
            "Entrée invalide"  // French 🇫🇷
    };

    /**
     * Message displayed when a Split Ace is drawn, in multiple languages.
     */
    public static String[] splitAceMessage = {
            "Split Ace drawn! Your score will be halved.",  // English 🇬🇧
            "¡As dividido extraído! Tu puntuación se reducirá a la mitad.", // Spanish 🇪🇸
            "Roinnte Ace tarraingthe! Laghdaítear do scór go leith.", // Irish 🇮🇪
            "Kihúzott Osztott Ász! Az eredményed feleződik.", // Hungarian 🇭🇺
            "تم سحب الآس المقسم! سيتم تقليل نتيجتك إلى النصف.", // Arabic 🇸🇦
            "As divisé tiré! Votre score sera réduit de moitié." // French 🇫🇷
    };

    /**
     * Message displayed when a Blackjack Bomb occurs, in multiple languages.
     */
    public static String[] blackjackBombMessage = {
            "Blackjack Bomb! The game is over, and the Blackjack Bomb wins.",  // English 🇬🇧
            "¡Bomba de Blackjack! El juego ha terminado, y la Bomba de Blackjack gana.", // Spanish 🇪🇸
            "Buama Blackjack! Tá an cluiche thart, agus bhuaigh an Buama Blackjack.", // Irish 🇮🇪
            "Blackjack Bomba! A játék véget ért, és a Blackjack Bomba nyert.", // Hungarian 🇭🇺
            "قنبلة البلاك جاك! انتهت اللعبة، وفازت قنبلة البلاك جاك.", // Arabic 🇸🇦
            "Bombe de Blackjack! La partie est terminée, et la Bombe de Blackjack gagne." // French 🇫🇷
    };

    /**
     * Message displayed when a Joker Wild event occurs, in multiple languages.
     */
    public static String[] jokerWildMessage = {
            "Joker Wild! Choose a value between 1 and 11: 🤡",  // English 🇬🇧
            "¡Joker Salvaje! Elige un valor entre 1 y 11: 🤡", // Spanish 🇪🇸
            "Joker Fiáin! Roghnaigh luach idir 1 agus 11: 🤡", // Irish 🇮🇪
            "Vad Joker! Válassz egy értéket 1 és 11 között: 🤡", // Hungarian 🇭🇺
            "جوكر البري! اختر قيمة بين 1 و 11: 🤡", // Arabic 🇸🇦
            "Joker Sauvage ! Choisissez une valeur entre 1 et 11 : 🤡" // French 🇫🇷
    };

    /**
     * Title for the Joker Wild dialog, in multiple languages.
     */
    public static String[] jokerWildTitle = {
            "Joker Wild",  // English 🇬🇧
            "Joker Salvaje", // Spanish 🇪🇸
            "Joker Fiáin", // Irish 🇮🇪
            "Vad Joker", // Hungarian 🇭🇺
            "جوكر البري", // Arabic 🇸🇦
            "Joker Sauvage" // French 🇫🇷
    };

    /**
     * Error message for invalid Joker Wild input, in multiple languages.
     */
    public static String[] invalidJokerInput = {
            "Invalid choice. Please choose a value between 1 and 11.",  // English 🇬🇧
            "Elección inválida. Elige un valor entre 1 y 11.", // Spanish 🇪🇸
            "Roghnú neamhbhailí. Roghnaigh luach idir 1 agus 11.", // Irish 🇮🇪
            "Érvénytelen választás. Válassz egy értéket 1 és 11 között.", // Hungarian 🇭🇺
            "اختيار غير صالح. اختر قيمة بين 1 و 11.", // Arabic 🇸🇦
            "Choix invalide. Choisissez une valeur entre 1 et 11." // French 🇫🇷
    };

    /**
     * Welcome message displayed at the start of the game, in multiple languages.
     */
    public static String[] welcomeMessage = {
            "Welcome to Blackjack!",  // English 🇬🇧
            "¡Bienvenido a Blackjack!", // Spanish 🇪🇸
            "Fáilte go Blackjack!", // Irish 🇮🇪
            "Üdvözöljük a Blackjackben!", // Hungarian 🇭🇺
            "مرحبًا بك في بلاك جاك!", // Arabic 🇸🇦
            "Bienvenue au Blackjack !" // French 🇫🇷
    };
/**
     * Messages to display when the game is over and the player runs out of money.
     * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
     */
    public static String[] gameOverMessage = {
        "Game Over! You ran out of money! 😢", // English 🇬🇧
        "¡Juego terminado! Te has quedado sin dinero. 😢", // Spanish 🇪🇸
        "Cluiche thart! Tá tú as airgead. 😢", // Irish 🇮🇪
        "Játék vége! Elfogyott a pénzed. 😢", // Hungarian 🇭🇺
        "انتهت اللعبة! لقد نفد مالك. 😢", // Arabic 🇸🇦
        "Jeu terminé ! Vous n'avez plus d'argent. 😢" // French 🇫🇷
        };

        /**
         * Titles for the game over message displayed when the player runs out of money.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] gameOverTitle = {
                "Game Over!", // English 🇬🇧
                "¡Juego terminado!", // Spanish 🇪🇸
                "Cluiche thart!", // Irish 🇮🇪
                "Játék vége!", // Hungarian 🇭🇺
                "انتهت اللعبة!", // Arabic 🇸🇦
                "Jeu terminé !" // French 🇫🇷
        };

        /**
         * Buttons or prompts related to restarting the game, available in different languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] restartGame = {
                "Restart Game", // English 🇬🇧
                "Reiniciar juego", // Spanish 🇪🇸
                "Tosaigh an cluiche arís", // Irish 🇮🇪
                "Játék újraindítása", // Hungarian 🇭🇺
                "إعادة تشغيل اللعبة", // Arabic 🇸🇦
                "Redémarrer la partie" // French 🇫🇷
        };

        /**
         * Prompts for exiting the game in multiple languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] exitGame = {
                "Exit", // English 🇬🇧
                "Salir", // Spanish 🇪🇸
                "Slí Amach", // Irish 🇮🇪
                "Kilépés", // Hungarian 🇭🇺
                "خروج", // Arabic 🇸🇦
                "Quitter" // French 🇫🇷
        };

        /**
         * Main title of the game, shown in multiple languages to welcome the player.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] mainTitle = {
                "Blackjack 2 : The boys are jack in town!!",
                "Blackjack 2: ¡¡Los chicos están de vuelta en la ciudad!!", // Spanish 🇪🇸
                "Blackjack 2: Tá na buachaillí ar ais sa bhaile!!", // Irish 🇮🇪
                "Blackjack 2: A fiúk visszatértek a városba!!", // Hungarian 🇭🇺
                "بلاك جاك 2: الأولاد عادوا إلى المدينة.", // Arabic 🇸🇦
                "Blackjack 2: Les garçons sont de retour en ville!!" // French 🇫🇷
        };

        /**
         * Title of the pause menu, shown in multiple languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] pauseTitle = {
                "Pause Menu",         // English
                "Menú de Pausa",      // Spanish
                "Pá Menú",            // Irish
                "Szünet menü",        // Hungarian
                "قائمة التوقف",       // Arabic
                "Menu Pause"          // French
        };

        /**
         * Pause action buttons in different languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] PAUSE = {
                "Pause", 
                "Pausa", 
                "Sos", 
                "Szünet", 
                "إيقاف مؤقت", 
                "Pause"
        };

        /**
         * Resume action buttons in multiple languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] RESUME = {
                "Resume", 
                "Reanudar", 
                "Atosaigh", 
                "Folytatás", 
                "استئناف", 
                "Reprendre"
        };

        /**
         * Message shown when the game is paused, in multiple languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] GAME_PAUSED = {
                "Game Paused", 
                "Juego en Pausa", 
                "Cluiche ar Sos", 
                "Játék Szünetel", 
                "اللعبة متوقفة", 
                "Jeu en Pause"
        };

        /**
         * Volume settings, showing "Volume" in various languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static final String[] VOLUME = {
                "Volume", "Volumen", "Volume", "Volume", "Volume", "Volume"
        };

        /**
         * Options for saving the game, translated into multiple languages.
         * Translated into English, Spanish, Irish, Hungarian, Arabic, and French.
         */
        public static String[] saveGame = {
                "Save",
                "Save",
                "Save",
                "Save",
                "Save",
                "Save",
        };
}
    


