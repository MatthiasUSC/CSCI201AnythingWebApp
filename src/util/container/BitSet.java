package util.container;

public class BitSet {
    private final long[] data;
    
    public BitSet(final short size) {data = new long[size / Long.SIZE + (size % Long.SIZE > 0? 1 : 0)];}
    
    public short setNextFalse(final short i) {
        short j,k; byte l;
        for(j = i,k = (short)(i / Long.SIZE),l = (byte)(i % Long.SIZE);j != i;++j) {
            if((data[k] & (1 << l)) == 0) {data[k] |= 1 << l; return j;}
            else if((l = (byte)((l + 1) % Long.SIZE)) == 0) k = (short)((k + 1) % data.length);
        }
        return -1;
    }
    public boolean get(final short i) {return (data[i / Long.SIZE] & (1 << (i % Long.SIZE))) != 0;}
}