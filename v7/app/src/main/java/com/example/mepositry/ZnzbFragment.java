package com.example.mepositry;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mepositry.recycle.ILanguageRecycleListener;
import com.example.mepositry.recycle.LanguageRecycleAdapter;
import com.example.mepositry.adapter.ZnzbAdapter;

import java.util.ArrayList;


public class ZnzbFragment extends Fragment  implements ILanguageRecycleListener {

    private static ArrayList<ProductBean> list;
    private ListView listview_znzb;
    private ZnzbAdapter adapter;


    private LanguageRecycleAdapter languageRecycleAdapter;
    private RecyclerView rvLanguage;

    public static Fragment newInstance(ArrayList<ProductBean> mlist) {
        ZnzbFragment fragment = new ZnzbFragment();
        list = mlist;

        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_znzb, null);
        rvLanguage = view.findViewById(R.id.rvLanguage);
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(getActivity());
        rvLanguage.setLayoutManager(layoutManager);
        languageRecycleAdapter = new LanguageRecycleAdapter(getActivity(),list, this);
        rvLanguage.setAdapter(languageRecycleAdapter);
        return view;
    }

    @Override
    public void itemOnClick(String position) {

    }
}
