package com.example.alejandrotorresruiz.taller.ServiceAPP;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by alejandrotorresruiz on 01/12/2018.
 */

public class ServiceUsuarios {

    /**
     * Método para recoger los datos de un formulario y enviarselo al SW.
     */
    public static String formPostRequest(String web,Map<String, String> params){

        String content = null;

        byte[] postDataBytes = new byte[0];

        try {
            String postData = getQuery(params);
            postDataBytes = postData.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            URL url= new URL(web);
            HttpURLConnection http=(HttpURLConnection) url.openConnection();
            http.setReadTimeout(15000);
            http.setConnectTimeout(15000);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Accept","application/json");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setRequestProperty("charset", "UTF-8");
            http.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));
            OutputStream out = http.getOutputStream();
            out.write(postDataBytes);
            out.flush();
            out.close();
            http.connect();
            /*Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario","pepe")
                    .appendQueryParameter("clave", "1234");
            String query = builder.build().getEncodedQuery();

            OutputStream os = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();*/

            /* Recperamos el JSON que devuelve el servidor */
            if(http.getResponseCode()==HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
                br.close();
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        return content;
    }


    /**
     * Método para recuperar datos del WS.
     */
    public static String GetRequest(String web){

        String content = null;

        try {
            URL url= new URL(web);
            HttpURLConnection http=(HttpURLConnection) url.openConnection();
            http.setReadTimeout(15000);
            http.setConnectTimeout(15000);
            http.setDoInput(true);
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept","application/json");
            if(http.getResponseCode()==HttpURLConnection.HTTP_OK){
                StringBuilder sb=new StringBuilder();
                BufferedReader br=new BufferedReader(new InputStreamReader(http.getInputStream()));
                String line;
                while((line=br.readLine())!=null){
                    sb.append(line);
                }
                content=sb.toString();
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return content;
    }




    private static String getQuery(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean haveData = params != null && params.size() > 0;
        if (haveData) {
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()){
                String value = entry.getValue();
                if (value != null) {
                    if (first) {
                        first = false;
                    } else {
                        result.append("&");
                    }
                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(value, "UTF-8"));
                }
            }
        }
        return result.toString();
    }
}
