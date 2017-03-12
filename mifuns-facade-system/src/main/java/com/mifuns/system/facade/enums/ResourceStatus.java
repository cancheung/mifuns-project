package com.mifuns.system.facade.enums;

public enum ResourceStatus {
    /** 1 有效 **/
    AVAILABLE(1),
    /** 0 无效 **/
    UNAVAILABLE(0);

    public int STATUS;
    ResourceStatus(int i) {
        this.STATUS = i;
    }

}
