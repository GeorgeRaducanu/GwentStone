package myclasses;

public final class EmpressThorina extends Card {
    public EmpressThorina(final Card card) {
        super(card);
        //health = 30
        this.setIsHero(1);
        this.setHealth(30);
    }
}
