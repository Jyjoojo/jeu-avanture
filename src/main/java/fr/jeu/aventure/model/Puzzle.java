package fr.jeu.aventure.model;

public class Puzzle {
    private final String question;
    private final String answer;
    private final Item reward;
    private final String clue;
    private final String safeAddress;

    public Puzzle(String question, String answer, Item reward, String clue, String safeAddress) {
        this.question = question;
        this.answer = answer;
        this.reward = reward;
        this.clue = clue;
        this.safeAddress = safeAddress;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Item getReward() {
        return reward;
    }

    public String getClue() {
        return clue;
    }

    public String getSafeAddress() {
        return safeAddress;
    }
}
