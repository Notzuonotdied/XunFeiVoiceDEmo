package zhaoq_qiang.xunfeidemo.Http;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import zhaoq_qiang.xunfeidemo.Bean.Constant;

/**
 * Created by wangyu892449346 on 4/8/17.
 */
class getNetWork {

    @Nullable
    String doGet(String content) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            content = URLEncoder.encode(content, "UTF-8");
            if (TextUtils.isEmpty(Constant.newUrl)) {
                url = new URL(Constant.myUrl + content);
                Log.i("Notzuonotdied", "Constant.myUrl + content = " + Constant.myUrl + content);
            } else {
                url = new URL(Constant.newUrl + content);
                Log.i("Notzuonotdied", "Constant.newUrl + content = " + Constant.myUrl + content);
            }
            //url = new URL("http://192.168.2.149:5000/?txt=" + mURL);
            urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod("GET");    //使用的http的get方法
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        if (stringBuffer == null) {
            return null;
        }
        return stringBuffer.toString();
    }
}

