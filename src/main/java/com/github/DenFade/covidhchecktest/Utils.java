package com.github.DenFade.covidhchecktest;

import java.util.Map;

public class Utils {

    private Utils(){

    }

    public static String cookieParser(Map<String, String> map){
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(";").append(k).append("=").append(v));
        return sb.substring(1);
    }

    public static String bodyParser(Map<String, String> map){
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
        return sb.substring(1);
    }
}
