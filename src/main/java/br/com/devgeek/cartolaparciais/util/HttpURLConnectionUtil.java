package br.com.devgeek.cartolaparciais.util;

import br.com.devgeek.cartolaparciais.controller.dto.ApiReturn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by geovannefduarte
 */
public class HttpURLConnectionUtil {

    private static final int TIMEOUT = 10000;

    public static String sendGetRequest(String requestURL) throws IOException {

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        connection.setUseCaches(false);
        connection.setDoInput(true);
//        connection.setUseCaches(false);
//        connection.setDoOutput(true);
//        connection.setDoInput(true);

        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept","*");

        StringBuffer response = new StringBuffer();
        int status = connection.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK){

            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null){
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return response.toString();

        } else {

            connection.disconnect();
            throw new IOException("ERRO -> "+status);
        }
    }

    public static ApiReturn sendRequestGET(String requestURL) throws IOException {

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//        connection.setRequestProperty("If-Modified-Since", "Thu, 20 Apr 2017 11:11:43 GMT");
//        System.out.println("HeaderFields -> "+connection.getHeaderFields());
//        HeaderFields -> {X-Frame-Options=[deny], Keep-Alive=[timeout=10], null=[HTTP/1.1 200 OK], Cache-Control=[max-age=60], Access-Control-Allow-Origin=[https://cartolafc.globo.com], X-Content-Type-Options=[nosniff], Connection=[keep-alive], Vary=[Accept-Encoding, Accept-Encoding], X-XSS-Protection=[1; mode=block], Content-Length=[440], Date=[Thu, 20 Apr 2017 11:11:43 GMT], Content-Type=[application/json;charset=UTF-8]}


        connection.setRequestMethod("GET");

        connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        connection.setUseCaches(false);
        connection.setDoInput(true);

        connection.setRequestProperty("Accept","*");

        StringBuffer response = new StringBuffer();
        int status = connection.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK){

            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null){ response.append(line); }
            reader.close();

        } else if (status == HttpURLConnection.HTTP_NO_CONTENT){

            // Do nothing - No content =)

        } else {

            Scanner scanner = new Scanner(connection.getErrorStream());
            while(scanner.hasNext()){ response.append(scanner.next()); }
            scanner.close();
        }

        connection.disconnect();

        return new ApiReturn(status, response.toString());

    }
}

//	https://android.googlesource.com/platform/tools/tradefederation/+/master/src/com/android/tradefed/util/net/HttpHelper.java