package com.waisblut.mychecklists.b_model;

import com.waisblut.mychecklists.d_enum.EnumChecklistItemState;

public class ChecklistItem {
    private long id;
    private String name;
    private int state; //0=empty 1=checked 2=paused 3=notOK

    public ChecklistItem() {

    }

    public ChecklistItem(String s) {
        this.name = s;
        this.state = EnumChecklistItemState.EMPTY.getCode();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void add(String name) {
        this.name = name;
    }
}
