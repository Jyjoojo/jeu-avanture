package fr.jeu.aventure.model;

public class Monster {
    private final String name;
    private final int hitPoints;
    private final Item reward;

    public Monster(String name, int hitPoints, Item reward) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public Item getReward() {
        return reward;
    }
}
