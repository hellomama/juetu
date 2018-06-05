package com.tony.juetu.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tony.juetu.R;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by bushi on 2018/6/4.
 */

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.AllCategoryHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> list;
    private ArrayList<String> checkList = new ArrayList<>();

    public AllCategoryAdapter(Context aContext,ArrayList aList,ArrayList aCheckList) {
        inflater = LayoutInflater.from(aContext);
        list = aList;
        checkList = aCheckList;
    }

    public ArrayList<String> getChecklist()
    {
        return checkList;
    }

    @Override
    public AllCategoryAdapter.AllCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category,parent,false);
        return new AllCategoryAdapter.AllCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(AllCategoryAdapter.AllCategoryHolder holder, final int position) {
        holder.name.setText(list.get(position));
        holder.aSwitch.setChecked(checkList.contains(list.get(position)));
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String name = list.get(position);
                if (isChecked)
                {
                    if (!checkList.contains(name))
                    {
                        checkList.add(name);
                    }
                }else {
                    if (checkList.contains(name))
                    {
                        checkList.remove(name);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class AllCategoryHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        Switch aSwitch;
        public AllCategoryHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            aSwitch = itemView.findViewById(R.id.switch_);
        }
    }
}
