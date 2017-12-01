package com.qdzl.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qdzl.R;
import com.qdzl.bean.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QDZL on 2017/11/30.
 */
public class ListViewAdapter extends BaseAdapter {
    Map<String, List<Book>> params;
    private LayoutInflater lif;
    private Context context;
    HomeAdapter adapter;
    OnListViewItemClickListener onListViewItemClickListener;

    public void setOnListViewItemClickListener(OnListViewItemClickListener onListViewItemClickListener) {
        this.onListViewItemClickListener = onListViewItemClickListener;
    }

    public ListViewAdapter(Context context, Map<String, List<Book>> params) {
        if (params == null) {
            this.params = new HashMap<>();
        } else {
            this.params = params;
        }
        this.context = context;
        lif = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = lif.inflate(R.layout.layout_home_books, null);
        TextView tvBookName = (TextView) v.findViewById(R.id.txt_book_type_name);
        RecyclerView rvBooks = (RecyclerView) v.findViewById(R.id.rec_list);
        LinearLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        adapter = new HomeAdapter(context, null, position);
        adapter.setOnItemClickListener(onItemClickListener);
        rvBooks.setAdapter(adapter);
        if (position == 0) {
            manager=new LinearLayoutManager(context);
            rvBooks.setLayoutManager(manager);
            tvBookName.setText("冒险|励志|热血");
            adapter.addAll(getBooks("book1"));
        } else {
            rvBooks.setLayoutManager(manager);
            tvBookName.setText("历史|妖怪|怪兽");
            adapter.addAll(getBooks("book2"));
        }
        return v;
    }
    public HomeAdapter.OnItemClickListener onItemClickListener=new HomeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View item, int position) {
            onListViewItemClickListener.onItemClick(parent,item,position);
        }
    };
    public List<Book> getBooks(String typeName) {
        if (params != null) {
            return params.get(typeName);
        }
        return null;
    }

    public void addAll(Map<String, List<Book>> data) {
        this.params = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return params.size();
    }

    @Override
    public Object getItem(int position) {
        return params.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public interface OnListViewItemClickListener{
        void onItemClick(RecyclerView parent,View item,int position);
    }
}
