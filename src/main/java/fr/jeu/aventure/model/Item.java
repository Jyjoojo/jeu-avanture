package fr.jeu.aventure.model;

public class Item {
    private final String name;
    private final String description;
    private final String utility;
    private final ItemType type;
    private final int quantity;

    public Item(String name, String description, String utility, ItemType type, int quantity) {
        this.name = name;
        this.description = description;
        this.utility = utility;
        this.type = type;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUtility() {
        return utility;
    }

    public ItemType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }
}
