package com.example.wastesorting.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wastesorting.ContentActivity;
import com.example.wastesorting.MyApplication;
import com.example.wastesorting.R;
import com.example.wastesorting.SearchActivity;
import com.example.wastesorting.httptool.HttpUtil;
import com.example.wastesorting.httptool.Utility;
import com.example.wastesorting.newsitem.News;
import com.example.wastesorting.newsitem.NewsList;
import com.example.wastesorting.newsitem.Title;
import com.example.wastesorting.newsitem.TitleAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jediburrell.customfab.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private List<Title> titleList = new ArrayList<>();
    private ListView listView;
    private TitleAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag, container, false);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar)
                view.findViewById(R.id.tool_bar);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.top_home);
        }
        actionBar.setTitle("首页");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        View headView = inflater.inflate(R.layout.view_header, null, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.addHeaderView(headView);
        adapter = new TitleAdapter(MyApplication.getContext(), R.layout.list_view_item, titleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent(getActivity(), ContentActivity.class);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Title title = titleList.get(position);
                intent.putExtra("title","今日新闻");
                intent.putExtra("uri",title.getUri());
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestNew();
            }
        });

        requestNew();

        return view;
    }



    public void requestNew(){
        String address = "http://api.tianapi.com/lajifenleinews/index?key=811e7094ccf939d3223017a76fa95f7e&num=10";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         Toast.makeText(MyApplication.getContext(), "新闻加载失败",
                                 Toast.LENGTH_SHORT).show();
                     }
                 });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText = response.body().string();
                NewsList newsList = Utility.parseJsonWithGSON(responseText);
                int code = newsList.code;
                String msg = newsList.msg;
                if(code == 200){
                    titleList.clear();
                    for(News news : newsList.newsList) {
                        Title title = new Title(news.title, news.description, news.picUrl, news.url);
                        titleList.add(title);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            refreshLayout.setRefreshing(false);
                            Log.d("HomePage", "success received");
                        }
                    });
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "数据错误返回",
                                    Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }
}
