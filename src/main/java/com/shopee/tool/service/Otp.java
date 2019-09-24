package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import com.shopee.tool.utils.Utils;
import domain.shopee.request.OtpRequest;
import domain.shopee.request.OtpShopeeRequest;
import domain.shopee.response.OtpResponse;
import domain.shopee.response.OtpShopeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shopee.tool.constants.Constants.*;

public class Otp {
    private final Logger logger = LoggerFactory.getLogger(Otp.class);

    private String getCookie(String csrftoken) throws URISyntaxException {
        String cookie = "csrftoken="+csrftoken;
        RestTemplate restTemplate = new RestTemplate();

        URI uri = new URI(URL_SHOPEE_LOGIN);

        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_AGENT_KEY, USER_AGENT_VALUE_MOBILE);
        headers.add(HttpHeaders.COOKIE, cookie);

        HttpEntity<Map> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        CookieModify cookieModify = new CookieModify();
        List array = result.getHeaders().get(SET_COOKIE);
        return cookieModify.convertCookieToString(array);
    }

    private Map<String, Object> loginPost(OtpShopeeRequest request) throws UnsupportedEncodingException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = new URI(URL_SHOPEE_LOGIN_POST);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, USER_AGENT_VALUE_MOBILE);
        headers.add(HttpHeaders.COOKIE, request.getCookie());
        headers.add(HttpHeaders.ORIGIN,URL_SHOPEE_HOME);
        headers.add(X_CSRFTOKEN, request.getCsrftoken());
        headers.add(HttpHeaders.REFERER, URL_SHOPEE_HOME);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(LOGIN_BODY_LOGIN_KEY, URLEncoder.encode(request.getPhone(), StandardCharsets.UTF_8.toString()));
        body.add(LOGIN_BODY_LOGIN_TYPE, URLEncoder.encode(LOGIN_BODY_LOGIN_TYPE_VALUE, StandardCharsets.UTF_8.toString()));
        body.add(LOGIN_BODY_PASSWORD, URLEncoder.encode(request.getPassword(), StandardCharsets.UTF_8.toString()));
        body.add(LOGIN_BODY_CSRFMIDDLEWARETOKEN, URLEncoder.encode(request.getCsrftoken(), StandardCharsets.UTF_8.toString()));
        if(request.getCaptcha() != null && !request.getCaptcha().isEmpty()){
            body.add(LOGIN_BODY_CAPTCHA, URLEncoder.encode(request.getCaptcha(), StandardCharsets.UTF_8.toString()));
        }else{
            body.add(LOGIN_BODY_CAPTCHA, "");
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(body, headers);

        ResponseEntity<OtpShopeeResponse> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, OtpShopeeResponse.class);
        Map<String,Object> res = new HashMap<>();
        res.put("cookie", result.getHeaders().get("set-cookie").toString());
        res.put("response", result.getBody());
        return  res;
    }
    public OtpResponse getOtp(OtpRequest request) throws URISyntaxException, UnsupportedEncodingException {
        String csrftoken = CookieModify.makeCsrftoken();
        String passwordHash = Utils.SHA256(Utils.MD5(request.getPassword()));
        String phone = request.getPhone().charAt(0) == 0 ? request.getPhone().replace("0","84") : request.getPhone();
        String cookie = getCookie(csrftoken) +";csrftoken=" + csrftoken;

        OtpShopeeRequest otpShopeeRequest = new OtpShopeeRequest();
        otpShopeeRequest.setCsrftoken(csrftoken);
        otpShopeeRequest.setPassword(passwordHash);
        otpShopeeRequest.setPhone(phone);
        otpShopeeRequest.setCookie(cookie);
        Map res = loginPost(otpShopeeRequest);
        OtpShopeeResponse otpShopeeResponse = (OtpShopeeResponse) res.get("response");
        Integer count = 0;
        while (MSG_ERROR_OTP_REQUIRED_CAPTCHA.equals(otpShopeeResponse.getError_msg()) && count < 3){
            count+=1;
        }
        OtpResponse response = new OtpResponse();
        String code = otpShopeeResponse.getError_msg().equals("error_need_otp") ? "0" : "1";
        response.setErrorCode(code);
        response.setMessage(otpShopeeResponse.getError_msg());
        response.setCookie(cookie);
        logger.info("Otp shopee response: "+response.toString());
        return response;

    }

    public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {
        Otp otp = new Otp();
        otp.getOtp(new OtpRequest("84369264578","Phiphuc1994@"));
    }
}
