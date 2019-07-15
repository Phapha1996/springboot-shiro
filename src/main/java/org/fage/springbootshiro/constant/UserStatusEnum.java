package org.fage.springbootshiro.constant;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/7/15 17:12
 * @description
 **/
public enum  UserStatusEnum {
    //正常
    NORMAL("0"),
    //锁定
    LOCKED("1");

    private String value;

    UserStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
