package com.github.DenFade.covidhchecktest;

public class Main {

    public static void main(String[] args){
        HcheckClient client = new HcheckClient("D100001756", "대진고등학교", "신호철", "021206", "dge");
        HcheckRequest request = new HcheckRequest(client);

        request.enter()
                .authorize()
                .submit();
    }

}
