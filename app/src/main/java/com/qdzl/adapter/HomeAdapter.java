package com.qdzl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdzl.R;
import com.qdzl.bean.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by QDZL on 2017/11/28.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Book> bookList;
    private LayoutInflater lif;
    private Picasso picasso;
    //    public RelativeLayout.LayoutParams lp;
    Integer position;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeAdapter(Context context, List<Book> bookList, Integer position) {
        this.lif = LayoutInflater.from(context);
        if (bookList == null) {
            this.bookList = new ArrayList<Book>();
        } else {
            this.bookList = bookList;
        }
        this.picasso = Picasso.with(context);
        this.position = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        lp = new RelativeLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
//                RecyclerView.LayoutParams.WRAP_CONTENT);
        View v = null;
        if (position == 0) {
            v = lif.inflate(R.layout.layout_item_h, null);
        } else if (position == 1) {
            v = lif.inflate(R.layout.layout_item_v, null);
        }
//        v.setLayoutParams(lp);
        RecyclerView.ViewHolder viewHolder = new ItemHolder(v, onItemClickListener);
        return viewHolder;
    }

    public void addAll(List<Book> data) {
        this.bookList = data;
        this.notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private TextView txtTitle;
        private TextView txtType;
        private TextView txtTag;
        int index;
        OnItemClickListener onItemClickListener;

        public ItemHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtType = (TextView) itemView.findViewById(R.id.txt_type);
            txtTag = (TextView) itemView.findViewById(R.id.txt_tag);

            itemView.setOnClickListener(onClickListener);
        }

        public View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(((RecyclerView) v.getParent()), v, index);
            }
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
//        itemHolder.index = position;
        Book book = bookList.get(position);
        picasso.load(book.getPhoto())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(itemHolder.imgPhoto);
        itemHolder.txtTitle.setText(book.getName());
        itemHolder.txtType.setText(book.getAuthor());
        itemHolder.index=book.getId();
        String tag = book.getTags();
        List<String> tags = Arrays.asList(tag.split("|"));
        tag = "";
        for (String str : tags) {
            tag += str + " ";
        }
        itemHolder.txtTag.setText(tag);
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View item, int position);
    }
//    @Override
//    public long getItemId(int position) {
//        return bookList.get(position).getBookId();
//    }
}
