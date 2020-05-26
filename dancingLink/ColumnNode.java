package dancingLink;

/**
 * @author David Manolitsas
 * @project rmitSudoku
 * @date 2020-05-26
 */
public class ColumnNode extends DancingNode {

    protected int size;
    protected String name;

    public ColumnNode(String name) {
        super();
        this.size = 0;
        this.name = name;
        this.column = this;
    }

    public void cover() {
        removeLeftRight();

        for (DancingNode i = bottom; i != this; i = i.bottom) {
            for (DancingNode j = i.right; j != i; j = j.right) {
                j.removeTopBottom();
                j.column.size--;
            }
        }
    }

    public void uncover() {
        for (DancingNode i = top; i != this; i = i.top) {
            for (DancingNode j = i.left; j != i; j = j.left) {
                j.column.size++;
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

    public void setName(String name) {
        this.name = name;
    }
}