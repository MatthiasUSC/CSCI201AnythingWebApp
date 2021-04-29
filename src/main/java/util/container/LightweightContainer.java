package util.container;

/**
 * A lightweight singly-linked list data structure.
 * 
 * @author Cole Petersen
 */
public abstract class LightweightContainer<V> {
    Node<V> h = null,t = null;
    int size = 0;
    
    /** Internally pushes the value onto the container. */
    abstract void internalPush(final V v);
    
    /** Pushes an element onto the top of the container. */
    public void push(final V v) {
        internalPush(v);
        ++size;
    }
    
    /**
     * Pops an element from the bottom of the container.
     * 
     * @return The element, or <code>null</code> if no such element exists.
     */
    public V pop() {
        if(h == null) return null;
        final V v = h.v;
        h = h.n;
        if(h == null) t = null;
        --size;
        return v;
    }
    
    /** Removes all elements from the container. */
    public void clear() {
        // It is better for garbage collection if the nodes are manually
        // un-linked before going out of scope.
        while(h != null) {
            final Node<V> n = h;
            h = h.n;
            n.n = null;
        }
        t = null;
        size = 0;
    }
    
    /**
     * Transfers all elements from the argument to the top of this container.
     * Specifically, the tail of the argument is linked to the head of this
     * container and the head is assigned to the head of the argument.
     */
    public <C extends LightweightContainer<V>> void addAll(final C d) {
        if(d.h != null) {
            if(h == null) {
                h = d.h;
                t = d.t;
                size = d.size;
            } else {
                d.t.n = h;
                h = d.h;
                size += d.size;
            }
            d.h = d.t = null;
            d.size = 0;
        }
    }
    
    /** @return The number of elements in the container. */
    public int size() {return size;}
    /** @return <code>true</code> iff the container has no elements. */
    public boolean empty() {return h == null;}
}