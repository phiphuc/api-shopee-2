package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import domain.shopee.request.FollowRequest;
import domain.shopee.response.FollowResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.shopee.tool.constants.Constants.*;

public class Follow {
    private final Logger logger = LoggerFactory.getLogger(Like.class);

    public FollowResponse follow(FollowRequest request){
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
        ResponseEntity<Map> response = restTemplate.postForEntity(URL_SHOPEE_FOLLOW+request.getShopId(), entity, Map.class);
        logger.info(response.getBody().toString());
        FollowResponse res = new FollowResponse();
        String errorCode = response.getBody().toString().equals("success=true") ? "0" : "1";
        res.setErrorCode(errorCode);
        res.setMessage(res.getMessage());

        return res;

    }

    public static void main(String[] args) {
        Follow follow = new Follow();
        follow.follow(new FollowRequest("SPC_IA=-1; SPC_EC=\"xYaLh1KyzRVrLQxgKw7IijFXulse+GOLhvyxuCpJmM3zo+sb+kwmy4Kzmm42NSOS9tFCOIccDJngsv330NYfnALM3kgXwwMfkdMRkDXB+AZ6v1uLD9zS0/ZiG/rjhJwTBZuQZig8dtVjyBH7zfqIo7POkgpWQ10CoKlJlbWMsy0=\"; SPC_T_ID=\"V9BcagbFKqR3s0vnVGEWj/UErtOP3m0F9KA3f8cvn4OR/n68RKIWRs4Ey9h7Z/6cJVQg3Vb5m0U1SPNK1iWEdtrdrLLHDW3cFpzCxja2dv0=\"; SPC_SI=hwnc8zoivrpkatvpmhq2hevx7cb6fccs; SPC_U=110270449; SPC_T_IV=\"K/MP6XI5jmRYeSd/rEjwDw==\";csrftoken=FHTtimJaZnh2w73Zzx6RUtgFg8ODriti","6145207"));
    }
}
