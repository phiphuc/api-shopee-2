package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import domain.shopee.response.InformationShopeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import static com.shopee.tool.constants.Constants.*;

public class InformationShop {
    private final Logger logger = LoggerFactory.getLogger(Like.class);

    public InformationShopeeResponse getInformationWithCookie(String cookie){
        CookieModify cookieModify = new CookieModify();
        String csrftoken = cookieModify.getCsrktokenFromCookie(cookie);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.USER_AGENT,USER_AGENT_VALUE_MOBILE);
        header.set(HttpHeaders.REFERER, URL_SHOPEE_HOME);
        header.set(X_CSRFTOKEN, csrftoken);
        header.set(HttpHeaders.COOKIE,cookie);

        HttpEntity entity = new HttpEntity(header);
        ResponseEntity<InformationShopeeResponse> response = restTemplate.exchange(URL_SHOPEE_GET_INFOR,  HttpMethod.GET,entity, InformationShopeeResponse.class);
        logger.info(response.getBody().toString());

        return response.getBody();
    }

    public static void main(String[] args) {
        InformationShop info = new InformationShop();
        info.getInformationWithCookie("SPC_IA=-1; SPC_EC=\"wNsVJz1zC8Xjd5fPgkMetD1F/Gu9HUqda1CXoa/A/fSKtdel2qU7pHBfaYt7AY8nvNAci5zBr8ppK5wbhnxZ9OpurWi4njSIt2znYBPsfcbaYEUTAZZ0WtDZp2U7koWDqwrkYquWiIhjc7mIOhhiYDN3L2gHQ92IKz/zY6uWJJ4=\"; SPC_T_ID=\"0DaoYOnU6S3ExKQlyuNZKHsti3fqUBKX2BxnxJFkAtGZkT56QDp+RXCqz/coM2hFcwh7sLxvXe8A52IZfzZqfoLjF6NntVtqAaVVkg7rMiU=\"; SPC_SI=higtkwn8mcd1qwvs8s1zrznuzlf0pvuj; SPC_U=110270449; SPC_T_IV=\"wNnN1tKpOkihpo8DoKyilg==\";csrftoken=u87zcPwPHPBC4sROxztvc1qML1Fy2vDJ");
    }
}
