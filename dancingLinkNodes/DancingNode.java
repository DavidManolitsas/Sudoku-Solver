package dancingLinkNodes;

/**
 * @author David Manolitsas
 * @project rmitSudoku
 * @date 2020-05-26
 */
public class DancingNode {
    protected DancingNode left, right, up, down;
    protected ColumnNode colNode;

    public DancingNode() {
        this.left = this;
        this.right = this;
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
        node.right = right;
        node.right.left = node;
        node.left = this;
        right = node;
        return node;
    }

    public void removeLeftRight() {
        left.right = right;
        right.left = left;
    }

    public void reinsertLeftRight() {
        left.right = this;
        right.left = this;
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

    public DancingNode getRight() {
        return right;
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

    public ColumnNode getColNode() {
        return colNode;
    }

}