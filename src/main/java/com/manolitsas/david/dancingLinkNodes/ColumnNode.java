package com.manolitsas.david.dancingLinkNodes;

/**
 * @author David Manolitsas
 */
public class ColumnNode extends DancingNode {

    protected int size;
    protected String name;

    public ColumnNode(String name) {
        super();
        this.size = 0;
        this.name = name;
        this.colNode = this;
    }

    public void cover() {
        removeLeftRight();

        for (DancingNode i = down; i != this; i = i.down) {
            for (DancingNode j = i.right; j != i; j = j.right) {
                j.removeTopBottom();
                j.colNode.size--;
            }
        }
    }

    public void uncover() {
        for (DancingNode i = up; i != this; i = i.up) {
            for (DancingNode j = i.left; j != i; j = j.left) {
                j.colNode.size++;
                j.reinsertTopBottom();
            }
        }

        reinsertLeftRight();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void incrementSize() {
        this.size++;
    }

}