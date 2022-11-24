package myclasses;

import fileio.CardInput;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> deck;

    public Deck(final ArrayList<CardInput> deckInput) {
        this.deck = new ArrayList<Card>();
        for (CardInput iterate : deckInput) {
            this.deck.add(new Card(iterate));
        }
    }

    public Deck(final Deck deck) {
        this.deck = new ArrayList<Card>();
        for (Card iterate : deck.getDeck()) {
            this.deck.add(new Card(iterate));
        }
    }

    public final void removeFirstElement() {
        this.deck.remove(0);
    }

    public final ArrayList<Card> getDeck() {
        return deck;
    }

    public final void setDeck(final ArrayList<Card> deck) {
        this.deck = deck;
    }
}
