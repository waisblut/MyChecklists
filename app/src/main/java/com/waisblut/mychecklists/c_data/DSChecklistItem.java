package com.waisblut.mychecklists.c_data;

import android.content.Context;
import android.database.Cursor;

import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.d_enum.EnumDataTypes;

import java.util.LinkedList;

public class DSChecklistItem
        extends MyDataSource {
    public static final String TABLE_NAME = "ChecklistItem";
    //region table fields
    public static final String ID = "_id";
    public static final String CHECKLISTID = "checklist_id";
    public static final String STATE = "state";
    public static final String NAME = "item_name";
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
        super.setColumn(NAME, EnumDataTypes.TEXT, "NOT NULL");

        super.setTableConstraint(super.getForeignKeyString(CHECKLISTID,
                                                           DSChecklist.TABLE_NAME,
                                                           DSChecklist.ID));
        //endregion
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

    public ChecklistItem createChecklistItem(Cursor c) {
        ChecklistItem item = null;

        if (c.getCount() > 0) {
            item = new ChecklistItem();
            item.setId(c.getLong(c.getColumnIndex(ID)));
            item.add(c.getString(c.getColumnIndex(NAME)));
            item.setState(c.getInt(c.getColumnIndex(STATE)));
        }

        return item;
    }
}
