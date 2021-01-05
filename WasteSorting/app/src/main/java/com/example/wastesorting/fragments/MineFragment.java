package com.example.wastesorting.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastesorting.MyApplication;
import com.example.wastesorting.R;
import com.example.wastesorting.mineListItem.MineItem;
import com.example.wastesorting.mineListItem.MineItemAdapter;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment {

    //华为应用商店
    public static final String PACKAGE_HUAWEI_MARKET = "com.huawei.appmarket";
    public static final String HUAWEI_MARKET_PAGE = "com.huawei.appmarket.service.externalapi.view.ThirdApiActivity";

    private List<MineItem> mineItemList = new ArrayList<>();
    private RecyclerView recyclerView;

    public MineFragment() {
        initItems();
    }

    LinearLayoutManager layoutManager;

    public interface MyOnItemClickListener {
        void OnItemClickListener(View itemView);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_frag, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.mine_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //标题居中
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.actionbar_mine, null);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setCustomView(mActionBarView, layoutParams);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView.setLayoutManager(layoutManager);
        MineItemAdapter adapter = new MineItemAdapter(mineItemList);
        adapter.setMyOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(final View itemView) {
                int position = recyclerView.getChildAdapterPosition(itemView);
                if(position == 0){
//                    Toast.makeText(MyApplication.getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                    List<HotCity> hotCities = new ArrayList<>();
                    hotCities.add(new HotCity("北京", "北京", "101010100"));
                    hotCities.add(new HotCity("上海", "上海", "101020100"));
                    hotCities.add(new HotCity("广州", "广东", "101280101"));
                    hotCities.add(new HotCity("深圳", "广东", "101280601"));
                    hotCities.add(new HotCity("杭州", "浙江", "101210101"));

                    CityPicker.getInstance()
                        .setFragmentManager(getActivity().getSupportFragmentManager())  //此方法必须调用
                        .enableAnimation(true)  //启用动画效果
                        .setLocatedCity(new LocatedCity("深圳", "广东", "101210101")) //APP自身已定位的城市，默认为null（定位失败）.setHotCities(hotCities)  //指定热门城市
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, City data) {
                                TextView cityName = itemView.findViewById(R.id.city_for_now);
                                cityName.setText(data.getName());
                            }

                            @Override
                            public void onLocate() {
                                //开始定位，这里模拟一下定位
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //定位完成之后更新数据
                                        CityPicker.getInstance()
                                                .locateComplete(new LocatedCity("深圳", "广东", "101280601"),
                                                        LocateState.SUCCESS);
                                    }
                                }, 2000);
                            }
                        })
                        .show();
                } else if(position == 2) {
                    toSelfSetting(getContext());
                } else if(position == 1) {
                    //跳转应用市场
                    goToMarket(getContext(), HUAWEI_MARKET_PAGE);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        Button quit = (Button) view.findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.wastesorting.FORCE_OFFLINE");
                getActivity().sendBroadcast(intent);
            }
        });

        return view;
    }

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }

    private void initItems() {
        MineItem item1 = new MineItem("我的位置", R.drawable.location, R.drawable.arrow);
        MineItem item2 = new MineItem("给我们评分", R.drawable.range, R.drawable.arrow);
        MineItem item3 = new MineItem("权限管理", R.drawable.security, R.drawable.arrow);
        MineItem item4 = new MineItem("关于我们", R.drawable.aboutus, R.drawable.arrow);
        mineItemList.add(item1);
        mineItemList.add(item2);
        mineItemList.add(item3);
        mineItemList.add(item4);
    }
}
