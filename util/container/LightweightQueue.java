package util.container;

/**
 * A queue container based on the {@linkplain LightweightContainer} class.
 * 
 * @author Cole Petersen
 */
public final class LightweightQueue<V> extends LightweightContainer<V> {
    @Override
    void internalPush(final V v) {
        final Node<V> n = t;
        t = new Node<>(v);
        if(n == null) h = t;
        else n.n = t;
    }
}