package com.example.mepositry.grid;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;



/**
 * Created by louisgeek on 2018/9/21.
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViewSparseArray = new SparseArray<>();
    protected View mItemView;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
    }

    public <T extends View> T findBindItemView(int viewId) {
        View view = mViewSparseArray.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViewSparseArray.put(viewId, view);
        }
        return (T) view;

    }

}