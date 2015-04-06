package com.waisblut.mychecklists.b_model;

import java.io.Serializable;
import java.util.LinkedList;

public class Checklist
        implements Serializable {
    private long id;
    private String name;
    private int order;
    private LinkedList<ChecklistItem> itemList;//TODO select the best type of Collection

    public Checklist() {

    }

    public Checklist(long id, String name, LinkedList<ChecklistItem> items, int order) {
        this.id = id;
        this.name = name;
        this.setItemList(items);
        this.order = order;
    }

    public Checklist(String name, LinkedList<ChecklistItem> list, int order) {
        this.setName(name);
        this.setItemList(list);
        this.order = order;
    }

    public void addItem(ChecklistItem item) {
        this.getItemList()
            .add(item);

    }

    public void addItemAt(ChecklistItem item, int pos) {
        this.getItemList()
            .add(pos, item);

    }

    public void removeItem(ChecklistItem item) {
        getItemList().remove(item);
    }

    public void removeItemAt(int pos) {
        getItemList().remove(pos);
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

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public LinkedList<ChecklistItem> getItemList() {
        return itemList;
    }

    public void setItemList(LinkedList<ChecklistItem> itemList) {
        this.itemList = itemList;
    }
}
