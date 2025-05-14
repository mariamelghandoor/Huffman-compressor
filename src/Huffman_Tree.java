import java.util.Map;
import java.util.PriorityQueue;
import java.util.HashMap;

public class Huffman_Tree {
    static class Node {
        int value;
        Node left;
        Node right;
        Character symbol;
        String binary_code;

        Node(int value, Character symbol) {
            this.symbol = symbol;
            this.value = value;
            left = right = null;
            binary_code = null;
        }

        Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.value = left.value + right.value;
            binary_code = null;
        }
    }

    public Node upgrade_tree(Node node1, Node node2) {
        if (node1.value < node2.value) {
            return new Node(node1, node2);
        }
        return new Node(node2, node1);
    }

    public Node build_tree(PriorityQueue<Node> pq) {
        while (pq.size() > 1) {
            Node node1 = pq.poll();
            Node node2 = pq.poll();
            pq.add(new Node(node1, node2));
        }
        return pq.poll();
    }

        public void assign_code(Node root, String code) {
            if (root == null) {
                return;
            }
            if (root.symbol != null) {
                root.binary_code = code;
            }
            assign_code(root.left, code + "0");
            assign_code(root.right, code + "1");
        }

    public Map<Character, String> build_mapping_dict(Node root) {
        Map<Character, String> map = new HashMap<>();
        build_mapping_helper(root, "", map);
        return map;
    }

    private void build_mapping_helper(Node root, String code, Map<Character, String> map) {
        if (root == null)
            return;
        if (root.symbol != null) {
            map.put(root.symbol, code);
        }
        build_mapping_helper(root.left, code + "0", map);
        build_mapping_helper(root.right, code + "1", map);
    }

    public void print_tree(Node root) {
        if (root == null) {
            return;
        }
        if (root.symbol != null) {
            System.out.println("Letter: " + root.symbol + ", Code: " + root.binary_code);
        }
        print_tree(root.left);
        print_tree(root.right);
    }

}
