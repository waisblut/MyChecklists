package com.waisblut.mychecklists.b_model;

import java.util.LinkedList;

public class Checklist {
    private long id;
    private String name;
    private int order;
    private LinkedList<ChecklistItem> list;//TODO select the best type of Collection

    public Checklist() {

    }

    public Checklist(long id, String name, LinkedList<ChecklistItem> items, int order) {
        this.id = id;
        this.name = name;
        this.list = items;
        this.order = order;
    }

    public Checklist(String name, LinkedList<ChecklistItem> list, int order) {
        this.setName(name);
        this.list = list;
        this.order = order;
    }

    public void addItem(ChecklistItem item) {
        this.list.add(item);

    }

    public void addItemAt(ChecklistItem item, int pos) {
        this.list.add(pos, item);

    }

    public void removeItem(ChecklistItem item) {
        list.remove(item);
    }

    public void removeItemAt(int pos) {
        list.remove(pos);
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
}
