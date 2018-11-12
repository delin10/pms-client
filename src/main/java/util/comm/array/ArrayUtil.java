package util.comm.array;

import java.util.function.Function;

public class ArrayUtil {
	private static ArrayContainJudger containJudger=null;
	
	public static <T> ArrayContainJudger parseContainJudger(T[] arr) {
		if (containJudger==null) {
			containJudger=new ArrayContainJudger();
		}
		containJudger.parse(arr);
		return containJudger;
	}
	
	public static <T> boolean contains(T e) {
		return containJudger.contains(e);
	}
	
	public static byte[][] splitArray(byte[] data,int len){
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if(y!=0){
			z = 1;
		}
		byte[][] arrays = new byte[x+z][];
		byte[] arr;
		for(int i=0; i<x+z; i++){
			arr = new byte[len];
			if(i==x+z-1 && y!=0){
				System.arraycopy(data, i*len, arr, 0, y);
			}else{
				System.arraycopy(data, i*len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}
	
	public static <T> int indexOf(T[] arr,Object o) {
		for (int i=0;i<arr.length;++i) {
			if (o==null) {
				if (o==arr[i]) {
					return i;
				}
			}else {
				if (o.equals(arr[i])) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static <T> int indexOf(T[] arr,Object o,int start_index,Function<T,?> func) {
		if (start_index<0||start_index>arr.length) {
			throw new IllegalArgumentException("Error start_index");
		}
		for (int i=start_index;i<arr.length;++i) {
			if (o==null) {
				if (o==arr[i]) {
					return i;
				}
			}else {
				Object value=func==null?arr[i]:func.apply(arr[i]);
				if (o.equals(value)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public static <T> int indexOf(T[] arr,Object o,Function<T,?> func) {
		return indexOf(arr, o, 0, func);
	}

}
