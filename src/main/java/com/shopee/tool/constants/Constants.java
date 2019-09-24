package com.shopee.tool.constants;

public class Constants {
    public final static String URL_SHOPEE_LOGIN = "https://shopee.vn/buyer/login/?next=https%3A%2F%2Fshopee.vn%2F%3F__hybrid__%3D1&__classic__=1";
    public final static String URL_SHOPEE_LOGIN_POST = "https://shopee.vn/api/v0/buyer/login/login_post/";
    public final static String URL_SHOPEE_HOME = "https://shopee.vn";
    public final static String URL_SHOPEE_VCODE = "https://shopee.vn/buyer/api/vcode_login/";
    public final static String URL_SHOPEE_FOLLOW = "https://shopee.vn/buyer/follow/shop/";
    public final static String URL_SHOPEE_UNFOLLOW = "https://shopee.vn/buyer/unfollow/shop/";
    public final static String URL_SHOPEE_GET_IDS = "https://shopee.vn/api/v2/shop/get?username=";
    public final static String URL_SHOPEE_GET_INFOR = "https://shopee.vn/api/v1/account_info/?need_cart=1&skip_address=1";
    public final static String URL_SHOPEE_VIEW = "https://shopee.vn/__t__";
    public final static String USER_AGENT_KEY = "user-agent";
    public final static String USER_AGENT_VALUE_MOBILE = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1t";
    public final static String USER_AGENT_VALUE_WEB = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
    public final static String SET_COOKIE = "Set-Cookie";
    public final static String X_CSRFTOKEN = "x-csrftoken";
    public final static String LOGIN_BODY_LOGIN_KEY = "login_key";
    public final static String LOGIN_BODY_LOGIN_TYPE = "login_type";
    public final static String LOGIN_BODY_LOGIN_TYPE_VALUE = "phone";
    public final static String LOGIN_BODY_PASSWORD = "password_hash";
    public final static String LOGIN_BODY_CAPTCHA = "captcha";
    public final static String LOGIN_BODY_CSRFMIDDLEWARETOKEN = "csrfmiddlewaretoken";
    public final static String LOGIN_BODY_VCODE = "vcode";
    public final static String MSG_ERROR_OTP_REQUIRED_CAPTCHA = "error_require_captcha";

}
