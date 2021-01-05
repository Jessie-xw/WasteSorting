package com.example.wastesorting.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.wastesorting.MyApplication;
import com.example.wastesorting.R;
import com.example.wastesorting.sortingFragments.FragmentAdapter;
import com.example.wastesorting.sortingFragments.FragmentFour;
import com.example.wastesorting.sortingFragments.FragmentOne;
import com.example.wastesorting.sortingFragments.FragmentThree;
import com.example.wastesorting.sortingFragments.FragmentTwo;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SortingFragment extends Fragment {

    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sorting_frag, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.sorting_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //标题居中
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(MyApplication.getContext())
                .inflate(R.layout.actionbar_sorting, null);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setCustomView(mActionBarView, layoutParams);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        fragmentList.add(new FragmentOne());
        fragmentList.add(new FragmentTwo());
        fragmentList.add(new FragmentThree());
        fragmentList.add(new FragmentFour());

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab;
        tab = tabLayout.getTabAt(0);
        tab.setCustomView(R.layout.tab_one);
        tab.getCustomView().findViewById(R.id.kehuishou_image).setSelected(true);
        tab.getCustomView().findViewById(R.id.kehuishou_text).setSelected(true);

        tab = tabLayout.getTabAt(1);
        tab.setCustomView(R.layout.tab_two);
        tab = tabLayout.getTabAt(2);
        tab.setCustomView(R.layout.tab_three);
        tab = tabLayout.getTabAt(3);
        tab.setCustomView(R.layout.tab_four);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setCustomView(R.layout.tab_one);
                        tab.getCustomView().findViewById(R.id.kehuishou_image).setSelected(true);
                        tab.getCustomView().findViewById(R.id.kehuishou_text).setSelected(true);
                    case 1:
                        tab.setCustomView(R.layout.tab_two);
                        tab.getCustomView().findViewById(R.id.chuyu_image).setSelected(true);
                        tab.getCustomView().findViewById(R.id.chuyu_text).setSelected(true);
                    case 2:
                        tab.setCustomView(R.layout.tab_three);
                        tab.getCustomView().findViewById(R.id.qita_image).setSelected(true);
                        tab.getCustomView().findViewById(R.id.qita_text).setSelected(true);
                    case 3:
                        tab.setCustomView(R.layout.tab_four);
                        tab.getCustomView().findViewById(R.id.youhai_image).setSelected(true);
                        tab.getCustomView().findViewById(R.id.youhai_text).setSelected(true);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setCustomView(R.layout.tab_one);
                        tab.getCustomView().findViewById(R.id.kehuishou_image).setSelected(false);
                        tab.getCustomView().findViewById(R.id.kehuishou_text).setSelected(false);
                    case 1:
                        tab.setCustomView(R.layout.tab_two);
                        tab.getCustomView().findViewById(R.id.chuyu_image).setSelected(false);
                        tab.getCustomView().findViewById(R.id.chuyu_text).setSelected(false);
                    case 2:
                        tab.setCustomView(R.layout.tab_three);
                        tab.getCustomView().findViewById(R.id.qita_image).setSelected(false);
                        tab.getCustomView().findViewById(R.id.qita_text).setSelected(false);
                    case 3:
                        tab.setCustomView(R.layout.tab_four);
                        tab.getCustomView().findViewById(R.id.youhai_image).setSelected(false);
                        tab.getCustomView().findViewById(R.id.youhai_text).setSelected(false);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
