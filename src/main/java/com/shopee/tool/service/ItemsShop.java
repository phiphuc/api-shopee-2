package com.shopee.tool.service;

import domain.shopee.request.ItemShopRequest;
import domain.shopee.response.GetIdsByUsernameShopeeResponse;
import domain.shopee.response.GetItemShopeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

import static com.shopee.tool.constants.Constants.*;

public class ItemsShop {
    private final Logger logger = LoggerFactory.getLogger(Like.class);

    public GetItemShopeeResponse getItemsShop(ItemShopRequest request) {
        String userId = getIdsByUsername(request.getUsername());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.USER_AGENT,USER_AGENT_VALUE_WEB);
        header.set(HttpHeaders.REFERER, URL_SHOPEE_HOME);

        String url = "https://shopee.vn/api/v2/search_items/?by=pop&limit=30&match_id="+userId+"&newest="+request.getPage()+"&order=desc&page_type=shop";

        HttpEntity entity = new HttpEntity(header);
        ResponseEntity<GetItemShopeeResponse> response = restTemplate.exchange(url,  HttpMethod.GET,entity, GetItemShopeeResponse.class);


        logger.info(response.getBody().getItems().toString());
        return response.getBody();
    }

    public String getIdsByUsername(String username){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set(HttpHeaders.USER_AGENT,USER_AGENT_VALUE_MOBILE);
        header.set(HttpHeaders.REFERER, URL_SHOPEE_HOME);

        HttpEntity entity = new HttpEntity(header);
        ResponseEntity<GetIdsByUsernameShopeeResponse> response = restTemplate.exchange(URL_SHOPEE_GET_IDS+username,  HttpMethod.GET,entity, GetIdsByUsernameShopeeResponse.class);
        return  response.getBody().getData().getShopid().toString();
    }

    public static void main(String[] args) throws URISyntaxException {
        ItemsShop item = new ItemsShop();
        item.getItemsShop(new ItemShopRequest());
    }
}
