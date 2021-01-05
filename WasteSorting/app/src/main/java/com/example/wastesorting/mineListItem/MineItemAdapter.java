package com.example.wastesorting.mineListItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastesorting.R;
import com.example.wastesorting.fragments.MineFragment;

import java.util.List;

public class MineItemAdapter extends RecyclerView.Adapter<MineItemAdapter.ViewHolder> {

    private List<MineItem> mineItemList;

    private MineFragment.MyOnItemClickListener myOnItemClickListener;

    public void setMyOnItemClickListener(MineFragment.MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnItemClickListener.OnItemClickListener(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MineItem mineItem = mineItemList.get(position);
        holder.frontImage.setImageResource(mineItem.getFrontImageId());
        holder.backImage.setImageResource(mineItem.getBackImageId());
        holder.name.setText(mineItem.getName());
        if(position == 0) {
            holder.cityName.setText("上海市");
        }
    }

    @Override
    public int getItemCount() {
        return mineItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView frontImage;
        ImageView backImage;
        TextView name;
        TextView cityName;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            frontImage = (ImageView) view.findViewById(R.id.frontImage);
            backImage = (ImageView) view.findViewById(R.id.backImage);
            name = (TextView) view.findViewById(R.id.name);
            cityName = (TextView) view.findViewById(R.id.city_for_now);
        }
    }

    public MineItemAdapter(List<MineItem> mineItemList) {
        this.mineItemList = mineItemList;
    }


}
