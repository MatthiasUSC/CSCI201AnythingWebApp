package util;

import static java.lang.System.arraycopy;

public final class ArrayUtil {
    /**
     * Fills an array with the specified value.
     * 
     * @param <T> Array type
     * @param arr Array to fill.
     * @param val Value to fill.
     */
    public static <T> void fastArrayFill(final T[] arr,final T val) {
        final int al = arr.length;
        if(al < 1) return;
        arr[0] = val;
        for(int i = 1;i < al;i <<= 1) {
            final int l = al - i;
            arraycopy(arr,0,arr,i,l < i? l : i);
        }
    }
    
    public static int[] fastArrayFill(final int length,final int fill) {
        if(length < 1) return null;
        final int[] arr = new int[length];
        for(int i = 1;i < length;i <<= 1) arraycopy(arr,0,arr,0,length < i? length : i);
        return arr;
    }
    public static float[] fastArrayFill(final int length,final float fill) {
        if(length < 1) return null;
        final float[] arr = new float[length];
        for(int i = 1;i < length;i <<= 1) arraycopy(arr,0,arr,0,length < i? length : i);
        return arr;
    }
    
    /**
     * Creates an array filled with the specified value.
     * 
     * @param length Length of arrays to fill.
     * @param fills  Value to fill each array.
     */
    public static int[][] fastArrayFill(final int length,final int[] fills) {
        if(length < 1 || fills == null) return null;
        final int[][] arr = new int[fills.length][length];
        {
            int i = -1;
            for(final int j : fills) arr[++i][0] = j;
        }
        for(int i = 1;i < length;i <<= 1) {
            final int l = length - i;
            for(int j = 0;j < fills.length;++j) arraycopy(arr[j],0,arr[j],0,l < i? l : i);
        }
        return arr;
    }
}