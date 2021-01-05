package com.example.wastesorting;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.wastesorting.fragments.HomeFragment;
import com.example.wastesorting.fragments.MineFragment;
import com.example.wastesorting.fragments.SortingFragment;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private String[] tabText = {"首页", "分类百科", "我的"};

    private int[] normalIcon = {R.drawable.home_unselected, R.drawable.sort_unselected, R.drawable.mine_unselected};

    private int[] selectIcon = {R.drawable.home_selected, R.drawable.sort_selected, R.drawable.mine_selected};

    private List<Fragment> fragmentList = new ArrayList<>();

    private EasyNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBar = (EasyNavigationBar) findViewById(R.id.navigationBar);
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SortingFragment());
        fragmentList.add(new MineFragment());

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragmentList)
                .fragmentManager(getSupportFragmentManager())
                .mode(EasyNavigationBar.MODE_NORMAL)
                .iconSize(22)
                .normalTextColor(Color.parseColor("#8a8a8a"))
                .smoothScroll(true)
                .canScroll(true)
                .build();

        Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(this, MyService.class);
        stopService(stopIntent);
    }
}