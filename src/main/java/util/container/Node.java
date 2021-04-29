package util.container;

/**
 * A node for a singly-linked list.
 * 
 * @author Cole Petersen
 */
final class Node<V> {
    final V v; Node<V> n;
    
    Node(final V v) {this.v = v;}
    Node(final V v,final Node<V> n) {this(v); this.n = n;}
}