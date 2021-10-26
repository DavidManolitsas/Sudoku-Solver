package com.manolitsas.david.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Manolitsas
 */
public class KillerCage {

    private int total;
    private List<int[]> positions;

    public KillerCage() {
        positions = new ArrayList<int[]>();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<int[]> getPositions() {
        return positions;
    }

}
