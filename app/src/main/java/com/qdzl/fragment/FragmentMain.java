package com.qdzl.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qdzl.App;
import com.qdzl.R;
import com.qdzl.activity.LoginActivity;
import com.qdzl.adapter.ListViewAdapter;
import com.qdzl.bean.Book;
import com.qdzl.bean.Result;
import com.qdzl.util.HttpUtils;
import com.qdzl.util.JsonCallbacks;
import com.qdzl.util.SharedUtil;
import com.qdzl.util.TextUtils;
import com.qdzl.util.ToastUtil;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by QDZL on 2017/11/30.
 */
public class FragmentMain extends Fragment {
    Button btnTest;
    TextView txtMyInfo;
    TextView txtLogout;
    ListView lvMain;
    ListViewAdapter listViewAdapter;
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_main, null);
            bind();
            init();
            verifyLogin();
            updateLogin();
        }
        return v;
    }

    private void bind() {
        lvMain = (ListView) v.findViewById(R.id.lv_main);
        lvMain.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.layout_home_user, null));

        listViewAdapter = new ListViewAdapter(this.getActivity(), null);
        lvMain.setAdapter(listViewAdapter);
        listViewAdapter.setOnListViewItemClickListener(onListViewItemClickListener);

        btnTest = (Button) lvMain.findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new OnClickLis());
        txtMyInfo = (TextView) lvMain.findViewById(R.id.txt_myinfo);
        txtLogout = (TextView) lvMain.findViewById(R.id.txt_logout);
        txtLogout.setOnClickListener(new OnClickLis());

        getActivity().registerReceiver(receiver, new IntentFilter("LOGIN_STATE"));
    }

    private void init() {
        loadBooks();
    }

    public void loadBooks() {
        HttpUtils.get("http://192.168.3.3:8080/Maven_Project/book/query.shtml", new JsonCallbacks<Result<Map<String, List<Book>>>>() {
            @Override
            public void OnSuccess(Call call, Result<Map<String, List<Book>>> data) {
                if (data.state) {
                    listViewAdapter.addAll(data.data);
                } else {
                    ToastUtil.show(data.msg);
                }
            }

            @Override
            public void onFailure(Call call, int code) {
                super.onFailure(call, code);
            }
        });
    }

    class OnClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_test:
                    loadBooks();
                    break;
                case R.id.txt_logout:
                    logout();
                    break;
            }
        }
    }

    private void logout() {
        SharedUtil.remove(this.getActivity(), "account");
        SharedUtil.remove(this.getActivity(), "password");
        LoginActivity.startActivity(this.getActivity());
        this.getActivity().finish();
    }

    ListViewAdapter.OnListViewItemClickListener onListViewItemClickListener = new ListViewAdapter.OnListViewItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View item, int position) {
            ToastUtil.show("------" + position + "------");
        }
    };

    private void updateLogin() {
        Log.e("bbbb", "-------updateLogin------" + getApp().user);
        if (getApp().user != null) {
            txtMyInfo.setText(getApp().user.getNick());
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateLogin();
        }
    };

    private void verifyLogin() {
        String[] user = SharedUtil.getUserAccount(this.getActivity());
        if (TextUtils.verStr(user[0]) && TextUtils.verStr(user[1])) {
            HttpUtils.httpLogin(this.getActivity(), user[0], user[1]);
        } else {
            LoginActivity.startActivity(this.getActivity());
            this.getActivity().finish();
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    public App getApp() {
        return (App) this.getActivity().getApplication();
    }
}
