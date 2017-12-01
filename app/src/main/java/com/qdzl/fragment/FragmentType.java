package com.qdzl.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qdzl.R;

/**
 * Created by QDZL on 2017/11/30.
 */
public class FragmentType extends Fragment {
    private View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(v ==null){
            v =inflater.inflate(R.layout.fragment_type,null);
            bindView();
            initView();
        }
        return v;
    }
    private void bindView(){
    }
    private void initView(){
    }
}
