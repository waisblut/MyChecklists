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
import java.util.LinkedList;
import java.util.List;

public class DSChecklistItem
        extends MyDataSource {
    public static final String TABLE_NAME = "ChecklistItem";
    //region table fields
    public static final String ID = "_id";
    public static final String CHECKLISTID = "checklist_id";
    public static final String STATE = "state";
    public static final String NAME = "item_name";
    public static final String ORDER = "item_order";

    //endregion

    //region constructors
    public DSChecklistItem() {
        super();

        setIni();
    }

    public DSChecklistItem(Context context) {
        super(context);

        setIni();

        super.openHelper = new MyOpenHelper(context);
    }
    //endregion

    private void setIni() {
        super.tableName = TABLE_NAME;

        //region columns
        super.setColumn(ID, EnumDataTypes.INTEGER, "PRIMARY KEY AUTOINCREMENT");
        super.setColumn(CHECKLISTID, EnumDataTypes.INTEGER, "");
        super.setColumn(STATE, EnumDataTypes.INTEGER, "");
        super.setColumn(ORDER, EnumDataTypes.INTEGER, "NOT NULL");
        super.setColumn(NAME, EnumDataTypes.TEXT, "NOT NULL");

        super.setTableConstraint(super.getForeignKeyString(CHECKLISTID,
                                                           DSChecklist.TABLE_NAME,
                                                           DSChecklist.ID));
        //endregion
    }

    public ChecklistItem create(ChecklistItem checklistItem, Checklist checklist) {
        long insertId;
        ContentValues values = new ContentValues();

        values.put(CHECKLISTID, checklist.getId());
        values.put(STATE, checklistItem.getState().getCode());
        values.put(NAME, checklistItem.getName());
        values.put(ORDER, checklistItem.getOrder());

        try {
            open();
            insertId = database.insertOrThrow(tableName, null, values);

            Logger.log('i', tableName + " being created \n with ID=" + insertId);

            checklistItem.setId(insertId);
        }
        catch (SQLException ex) {
            Logger.log('e', tableName + " ERROR - " + ex.getMessage());
        }

        return checklistItem;
    }

    public LinkedList<ChecklistItem> getListItem(long id) {

        LinkedList<ChecklistItem> list = new LinkedList<>();
        Cursor c = super.getItem(id, CHECKLISTID);

        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    list.add(createChecklistItem(c));
                }
            }
        }

        return list;
    }

    public List<ChecklistItem> getAllChecklistItems(Checklist checklist) {
        this.open();
        List<ChecklistItem> lstChecklistItem = new ArrayList<>();

        Cursor c = null;

        try {
            c = database.query(this.tableName,
                               super.columns.toArray(new String[columns.size()]),
                               CHECKLISTID + "=" + checklist.getId(),
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
                    lstChecklistItem.add(createChecklistItem(c));
                }
            }
        }

        return lstChecklistItem;
    }

    public void updateOrder(List<ChecklistItem> list) {
        open();

        for (ChecklistItem c : list) {
            this.updateOrder(c, list.indexOf(c));
        }
    }

    private void updateOrder(ChecklistItem c, int newPosition) {
        ContentValues values = new ContentValues();

        values.put(ORDER, newPosition);
        database.update(TABLE_NAME, values, ID + "=" + c.getId(), null);
        c.setOrder(newPosition);
    }

    public ChecklistItem createChecklistItem(Cursor c) {
        ChecklistItem item = null;

        if (c.getCount() > 0) {
            item = new ChecklistItem();
            item.setId(c.getLong(c.getColumnIndex(ID)));
            item.add(c.getString(c.getColumnIndex(NAME)));
            item.setState(c.getInt(c.getColumnIndex(STATE)));
            item.setOrder(c.getInt(c.getColumnIndex(ORDER)));
        }

        return item;
    }
}
