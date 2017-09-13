package zhaoq_qiang.xunfeidemo.Http;

import java.io.IOException;

/**
 * Created by wangyu892449346 on 4/8/17.
 */

public class getData extends getNetWork {

    public String getResult(String content) {
        try {
            return doGet(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
