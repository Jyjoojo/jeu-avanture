package fr.jeu.aventure.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Room {
    private final String name;
    private final String description;
    private final String environment;
    private final List<Item> items;
    private final List<Npc> npcs;
    private final List<String> exits;
    private final String requiredKey;
    private Monster monster;
    private final Puzzle puzzle;
    private boolean puzzleSolved;

    public Room(
            String name,
            String description,
            String environment,
            List<Item> items,
            List<Npc> npcs,
            Monster monster,
            List<String> exits,
            Puzzle puzzle,
            String requiredKey) {
        this.name = name;
        this.description = description;
        this.environment = environment;
        this.items = new ArrayList<>(items);
        this.npcs = new ArrayList<>(npcs);
        this.monster = monster;
        this.exits = new ArrayList<>(exits);
        this.puzzle = puzzle;
        this.requiredKey = requiredKey;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEnvironment() {
        return environment;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Npc> getNpcs() {
        return Collections.unmodifiableList(npcs);
    }

    public Optional<Monster> getMonster() {
        return Optional.ofNullable(monster);
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    public List<String> getExits() {
        return Collections.unmodifiableList(exits);
    }

    public String getRequiredKey() {
        return requiredKey;
    }

    public Optional<Item> takeItem(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(i);
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public Optional<Npc> findNpc(String npcName) {
        return npcs.stream().filter(npc -> npc.getName().equalsIgnoreCase(npcName)).findFirst();
    }

    public void clearMonster() {
        this.monster = null;
    }

    public void markPuzzleSolved() {
        this.puzzleSolved = true;
    }
}
