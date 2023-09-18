import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;




public class Tree {
    public abstract class MultiSet {
        public abstract boolean add(Object item);

        public abstract void remove(Object item);

        public abstract boolean contains(Object item);

        public abstract boolean isEmpty();

        public abstract int count(Object item);

        public abstract int size();
    }
    private Optional<Object> root;
    private List<Tree> subtrees;
    private static Random random = new Random();

    public Tree(Object root, List<Tree> subtrees) {
        this.root = Optional.ofNullable(root);
        this.subtrees = new ArrayList<>(subtrees);
    }

    public Tree(Object root) {
        this(root, new ArrayList<>());
    }

    public boolean isEmpty() {
        return !root.isPresent();
    }

    public int size() {
        if (isEmpty()) {
            return 0;
        } else {
            int size = 1;
            for (Tree subtree : subtrees) {
                size += subtree.size();
            }
            return size;
        }
    }

    public int count(Object item) {
        if (isEmpty()) {
            return 0;
        } else {
            int num = 0;
            if (root.get().equals(item)) {
                num += 1;
            }
            for (Tree subtree : subtrees) {
                num += subtree.count(item);
            }
            return num;
        }
    }

    @Override
    public String toString() {
        return _str_indented();
    }

    private String _str_indented() {
        return _str_indented(0);
    }

    private String _str_indented(int depth) {
        if (isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("  ".repeat(depth)).append(root.get()).append("\n");
            for (Tree subtree : subtrees) {
                sb.append(subtree._str_indented(depth + 1));
            }
            return sb.toString();
        }
    }

    public double average() {
        if (isEmpty()) {
            return 0.0;
        } else {
            double[] result = _average_helper();
            return result[0] / result[1];
        }
    }

    private double[] _average_helper() {
        if (isEmpty()) {
            return new double[]{0, 0};
        } else {
            double total = ((Number) root.get()).doubleValue();
            int count = 1;
            for (Tree subtree : subtrees) {
                double[] subtreeResult = subtree._average_helper();
                total += subtreeResult[0];
                count += subtreeResult[1];
            }
            return new double[]{total, count};
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tree other = (Tree) obj;
        if (isEmpty() && other.isEmpty()) {
            return true;
        }
        if (isEmpty() || other.isEmpty()) {
            return false;
        }
        if (!root.get().equals(other.root.get())) {
            return false;
        }
        if (subtrees.size() != other.subtrees.size()) {
            return false;
        }
        return subtrees.equals(other.subtrees);
    }

    public boolean contains(Object item) {
        if (isEmpty()) {
            return false;
        }
        if (root.get().equals(item)) {
            return true;
        }
        for (Tree subtree : subtrees) {
            if (subtree.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public List<Object> leaves() {
        List<Object> result = new ArrayList<>();
        _leaves_helper(result);
        return result;
    }

    private void _leaves_helper(List<Object> result) {
        if (isEmpty()) {
            return;
        }
        if (subtrees.isEmpty()) {
            result.add(root.get());
        }
        for (Tree subtree : subtrees) {
            subtree._leaves_helper(result);
        }
    }

    public boolean deleteItem(Object item) {
        if (isEmpty()) {
            return false;
        }
        if (root.get().equals(item)) {
            _deleteRoot();
            return true;
        }
        for (int i = 0; i < subtrees.size(); i++) {
            if (subtrees.get(i).deleteItem(item)) {
                if (subtrees.get(i).isEmpty()) {
                    subtrees.remove(i);
                }
                return true;
            }
        }
        return false;
    }

    private void _deleteRoot() {
        if (subtrees.isEmpty()) {
            root = Optional.empty();
        } else {
            Tree chosenSubtree = subtrees.remove(subtrees.size() - 1);
            root = chosenSubtree.root;
            subtrees.addAll(chosenSubtree.subtrees);
        }
    }

    public void insert(Object item) {
        if (isEmpty()) {
            root = Optional.of(item);
        } else if (subtrees.isEmpty()) {
            subtrees.add(new Tree(item));
        } else {
            int randomNum = random.nextInt(3) + 1;
            if (randomNum == 3) {
                subtrees.add(new Tree(item));
            } else {
                int subtreeIndex = random.nextInt(subtrees.size());
                subtrees.get(subtreeIndex).insert(item);
            }
        }
    }

    public boolean insertChild(Object item, Object parent) {
        if (isEmpty()) {
            return false;
        }
        if (root.get().equals(parent)) {
            subtrees.add(new Tree(item));
            return true;
        }
        for (Tree subtree : subtrees) {
            if (subtree.insertChild(item, parent)) {
                return true;
            }
        }
        return false;
    }
}
