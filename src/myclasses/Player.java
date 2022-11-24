package myclasses;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> handCards = new ArrayList<Card>();
    private int manaPlayer;
    private Card hero;
    private int handIndex;
    private int playerIndex;
    public final ArrayList<Card> getHandCards() {
        return this.handCards;
    }
    /**
     * Method for adding a card to the player's hand
     */
    public final void addHandCard(final Card card) {
        this.handCards.add(card);
    }
    /**
     * Method for adding mana to the player
     */
    public final void addMana(final int round) {
        this.setManaPlayer(this.getManaPlayer() + round);
    }
    /**
     * Method for removing a card by its index from the hand
     */
    public final void removeFromHand(final int index) {
        this.handCards.remove(index);
    }
    public final int getManaPlayer() {
        return manaPlayer;
    }
    public final void setManaPlayer(final int manaPlayer) {
        this.manaPlayer = manaPlayer;
    }

    public final Card getHero() {
        return hero;
    }

    public final void setHero(final Card hero) {
        this.hero = hero;
    }
}
