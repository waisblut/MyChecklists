package com.waisblut.mychecklists.c_data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.waisblut.mychecklists.e_util.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyOpenHelper
        extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "parkingmanager.db";
    private static final int DATABASE_VERSION = 1;
    private final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private LinkedHashMap<String, String> lstMyTables;

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        lstMyTables = new LinkedHashMap<>();

        lstMyTables.put(DSChecklist.TABLE_NAME, createTable(new DSChecklist()));
        lstMyTables.put(DSChecklistItem.TABLE_NAME, createTable(new DSChecklistItem()));
    }

    public MyOpenHelper(Context context,
                        String tableName,
                        List<String> columns,
                        List<String> columnsType,
                        List<String> columnsConstraint) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Map.Entry<String, String> m : lstMyTables.entrySet()) {
            try {
                db.execSQL(m.getValue());
                Logger.log('i', Logger.getCreatedMessage(m.getKey()));
            }
            catch (SQLException e) {
                Logger.log('e', Logger.TableCreationError(m.getKey()) + " - " + e.getMessage());
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Map.Entry<String, String> m : lstMyTables.entrySet()) {
            db.execSQL(DROP_TABLE + m.getKey());
        }

        onCreate(db);

        Logger.log('i', Logger.getUpgradedMessage());
    }

    private String createTable(MyDataSource ds) {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ");
        sb.append(ds.tableName);
        sb.append(" (\n");

        for (int i = 0; i < ds.columns.size(); i++) {
            String constraint = ds.columnsExtra.get(i) == null ? "" : ds.columnsExtra.get(i);
            sb.append(ds.columns.get(i))
              .append(" ")
              .append(ds.columnsType.get(i))
              .append(" ")
              .append(constraint)
              .append(", \n");
        }

        for (int i = 0; i < ds.columnsConst.size(); i++) {
            sb.append(ds.columnsConst.get(i))
              .append(", \n");
        }

        sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, "");

        sb.append(") ");

        return sb.toString();
    }
}

