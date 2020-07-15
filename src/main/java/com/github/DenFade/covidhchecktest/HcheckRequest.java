package com.github.DenFade.covidhchecktest;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HcheckRequest {

    private final HcheckClient client;

    private String body;

    private static final String baseURL = "https://eduro.%s.go.kr";
    private static final String initURL = baseURL + "/hcheck/index.jsp";
    private static final String authURL = baseURL + "/stv_cvd_co00_012.do";
    private static final String finalURL = baseURL + "/stv_cvd_co01_000.do";

    public HcheckRequest(HcheckClient client){
        this.client = client;
    }

    public String getInitURL(){
        return String.format(initURL, client.getLocalEdu());
    }

    public String getAuthURL() {
        return String.format(authURL, client.getLocalEdu());
    }

    public String getFinalURL(){
        return String.format(finalURL, client.getLocalEdu());
    }

    public HcheckRequest enter(){
        Connection con = Jsoup.connect(getInitURL())
                                .method(Connection.Method.GET);
        try {
            Connection.Response res = con.execute();
            client.setCookie("Cookie", Utils.cookieParser(res.cookies()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HcheckRequest authorize(){
        Map<String, String> params = new HashMap<>();
        params.put("qstnCrtfcNoEncpt", "");
        params.put("rtnRsltCode", "");
        params.put("schulCode", client.getSchoolCode());
        params.put("schulNm", client.getSchoolName());
        params.put("pName", client.getMyName());
        params.put("frnoRidno", client.getBirthday());
        params.put("aditCrtfcNo", "");

        Connection con = Jsoup.connect(getAuthURL())
                .ignoreContentType(true)
                .header("User-Agent", HcheckClient.userAgent)
                .header("X-Requested-With", HcheckClient.xReqWith)
                .header("Referer", client.getReferer())
                .header("Accept-Language", HcheckClient.acceptLang)
                .header("Cookie", client.getCookie("Cookie"))
                    .requestBody(Utils.bodyParser(params))
                    .method(Connection.Method.POST);
        try{
            Connection.Response res = con.execute();
            body = res.body();
        } catch (IOException e){
            e.printStackTrace();
        }
        return this;
    }

    public void submit(){
        JSONObject json = new JSONObject(body);
        Map<String, String> params = new HashMap<>();
        String no_encpt = json.getJSONObject("resultSVO").getJSONObject("data").getString("qstnCrtfcNoEncpt");
        System.out.println(no_encpt);
        String result_code = json.getJSONObject("resultSVO").getJSONObject("data").getString("rtnRsltCode");
        System.out.println(result_code);
        params.put("qstnCrtfcNoEncpt", no_encpt);
        params.put("rtnRsltCode", result_code);
        params.put("schulNm", "");
        params.put("stdntName", "");
        params.put("rspns01", "1");
        params.put("rspns02", "1");
        params.put("rspns07", "0");
        params.put("rspns08", "0");
        params.put("rspns09", "0");

        Connection con = Jsoup.connect(getFinalURL())
                .ignoreContentType(true)
                .header("User-Agent", HcheckClient.userAgent)
                .header("X-Requested-With", HcheckClient.xReqWith)
                .header("Referer", client.getReferer())
                .header("Accept-Language", HcheckClient.acceptLang)
                .header("Cookie", client.getCookie("Cookie"))
                .requestBody(Utils.bodyParser(params))
                .method(Connection.Method.POST);
        try{
            Connection.Response res = con.execute();
            System.out.println(res.body());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
