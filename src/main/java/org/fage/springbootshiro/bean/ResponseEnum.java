package org.fage.springbootshiro.bean;

/**
 * @author Caizhf
 * @version 1.0
 * @date 上午9:35 2019/3/20
 * @description 响应状态枚举，如果需要增加相应的状态群请标明注释
 **/
public enum ResponseEnum {

    /**
     * 0000代表业务处理成功
     */
    SUCCESS("0000", "处理成功"),

    //----------------业务处理失败：0001-0999代码属于系统处理成功，但业务处理失败——BusinessException----------------（success=true）//
    /**
     *
     */
    BUSI_FAILED_USER_LOGIN("0001", "登录失败，用户名或密码不正确"),
    BUSI_FAILED_USER_AUTH("0002", "登录失败，用户认证失败"),
    BUSI_FAILED_USER_UNKNOWN("0999", "业务处理失败，原因未知"),

    //----------------系统处理失败：1001-1999错误代码产生的错误代码属于正常容错，不需要记录异常堆栈，但需要记录错误信息——ProcessException（success=false）----------------//
    /**
     * 1000-1999编码群：代表客户端发送的请求错误，对请求的数据校验如（参数验证、查询验证、插入验证等）失败
     */
    FAILED_PARAM("1001", "参数校验不通过"),
    FAILED_EMPTY_DATA("1002", "未查询到相关数据"),
    FAILED_DATA_EXIST("1003", "已存在相关数据"),
    FAILED_REQUEST_REPEAT("1004", "客户端请求重复"),
    FAILED_REQUEST_METHOD_NOT_SUPPORTED("1005", "不支持的请求类型"),


    //----------------系统处理失败：8001-9999错误代码产生错误代属于严重错误，需要记录异常堆栈，需要记录错误信息——ProcessException（success=false）----------------//
    /**
     * 8000-8999编码群：代表本服务内部无错误，但是本服务访问其他第三方服务出现失败
     */
    FAILED_SERVICE_INVOKE("8001", "请求远程服务失败"),
    FAILED_SERVICE_NOT_RESPONSE("8002", "请求远程服务超时"),
    FAILED_SERVICE_DEAL("8003", "远程服务业务处理失败"),
    FAILED_SERVICE_NULL_RESPONSE("8004", "远程服务返回报文为空"),
    FAILED_SERVICE_ERROR_RESPONSE("8005", "远程服务返回报文格式不正确"),
    FAILED_SERVICE_PARAM_VALIDATED_FAILED("8006", "调用远程服务返回参数校验失败"),
    FAILED_SERVICE_UNKNOWN("8999", "调用远程服务出错，原因未知"),

    /**
     * 9000-9999编码群：代表系统内部出错
     */
    ERROR_DB_QUERY("9001", "数据查询失败"),
    ERROR_DB_INSERT("9002", "数据插入失败"),
    ERROR_DB_UPDATE("9003", "数据更新失败"),
    ERROR_DB_DELETE("9003", "数据删除失败"),
    ERROR_NOSUCH_ELEM("9004", "没有对应的必须存在的对象"),
    ERROR_MONGO_UPLOAD("9005","文件上传mongo失败"),
    ERROR_MONGO_DOWNLOAD("9006","mongo下载文件失败"),
    ERROR_UNKNOWN("9999", "未知错误")

    ;
    private String code;
    private String message;

    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 匹配编码所对应的提示信息
     * @param code
     * @return
     */
    public static String codeMessage(String code){
        for(ResponseEnum resEnum : ResponseEnum.values()){
            if(resEnum.getCode().equals(code)){
                return resEnum.getMessage();
            }
        }
        return ResponseEnum.ERROR_UNKNOWN.getMessage();
    }

    /**
     * 匹配编码所对应的枚举
     * @param code
     * @return
     */
    public static ResponseEnum codeEnum(String code){
        for(ResponseEnum resEnum : ResponseEnum.values()){
            if(resEnum.getCode().equals(code)){
                return resEnum;
            }
        }
        return ResponseEnum.ERROR_UNKNOWN;
    }

    /**
     * 匹配外部编码
     * @param outerCode
     * @return
     */
    public static String codeMatch(String outerCode){
        for(ResponseEnum resEnum : ResponseEnum.values()){
            if(resEnum.getCode().equals(outerCode)){
                return resEnum.getCode();
            }
        }
        return ResponseEnum.ERROR_UNKNOWN.getCode();
    }
}
