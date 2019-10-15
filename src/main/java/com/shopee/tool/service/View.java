package com.shopee.tool.service;

import com.shopee.tool.utils.CookieModify;
import domain.shopee.request.ViewRequest;
import domain.shopee.response.ViewResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shopee.tool.constants.Constants.*;

public class View {

    private final Logger logger = LoggerFactory.getLogger(View.class);

    public ViewResponse view(ViewRequest request) throws URISyntaxException {
        List<String> cookieArr = getCookie();
        String spc_t_iv = "";
        String spc_t_id = "";
        for (String item : cookieArr) {
            if (item.contains("SPC_T_IV")) {
                Pattern pattern = Pattern.compile("SPC_T_IV=([^;]*);", Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(item);
                while (matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        spc_t_iv = matcher.group(i);
                    }
                }
            }
            if (item.contains("SPC_T_ID")) {
                Pattern pattern = Pattern.compile("SPC_T_ID=([^;]*);", Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(item);
                while (matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        spc_t_id = matcher.group(i);
                    }
                }
            }
        }

        CookieModify cookieModify = new CookieModify();
        String cookie = cookieModify.convertCookieToString(cookieArr);
        long date = System.currentTimeMillis();
        String body = "[null,null,"+spc_t_id+",null,null,5,3,[[null,"+request.getItemId()+","+request.getShopId()+",null,false,false,null,null,null,null,[],null,null,null,null,null,null,null,null,],],"+date+", \"VN\", "+spc_t_iv+", null,[],]";
        URI uri = new URI(URL_SHOPEE_VIEW);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_AGENT_KEY, USER_AGENT_VALUE_WEB);
        headers.add(HttpHeaders.COOKIE, cookie);
        headers.add(HttpHeaders.REFERER, URL_SHOPEE_HOME);

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, requestEntity, String.class);

        logger.info(result.getBody());

        ViewResponse response = new ViewResponse();
        response.setErrorCode("0");
        response.setMessage(result.getBody());

        return  response;

    }

    private List getCookie() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = new URI(URL_SHOPEE_LOGIN);

        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_AGENT_KEY, USER_AGENT_VALUE_MOBILE);

        HttpEntity<Map> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        List array = result.getHeaders().get(SET_COOKIE);
        return array;
    }


}
