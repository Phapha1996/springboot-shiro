package org.fage.springbootshiro.constant;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/7/15 17:33
 * @description
 **/
public enum RoleStatusEnum {
    //正常
    AVAILABLE("0"),
    //锁定
    DISABLE("1");

    private String value;

    RoleStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
