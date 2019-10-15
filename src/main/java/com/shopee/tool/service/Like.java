package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import domain.shopee.request.LikeRequest;
import domain.shopee.response.LikeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.shopee.tool.constants.Constants.*;

public class Like {
    private final Logger logger = LoggerFactory.getLogger(Like.class);

    public LikeResponse like(LikeRequest request){
        CookieModify cookieModify = new CookieModify();
        String csrftoken = cookieModify.getCsrktokenFromCookie(request.getCookie());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.USER_AGENT,USER_AGENT_VALUE_MOBILE);
        header.set(HttpHeaders.REFERER, URL_SHOPEE_HOME);
        header.set(X_CSRFTOKEN, csrftoken);
        header.set(HttpHeaders.COOKIE, request.getCookie());

        HttpEntity entity = new HttpEntity("", header);
        String url = "https://shopee.vn/buyer/like/shop/" + request.getShopId() + "/item/" + request.getItemId();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        logger.info(response.getBody().toString());

        LikeResponse likeResponse = new LikeResponse();
        String code = response.getBody().toString().equals("{success=1}") ? "0" : "1";
        likeResponse.setErrorCode(code);
        likeResponse.setMessage(response.getBody().toString());

        return likeResponse;
    }


}
