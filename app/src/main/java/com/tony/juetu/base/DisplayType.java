package com.tony.juetu.base;

/**
 * Created by dev on 6/1/18.
 */

public enum DisplayType {
    EAllCategroy(0x1000),
    ENone(0xFFFF);

    private int mType;
    DisplayType(int aType)
    {
        mType = aType;
    }

    public int type()
    {
        return mType;
    }

    public static DisplayType getType(int aType)
    {
        for (DisplayType displayType : DisplayType.values())
        {
            if (displayType.type() == aType)
            {
                return displayType;
            }
        }

        return ENone;
    }
}
