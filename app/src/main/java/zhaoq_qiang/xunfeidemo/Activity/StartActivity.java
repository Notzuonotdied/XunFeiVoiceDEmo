package zhaoq_qiang.xunfeidemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zhaoq_qiang.xunfeidemo.Bean.Constant;
import zhaoq_qiang.xunfeidemo.R;

public class StartActivity extends AppCompatActivity {

    private android.widget.Button defaultip;
    private android.widget.EditText ipaddress;
    private android.widget.EditText port;
    private android.widget.Button submit;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.submit:
                    Constant.newUrl = "http://" + ipaddress.getText().toString() + ":" +
                            port.getText().toString() + "/?txt=";
                    Log.i("Notzuonotdied", "Constant.newUrl = " + Constant.newUrl);
                    intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.default_ip:
                    Constant.newUrl = "";
                    Log.i("Notzuonotdied", "Constant.newUrl = " + Constant.newUrl);
                    intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
        initListener();
    }

    private void initListener() {
        this.defaultip.setOnClickListener(listener);
        this.submit.setOnClickListener(listener);
    }

    private void initView() {
        this.submit = (Button) findViewById(R.id.submit);
        this.port = (EditText) findViewById(R.id.port);
        this.ipaddress = (EditText) findViewById(R.id.ip_address);
        this.defaultip = (Button) findViewById(R.id.default_ip);
    }
}
