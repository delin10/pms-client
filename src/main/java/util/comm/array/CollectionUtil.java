package util.comm.array;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CollectionUtil {
    /**
     * @������������ȡ����ArrayList�Ĳ
     * @param firstArrayList ��һ��ArrayList
     * @param secondArrayList �ڶ���ArrayList
     * @return resultList �ArrayList
     */
    public static <T> List<T> diff(List<T> firstArrayList, List<T> secondArrayList) {
        List<T> resultList = new ArrayList<T>();
        LinkedList<T> result = new LinkedList<T>(firstArrayList);// �󼯺���linkedlist  
        HashSet<T> othHash = new HashSet<T>(secondArrayList);// С������hashset  
        Iterator<T> iter = result.iterator();// ����Iterator�������������ݵĲ���  
        while(iter.hasNext()){  
            if(othHash.contains(iter.next())){  
                iter.remove();            
            }     
        }  
        resultList = new ArrayList<T>(result);
        return resultList;
    }
    
    public static Object mergeArrays(ArrayList<Object> arrays,Class<?> clazz) {
    	int size=0;
    	for (int i=0;i<arrays.size();++i) {
    		size+=Array.getLength(arrays.get(i));
    	}

    	Object array=Array.newInstance(clazz, size);
    	size=0;
      	for (int i=0;i<arrays.size();++i) {
    		Object arr=arrays.get(i);
    		int len=Array.getLength(arr);
    		System.arraycopy(arr, 0, array, size, len);
    		size+=len;
    	}
    	
    	return array;
    }
    
    public static Object trimArray(Object o,int length) {
    	Object array=Array.newInstance(o.getClass().getComponentType(), length);
    	System.arraycopy(o, 0, array, 0, length);
    	return array;
    }
    
    public static <S,T> Map<S,T> putIn(S[] keys,Map<S, T> from){
    	Map<S, T> map=new HashMap<S,T>();
    	Arrays.stream(keys).forEach(key->{
    		T v=from.get(key);
    		if (v!=null) {
    			map.put(key, v);
    		}
    	});
    	return map;
    }
}
