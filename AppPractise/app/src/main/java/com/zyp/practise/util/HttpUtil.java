package com.zyp.practise.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2015/12/1.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String requestUrl,final HttpCallBackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(requestUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    if(listener!=null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if(listener!=null){
                        listener.onFinish(e.getMessage());
                    }
                }finally{
                    if(connection!=null){
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

    public interface HttpCallBackListener{
        void onFinish(String response);

        void onError(String error);
    }
}
