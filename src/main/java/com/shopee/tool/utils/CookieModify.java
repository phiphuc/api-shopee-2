package com.shopee.tool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CookieModify {

    private final static String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    final String regex = "csrftoken=([a-zA-Z0-9]*)";

    public static String makeCsrftoken() {
        String result = "";
        for (int i = 0; i < 32; i++) {
            result += character.charAt(ThreadLocalRandom.current().nextInt(1, 62));
        }
        //logger.info("csrftoken = "+result);
        return result;
    }

    public String convertCookieToString(List<String> cookieArr) {
        ArrayList cookieTemp = (ArrayList) cookieArr.stream().map(item -> item.split(";")[0]).collect(Collectors.toList());
        return cookieTemp.toString().replaceAll(",", ";").replace("[", "").replace("]", "");
    }

    public String getCsrktokenFromCookie(String cookie) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(cookie);
        String result = "";
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result = matcher.group(i);
            }
        }

        return result;

    }
}
