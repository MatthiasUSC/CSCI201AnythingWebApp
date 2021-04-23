package util.container;

public final class LightweightStack<V> extends LightweightContainer<V> {
    @Override
    void internalPush(final V v) {
        h = new Node<>(v,h);
        if(t == null) t = h;
    }
}