package fr.jeu.aventure.model;

public class Npc {
    private final String name;
    private final String information;
    private final boolean truthful;

    public Npc(String name, String information, boolean truthful) {
        this.name = name;
        this.information = information;
        this.truthful = truthful;
    }

    public String getName() {
        return name;
    }

    public String getInformation() {
        return information;
    }

    public boolean isTruthful() {
        return truthful;
    }
}
