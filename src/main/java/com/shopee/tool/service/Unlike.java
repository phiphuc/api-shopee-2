package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import domain.shopee.request.UnlikeRequest;
import domain.shopee.response.UnlikeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.shopee.tool.constants.Constants.URL_SHOPEE_HOME;
import static com.shopee.tool.constants.Constants.USER_AGENT_VALUE_MOBILE;
import static com.shopee.tool.constants.Constants.X_CSRFTOKEN;

public class Unlike {
    private final Logger logger = LoggerFactory.getLogger(Unlike.class);

    public UnlikeResponse unlike(UnlikeRequest request){
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
        String url = "https://shopee.vn/buyer/unlike/shop/" + request.getShopId() + "/item/" + request.getItemId();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        logger.info(response.getBody().toString());

        UnlikeResponse unlikeResponse = new UnlikeResponse();
        unlikeResponse.setErrorCode("0");
        unlikeResponse.setMessage(response.getBody().toString());

        return unlikeResponse;
    }
}
