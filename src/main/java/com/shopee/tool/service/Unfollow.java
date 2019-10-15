package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import domain.shopee.request.UnFollowRequest;
import domain.shopee.response.UnfollowResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.shopee.tool.constants.Constants.*;

public class Unfollow {
    private final Logger logger = LoggerFactory.getLogger(Unfollow.class);

    public UnfollowResponse unfollow(UnFollowRequest request){
        CookieModify cookieModify = new CookieModify();
        String csrftoken = cookieModify.getCsrktokenFromCookie(request.getCookie());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.USER_AGENT,USER_AGENT_VALUE_MOBILE);
        header.set(HttpHeaders.REFERER, URL_SHOPEE_HOME);
        header.set(X_CSRFTOKEN, csrftoken);
        header.set(HttpHeaders.COOKIE, request.getCookie());
        Map body = new HashMap();
        HttpEntity entity = new HttpEntity(body, header);
        ResponseEntity<Map> response = restTemplate.postForEntity(URL_SHOPEE_UNFOLLOW+request.getShopId(), entity, Map.class);
        logger.info(response.getBody().toString());

        UnfollowResponse unfollowResponse = new UnfollowResponse();
        String code = response.getBody().toString().equals("{success=1}") ? "0" : "1";
        unfollowResponse.setErrorCode(code);
        unfollowResponse.setMessage(response.getBody().toString());
        return  unfollowResponse;
    }


}
