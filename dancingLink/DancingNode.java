package dancingLink;

/**
 * @author David Manolitsas
 * @project rmitSudoku
 * @date 2020-05-26
 */
public class DancingNode {
    protected DancingNode left, right, top, bottom;
    protected ColumnNode column;

    public DancingNode() {
        this.left = this;
        this.right = this;
        this.top = this;
        this.bottom = this;
    }

    public DancingNode(ColumnNode columnNode) {
        this();
        this.column = columnNode;
    }


    public DancingNode linkDown(DancingNode node) {
        node.bottom = bottom;
        node.bottom.top = node;
        node.top = this;
        bottom = node;
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
        top.bottom = bottom;
        bottom.top = top;
    }

    public void reinsertTopBottom() {
        top.bottom = this;
        bottom.top = this;
    }

}