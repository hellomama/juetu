package com.tony.juetu.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tony.juetu.R;
import com.tony.juetu.utils.PreUtils;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/1/18.
 */

public class AllCategoryFragment extends SupportFragment {

    @NonNull
    public static AllCategoryFragment getInstance()
    {
        return new AllCategoryFragment();
    }

    private RecyclerView recyclerView;
    private AllCategoryAdapter categoryAdapter;
    private ArrayList<String> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category,container,false);
        initHeader(view);
        initList(view);
        return view;
    }

    private void initHeader(View aView)
    {
        TextView title = aView.findViewById(R.id.text_title);
        title.setText(getString(R.string.title_all_category));
        ImageView back = aView.findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategoryList();
                getActivity().finish();
            }
        });
    }

    private void initList(View aView)
    {
        list.add("Android");
        list.add("前端");
        list.add("iOS");
        list.add("产品");
        list.add("设计");
        list.add("工具资源");
        list.add("阅读");
        list.add("后端");
        list.add("人工智能");
        recyclerView = aView.findViewById(R.id.list_category);
        ArrayList<String> checkList = PreUtils.getInstance().getList();
        categoryAdapter = new AllCategoryAdapter(getActivity(),list,checkList);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void saveCategoryList()
    {
        if (categoryAdapter != null)
        {
            PreUtils.getInstance().saveList(categoryAdapter.getChecklist());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onBackPressedSupport() {
        saveCategoryList();
        return super.onBackPressedSupport();
    }
}
