package com.example.mepositry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mepositry.ProductBean;
import com.example.mepositry.R;

import java.util.ArrayList;

public class ZnzbAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductBean> list;

    public ZnzbAdapter(Context context, ArrayList<ProductBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.znzb_item,null);

        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_content = convertView.findViewById(R.id.tv_content);

        tv_name.setText(list.get(position).getName());
        tv_content.setText(list.get(position).getContent());


        return convertView;
    }
}
