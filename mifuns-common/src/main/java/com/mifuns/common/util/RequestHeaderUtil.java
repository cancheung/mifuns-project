package com.mifuns.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miguangying on 2017/3/11.
 */
public class RequestHeaderUtil {
    /**
     * 将Request Header 转换为Map
     * @param request
     * @return
     */
    public static Map<String,String> getHeaderMap(HttpServletRequest request){
        Map<String, String> result = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        for (Enumeration enumeration = headerNames; enumeration.hasMoreElements(); ) {
            String name = enumeration.nextElement().toString();
            String value = request.getHeader(name);
            result.put(name, value);
        }
        return result;
    }
}
