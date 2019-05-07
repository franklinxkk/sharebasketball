package com.bblanqiu.common.exception;

/**
 * 
 * @author xkk
 *
 */
public enum ErrorCode {

    //System Error
    SYSTEM_ERROR(10001, "系统内部错误,请联系客服人员"),//System error
    SERVICE_UNAVAILABLE(10002, "服务不可达，请检查网络"),//Service unavailable
    
    MISS_REQUIRED_PARAMETER(10103, "缺少必填参数(%s),请查看对应的文档说明"),//Miss requred parameter (%s),see doc for more info
    ILLEGAL_REQUEST(10104,"请求不合法"),//Illegal request
    PARAMETER_VALUE_INVALID(10105, "请求参数(%s)的值验证错误,请查看对应的文档说明"),//Parameter (%s)'s value invalid, see doc for more info
    PARAMETER_ERROR(10106, "请求参数错误，请查看对应的文档说明"),//Parameter error, see doc for more info
    HTTP_BODY_FORMAT_ERROR(10107, "HTTP请求消息内容的格式错误，请查看对应的文档说明"),//HTTP body format error, see doc for more info
    HTTP_METHOD_SUPORT_ERROR(10108, "HTTP方法不支持此种请求"),//HTTP method is not suported for this request

    //Server Error
    UID_OR_ACCESS_TOKEN_ERROR(20001, "用户标识或者令牌错误"),//Uid or access_token error
    THIRD_TOKEN_UNAUTHORIZED(20002, "第三方令牌未授权"),//Third token unauthorized
    NONSUPPORT_SNS_AUTH_TYPE(20003, "不支持的第三方授权类型"),//Nonsupport sns authorization type
    CLIENT_TYPE_UNAUTHORIZED(20004, "客户端类型未授权"),//Client type unauthorized
    USER_DOSE_NOT_EXISTS(20005, "用户不存在"),//User already exists
    USERNAME_ALREADY_EXISTS(20007, "用户名已存在"),//Username already exists
    PHONE_ALREADY_EXISTS(20009, "手机号(%s)已存在"),
    PHONE_NUMBER_ERROR(20010, "手机号错误"),
    PHOTO_ERROR(20011, "图片文件错误"),
    EMAIL_NUMBER_ERROR(20012, "郵箱错误"),
    USER_NOT_FRIENDS(20051, "非朋友权限"),
    
    DEVICE_DISCONNECTED(20101, "设备未连接服务器"),
    DEVICE_BOUND_ERROR(20102, "设备未绑定,请重新配置!"),
    DEVICE_REBOUND_ERROR(20103, "请重新配置设备联网!"),
    DEVICE_OLREADY_SLEEP(20104,"设备休眠中"),
    DEVICE_CONFIG_OUTOFDATE(20105, "设备配置过低"),
    DEVICE_UNAUTHORIZED(20106, "设备未授权"),
    DEVICE_WAIT_FOR_DATA_CONN(20107, "等待设备数据上传"),
    DEVICE_NOT_ONLINE(20108, "设备正在维护"),
    DEVICE_WIFI_INSTABLE(20109, "Wi-Fi网络不稳定，请稍后重试"),
    DEVICE_DOSE_NOT_EXISTS(20111, "设备（%s）序列号错误"),
    DEVICE_DOSE_NOT_PROVIDE(20112, "设备（%s）尚未提供服务"),
    BALL_DOSE_NOT_PROVIDE(20113, "篮球（%s）尚未初始化"),
    
    RESOURCE_DOSENOT_EXISTS(20211, "资源对象不存在"),
    
    VARIFICATION_CODE_ERROR(20201, "验证码错误"),//Varification code error
    OPERATION_OUT_OF_LIMIT(20202, "请勿频繁操作"),//Operation out of limit
    FAILED_GET_VARIFICATION_CODE(20203, "获取验证码失败"),//Failed get varification code error
    OPERATION_ERROR(20204, "操作错误"),
    SECQA_ERROR(20205,"安全问题验证失败"),
    
    DEVICE_OLREADY_USED(20301,"设备正在使用中"),
    OLREADY_USED(20302,"服务尚未结束"),
    ALLBOX_USED(20304,"所有储物柜都在使用中"),
    OLREADY_BROKEN(20303,"设备正在维护"),
    DEVICE_OPENING(20305,"未关闭储物柜！"),
    DEVICE_OPENING_PASS(20306,"请设置开箱密码！"),
    OLREADY_END(20307,"服务已结束"),
    
    PERMISSION_DENIED(21101, "权限不足"),//Permission denied
    API_DOES_NOT_EXISTS(21102, "接口不存在"),//Permission denied
    CAN_NOT_HANLE_YOURSELF(21103, "无法操作自己"),
    ARREARAGE(21112, "请充值"),//Arrearage
    AUTH_ZM_SCORE_LIMIT(21113,"芝麻信用分过低"),
    AUTH_BB_SCORE_LIMIT(21114,"八拜信用分过低"),
    
    AUTH_FAILED(21301, "鉴权失败"),//Auth failed
    USERNAME_OR_PASSWORD_ERROR(21302, "用户名或密码错误"),//Username or password error
    USERNAME_ADD_PWD_AUTH_OUT_OF_RATE_LIMIT(21303, "登录失败次数限制"),//Username and pwd auth out of rate limit

    USER_LOCKED(21304, "User locked, unlock left (%s)"),
    USER_LOGIN_REQUIRED(21305, "请登录"),//User login requried
    REQUEST_BODY_IS_ILLEGAL(21502, "非法的请求内容"),//Request body is illegal

    //oAuth error
    REDIRECT_URI_MISMATCH(21322, "不匹配的重定向链接"),//Redirect uri mismatch
    INVALID_REQUEST(21323, "非法请求"),//Invalid request
    INVALID_CLIENT(21324, "非法客户端"),//Invalid client
    INVALID_GRANT(21325, "非法授权"),//Invalid grant
    UNAUTHORIZED_CLIENT(21326, "未授权的客户端"),//Unauthorized client
    EXPIRED_TOKEN(21327, "令牌过期"),//Expired token
    UNSUPPORTED_GRANT_TYPE(21328, "不支持的授权类型"),//Unsupported grant type
    UNSUPPORTED_RESPONSE_TYPE(21329, "不支持的响应类型"),//Unsupported response type
    ACCESS_DENIED(21330, "请求拒绝"),//Access denied
    TEMPORARILY_UNAVAILABLE(21331, "Temporarily unavailable"),
    REFRESH_TOKEN_ERROR(21332, "Refresh token (%s) error"),
    REFRESH_TOKEN_EXPIRED(21333, "Refresh token (%s) expired"),
    CODE_ERROR(21334, "Code (%s) error"),
    CODE_EXPIRED(21335, "Code (%s) expired"),
    TOKEN_ERROR(21336, "令牌(%s)错误"),//Token (%s) error
    SERVER_TOKEN_ERROR(21337, "Server token (%s) error"),
    SERVER_TOKEN_EXPIRED(21338, "Server token (%s) expired"),
    AUTH_ERROR(21339,"授权失败(%s)"),
    PAY_ORDER_ERROR(21340,"支付订单失败"),
    ALI_UN_VERIFIED(21341,"未实名认证"),

    HTTP_CONNECTION_TO_REFUSED(21801, "Http connection to (%s) refused"),
    HTTP_RESPONSE_TIMEOUT(21802, "Http response from (%s) timeout"),
    HTTP_INVALID_RESPONSE(21803, "Http invalid response from (%s)"),
    HTTP_RESPONSE_BAD_STATUS_CODE(21804, "Http response bad status code (%s) from (%s)"),

    DATABASE_CONNECTION_ERROR(30002, "数据库连接错误"),//Database connection error
    DATABASE_OPERATION_ERROR(30003, "数据库操作错误");//Database operation error
    
    private int errorCode;
    private String error;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private ErrorCode(int errorCode, String error) {
        this.errorCode = errorCode;
        this.error = error;
    }

}
