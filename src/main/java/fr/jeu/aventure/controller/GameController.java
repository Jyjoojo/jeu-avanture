package fr.jeu.aventure.controller;

import fr.jeu.aventure.model.GameSession;
import fr.jeu.aventure.model.GameStatus;

import java.util.Random;

public class GameController {
    private final GameSession session;

    public GameController(String playerName, int availableTime, Random random) {
        this.session = new GameSession(playerName, availableTime, random);
    }

    public GameSession getSession() {
        return session;
    }

    public GameStatus getStatus() {
        return session.getStatus();
    }

    public String observe() {
        return session.observe();
    }

    public String collect(String itemName) {
        return session.pickUpItem(itemName);
    }

    public String talkTo(String npcName) {
        return session.talkToNpc(npcName);
    }

    public String solve(String answer) {
        return session.solvePuzzle(answer);
    }

    public String move() {
        return session.moveToNextRoom();
    }

    public String fight() {
        return session.fightMonster();
    }

    public String reachSafe() {
        return session.goToSafeAddress();
    }

    public String openSafe(String phrase) {
        return session.openSafe(phrase);
    }
}
