package com.example.wastesorting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wastesorting.httptool.HttpUtil;
import com.example.wastesorting.httptool.Utility_Search;
import com.example.wastesorting.searchResult.ItemTitle;
import com.example.wastesorting.searchResult.ItemTitleAdapter;
import com.example.wastesorting.searchResult.SearchItem;
import com.example.wastesorting.searchResult.SearchItemList;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    private List<ItemTitle> itemTitleList = new ArrayList<>();
    private ListView listView;
    private ItemTitleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchAutoComplete.isShown()) {
                    try {
                        //如果搜索框中有文字，则会先清空文字
                        mSearchAutoComplete.setText("");
                        Method method = searchView.getClass().getDeclaredMethod("onCloseClicked");
                        method.setAccessible(true);
                        method.invoke(searchView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
            }
        });

        listView = (ListView)findViewById(R.id.search_result);
        adapter = new ItemTitleAdapter(this,R.layout.search_item, itemTitleList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        searchView.onActionViewExpanded();
        searchView.setIconified(true);
        //通过id得到搜索框控件
        mSearchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        mSearchAutoComplete.setTextSize(15);

        // 监听搜索框文字变化
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if("".equals(s) != true){
                    requestNew(s);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 请求处理数据
     */
    public void requestNew(final String itemName) {

        // 根据返回到的 URL 链接进行申请和返回数据
        String address = "http://api.tianapi.com/txapi/lajifenlei/index?key=811e7094ccf939d3223017a76fa95f7e&word="
                + itemName;   // key
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final SearchItemList searchItemList = Utility_Search.parseJsonWithGson(responseText);
                final int code = searchItemList.code;
                final String msg = searchItemList.msg;
                if (code == 200) {
                    itemTitleList.clear();
                    for (SearchItem searchItem : searchItemList.searchItemList) {
                        ItemTitle itemTitle = new ItemTitle(searchItem.name, searchItem.type);
                        itemTitleList.add(itemTitle);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(menu != null){
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.take_photo:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 1);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                break;
            default:
        }
    }
}