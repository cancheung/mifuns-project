package com.mifuns.common.util;

import java.nio.charset.Charset;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/5/17 </p>
 * <p>Time: 10:21 </p>
 * <p>Version: 1.0 </p>
 */
public abstract class CharsetUtil {

    /**
     * UTF-8: eight-bit UCS Transformation Format.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * US-ASCII: seven-bit ASCII, the Basic Latin block of the Unicode character set (ISO646-US).
     */
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
}
