package com.mifuns.system.facade.enums;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/6/16 </p>
 * <p>Time: 18:26 </p>
 * <p>Version: 1.0 </p>
 */
public enum UserStatus {

    /** 0 锁定 **/
    LOCKED(0),
    /** 1 解锁 **/
    UNLOCKED(1);

    public int STATUS;
    UserStatus(int i) {
        this.STATUS = i;
    }
}
