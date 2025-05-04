package main.view;

/*
 * The Texts class serves as a centralized repository for
 * all localized UI strings used in the Blackjack game. It supports
 * English, Spanish, Irish, Hungarian, Arabic, and French.
 */
public class Texts {

    public static String[] startGame =
            {"Start Game!", "Empieza el juego!", "Tús a chur leis an gcluiche!", "Kezdjük a játékot!", "ابدأ اللعبة", "Commencer le jeu!"};

    public static String[] loadGame = {
            "Load Game",
            "Cargar el juego!",
            "Luchtaigh Cluiche",
            "Játék Betöltése",
            "تحميل اللعبة",
            "Charger la Partie"
    };

    public static String[] instructions =
            {"Instructions", "Instrucciones", "Treoir",
                    "Szabályok", " تعليمات", "Instructions"};

    public static String[] exit =
            {"Exit", "Salida", "Slí Amach",
                    "Kilépés", "خروج", "Quitter"};

    public static String[] options =
            {"Options", "Opciones", "Rogha",
                    "Opciók", "خيارات", "Options"};


    public static String[] guiTitle =
            {"Blackjack Game", "El juego de BlackJack", "cluiche BlackJack",
                    "Huszonegy", "لعبة البلاك جاك", "Jeu de Blackjack"};

    public static String[] guiHit =
            {"Hit", "Robar", "tháinig sé",
                    "Lapkérés", "سحب", "Tirer"};

    public static String[] guiStand =
            {"Stand", "Plantar", " folamh le",
                    "\n" +
                            "Állj", "توقف", "Rester"};

    public static String[] guiNewGame =
            {"New Game", "Juego nuevo", "cluiche nua",
                    "Új játék", "لعبة جديدة", "Nouvelle Partie"};

    public static String[] guiDealerScore =
            {"Dealer's Score ", "El scor del distribudor", "scor le déanta",
                    "Osztó pontszáma", "نتيجة الموزع", "Score du Croupier"};

    public static String[] guiPlayerScore =
            {"Player's Score ", "Puntuación del jugador", "scor le imreoir",
                    "Játékos pontszáma", "نتيجة اللاعب", "Score du Joueur"};

    public static String[] guiWelcome =
            {"Welcome to BlackJack!", "Bienvenido a BlackJack", "failte go dti BlackJack",
                    "Üdv a játékban", "مرحبًا بك في بلاك جاك!", "Bienvenue au Blackjack!"};

    public static String[] guiBackToMain =
            {"Back to Main Menu.", "Salida al menú principal", "Ar ais go dtí an príomh-roghchlár",
                    "Vissza a főmenübe", "العودة إلى القائمة الرئيسية", "Retour au menu principal"};

    public static String[] gameManagerGameOn =
            {"Game On! Your turn.",
                    "¡Juego encendido! Tu turno.", "Cluiche Ar! Do sheal.",
                    "Game On! Te jössz!", "اللعبة بدأت! دورك الآن.", "C'est parti ! À vous."};

    public static String[] dealerBlackjack =
            {"Dealer has Blackjack! Dealer wins!", "¡El repartidor tiene Blackjack! ¡El repartidor gana!", "Tá Blackjack ag an dáileoir! Buaileann an dáileoir!", "Az osztónak Blackjackje van! Az osztó nyer!", "الموزع لديه بلاك جاك! الموزع يفوز!", "Le croupier a un Blackjack ! Il gagne !"};

    public static String[] playerBlackjack =
            {"Player has Blackjack! Player wins!", "¡El jugador tiene Blackjack! ¡El jugador gana!", "Tá Blackjack ag an imreoir! Buaileann an t-imreoir!", "A játékosnak Blackjackje van! A játékos nyer!", "اللاعب لديه بلاك جاك! اللاعب يفوز!", "Le joueur a un Blackjack ! Il gagne !"};

    public static String[] playerBusts =
            {"Player busts! Dealer wins!", "¡El jugador se pasa! ¡El repartidor gana!", "Tá an t-imreoir imithe thar fóir! Buaileann an dáileoir!", "A játékos túllépte a 21-et! A kereskedő nyer!", "تجاوز اللاعب 21! الموزع يفوز!", "Le joueur dépasse 21 ! Le croupier gagne !"};

    public static String[] dealerBusts =
            {"Dealer busts! Player wins!", "¡El repartidor se pasa! ¡El jugador gana!", "Tá an dáileoir imithe thar fóir! Buaileann an t-imreoir!", "A kereskedő túllépte a 21-et! A játékos nyer!", "تجاوز الموزع 21! اللاعب يفوز!", "Le croupier dépasse 21 ! Le joueur gagne !"};

    public static String[] playerWins =
            {"Player wins!", "¡El jugador gana!", "Buaileann an t-imreoir!", "A játékos nyer!", "اللاعب يفوز!", "Le joueur gagne !"};

    public static String[] dealerWins =
            {"Dealer wins!", "¡El repartidor gana!", "Buaileann an dáileoir!", "Az osztó nyer!", "الموزع يفوز!", "Le croupier gagne !"};


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
                { // English 🇬🇧
                    "How to Play:",
                    "1. Click 'Hit' to draw a card.",
                    "2. Click 'Stand' to end your turn.",
                    "3. Try to get as close to 21 without going over.",
                    "4. Face cards are worth 10, Aces are 1 or 11.",
                    "5. Place your bet using the 'Place Bet' field before starting.",
                    "6. You can undo your last bet using the 'Undo' button.",
                    "7. Track your progress in the Achievements panel."
                },
                { // Spanish 🇪🇸
                    "Cómo jugar:",
                    "1. Haz clic en 'Robar' para sacar una carta.",
                    "2. Haz clic en 'Plantarse' para terminar tu turno.",
                    "3. Intenta acercarte a 21 sin pasarte.",
                    "4. Las figuras valen 10, los Ases valen 1 u 11.",
                    "5. Introduce tu apuesta antes de empezar la ronda.",
                    "6. Puedes deshacer tu apuesta con el botón 'Deshacer'.",
                    "7. Consulta tus logros en el panel de Logros."
                },
                { // Irish (Gaeilge) 🇮🇪
                    "Conas a Imirt:",
                    "1. Cliceáil 'Buail' chun cárta a tharraingt.",
                    "2. Cliceáil 'Fanacht' chun do sheal a chríochnú.",
                    "3. Déan iarracht teacht chomh gar do 21 agus is féidir.",
                    "4. Is fiú 10 na cártaí aghaidhe, is fiú 1 nó 11 an tAs.",
                    "5. Cuir isteach do gheall sula dtosaíonn tú.",
                    "6. Is féidir leat an geall deireanach a chealú le 'Cealaigh'.",
                    "7. Féach ar do dhul chun cinn sa phainéal Gaisce."
                },
                { // Hungarian 🇭🇺
                    "Hogyan játssz:",
                    "1. Kattints a 'Lapkérés' gombra egy lap húzásához.",
                    "2. Kattints az 'Állj' gombra a köröd befejezéséhez.",
                    "3. Próbálj meg minél közelebb kerülni a 21-hez anélkül, hogy túllépnéd.",
                    "4. A képes lapok értéke 10, az Ász 1 vagy 11.",
                    "5. Add meg a tétet a 'Tét' mezőben a kör kezdete előtt.",
                    "6. Az utolsó tét visszavonható a 'Visszavonás' gombbal.",
                    "7. Kövesd a haladásodat az Eredmények panelen."
                },
                { // Arabic 🇸🇦
                    "كيفية اللعب:",
                    "1. اضغط على 'سحب' لسحب بطاقة.",
                    "2. اضغط على 'توقف' لإنهاء دورك.",
                    "3. حاول الاقتراب من 21 دون تجاوزها.",
                    "4. البطاقات الوجهية تساوي 10، والآس يساوي 1 أو 11.",
                    "5. أدخل رهانك في الحقل قبل بدء الجولة.",
                    "6. يمكنك التراجع عن الرهان باستخدام زر 'تراجع'.",
                    "7. راقب إنجازاتك في لوحة الإنجازات."
                },
                { // French 🇫🇷
                    "Comment jouer :",
                    "1. Cliquez sur 'Tirer' pour piocher une carte.",
                    "2. Cliquez sur 'Rester' pour terminer votre tour.",
                    "3. Essayez d’atteindre 21 sans le dépasser.",
                    "4. Les figures valent 10, l’As vaut 1 ou 11.",
                    "5. Saisissez votre mise avant de commencer le tour.",
                    "6. Vous pouvez annuler votre dernière mise avec le bouton 'Annuler'.",
                    "7. Suivez vos progrès dans le panneau Succès."
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
    public static String[] splitAceMessage = {
            "Split Ace drawn! Your score will be halved.",  // English 🇬🇧
            "¡As dividido extraído! Tu puntuación se reducirá a la mitad.", // Spanish 🇪🇸
            "Roinnte Ace tarraingthe! Laghdaítear do scór go leith.", // Irish 🇮🇪
            "Kihúzott Osztott Ász! Az eredményed feleződik.", // Hungarian 🇭🇺
            "تم سحب الآس المقسم! سيتم تقليل نتيجتك إلى النصف.", // Arabic 🇸🇦
            "As divisé tiré! Votre score sera réduit de moitié." // French 🇫🇷
    };
    public static String[] blackjackBombMessage = {
            "Blackjack Bomb! The game is over, and the Blackjack Bomb wins.",  // English 🇬🇧
            "¡Bomba de Blackjack! El juego ha terminado, y la Bomba de Blackjack gana.", // Spanish 🇪🇸
            "Buama Blackjack! Tá an cluiche thart, agus bhuaigh an Buama Blackjack.", // Irish 🇮🇪
            "Blackjack Bomba! A játék véget ért, és a Blackjack Bomba nyert.", // Hungarian 🇭🇺
            "قنبلة البلاك جاك! انتهت اللعبة، وفازت قنبلة البلاك جاك.", // Arabic 🇸🇦
            "Bombe de Blackjack! La partie est terminée, et la Bombe de Blackjack gagne." // French 🇫🇷
    };
    public static String[] jokerWildMessage = {
            "Joker Wild! Choose a value between 1 and 11: 🤡",  // English 🇬🇧
            "¡Joker Salvaje! Elige un valor entre 1 y 11: 🤡", // Spanish 🇪🇸
            "Joker Fiáin! Roghnaigh luach idir 1 agus 11: 🤡", // Irish 🇮🇪
            "Vad Joker! Válassz egy értéket 1 és 11 között: 🤡", // Hungarian 🇭🇺
            "جوكر البري! اختر قيمة بين 1 و 11: 🤡", // Arabic 🇸🇦
            "Joker Sauvage ! Choisissez une valeur entre 1 et 11 : 🤡" // French 🇫🇷
    };

    public static String[] jokerWildTitle = {
            "Joker Wild",  // English 🇬🇧
            "Joker Salvaje", // Spanish 🇪🇸
            "Joker Fiáin", // Irish 🇮🇪
            "Vad Joker", // Hungarian 🇭🇺
            "جوكر البري", // Arabic 🇸🇦
            "Joker Sauvage" // French 🇫🇷
    };

    public static String[] invalidJokerInput = {
            "Invalid choice. Please choose a value between 1 and 11.",  // English 🇬🇧
            "Elección inválida. Elige un valor entre 1 y 11.", // Spanish 🇪🇸
            "Roghnú neamhbhailí. Roghnaigh luach idir 1 agus 11.", // Irish 🇮🇪
            "Érvénytelen választás. Válassz egy értéket 1 és 11 között.", // Hungarian 🇭🇺
            "اختيار غير صالح. اختر قيمة بين 1 و 11.", // Arabic 🇸🇦
            "Choix invalide. Choisissez une valeur entre 1 et 11." // French 🇫🇷
    };
    public static String[] welcomeMessage = {
            "Welcome to Blackjack!",  // English 🇬🇧
            "¡Bienvenido a Blackjack!", // Spanish 🇪🇸
            "Fáilte go Blackjack!", // Irish 🇮🇪
            "Üdvözöljük a Blackjackben!", // Hungarian 🇭🇺
            "مرحبًا بك في بلاك جاك!", // Arabic 🇸🇦
            "Bienvenue au Blackjack !" // French 🇫🇷
    };
    public static String[] gameOverMessage = {
            "Game Over! You ran out of money! 😢", // English 🇬🇧
            "¡Juego terminado! Te has quedado sin dinero. 😢", // Spanish 🇪🇸
            "Cluiche thart! Tá tú as airgead. 😢", // Irish 🇮🇪
            "Játék vége! Elfogyott a pénzed. 😢", // Hungarian 🇭🇺
            "انتهت اللعبة! لقد نفد مالك. 😢", // Arabic 🇸🇦
            "Jeu terminé ! Vous n'avez plus d'argent. 😢" // French 🇫🇷
    };

    public static String[] gameOverTitle = {
            "Game Over!", // English 🇬🇧
            "¡Juego terminado!", // Spanish 🇪🇸
            "Cluiche thart!", // Irish 🇮🇪
            "Játék vége!", // Hungarian 🇭🇺
            "انتهت اللعبة!", // Arabic 🇸🇦
            "Jeu terminé !" // French 🇫🇷
    };

    public static String[] restartGame = {
            "Restart Game", // English 🇬🇧
            "Reiniciar juego", // Spanish 🇪🇸
            "Tosaigh an cluiche arís", // Irish 🇮🇪
            "Játék újraindítása", // Hungarian 🇭🇺
            "إعادة تشغيل اللعبة", // Arabic 🇸🇦
            "Redémarrer la partie" // French 🇫🇷
    };

    public static String[] exitGame = {
            "Exit", // English 🇬🇧
            "Salir", // Spanish 🇪🇸
            "Slí Amach", // Irish 🇮🇪
            "Kilépés", // Hungarian 🇭🇺
            "خروج", // Arabic 🇸🇦
            "Quitter" // French 🇫🇷
    };

    public static String[] mainTitle = {
            "Blackjack 2 : The boys are jack in town!!",
            "Blackjack 2: ¡¡Los chicos están de vuelta en la ciudad!!", // Spanish 🇪🇸
            "Blackjack 2: Tá na buachaillí ar ais sa bhaile!!", // Irish 🇮🇪
            "Blackjack 2: A fiúk visszatértek a városba!!", // Hungarian 🇭🇺
            "بلاك جاك 2: الأولاد عادوا إلى المدينة.", // Arabic 🇸🇦
            "Blackjack 2: Les garçons sont de retour en ville!!" // French 🇫🇷
    };
    public static String[] multiplayer = {
        "Multiplayer",                        // 🇬🇧 English
        "Multijugador",                      // 🇪🇸 Spanish
        "Ilbhliantach",                      // 🇮🇪 Irish
        "Többjátékos mód",                   // 🇭🇺 Hungarian
        "متعدد اللاعبين",                   // 🇸🇦 Arabic
        "Multijoueur"                        // 🇫🇷 French
    };
    
    
 

  
    public static String[] PAUSE = {
        "Pause", 
        "Pausa", 
        "Sos", 
        "Szünet", 
        "إيقاف مؤقت", 
        "Pause"
    };

    public static String[] RESUME = {
        "Resume", 
        "Reanudar", 
        "Atosaigh", 
        "Folytatás", 
        "استئناف", 
        "Reprendre"
    };

    public static String[] GAME_PAUSED = {
        "Game Paused", 
        "Juego en Pausa", 
        "Cluiche ar Sos", 
        "Játék Szünetel", 
        "اللعبة متوقفة", 
        "Jeu en Pause"
    };

    public static final String[] VOLUME = {
            "Volume",        // English
            "Volumen",       // Spanish
            "Toirte",        // Irish
            "Hangerő",       // Hungarian
            "مستوى الصوت",   // Arabic
            "Volume"         // French
    };

    public static String[] saveGame = {
            "Save",          // English
            "Guardar",       // Spanish
            "Sábháil",       // Irish
            "Mentés",        // Hungarian
            "حفظ",           // Arabic
            "Sauvegarder"    // French
    };

    public static String[] turns = {
            "'s turn",
            " turno",
            " cas",
            " köre",
            " دوره",
            " joue",
    };

    public static String[] total = {
            "Total",
            "Total",        // Spanish
            "Iomlán",       // Irish
            "Összesen",     // Hungarian
            "المجموع",      // Arabic
            "Total",        // French
    };

    public static String[] achievements = {
      "Achievements",
            "Logros",             // Spanish
            "Éachtaí",            // Irish
            "Eredmények",         // Hungarian
            "الإنجازات",          // Arabic
            "Succès"              // French
    };

    public static String[] firstWin = {
            "First Win!",
            "¡Primera Victoria!", // Spanish
            "An Chéad Bhua!",     // Irish
            "Első Győzelem!",     // Hungarian
            "أول فوز!",           // Arabic
            "Première Victoire!"  // French
    };

    public static String[] firstLoss = {
            "First Loss",         // English
            "¡Primera Derrota!",  // Spanish
            "An Chéad Chaill!",   // Irish
            "Első Vereség!",      // Hungarian
            "أول خسارة!",         // Arabic
            "Première Défaite!"   // French
    };

    public static String[] firstBet = {
            "First Bet!",         // English
            "¡Primera Apuesta!",  // Spanish
            "An Chéad Gheall!",   // Irish
            "Első Tét!",          // Hungarian
            "أول رهان!",          // Arabic
            "Premier Pari!"       // French
    };

    public static String[] firstBlackjack = {
            "First Blackjack!",         // English
            "¡Primer Blackjack!",       // Spanish
            "An Chéad Blackjack!",      // Irish
            "Első Blackjack!",          // Hungarian
            "أول بلاك جاك!",            // Arabic
            "Premier Blackjack!"        // French
    };

    public static String[] fiveWins = {
            "5 Wins",              // English
            "5 Victorias",         // Spanish
            "5 Bhuaigh",           // Irish
            "5 Győzelem",          // Hungarian
            "٥ انتصارات",          // Arabic
            "5 Victoires"          // French
    };

    public static String[] bigWin = {
            "Big Winner",          // English
            "Gran Ganador",        // Spanish
            "Buaiteoir Mór",       // Irish
            "Nagy Nyertes",        // Hungarian
            "فائز كبير",           // Arabic
            "Gros Gagnant"         // French
    };

    public static String[] dealerStreak = {
            "Dealer: 3-Win Streak",       // English
            "Crupier: Racha de 3 Victorias", // Spanish
            "Dáileoir: Sraith 3 mBua",       // Irish
            "Oszto: 3 Nyerő Sorozat",        // Hungarian
            "الموزع: سلسلة 3 انتصارات",      // Arabic
            "Croupier : Série de 3 Victoires"// French
    };

    public static String[] multiplayerAchievement = {
            "Multiplayer Madness",     // English
            "Locura Multijugador",     // Spanish
            "Craiceáilte Il-imreora",  // Irish
            "Többszemélyes Őrület",     // Hungarian
            "جنون اللاعبين المتعددين", // Arabic
            "Folie Multijoueur"        // French
    };

    public static String[] betConfirmed = {
            "Bet Confirmed:",         // English
            "Apuesta Confirmada:",    // Spanish
            "Geall Dearbhaithe:",     // Irish
            "Tét Megerősítve:",       // Hungarian
            "تم تأكيد الرهان:",        // Arabic
            "Pari Confirmé :"         // French
    };

    public static String[] selectLanguage = {
            "Select Language",
            "Seleccionar idioma",
            "Roghnaigh Teanga",
            "Válasszon nyelvet",
            "اختر اللغة",
            "Choisir la langue"
    };

    public static String[] selectDifficulty = {
            "Select Difficulty",
            "Seleccionar dificultad",
            "Roghnaigh Deacracht",
            "Válasszon nehézségi szintet",
            "اختر مستوى الصعوبة",
            "Choisir la difficulté"
    };

    public static String[] easy = {
            "Easy",
            "Fácil",
            "Éasca",
            "Könnyű",
            "سهل",
            "Facile"
    };

    public static String[] medium = {
            "Medium",
            "Medio",
            "Measartha",
            "Közepes",
            "متوسط",
            "Moyen"
    };

    public static String[] hard = {
            "Hard",
            "Difícil",
            "Deacair",
            "Nehéz",
            "صعب",
            "Difficile"
    };
}
