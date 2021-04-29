package util.container;

public class BitSet {
    private static final byte LONG_SIZE = Long.SIZE;
    private final long[] data;
    
    public BitSet(final short size) {data = new long[size / Long.SIZE + (size % Long.SIZE > 0? 1 : 0)];}
    
    public short setNextFalse(final short i) {
        short j = i,k = (short)(i / LONG_SIZE); byte l = (byte)(i % LONG_SIZE);
        do {
            if((data[k] & (long)(1L << (long)l)) == 0) {data[k] |= (long)(1L << (long)l); return j;}
            else if((l = (byte)((l + 1) % LONG_SIZE)) == 0 && (k = (short)((k + 1) % data.length)) == 0) j = 0;
        } while(++j != i);
        return -1;
    }
    public boolean get(final short i) {return (data[i / LONG_SIZE] & (1 << (i % LONG_SIZE))) != 0;}
    public void clear(final short i) {data[i / LONG_SIZE] &= ~(long)(1L << (long)(i % LONG_SIZE));}
}