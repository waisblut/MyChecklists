package com.waisblut.mychecklists.c_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.d_enum.EnumDataTypes;
import com.waisblut.mychecklists.e_util.Logger;

import java.util.ArrayList;
import java.util.List;

public class DSChecklist
        extends MyDataSource {
    public static final String TABLE_NAME = "Checklist";
    //region table fields
    public static final String ID = "_id";
    public static final String ORDER = "order_in_list";
    public static final String NAME = "list_name";
    //endregion

    //region constructors
    public DSChecklist() {
        super();

        setIni();
    }

    public DSChecklist(Context context) {
        super(context);

        setIni();

        super.openHelper = new MyOpenHelper(context);
    }
    //endregion

    private void setIni() {
        super.tableName = TABLE_NAME;

        //region columns
        super.setColumn(ID, EnumDataTypes.INTEGER, "PRIMARY KEY AUTOINCREMENT");
        super.setColumn(NAME, EnumDataTypes.TEXT, "NOT NULL UNIQUE");
        super.setColumn(ORDER, EnumDataTypes.INTEGER, "NOT NULL");
        //endregion
    }

    public Checklist create(Checklist checklist) {
        long insertId;
        ContentValues values = new ContentValues();

        values.put(NAME, checklist.getName());
        values.put(ORDER, checklist.getOrder());

        try {
            open();
            checklist.setId(database.insertOrThrow(tableName, null, values));

            DSChecklistItem dsItem = new DSChecklistItem(super.context);

            for (ChecklistItem c : checklist.getItemList()) {
                dsItem.create(c, checklist);
            }

            Logger.log('i', tableName + " being created \n with ID=" + checklist.getId());
        }
        catch (SQLException ex) {
            Logger.log('e', tableName + " ERROR - " + ex.getMessage());
        }

        return checklist;
    }

    public List<Checklist> getAllChecklists() {
        this.open();
        List<Checklist> lstChecklist = new ArrayList<>();

        Cursor c = null;

        try {
            c = database.query(this.tableName,
                               super.columns.toArray(new String[columns.size()]),
                               null,
                               null,
                               null,
                               null,
                               ORDER);
            Logger.log('i', this.tableName + " Returned " + c.getCount() + " rows");
        }
        catch (Exception ex) {
            Logger.log('i', "ERROR READING " + tableName + ": " + ex.getMessage());
        }

        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    lstChecklist.add(createCheckList(c));
                }
            }
        }

        return lstChecklist;
    }

    public void updateOrder(List<Checklist> list) {
        open();

        for (Checklist c : list) {
            this.updateOrder(c, list.indexOf(c));
        }
    }

    private void updateOrder(Checklist c, int newPosition) {
        ContentValues values = new ContentValues();

        values.put(ORDER, newPosition);
        database.update(TABLE_NAME, values, ID + "=" + c.getId(), null);
        c.setOrder(newPosition);
    }

    private Checklist createCheckList(Cursor c) {
        Checklist chkList = new Checklist();
        DSChecklistItem dsItem;

        if (c.getCount() > 0) {
            dsItem = new DSChecklistItem(super.context);
            dsItem.open();

            chkList = new Checklist(c.getLong(c.getColumnIndex(ID)),
                                    c.getString(c.getColumnIndex(NAME)),
                                    dsItem.getListItem(c.getLong(c.getColumnIndex(ID))),
                                    c.getInt(c.getColumnIndex(ORDER)));
        }

        return chkList;
    }
}




