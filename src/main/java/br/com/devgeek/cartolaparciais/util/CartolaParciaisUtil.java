package br.com.devgeek.cartolaparciais.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * Created by geovannefduarte
 */
public class CartolaParciaisUtil {

    public static boolean mapsAreEqual(Map<String, Object> one, Map<String, Object> two){

        try {
            for (String k : two.keySet()){
                if (!one.get(k).equals(two.get(k))){
                    return false;
                }
            }
            for (String y : one.keySet()){
                if (!two.containsKey(y)){
                    return false;
                }
            }
        } catch (NullPointerException e){
            return false;
        }
        return true;
    }

    public static boolean equalLists(ArrayList<Map<String, Object>> one, ArrayList<Map<String, Object>> two){

        if (one == null && two == null){
            return true;
        }

        if ((one == null && two != null) || (one != null && two == null) || (one.size() != two.size())){
            return false;
        }

        //to avoid messing the order of the lists we will use a copy
        //as noted in comments by A. R. S.
        one = new ArrayList<Map<String, Object>>(one);
        two = new ArrayList<Map<String, Object>>(two);

        return CollectionUtils.isEqualCollection(one, two);
    }
}