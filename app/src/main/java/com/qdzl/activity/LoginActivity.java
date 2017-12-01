package com.qdzl.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qdzl.R;
import com.qdzl.bean.Book;
import com.qdzl.bean.Result;
import com.qdzl.bean.SysUser;
import com.qdzl.util.HttpUtils;
import com.qdzl.util.JsonCallbacks;
import com.qdzl.util.TextUtils;
import com.qdzl.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by QDZL on 2017/11/29.
 */
public class LoginActivity extends Activity {
    View mainBody;
    TextView txtTime;
    EditText etLoginAccount;
    EditText etLoginPwd;
    public Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        bind();
        init();
    }

    private void bind() {
        mainBody = this.findViewById(R.id.main_body);
        txtTime = (TextView) this.findViewById(R.id.txt_time);
        etLoginAccount = (EditText) this.findViewById(R.id.login_account);
        etLoginPwd = (EditText) this.findViewById(R.id.login_pwd);
        btnLogin = (Button) this.findViewById(R.id.btn_login);
    }

    private void init() {
        new TimeThread().start();
        //模糊------
        Bitmap bmp = ((BitmapDrawable) mainBody.getBackground()).getBitmap();
        blur(bmp);
        //------------------------------------
        btnLogin.setOnClickListener(new LoginOnClickLis());
    }
    class LoginOnClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_login:
                    login();
                    break;
            }
        }
    }

    private void login() {
        btnLogin.setText("登录中...");
        if(etLoginAccount.getText()!=null&&etLoginPwd.getText()!=null){
            String account=etLoginAccount.getText().toString().trim();
            String pwd=etLoginPwd.getText().toString().trim();
            if(TextUtils.verStr(account)&&TextUtils.verStr(pwd)){
                HttpUtils.httpLogin(this,account,pwd);
            }else{
                ToastUtil.show("账号或密码不能为空！");
            }
        }else{
            ToastUtil.show("请输入内容！");
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
//----------

    //模糊
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg) {
        long startMs = System.currentTimeMillis();
        float radius = 25;

        bkg = small(bkg);
        Bitmap bitmap = bkg.copy(bkg.getConfig(), true);

        final RenderScript rs = RenderScript.create(this);
        final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        bitmap = big(bitmap);
        mainBody.setBackground(new BitmapDrawable(getResources(), bitmap));
        rs.destroy();
        Log.d("zhangle", "-----------blur take away:" + (System.currentTimeMillis() - startMs) + "ms");
    }

    private static Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(4f, 4f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    private static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.25f, 0.25f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEE");
                    txtTime.setText(format.format(date));
                    break;
            }
        }
    };
}
