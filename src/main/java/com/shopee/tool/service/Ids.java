package com.shopee.tool.service;
import domain.shopee.response.GetIdsByUsernameShopeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import static com.shopee.tool.constants.Constants.*;

public class Ids {
    private final Logger logger = LoggerFactory.getLogger(Like.class);
    public GetIdsByUsernameShopeeResponse getIdsByUsername(String username){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.USER_AGENT,USER_AGENT_VALUE_MOBILE);
        header.set(HttpHeaders.REFERER, URL_SHOPEE_HOME);

        HttpEntity entity = new HttpEntity(header);
        ResponseEntity<GetIdsByUsernameShopeeResponse> response = restTemplate.exchange(URL_SHOPEE_GET_IDS+username,  HttpMethod.GET,entity, GetIdsByUsernameShopeeResponse.class);
        logger.info(response.getBody().toString());
        return response.getBody();
    }

    public static void main(String[] args) {
        Ids ids = new Ids();
        ids.getIdsByUsername("thaylamua");
    }
}
