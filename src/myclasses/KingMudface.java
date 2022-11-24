package myclasses;

public final class KingMudface extends Card {
    public KingMudface(final Card card) {
        super(card);
        //health 30
        this.setIsHero(1);
        this.setHealth(30);
    }
}
