package myclasses;

import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;

public class Decks {

    private int nrCardsInDeck;
    private int nrDecks;

    public final ArrayList<Deck> getDecks() {
        return decks;
    }

    public final void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    private ArrayList<Deck> decks;

    public Decks(final DecksInput decks) {
        this.nrDecks = decks.getNrDecks();
        this.nrCardsInDeck = decks.getNrCardsInDeck();
        this.decks = new ArrayList<Deck>();
        for (ArrayList<CardInput> iterate1 : decks.getDecks()) {
            this.decks.add(new Deck(iterate1));
        }
    }


    public final int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public final void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public final int getNrDecks() {
        return nrDecks;
    }

    public final void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }
}
