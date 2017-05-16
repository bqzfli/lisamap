package srs.Utility;

import java.util.ArrayList;
import java.util.List;

public class ListOperate {
	public static List<Integer> ListFilter(List<Integer> index, int lBound, int rBound){
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < index.size(); i++){
            if (index.get(i) >= lBound && index.get(i) <= rBound){
                result.add(index.get(i));
            }
        }
        return result;
    }
}
