package com.mifuns.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2016/5/6.
 */
public class RandomsUtil {

    /***
     * 生成充值卡所需的随机数
     * @return
     */
    public static String Randoms() {
        int[] intRet = new int[4];
        int intRd = 0; // 存放随机数
        int count = 0; // 记录生成的随机数个数
        int flag = 0; // 是否已经生成过标志
        while (count < 4) {
            Random rdm = new Random(System.currentTimeMillis());
            intRd = Math.abs(rdm.nextInt()) % 32 + 1;

            if (intRd < 10 || intRd > 99) {
                continue;
            }

            for (int i = 0; i < count; i++) {
                if (intRet[i] == intRd) {
                    flag = 1;
                    break;
                } else {
                    flag = 0;
                }
            }
            if (flag == 0) {
                intRet[count] = intRd;
                count++;
            }
        }
        String str = "";
        for (int t = 0; t < 4; t++) {
            str = str + intRet[t];

        }

        return str;
    }


    public static Map<String, Object> GetCardNum() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
        String str = Randoms() + format.format(new Date()) + Randoms() + format1.format(new Date()) + Randoms();

        String md5Str = MD5Util.md5(str);
        String base64Str = BASE64Util.encode(str.getBytes()).toLowerCase();
        map.put("cardNo", md5Str.substring(0, 10) + base64Str.substring(0, 10));
        // map.put("pwd",
        // md5Str.substring(12,18)+Randoms().substring(0,1)+md5Str.substring(20,22)+Randoms().substring(0,1)+Randoms().substring(0,2)+base64Str.substring(27,30)+Randoms().substring(0,2)+md5Str.substring(25,26)+Randoms().substring(0,2));
        map.put("pwd", md5Str.substring(12, 18) + Randoms().substring(0, 1) + md5Str.substring(20, 22) + Randoms().substring(0, 1) + base64Str.substring(27, 29));
        return map;
    }

    /**
     * 生成4位数数字
     * @return
     */
    public static int generateCode(){
        int rand = new Random().nextInt(9000) + 1000;
        return rand;
    }
}
