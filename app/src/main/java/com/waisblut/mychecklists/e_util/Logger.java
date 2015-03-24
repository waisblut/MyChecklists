package com.waisblut.mychecklists.e_util;

import android.util.Log;

public final class Logger
{
    private static final String TAG = "waisblut";
    private static final String CREATED = " has been created!";
    private static final String ERROR_CREATED = "ERROR CREATING ";
    private static final String UPGRADED = "Database has been UpGraded!";

    private static boolean canDebug = true;  //TODO Set it to FALSE ONRELEASE MODE

    public static boolean isDebug()
    {
        return canDebug;
    }

    public static String getCreatedMessage(String TableName)
    {
        return TableName + CREATED;
    }
    public static String TableCreationError(String TableName)
    {
        return ERROR_CREATED + TableName;
    }

    public static String getUpgradedMessage()
    {
        return UPGRADED;
    }

    public static void log(char type, String s)
    {
        if (canDebug)
        {
            switch (type)
            {
            case 'd':
                Log.d(TAG, s);
                break;

            case 'e':
                Log.e(TAG, s);
                break;

            case 'i':
                Log.i(TAG, s);
                break;

            case 'v':
                Log.v(TAG, s);
                break;

            case 'w':
                Log.w(TAG, s);
                break;

            default:
                break;
            }
        }
    }
}