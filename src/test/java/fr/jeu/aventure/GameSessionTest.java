package fr.jeu.aventure;

import fr.jeu.aventure.controller.GameController;
import fr.jeu.aventure.model.GameSession;
import fr.jeu.aventure.model.GameStatus;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSessionTest {

    @Test
    void shouldGenerateFiveRoomsWithFixedStartAndEnd() {
        GameSession session = new GameSession("Test", 40, new Random(1));

        assertEquals(5, session.getRooms().size());
        assertEquals("Hall d'entrée", session.getRooms().get(0).getName());
        assertEquals("Bibliothèque finale", session.getRooms().get(session.getRooms().size() - 1).getName());
    }

    @Test
    void shouldConsumeExtraLifeThenLoseWhenCombatIsRetriedWithoutAmmo() {
        GameController controller = new GameController("Test", 40, new Random(2));
        controller.collect("Pistolet ancien");
        controller.collect("Munitions de départ");
        controller.collect("Vie supplémentaire");

        String firstFight = controller.fight();
        assertTrue(firstFight.contains("vie supplémentaire"));
        assertEquals(2, controller.getSession().getPlayer().getAmmunitions());
        assertEquals(0, controller.getSession().getPlayer().getExtraLives());

        String secondFight = controller.fight();
        assertTrue(secondFight.contains("Défaite définitive"));
        assertEquals(GameStatus.LOST, controller.getStatus());
    }

    @Test
    void shouldExpireWhenTimeRunsOut() {
        GameController controller = new GameController("Test", 1, new Random(3));

        controller.observe();

        assertEquals(GameStatus.TIME_EXPIRED, controller.getStatus());
    }

    @Test
    void shouldWinAfterSolvingFinalPuzzleDefeatingGuardianAndUsingPhrase() {
        GameController controller = new GameController("Test", 120, new Random(4));

        controller.collect("Clé du domaine");
        controller.collect("Pistolet ancien");
        controller.collect("Munitions de départ");
        controller.collect("Munitions de secours");
        controller.collect("Vie supplémentaire");

        controller.fight();
        controller.solve("laiton");

        for (int i = 0; i < 4; i++) {
            controller.move();
            while (!controller.getSession().getCurrentRoom().getItems().isEmpty()) {
                controller.collect(controller.getSession().getCurrentRoom().getItems().get(0).getName());
            }
            if (controller.getSession().getCurrentRoom().getMonster().isPresent()) {
                controller.fight();
            }
            String answer = controller.getSession().getCurrentRoom().getPuzzle().getAnswer();
            controller.solve(answer);
        }

        controller.reachSafe();
        controller.fight();
        controller.openSafe("LUMIERE GUIDE VERS TRESOR");

        assertEquals(GameStatus.WON, controller.getStatus());
    }
}
