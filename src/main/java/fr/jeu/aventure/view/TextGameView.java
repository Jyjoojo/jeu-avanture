package fr.jeu.aventure.view;

import fr.jeu.aventure.model.GameSession;

public class TextGameView {
    public String renderHeader(GameSession session) {
        return "Joueur: " + session.getPlayer().getName()
                + " | Temps restant: " + session.getRemainingTime()
                + " | Salle: " + session.getCurrentRoom().getName();
    }

    public String renderInventory(GameSession session) {
        return "Inventaire: " + session.getPlayer().getInventory().stream().map(item -> item.getName() + "(" + item.getType() + ")").toList()
                + " | Munitions: " + session.getPlayer().getAmmunitions()
                + " | Vies supplémentaires: " + session.getPlayer().getExtraLives();
    }

    public String renderOutcome(GameSession session) {
        return "Statut: " + session.getStatus();
    }
}
