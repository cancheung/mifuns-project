package com.mifuns.common.util;

import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/4/27 </p>
 * <p>Time: 12:59 </p>
 * <p>Version: 1.0 </p>
 */
public abstract class CollectionUtil {


    /**
     * Default delimiter ','
     * @param str
     * @return
     */
    public static Iterable<String> split(String str){
        return split(str, ",");
    }
    public static Iterable<String> split(String str,String delimiter){
        return Splitter.on(delimiter).trimResults().omitEmptyStrings().split(str);
    }
    public static List<Long> stringConvertLong(String[] arry){
        List<Long> result = new ArrayList<Long>();
        for (String a : arry){
            result.add(Long.parseLong(a));
        }
        return result;
    }
    public static List<Long> arrayStringConvertList(Iterable<String> it) {
        List<Long> result = new ArrayList<Long>();
        for (String a : it){
            result.add(Long.parseLong(a));
        }
        return result;
    }
    public static String arrayIntConvertStr(Iterable<Integer> ids,String delimiter){
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        for(Integer id : ids) {
            if(first){
                buffer.append(id);
                first = false;
            }else{
                buffer.append(delimiter).append(id);
            }
        }
        return buffer.toString();
    }

    /**
     * 默认逗号分隔
     * @param ids
     * @return
     */
    public static String arrayIntConvertStr(Iterable<Integer> ids){
        return arrayIntConvertStr(ids,",");
    }
    public static String arrayLongConvertStr(Long[] ids, String delimiter){
        return arrayLongConvertStr(Arrays.asList(ids),delimiter);
    }
    /**
     * 默认逗号分隔
     * @param ids
     * @return
     */
    public static String arrayLongConvertStr(Long... ids){
        return arrayLongConvertStr(Arrays.asList(ids), ",");
    }

    public static String arrayLongConvertStr(Iterable<Long> ids,String delimiter){
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        for(Long id : ids) {
            if(first){
                buffer.append(id);
                first = false;
            }else{
                buffer.append(delimiter).append(id);
            }
        }
        return buffer.toString();
    }
    /**
     * 默认逗号分隔
     * @param ids
     * @return
     */
    public static String arrayLongConvertStr(Iterable<Long> ids){
        return arrayLongConvertStr(ids,",");
    }

    public static String arrayStringConvertStr(Iterable<String> ids,String delimiter){
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        for(String id : ids) {
            if(first){
                buffer.append(id);
                first = false;
            }else{
                buffer.append(delimiter).append(id);
            }
        }
        return buffer.toString();
    }
    /**
     * 默认逗号分隔
     * @param ids
     * @return
     */
    public static String arrayStringConvertStr(Iterable<String> ids){
        return arrayStringConvertStr(ids,",");
    }

    /**
     * 星号替换为下滑线
     * @param src
     * @return
     */
    public static String replaceComma(String src){
        if(StringUtils.isEmpty(src)){
            return src;
        }
        return src.replaceAll(",","_");
    }

    /**
     * 将集合内数字相加
     * @param values
     * @return
     */
    public static int addValues(Collection<Integer> values){
        int result = 0;
        for (Integer val : values){
            result += val;
        }
        return result;
    }
}
