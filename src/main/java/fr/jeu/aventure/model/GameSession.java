package fr.jeu.aventure.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameSession {
    private static final String EXPECTED_PHRASE = "LUMIERE GUIDE VERS TRESOR";

    private final List<Room> rooms;
    private final Player player;
    private int roomIndex;
    private int remainingTime;
    private GameStatus status = GameStatus.ONGOING;
    private String safeAddress;
    private Monster safeGuardian;
    private boolean safeReached;

    public GameSession(String playerName, int availableTime, Random random) {
        this.player = new Player(playerName);
        this.remainingTime = availableTime;
        this.rooms = createRooms(random);
        this.safeGuardian = new Monster(
                "Golem du Coffre",
                2,
                new Item("Insigne du Gardien", "Prouve votre victoire", "Ouvre l'accès final", ItemType.CLUE, 1));
    }

    public List<Room> getRooms() {
        return List.copyOf(rooms);
    }

    public Room getCurrentRoom() {
        return rooms.get(roomIndex);
    }

    public Player getPlayer() {
        return player;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getSafeAddress() {
        return safeAddress;
    }

    public boolean isSafeReached() {
        return safeReached;
    }

    public String getExpectedPhrase() {
        return EXPECTED_PHRASE;
    }

    public String observe() {
        Room room = getCurrentRoom();
        consumeTime(1);
        return "Salle: " + room.getName()
                + "\nDescription: " + room.getDescription()
                + "\nEnvironnement: " + room.getEnvironment()
                + "\nObjets: " + room.getItems().stream().map(Item::getName).toList()
                + "\nPNJ: " + room.getNpcs().stream().map(Npc::getName).toList()
                + "\nSorties: " + room.getExits()
                + "\nMonstre: " + room.getMonster().map(Monster::getName).orElse("Aucun");
    }

    public String pickUpItem(String itemName) {
        consumeTime(1);
        return getCurrentRoom().takeItem(itemName)
                .map(item -> {
                    player.addItem(item);
                    return "Objet récupéré: " + item.getName();
                })
                .orElse("Aucun objet trouvé avec ce nom.");
    }

    public String talkToNpc(String npcName) {
        consumeTime(1);
        return getCurrentRoom().findNpc(npcName)
                .map(npc -> "PNJ " + npc.getName() + ": " + npc.getInformation() + (npc.isTruthful() ? "" : " (information possiblement fausse)"))
                .orElse("Ce PNJ est introuvable.");
    }

    public String solvePuzzle(String answer) {
        Room room = getCurrentRoom();
        consumeTime(1);
        if (room.isPuzzleSolved()) {
            return "Énigme déjà résolue.";
        }
        Puzzle puzzle = room.getPuzzle();
        if (!puzzle.getAnswer().equalsIgnoreCase(answer.trim())) {
            return "Mauvaise réponse.";
        }

        room.markPuzzleSolved();
        if (puzzle.getReward() != null) {
            player.addItem(puzzle.getReward());
        }
        if (puzzle.getClue() != null && !puzzle.getClue().isBlank()) {
            player.addItem(new Item("Indice", "Indice de phrase magique", puzzle.getClue(), ItemType.CLUE, 1));
        }
        if (puzzle.getSafeAddress() != null) {
            safeAddress = puzzle.getSafeAddress();
        }
        return "Énigme résolue.";
    }

    public String moveToNextRoom() {
        Room room = getCurrentRoom();
        consumeTime(1);
        if (room.getMonster().isPresent()) {
            return "Un monstre bloque le passage.";
        }
        if (roomIndex >= rooms.size() - 1) {
            return "Vous êtes déjà dans la dernière salle.";
        }
        Room nextRoom = rooms.get(roomIndex + 1);
        if (nextRoom.getRequiredKey() != null && !player.hasItem(nextRoom.getRequiredKey())) {
            return "La sortie est verrouillée. Clé requise: " + nextRoom.getRequiredKey();
        }
        roomIndex++;
        return "Déplacement vers: " + getCurrentRoom().getName();
    }

    public String fightMonster() {
        Room room = getCurrentRoom();
        consumeTime(1);
        Monster monster = room.getMonster().orElse(null);
        if (monster == null) {
            if (safeReached && safeGuardian != null) {
                monster = safeGuardian;
            } else {
                return "Aucun monstre à combattre.";
            }
        }

        if (!player.hasWeapon() || !player.consumeAmmunition(monster.getHitPoints())) {
            if (player.consumeLife()) {
                return "Combat perdu. Une vie supplémentaire a été consommée.";
            }
            status = GameStatus.LOST;
            return "Défaite définitive: plus de vies.";
        }

        if (monster.getReward() != null) {
            player.addItem(monster.getReward());
        }
        if (safeReached && safeGuardian != null && monster == safeGuardian) {
            safeGuardian = null;
            return "Gardien vaincu. Le coffre-fort est accessible.";
        }

        room.clearMonster();
        return "Monstre vaincu: " + monster.getName();
    }

    public String goToSafeAddress() {
        consumeTime(1);
        if (safeAddress == null) {
            return "L'adresse du coffre-fort est encore inconnue.";
        }
        if (roomIndex != rooms.size() - 1) {
            return "Vous devez atteindre la dernière salle d'abord.";
        }
        safeReached = true;
        return "Vous atteignez le coffre-fort à l'adresse: " + safeAddress;
    }

    public String openSafe(String phrase) {
        consumeTime(1);
        if (!safeReached) {
            return "Le coffre-fort n'a pas encore été atteint.";
        }
        if (safeGuardian != null) {
            return "Le gardien du coffre-fort bloque l'accès.";
        }
        if (!EXPECTED_PHRASE.equals(normalize(phrase))) {
            return "Phrase magique incorrecte. Accès refusé.";
        }
        status = GameStatus.WON;
        return "Coffre-fort ouvert. Trésor découvert!";
    }

    private void consumeTime(int amount) {
        if (status != GameStatus.ONGOING) {
            return;
        }
        remainingTime -= amount;
        if (remainingTime <= 0) {
            status = GameStatus.TIME_EXPIRED;
        }
    }

    private static String normalize(String phrase) {
        return phrase == null
                ? ""
                : phrase.trim().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    private static List<Room> createRooms(Random random) {
        List<Room> route = new ArrayList<>();
        route.add(new Room(
                "Hall d'entrée",
                "Point de départ de l'aventure.",
                "Des statues surveillent une porte ancienne.",
                List.of(
                        new Item("Clé du domaine", "Clé en laiton", "Ouvre les portes verrouillées", ItemType.KEY, 1),
                        new Item("Pistolet ancien", "Arme à un coup", "Permet de combattre", ItemType.WEAPON, 1),
                        new Item("Munitions de départ", "Quelques balles", "Utiles pour les premiers combats", ItemType.AMMUNITION, 2),
                        new Item("Munitions de secours", "Réserve de balles", "Renforce les chances de victoire", ItemType.AMMUNITION, 2),
                        new Item("Vie supplémentaire", "Potion de vie", "Réanime après une défaite", ItemType.EXTRA_LIFE, 1)),
                List.of(new Npc("Majordome", "Le premier mot ne se place pas forcément en premier.", true)),
                new Monster("Rat Géant", 3, new Item("Cartouche", "Munition récupérée", "Recharge l'arme", ItemType.AMMUNITION, 2)),
                List.of("Salle suivante"),
                new Puzzle(
                        "Quel matériau compose la clé visible dans la salle ?",
                        "laiton",
                        new Item("LUMIERE", "Mot magique", "Mot de phrase magique", ItemType.MAGIC_WORD, 1),
                        "Le mot LUMIERE est en position 1.",
                        null),
                null));

        List<Room> middlePool = new ArrayList<>(List.of(
                new Room(
                        "Galerie des Ombres",
                        "Couloir rempli de miroirs fissurés.",
                        "Un symbole solaire est gravé sur un mur.",
                        List.of(new Item("Carte partielle", "Plan incomplet", "Donne des informations sur le parcours", ItemType.MAP, 1)),
                        List.of(new Npc("Guide", "Le mot GUIDE est en position 2.", true)),
                        new Monster("Spectre", 2, new Item("Munitions spectrales", "Récompense de combat", "Recharge l'arme", ItemType.AMMUNITION, 2)),
                        List.of("Salle suivante"),
                        new Puzzle(
                                "Quel symbole est gravé sur le mur ?",
                                "solaire",
                                new Item("GUIDE", "Mot magique", "Mot de phrase magique", ItemType.MAGIC_WORD, 1),
                                "Le mot GUIDE est en position 2.",
                                null),
                        "Clé du domaine"),
                new Room(
                        "Atelier Mécanique",
                        "Des engrenages tournent en cadence.",
                        "Une plaque indique le nombre 4.",
                        List.of(new Item("Munitions lourdes", "Boîte de cartouches", "Recharge importante", ItemType.AMMUNITION, 4)),
                        List.of(new Npc("Horloger", "Le coffre se trouve près de la fontaine du domaine.", true)),
                        new Monster("Automate", 3, new Item("Vie de secours", "Élixir", "Récupère une chance", ItemType.EXTRA_LIFE, 1)),
                        List.of("Salle suivante"),
                        new Puzzle(
                                "Quel nombre est affiché sur la plaque ?",
                                "4",
                                new Item("VERS", "Mot magique", "Mot de phrase magique", ItemType.MAGIC_WORD, 1),
                                "Le mot VERS est en position 3.",
                                null),
                        null),
                new Room(
                        "Jardin Intérieur",
                        "Un ancien jardin couvert.",
                        "Trois colonnes entourent une fontaine sèche.",
                        List.of(new Item("Munitions fines", "Petites balles", "Recharge légère", ItemType.AMMUNITION, 2)),
                        List.of(new Npc("Jardinière", "Ne crois pas tous les PNJ.", false)),
                        null,
                        List.of("Salle suivante"),
                        new Puzzle(
                                "Combien de colonnes entourent la fontaine ?",
                                "3",
                                null,
                                "Le mot TRESOR est en position 4.",
                                null),
                        null),
                new Room(
                        "Crypte des Échos",
                        "Une crypte avec des murmures persistants.",
                        "Une inscription évoque la victoire finale.",
                        List.of(),
                        List.of(new Npc("Érudit", "La phrase complète comporte 4 mots.", true)),
                        new Monster("Garde Maudit", 2, new Item("Munitions sacrées", "Butin", "Recharge", ItemType.AMMUNITION, 2)),
                        List.of("Salle suivante"),
                        new Puzzle(
                                "Combien de mots composent la phrase magique ?",
                                "4",
                                new Item("TRESOR", "Mot magique", "Mot de phrase magique", ItemType.MAGIC_WORD, 1),
                                "Le mot TRESOR est en position 4.",
                                null),
                        null),
                new Room(
                        "Observatoire",
                        "Une coupole ouverte sur les étoiles.",
                        "Un télescope pointe vers le nord.",
                        List.of(),
                        List.of(new Npc("Astronome", "Le deuxième mot se lit après le premier.", true)),
                        null,
                        List.of("Salle suivante"),
                        new Puzzle(
                                "Dans quelle direction pointe le télescope ?",
                                "nord",
                                null,
                                "Conservez les mots obtenus, ils ouvrent le coffre-fort.",
                                null),
                        null)));

        Collections.shuffle(middlePool, random);
        route.addAll(middlePool.subList(0, 3));

        route.add(new Room(
                "Bibliothèque finale",
                "Dernière salle contenant l'énigme la plus difficile.",
                "Des grimoires mentionnent une fontaine secrète.",
                List.of(new Item("Parchemin final", "Indices de fin", "Confirme l'adresse", ItemType.CLUE, 1)),
                List.of(new Npc("Archiviste", "Le coffre-fort est gardé par un golem.", true)),
                null,
                List.of("Adresse du coffre-fort"),
                new Puzzle(
                        "Quel type de lieu est mentionné par les grimoires pour trouver le coffre ?",
                        "fontaine",
                        new Item("TRESOR", "Mot magique", "Mot de phrase magique", ItemType.MAGIC_WORD, 1),
                        "Le mot final est TRESOR.",
                        "Domaine ancien, fontaine centrale"),
                null));

        return route;
    }
}
