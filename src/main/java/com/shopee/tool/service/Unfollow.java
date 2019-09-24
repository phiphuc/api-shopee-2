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

    public static void main(String[] args) {
        Unfollow follow = new Unfollow();
        follow.unfollow(new UnFollowRequest("SPC_IA=-1; SPC_EC=\"wNsVJz1zC8Xjd5fPgkMetD1F/Gu9HUqda1CXoa/A/fSKtdel2qU7pHBfaYt7AY8nvNAci5zBr8ppK5wbhnxZ9OpurWi4njSIt2znYBPsfcbaYEUTAZZ0WtDZp2U7koWDqwrkYquWiIhjc7mIOhhiYDN3L2gHQ92IKz/zY6uWJJ4=\"; SPC_T_ID=\"0DaoYOnU6S3ExKQlyuNZKHsti3fqUBKX2BxnxJFkAtGZkT56QDp+RXCqz/coM2hFcwh7sLxvXe8A52IZfzZqfoLjF6NntVtqAaVVkg7rMiU=\"; SPC_SI=higtkwn8mcd1qwvs8s1zrznuzlf0pvuj; SPC_U=110270449; SPC_T_IV=\"wNnN1tKpOkihpo8DoKyilg==\";csrftoken=u87zcPwPHPBC4sROxztvc1qML1Fy2vDJ","6145207"));
    }
}
