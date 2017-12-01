package com.qdzl.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qdzl.App;
import com.qdzl.R;
import com.qdzl.adapter.ListViewAdapter;
import com.qdzl.adapter.MyPageAdapter;
import com.qdzl.bean.Book;
import com.qdzl.bean.Result;
import com.qdzl.fragment.FragmentMain;
import com.qdzl.fragment.FragmentMe;
import com.qdzl.fragment.FragmentSearch;
import com.qdzl.fragment.FragmentType;
import com.qdzl.util.HttpUtils;
import com.qdzl.util.JsonCallbacks;
import com.qdzl.util.SharedUtil;
import com.qdzl.util.TextUtils;
import com.qdzl.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {


    ViewPager vpBody;
    MyPageAdapter myPageAdapter;
    FragmentMain fragmentMain;
    FragmentType fragmentType;
    FragmentSearch fragmentSearch;
    FragmentMe fragmentMe;
    RadioGroup rgBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bind();
        init();

    }



    private void bind() {


        List<Fragment> fragments = new ArrayList<>();
        fragmentMain = new FragmentMain();
        fragmentType = new FragmentType();
        fragmentSearch = new FragmentSearch();
        fragmentMe = new FragmentMe();
        fragments.add(fragmentMain);
        fragments.add(fragmentType);
        fragments.add(fragmentSearch);
        fragments.add(fragmentMe);
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        vpBody = (ViewPager) this.findViewById(R.id.vp_body);
        vpBody.setAdapter(myPageAdapter);

        rgBottom= (RadioGroup) this.findViewById(R.id.rg_bottom);
        rgBottom.setOnCheckedChangeListener(onCheckedChangeListener);




    }
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.vp_rg_rb1:
                    vpBody.setCurrentItem(0);
                    break;
                case R.id.vp_rg_rb2:
                    vpBody.setCurrentItem(1);
                    break;
                case R.id.vp_rg_rb3:
                    vpBody.setCurrentItem(2);
                    break;
                case R.id.vp_rg_rb4:
                    vpBody.setCurrentItem(3);
                    break;
            }
        }
    };


    private void init() {

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }




}
