package myclasses;

public final class LordRoyce extends Card {
    public LordRoyce(final Card card) {
        super(card);
        // health = 30;
        this.setIsHero(1);
        this.setHealth(30);
    }
}
