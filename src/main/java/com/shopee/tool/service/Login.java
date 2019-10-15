package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import domain.shopee.request.LoginRequest;
import domain.shopee.response.LoginResponse;
import domain.shopee.response.LoginShoppeResponse;
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

import static com.shopee.tool.constants.Constants.*;

public class Login {

    private final Logger logger = LoggerFactory.getLogger(Login.class);

    public LoginResponse getLogin(LoginRequest request) throws URISyntaxException, UnsupportedEncodingException {
        CookieModify cookieModify = new CookieModify();
        String csrftoken = cookieModify.getCsrktokenFromCookie(request.getCookie());
        RestTemplate restTemplate = new RestTemplate();

        URI uri = new URI(URL_SHOPEE_VCODE);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, USER_AGENT_VALUE_MOBILE);
        headers.add(HttpHeaders.COOKIE, request.getCookie());
        headers.add(HttpHeaders.REFERER, URL_SHOPEE_HOME);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(LOGIN_BODY_LOGIN_KEY, URLEncoder.encode(request.getPhone(), StandardCharsets.UTF_8.toString()));
        body.add(LOGIN_BODY_LOGIN_TYPE, URLEncoder.encode(LOGIN_BODY_LOGIN_TYPE_VALUE, StandardCharsets.UTF_8.toString()));
        body.add(LOGIN_BODY_CSRFMIDDLEWARETOKEN, URLEncoder.encode(csrftoken, StandardCharsets.UTF_8.toString()));
        body.add(LOGIN_BODY_VCODE, URLEncoder.encode(request.getOtp(), StandardCharsets.UTF_8.toString()));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(body, headers);
        ResponseEntity<LoginShoppeResponse> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, LoginShoppeResponse.class);

        logger.info(result.getBody().toString());

        String cookie = cookieModify.convertCookieToString(result.getHeaders().get(SET_COOKIE)) +";csrftoken=" + csrftoken;

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setError(result.getBody().getError());
        loginResponse.setCookie(cookie);
        loginResponse.setIs_new_user(result.getBody().isIs_new_user());
        loginResponse.setPhone_auto_converted(result.getBody().isPhone_auto_converted());
        loginResponse.setUserid(result.getBody().getUserid());

        logger.info("Login response: "+loginResponse.toString());

        return loginResponse;

    }

}
