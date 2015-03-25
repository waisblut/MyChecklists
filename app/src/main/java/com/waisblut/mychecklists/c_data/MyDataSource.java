package com.waisblut.mychecklists.c_data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.waisblut.mychecklists.d_enum.EnumDataTypes;
import com.waisblut.mychecklists.e_util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MyDataSource {
    protected String tableName = "";
    MyOpenHelper openHelper;
    SQLiteDatabase database = null;
    Context context = null;
    List<String> columns = new ArrayList<String>();
    List<String> columnsType = new ArrayList<String>();
    List<String> columnsExtra = new ArrayList<String>();
    List<String> columnsConst = new ArrayList<String>();

    public MyDataSource() {

    }

    public MyDataSource(Context context) {
        this.context = context;
    }

    protected void setColumn(String columnName, EnumDataTypes type, String extra) {
        this.columns.add(columnName);
        this.getColumnsType()
            .add(type.getCode());
        this.columnsExtra.add(extra);
    }

    protected void setTableConstraint(String constr) {
        this.columnsConst.add(constr);
    }

    protected String getForeignKeyString(String fkName, String pkTableName, String pkFieldName) {

        return " FOREIGN KEY (" + fkName + ") " + "references " + pkTableName + " (" +
               pkFieldName + ") " + "ON DELETE CASCADE ON UPDATE CASCADE";
    }

    protected String getMultipleUniqueConstraint(List<String> Fields) {
        StringBuilder sb = new StringBuilder();

        sb.append(" UNIQUE(");
        for (String s : Fields) {
            sb.append(s)
              .append(", ");
        }
        sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, "");

        sb.append(")");

        return sb.toString();
    }

    protected String getMultiplePrimaryKeyConstraint(List<String> Fields) {
        String s = getMultipleUniqueConstraint(Fields);
        s = s.replaceAll("UNIQUE", "PRIMARY KEY");

        return s;
    }

    protected boolean idExists(Long id, String IdColumn) {
        {
            Cursor c = null;

            try {
                c = database.rawQuery("SELECT " + IdColumn + " FROM " + tableName, null);
            }
            catch (Exception ex) {
                Logger.log('i', "ERROR:" + ex.getMessage());
            }

            if (c != null && c.getCount() > 0) {
                c.moveToFirst();

                while (!c.isAfterLast()) {
                    if (id == c.getLong(0)) {
                        Logger.log('i', "FOUND " + IdColumn + ":" + c.getLong(0));

                        return true;
                    }

                    c.moveToNext();
                }
            }

            Logger.log('i', IdColumn + " " + id + " NOT FOUND!");
            return false;
        }
    }

    public void open() {
        Logger.log('i', "TABLE " + tableName + " called OPEN database.");
        database = openHelper.getWritableDatabase();
    }

    public void close() {
        Logger.log('i', "TABLE " + tableName + " - called CLOSE database");
        openHelper.close();
    }

    @SuppressLint("DefaultLocale")
    public void selectWholeTableOnLogCat() {
        StringBuilder tag = new StringBuilder();
        Cursor c = null;

        try {
            c = database.rawQuery("SELECT * FROM " + tableName + ";", null);
        }
        catch (Exception e) {
            Logger.log('i', "ERROR:" + e.getMessage());
        }

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            tag.append(this.tableName)
               .append(" has:\n");

            for (int i = 0; i < c.getColumnCount(); i++) {
                tag.append(c.getColumnName(i)
                            .toUpperCase())
                   .append(" - \t")
                   .append(c.getType(i))
                   .append("\n");
            }

            tag.append("-----------------\n");

            while (!c.isAfterLast()) {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    tag.append(c.getString(i))
                       .append(" - \t");
                }

                tag.replace(tag.lastIndexOf("-"), tag.lastIndexOf("-") + 1, "");
                tag.append("\n");

                c.moveToNext();
            }

            tag.append("-----------------\n\n\n");

            Logger.log('i', tag.toString());

            c.close();
        }
    }

    public int getRowsCount() {
        Cursor c = database.rawQuery("SELECT 1 FROM " + tableName + ";", null);
        int i = c.getCount();

        Logger.log('i', "Checking " + tableName + " ROWS AMOUNT = " + i);

        return i;
    }

    public Cursor getAll() {
        Cursor c = null;

        try {
            c = database.query(this.tableName,
                               this.columns.toArray(new String[columns.size()]),
                               null,
                               null,
                               null,
                               null,
                               null);
            Logger.log('i', this.tableName + " Returned " + c.getCount() + " rows");
        }
        catch (Exception ex) {
            Logger.log('i', "ERROR READING " + tableName + ": " + ex.getMessage());
        }

        return c;
    }

    public Cursor getItem(long id, String id_column) {
        Cursor c = null;

        try {
            c = database.query(this.tableName,
                               this.columns.toArray(new String[columns.size()]),
                               id_column + "=" + id,
                               null,
                               null,
                               null,
                               null);
        }
        catch (Exception ex) {
            Logger.log('i', "ERROR READING " + tableName + ": " + ex.getMessage());
        }


        return c;
    }

    public int getColumnsCount() {
        open();

        Cursor c = database.rawQuery("SELECT * FROM " + tableName + ";", null);
        int i = c.getColumnCount();

        Logger.log('i', "Checking " + tableName + " COLUMNS AMOUNT = " + i);

        return i;
    }

    public List<String> getColumnsName() {
        return columns;
    }

    public List<String> getColumnsType() {
        return columnsType;
    }
}

