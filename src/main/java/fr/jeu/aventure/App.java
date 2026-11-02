package fr.jeu.aventure;

import fr.jeu.aventure.controller.GameController;
import fr.jeu.aventure.model.GameStatus;
import fr.jeu.aventure.view.TextGameView;

import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameController controller = new GameController("Aventurier", 40, new Random());
        TextGameView view = new TextGameView();

        System.out.println("Jeu d'aventures MVC. Commandes: observer, ramasser <objet>, parler <pnj>, enigme <reponse>, combattre, avancer, coffre, ouvrir <phrase>, inventaire, quitter");

        while (controller.getStatus() == GameStatus.ONGOING) {
            System.out.println(view.renderHeader(controller.getSession()));
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quitter")) {
                break;
            }

            String result;
            if (input.equalsIgnoreCase("observer")) {
                result = controller.observe();
            } else if (input.toLowerCase().startsWith("ramasser ")) {
                result = controller.collect(input.substring("ramasser ".length()));
            } else if (input.toLowerCase().startsWith("parler ")) {
                result = controller.talkTo(input.substring("parler ".length()));
            } else if (input.toLowerCase().startsWith("enigme ")) {
                result = controller.solve(input.substring("enigme ".length()));
            } else if (input.equalsIgnoreCase("combattre")) {
                result = controller.fight();
            } else if (input.equalsIgnoreCase("avancer")) {
                result = controller.move();
            } else if (input.equalsIgnoreCase("coffre")) {
                result = controller.reachSafe();
            } else if (input.toLowerCase().startsWith("ouvrir ")) {
                result = controller.openSafe(input.substring("ouvrir ".length()));
            } else if (input.equalsIgnoreCase("inventaire")) {
                result = view.renderInventory(controller.getSession());
            } else {
                result = "Commande inconnue.";
            }

            System.out.println(result);
        }

        System.out.println(view.renderOutcome(controller.getSession()));
    }
}
