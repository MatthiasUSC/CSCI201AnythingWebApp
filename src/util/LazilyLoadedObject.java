package util;

public abstract class LazilyLoadedObject<V> {
    public static interface Mutator<V> {void mutate(final V v);}
    
    protected V v = null;
    private boolean lazy = true;
    protected LazilyLoadedObject<V> p;
    
    public LazilyLoadedObject(final LazilyLoadedObject<V> previous) {
        p = previous;
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
    
    protected abstract void init();
    public V read() {
        if(v != null) return v;
        if(p != null) return v = p.read();
        lazy = false;
        init();
        return v;
    }
    
    /*\
     * Lazy mutating:
     * The result of lazily-loading values is that said values must either exist in the current
     * state or be copied from a previous state before being mutable.
    \*/
    
    protected abstract void cpy();
    private void icpy() {if(p == null) init(); else cpy();}
    public void write(final Mutator<V> m) {if(lazy) {lazy = false; icpy();} m.mutate(v);}
    public void set(final V v) {lazy = false; this.v = v;}
}