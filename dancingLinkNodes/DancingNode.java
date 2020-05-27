package dancingLinkNodes;

/**
 * @author David Manolitsas
 * @project rmitSudoku
 * @date 2020-05-26
 */
public class DancingNode {
    protected DancingNode left, getRight, up, down;
    protected ColumnNode colNode;

    public DancingNode() {
        this.left = this;
        this.getRight = this;
        this.up = this;
        this.down = this;
    }

    public DancingNode(ColumnNode columnNode) {
        this();
        this.colNode = columnNode;
    }


    public DancingNode linkDown(DancingNode node) {
        node.down = down;
        node.down.up = node;
        node.up = this;
        down = node;
        return node;
    }

    public DancingNode linkRight(DancingNode node) {
        node.getRight = getRight;
        node.getRight.left = node;
        node.left = this;
        getRight = node;
        return node;
    }

    public void removeLeftRight() {
        left.getRight = getRight;
        getRight.left = left;
    }

    public void reinsertLeftRight() {
        left.getRight = this;
        getRight.left = this;
    }

    public void removeTopBottom() {
        up.down = down;
        down.up = up;
    }

    public void reinsertTopBottom() {
        up.down = this;
        down.up = this;
    }

    public DancingNode getLeft() {
        return left;
    }

    public void setLeft(DancingNode left) {
        this.left = left;
    }

    public DancingNode getRight() {
        return getRight;
    }

    public void setRight(DancingNode right) {
        this.getRight = right;
    }

    public DancingNode getUp() {
        return up;
    }

    public void setUp(DancingNode up) {
        this.up = up;
    }

    public DancingNode getDown() {
        return down;
    }

    public void setDown(DancingNode down) {
        this.down = down;
    }

    public ColumnNode getColNode() {
        return colNode;
    }

    public void setColNode(ColumnNode colNode) {
        this.colNode = colNode;
    }
}