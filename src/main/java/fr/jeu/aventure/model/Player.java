package fr.jeu.aventure.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Player {
    private final String name;
    private final List<Item> inventory = new ArrayList<>();
    private final Set<String> clues = new HashSet<>();
    private final List<String> magicWords = new ArrayList<>();
    private int ammunitions;
    private int extraLives;
    private boolean weaponEquipped;

    public Player(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        inventory.add(item);
        switch (item.getType()) {
            case WEAPON -> weaponEquipped = true;
            case AMMUNITION -> ammunitions += item.getQuantity();
            case EXTRA_LIFE -> extraLives += item.getQuantity();
            case CLUE -> clues.add(item.getUtility());
            case MAGIC_WORD -> magicWords.add(item.getName().toUpperCase(Locale.ROOT));
            default -> {
                // intentionally empty for other item types
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<Item> getInventory() {
        return List.copyOf(inventory);
    }

    public Set<String> getClues() {
        return Set.copyOf(clues);
    }

    public List<String> getMagicWords() {
        return List.copyOf(magicWords);
    }

    public int getAmmunitions() {
        return ammunitions;
    }

    public int getExtraLives() {
        return extraLives;
    }

    public boolean hasWeapon() {
        return weaponEquipped;
    }

    public boolean hasItem(String itemName) {
        return inventory.stream().anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
    }

    public boolean consumeAmmunition(int amount) {
        if (ammunitions < amount) {
            return false;
        }
        ammunitions -= amount;
        return true;
    }

    public boolean consumeLife() {
        if (extraLives <= 0) {
            return false;
        }
        extraLives--;
        return true;
    }
}
