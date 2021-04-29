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
        final int l = Integer.highestOneBit(al);
        for(int i = 1;i < l;i <<= 1) arraycopy(arr,0,arr,i,i);
        if(al != l) arraycopy(arr,0,arr,l,al-l);
    }
    
    public static void fastArrayFill(final int[] arr,final int value,final int start,final int length) {
        arr[start] = value;
        final int l = Integer.highestOneBit(length);
        for(int i = 1;i < l;i <<= 1) arraycopy(arr,start,arr,start + i,i);
        if(length != l) arraycopy(arr,start,arr,start+l,length-l);
    }
    public static void fastArrayFill(final float[] arr,final float value,final int start,final int length) {
        arr[start] = value;
        final int l = Integer.highestOneBit(length);
        for(int i = 1;i < l;i <<= 1) arraycopy(arr,start,arr,start + i,i);
        if(length != l) arraycopy(arr,start,arr,start+l,length-l);
    }
    
    public static int[] fastArrayFill(final int length,final int value) {
        if(length < 1) return null;
        final int[] arr = new int[length];
        fastArrayFill(arr,value,0,length);
        return arr;
    }
    public static float[] fastArrayFill(final int length,final float value) {
        if(length < 1) return null;
        final float[] arr = new float[length];
        fastArrayFill(arr,value,0,length);
        return arr;
    }
    
    /**
     * Creates an array filled with the specified value.
     * 
     * @param length Length of arrays to fill.
     * @param fills  Value to fill each array.
     */
    public static int[][] fastArrayFill(final int length,final int[] fills) {
        final int[][] arr = new int[fills.length][length];
        {
            int i = -1;
            for(final int j : fills) arr[++i][0] = j;
        }
        final int l = Integer.highestOneBit(length);
        for(int i = 1;i < l;i <<= 1) for(final int[] j : arr) arraycopy(j,0,j,i,i);
        final int nl = length - l;
        if(nl != 0) for(final int[] j : arr) arraycopy(j,0,j,l,nl);
        return arr;
    }
}