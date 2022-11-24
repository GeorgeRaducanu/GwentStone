package myclasses;

import java.util.ArrayList;

public class Table {
    final ArrayList<ArrayList<Card>> table;
    public final ArrayList<ArrayList<Card>> getTable() {
        return table;
    }

    /**
     * Method for adding a minion to the table
     */
    public final void addMinion(final int row, final Card card) {
        this.table.get(row).add(card);
    }

    public Table() {
        table = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            table.add(i, new ArrayList<Card>());
        }
    }
}
