package com.example.wastesorting.searchResult;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wastesorting.R;

import java.util.List;

public class ItemTitleAdapter extends ArrayAdapter<ItemTitle> {
    private int resourceId;

    public ItemTitleAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ItemTitle> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemTitle itemTitle = getItem(position);
        View view;
        ViewHolder viewHolder;
        /**
         * 缓存布局和实例，优化 listView
         */
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) view.findViewById(R.id.rubbish_name);
            viewHolder.itemType = (TextView) view.findViewById(R.id.item_type);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.itemName.setText(itemTitle.getName());
        if(itemTitle.getType() == 0){
            viewHolder.itemType.setText("可回收垃圾");
            viewHolder.itemType.setTextColor(Color.parseColor("#0072E3"));
        } else if(itemTitle.getType() == 1) {
            viewHolder.itemType.setText("有害垃圾");
            viewHolder.itemType.setTextColor(Color.parseColor("#FF0000"));
        } else if(itemTitle.getType() == 2) {
            viewHolder.itemType.setText("厨余垃圾");
            viewHolder.itemType.setTextColor(Color.parseColor("#00DB00"));
        } else if(itemTitle.getType() == 3) {
            viewHolder.itemType.setText("其他垃圾");
            viewHolder.itemType.setTextColor(Color.parseColor("#7B7B7B"));
        }
        return view;
    }

    public class ViewHolder{
        TextView itemName;
        TextView itemType;
    }
}
