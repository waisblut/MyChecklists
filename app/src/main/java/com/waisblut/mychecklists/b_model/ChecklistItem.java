package com.waisblut.mychecklists.b_model;

import com.waisblut.mychecklists.d_enum.EnumChecklistItemState;

import java.io.Serializable;

public class ChecklistItem implements Serializable {
    private long id;
    private String name;
    private int state; //0=empty 1=checked 2=paused 3=notOK
    private int order;

    public ChecklistItem() {

    }

    public ChecklistItem(String s, int order) {
        this.name = s;
        this.state = EnumChecklistItemState.EMPTY.getCode();
        this.order = order;
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

    //    public int getState() {
    //        return state;
    //    }

    public EnumChecklistItemState getState() {

        return EnumChecklistItemState.values()[state];
    }

    public void setState(int state) {
        this.state = state;
    }

    public void add(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
