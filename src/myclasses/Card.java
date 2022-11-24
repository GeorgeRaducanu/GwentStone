package myclasses;

import fileio.CardInput;
import java.util.ArrayList;

public class Card {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int isMinion;
    private int isEnvironment;
    private int isHero;
    private int isFrozen;
    private int isTank;

    private int hasAttacked;

    public Card(final CardInput card) {
        this.mana = card.getMana();
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
        this.description = new String(card.getDescription());
        this.colors = new ArrayList<String>(card.getColors().size());
        for (String iterate : card.getColors()) {
            this.colors.add(iterate);
            //deep copy for array list
        }
        this.name = new String(card.getName());
    }

    public Card(final Card card) {
        this.mana = card.getMana();
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
        this.description = new String(card.getDescription());
        this.colors = new ArrayList<String>(card.getColors().size());
        for (String iterate : card.getColors()) {
            this.colors.add(iterate);
            //deep copy for array list
        }
        this.name = new String(card.getName());
        this.hasAttacked = card.getHasAttacked();
        this.isTank = card.getIsTank();
        this.isHero = card.getIsHero();
        this.isEnvironment = card.getIsEnvironment();
        this.isMinion = card.getIsMinion();

    }
    /**
     * Check if the card can pe placed on the first
     row of the player (the one from the enemy)
     */
    public final int is1stRow() {

        if (this.name.equals("The Ripper")) {
            return 1;
        }
        if (this.name.equals("Miraj")) {
            return 1;
        }
        if (this.name.equals("Goliath")) {
            return 1;
        }
        if (this.name.equals("Warden")) {
            return 1;
        }
        return 0;
    }

    /**
     * Check if the card can pe placed on the second
     row of the player (the one from the enemy)
     */
    public final int is2ndRow() {

        if (this.name.equals("The Cursed One")) {
            return 1;
        }
        if (this.name.equals("Sentinel")) {
            return 1;
        }
        if (this.name.equals("Berserker")) {
            return 1;
        }
        if (this.name.equals("Disciple")) {
            return 1;
        }
        return 0;
    }

    /**
     * Check if the card is a tank card
     */
    public final int isTank() {
        if (this.name.equals("Goliath")) {
            return 1;
        }
        if (this.name.equals("Warden")) {
            return 1;
        }
        return 0;
    }
    public final int getHasAttacked() {
        return hasAttacked;
    }

    public final void setHasAttacked(final int hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final int getMana() {
        return mana;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final int getIsMinion() {
        return isMinion;
    }
    public final int getIsEnvironment() {
        return isEnvironment;
    }
    public final void setIsEnvironment(final int isEnvironment) {
        this.isEnvironment = isEnvironment;
    }

    public final int getIsHero() {
        return isHero;
    }

    public final void setIsHero(final int isHero) {
        this.isHero = isHero;
    }

    public final int getIsFrozen() {
        return isFrozen;
    }

    public final void setIsFrozen(final int isFrozen) {
        this.isFrozen = isFrozen;
    }
    public final int getIsTank() {
        return isTank;
    }
    public final void setIsTank(final int isTank) {
        this.isTank = isTank;
    }
}
