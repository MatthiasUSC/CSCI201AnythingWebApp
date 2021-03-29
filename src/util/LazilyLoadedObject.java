package util;

public class LazilyLoadedObject<T> {
    public interface Copier<T> {void copy(final T src,final T dst);}
    public interface Instantiator<T> {T create();}
    public interface Mutator<T> {void mutate(final T value);}
    
    private T value = null;
    private boolean lazy = true;
    private LazilyLoadedObject<T> previous;
    private Copier<T> copier;
    private Instantiator<T> instantiator;
    
    public LazilyLoadedObject(final LazilyLoadedObject<T> previous,
                              final Copier<T> copier,
                              final Instantiator<T> instantiator) {
        this.previous = previous;
        this.copier = copier;
        this.instantiator = instantiator;
    }
    
    /*\
     * Lazy loading:
     * To prevent unnecessary copying, values can be yoinked from previous states.
     * 
     * x: previous state
     * y: this state
     * v: value
     * f: loading function
     * g: default value initializer
     * 
     * == Pseudocode ==
     * 
     * if(y.v == null) {
     *     if(x == null) {
     *         // No state to steal from.
     *         return y.v = g()
     *     } else {
     *         // Steal from previous state.
     *         return x.f()
     *     }
     * } else {
     *     return y.v
     * }
     * 
     * If 'g' does not exist, we can ignore the assignment to y.v. This would then simplify to:
     * 
     * if(y.v != null OR x == null) {
     *     return y.v
     * } else {
     *     return x.f()
     * }
    \*/
    
    private void init() {
        lazy = false;
        value = instantiator.create();
    }
    public T read() {
        if(value != null) return value;
        if(previous != null) return value = previous.read();
        init();
        return value;
    }
    
    
    /*\
     * Lazy mutating:
     * The result of lazily-loading values is that said values must either exist in the current
     * state or be copied from a previous state before being mutable.
    \*/
    
    private void cpy() {
        if(previous != null) copier.copy(previous.read(),value);
        else init();
    }
    public void write(final Mutator<T> m) {
        if(lazy) {lazy = false; cpy();}
        m.mutate(value);
    }
    public void set(final T value) {
        lazy = false;
        this.value = value;
    }
}



















































