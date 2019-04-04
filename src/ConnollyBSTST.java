/**
 * A binary search tree based implementation of a symbol table.
 * 
 * @author Collin Connolly, Sedgewick and Wayne, Acuna
 * @version 1.0
 */
import java.util.LinkedList;
import java.util.Queue;
        
public class ConnollyBSTST<Key extends Comparable<Key>, Value> implements OrderedSymbolTable<Key, Value> {
    private Node root;

    private class Node
    {
        private final Key key;
        private Value val;
        private Node left, right;
        private int N;

        public Node(Key key, Value val, int N) {
            this.key = key; this.val = val; this.N = N;
        }
    }
    
    @Override
    public int size() {
        return size(root);
    }
    
    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.N;
    }
    
    @Override
    public Value get(Key key) {
        return get(root, key);
    }
    
    private Value get(Node x, Key key) {
        // Return value associated with key in the subtree rooted at x;
        // return null if key not present in subtree rooted at x.
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }
    
    @Override
    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        // Change key’s value to val if key in subtree rooted at x.
        // Otherwise, add new node to subtree associating key with val.
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }
    
    @Override
    public Key min() {
      return min(root).key;
    }
    
    private Node min(Node x) {
        if (x.left == null)
            return x;
        return min(x.left);
    }
    
    @Override
    public Key max() {
      return max(root).key;
    }
    
    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);
    }
    
    @Override
    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null)
            return null;
        return x.key;
    }
    
    private Node floor(Node x, Key key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }
    
    @Override
    public Key select(int k) {
        return select(root, k).key;
    }
    
    private Node select(Node x, int k) {
        if (x == null) return null;
        int t = size(x.left);
        if (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k-t-1);
        else return x;
    }
    
    @Override
    public int rank(Key key) {
        return rank(key, root);
    }
    
    private int rank(Key key, Node x) {
        // Return number of keys less than x.key in the subtree rooted at x.
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }
    
    @Override
    public void deleteMin() {
        root = deleteMin(root);
    }
    
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void deleteMax(){
        return;
    }
    
    @Override
    public void delete(Key key) {
        root = delete(root, key);
    }
    
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else
        {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public Iterable<Key> keys() {
        return keys(min(), max());
    }
    
    @Override
    public Iterable<Key> keys(Key lo, Key hi)
    {
        Queue<Key> queue = new LinkedList<>();
        keys(root, queue, lo, hi);
        return queue;
    }
    
    private void keys(Node x, Queue<Key> queue, Key lo, Key hi)
    {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public boolean contains(Key key) {

        Node iterNode = root;

        while(iterNode.key.compareTo(key) != 0){

            if(iterNode.key.compareTo(key) > 0) {
                if(iterNode.left == null) return false;
                iterNode = iterNode.left;
            } else {
                if (iterNode.right == null) return false;
                iterNode = iterNode.right;
            }
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        return (root == null);
    }

    // Returns the lowest key above the param if it exists
    public Key ceiling(Key key){
        
        Node iter = root;
        Key ceiling = null;

        while(iter != null){
            int cmp = key.compareTo(iter.key);
            if(cmp == 0) return iter.key;

            else if(cmp < 0) {
                if(ceiling == null) ceiling = iter.key;
                if(ceiling.compareTo(iter.key) < 0)
                    ceiling = iter.key;
                iter = iter.left;
            }

            else if(cmp > 0) {
                if(ceiling == null) ceiling = iter.key;
                if(ceiling.compareTo(iter.key) < 0)
                    ceiling = iter.key;
                iter = iter.right;
            }
        }
        return ceiling;
    }

    @Override
    // returns the # of elements between key values
    public int size(Key lo, Key hi) {
        if(hi.compareTo(lo) < 0)
            return 0;
        else if (contains(hi))
            return 1 + rank(hi) - rank(lo);
        return rank(hi) - rank(lo);
    }


    // returns the value of the element at the key in the subtree
    private Value getFast(Node parent, Key key){

        if( parent == null) return null;

        int cmp = key.compareTo(parent.key);

        while(cmp != 0){
            if(cmp < 0) parent = parent.left;
            else if(cmp > 0) parent = parent.right;
        }
        return parent.val;
    }


    // puts an element into the BST quickly
    private void putFast(Key key, Value val){

          Node tempNode = new Node(key, val, 1);

          if(root == null){
              root = tempNode;
              return;
          }

          Node parent = null;
          Node child = root;

          while(child != null){
              parent = child;

              int cmp = key.compareTo(child.key);
              if(cmp < 0) child = child.left;
              else if(cmp > 0) child = child.right;
              else {
                  child.val = val;
                  child.N = size(child.left) + size(child.right) + 1;
                  return;
              }
          }

          int cmp = key.compareTo(parent.key);
          if(cmp < 0) parent.left = tempNode;
          if(cmp > 0) parent.right = tempNode;
    }


    //balances binary search tree
    public void balance() {
        LinkedList<Node> list = new LinkedList<Node>();

        createLinkedList(root, list);
        root = createBalanced(0, size() - 1, list);
        updateChildren(root);
    }

    //helper method for balance which adds
    private void createLinkedList(Node current, LinkedList<Node> list) {

        if(current == null) return;

        createLinkedList(current.left, list);
        list.add(current);
        createLinkedList(current.right, list);
    }

    // structures the balanced tree from a linked list
    private Node createBalanced(int start, int end, LinkedList<Node> list){

        if (start > end) return null;

        int mid = (start + end) / 2;
        if((start + end) % 2 != 0) mid++;

        Node midPoint = list.get(mid);
        midPoint.left = createBalanced(start, mid - 1, list);
        midPoint.right = createBalanced(mid + 1, end, list);
        
        return midPoint;
    }

    // creates the N children var for new nodes
    private void updateChildren(Node current) {
        if(current == null) return;

        current.N = getChildren(current);
        updateChildren(current.left);
        updateChildren(current.right);
    }

    // finds the number of children a node in the tree has
    private int getChildren(Node current) {
        if(current == null){
            return 0;
        } else {
            return 1 + getChildren(current.left) + getChildren(current.right);
        }
    }


    // Prints the tree by level
    public void printLevel(Key key) {
        if(!contains(key)) System.out.println("does not contain that key");

        Node iter = root;

        Queue<Node> queue = new LinkedList<Node>();
        Queue<Key> keys = new LinkedList<Key>();

        while(key.compareTo(iter.key) != 0){
            if(key.compareTo(iter.key) < 0){
                iter = iter.left;
            } else {
                iter = iter.right;
            }
        }

        queue.add(iter);

        while(!queue.isEmpty()){
            Node temp = queue.remove();
            if(temp == null) continue;
            keys.add(temp.key);
            queue.add(temp.left);
            queue.add(temp.right);
        }
        System.out.println(keys);

    }

    /**
     * entry point for testing.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ConnollyBSTST<Integer, String> bst = new ConnollyBSTST<>();

        bst.put(10, "TEN");
        bst.put(3, "THREE");
        bst.put(1, "ONE");
        bst.put(5, "FIVE");
        bst.put(2, "TWO");
        bst.put(7, "SEVEN");

        //bst.put(13, "THIRTEEN");
        //bst.put( 15, "Fifteen");

        bst.printLevel(bst.root.key);
        //System.out.println(bst.ceiling(6));

        //bst.printLevel(10);
        //System.out.println(bst.ceiling(11));
        //bst.balance();
        //System.out.println(bst.size(3, 9));


        System.out.println("Before balance:");
        bst.printLevel(10); //root

        System.out.println("After balance:");
        bst.balance();
        bst.printLevel(5); //root

    }
}