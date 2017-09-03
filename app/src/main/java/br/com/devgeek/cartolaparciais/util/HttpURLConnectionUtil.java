package br.com.devgeek.cartolaparciais.util;

import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import br.com.devgeek.cartolaparciais.api.model.ApiReturn;

/**
 * Created by geovannefduarte
 */

public class HttpURLConnectionUtil {

    private static final int TIMEOUT = 10000;

    public static ApiReturn sendRequestGET(String requestURL) throws IOException {

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        connection.setUseCaches(false);
        connection.setDoInput(true);

        connection.setRequestProperty("Accept","*");

        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13){
            connection.setRequestProperty("Connection", "close");
        }

        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }

        StringBuffer response = new StringBuffer();
        int status = connection.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK){

            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null){ response.append(line); }
            reader.close();

        } else {

            Scanner scanner = new Scanner(connection.getErrorStream());
            while(scanner.hasNext()){ response.append(scanner.next()); }
            scanner.close();
        }

        connection.disconnect();

        return new ApiReturn(status, response.toString());

    }

    public static ApiReturn sendRequestPOST(String requestURL, String jsonData) throws IOException {

        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");

        connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        connection.setUseCaches(false);
        connection.setDoInput(true);    // true indicates the server returns response
        connection.setDoOutput(true);   // true indicates POST request

        connection.setRequestProperty("Accept","*");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.connect();

        // sends POST data
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(jsonData);
        writer.flush();
        writer.close();

        StringBuffer response = new StringBuffer();
        int status = connection.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK){

            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null){ response.append(line); }
            reader.close();

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