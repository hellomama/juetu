package com.tony.juetu.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by bushi on 2018/6/5.
 */

public class PreUtils {

    private SharedPreferences   mPrefs;
    private static PreUtils sInstance = null;

    private static final String JSON_TITLE_LIST = "title_list";

    private JSONArray mTitleList = null;


    public static PreUtils getInstance()
    {
        if (sInstance == null)
        {
            synchronized (PreUtils.class)
            {
                if (sInstance == null)
                {
                    sInstance = new PreUtils();
                }
            }
        }

        return sInstance;
    }

    public void setPrefs(SharedPreferences aPrefs)
    {
        mPrefs = aPrefs;
    }

    public SharedPreferences getPrefs()
    {
        return mPrefs;
    }

    public void setContent(String aKey, String aContent)
    {
        String content = getContent(aKey, "");
        if (!content.equals(aContent))
        {
            SharedPreferences prefs = mPrefs;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(aKey, aContent);
            editor.apply();
        }
    }

    public String getContent(String aKey, String aDefaultValue)
    {
        if (!TextUtils.isEmpty(aKey))
        {
            SharedPreferences prefs = mPrefs;
            return prefs.getString(aKey, aDefaultValue);
        }

        return "";
    }

    public void saveList(ArrayList<String> aList)
    {
        if (aList != null)
        {
            JSONArray customArray = new JSONArray();
            for (String title : aList) {
                customArray.put(title);
            }
            mTitleList = customArray;
        }

        setContent(JSON_TITLE_LIST,mTitleList.toString());
    }

    public ArrayList<String> getList()
    {
        ArrayList<String> result = new ArrayList<>();
        if (mTitleList != null && mTitleList.length()>0)
        {
            String content = getContent(JSON_TITLE_LIST,"");
            try {
                JSONArray array = new JSONArray(content);
                for (int i = 0;i<array.length();i++)
                {
                    result.add(array.getString(i));
                }
                return result;
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
}
