package zhaoq_qiang.xunfeidemo.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zhaoq_qiang.xunfeidemo.Adapter.ChatMsgAdapter;
import zhaoq_qiang.xunfeidemo.Bean.Constant;
import zhaoq_qiang.xunfeidemo.Bean.PlayerInfo;
import zhaoq_qiang.xunfeidemo.Http.getData;
import zhaoq_qiang.xunfeidemo.R;

public class MainActivity extends AppCompatActivity {

    Handler chat_handler = new Handler();
    private int mark = 0;
    private List<PlayerInfo> playerInfoList;
    private ChatMsgAdapter adapter;
    private android.widget.ListView chatListView;
    private View touchView;
    private ImageButton voiceBtn;
    private EditText msgEdit;
    private android.widget.Button sendMsg;
    private RelativeLayout relativeLayout;
    private TextView textView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String response = (String) msg.obj;
                    // 这里服务器开始回馈数据
                    PlayerInfo playerInfo = new PlayerInfo();
                    playerInfo.setTextContent(response);
                    playerInfo.setMine(false);
                    playerInfoList.add(playerInfo);
                    adapter.notifyDataSetChanged();//属性里设置自动滚动
                    textView.setText(R.string.app_name);
                    //msgEdit.setText(response);
                    //say(response);
                    break;
                default:
                    break;
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (mark == 0) {
                mark = 1;
                btnVoice();
            }
            //要做的事情
            chat_handler.postDelayed(this, 2000);
        }
    };
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.voice_btn:
                    btnVoice();
                    handler.removeCallbacks(runnable);
                    break;
                case R.id.send_msg:
                    String content = msgEdit.getText().toString();
                    PlayerInfo playerInfo = new PlayerInfo();
                    playerInfo.setTextContent(content);
                    playerInfo.setMine(true);
                    playerInfoList.add(playerInfo);
                    adapter.notifyDataSetChanged();//属性里设置自动滚动
                    new asyncData().execute(content);
                    Log.i("Notzuonotdied", "输入框中的内容： " + content);
                    msgEdit.setText("");
                    break;
                case R.id.msg_edit:
                    touchView.setVisibility(View.VISIBLE);
                    break;
                case R.id.touch_view:
                    touchView.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private SynthesizerListener listener = new SynthesizerListener() {

        @Override
        public void onSpeakResumed() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeakProgress(int arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeakPaused() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeakBegin() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCompleted(SpeechError arg0) {
            // TODO Auto-generated method stub
            delay(500);
            //arg0.getPlainDescription(true);
        }

        @Override
        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
            // TODO Auto-generated method stub

        }
    };

    @NonNull
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        // 讯飞组件初始化
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=564499f1");
        initData();
    }

    private void initData() {
        playerInfoList = new ArrayList<>();
        adapter = new ChatMsgAdapter(this, playerInfoList);
        chatListView.setAdapter(adapter);
    }

    private void initListener() {
        this.voiceBtn.setOnClickListener(viewListener);
        this.sendMsg.setOnClickListener(viewListener);
        this.msgEdit.setOnClickListener(viewListener);
        this.touchView.setOnClickListener(viewListener);
        //this.msgEdit.addTextChangedListener(watcher);
    }

    private void initView() {
        this.relativeLayout = (RelativeLayout) findViewById(R.id.header);
        this.sendMsg = (Button) findViewById(R.id.send_msg);
        this.msgEdit = (EditText) findViewById(R.id.msg_edit);
        this.voiceBtn = (ImageButton) findViewById(R.id.voice_btn);
        this.touchView = findViewById(R.id.touch_view);
        this.chatListView = (ListView) findViewById(R.id.chat_list_view);
        this.textView = (TextView) relativeLayout.findViewById(R.id.main_header_tv);
        this.playerInfoList = new ArrayList<>();
        this.adapter = new ChatMsgAdapter(this, playerInfoList);
        this.chatListView.setAdapter(adapter);
    }

    private void delay(int ms) {
        TimerTask task = new TimerTask() {
            public void run() {
                //execute the task
                mark = 0;
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, ms);
    }

    public void send(final String params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpCient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet("http://104.128.83.198:5000/?txt=" + params);
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //    e.printStackTrace();
                }
            }
        }).start();
    }

    public void say(String words) {
        //SpeechUtility.createUtility(this,SpeechConstant.APPID+"=12345678");
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(MainActivity.this, null);
        //2.合成参数设置
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //开始合成
        mTts.startSpeaking(words, listener);
        //mark = 0;
    }

    //TODO 开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(this, null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
        Toast.makeText(this, R.string.please_speak, Toast.LENGTH_SHORT).show();
    }

    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        msgEdit.setText(text);
        text = text.replaceAll("\\p{P}", "");
        if (!TextUtils.isEmpty(text)) {
            textView.setText(R.string.inputint);
            send(text);
        }
    }

    private class asyncData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            msgEdit.setEnabled(false);
            textView.setText(R.string.inputint);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... Strings) {
            getData get = new getData();
            return get.getResult(Strings[0]);
        }

        @Override
        protected void onPostExecute(String string) {
            if (!TextUtils.isEmpty(string)) {
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setTextContent(string);
                playerInfo.setMine(false);
                playerInfoList.add(playerInfo);
                adapter.notifyDataSetChanged();//属性里设置自动滚动
            }
            msgEdit.setEnabled(true);
            textView.setText(R.string.app_name);
            super.onPostExecute(string);
        }
    }
}
