package com.ng.ui.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.ng.ui.R;

import java.util.List;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-02-29
 */
public class TestAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<TestData> mData;

    public TestAdapter(Context mContext, List<TestData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createViewHolder(mContext, R.layout.test_item, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }


    }

    public static ViewHolder createViewHolder(Context context, @LayoutRes int layoutId, ViewGroup viewGroup) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }
}
