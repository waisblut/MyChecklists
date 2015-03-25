package com.waisblut.mychecklists.b_model;

import java.util.LinkedList;

public class Checklist {
    private long id;
    private String name;
    private LinkedList<ChecklistItem> list;//TODO select the best type of Collection

    public Checklist() {

    }

    public Checklist(long id, String name, LinkedList<ChecklistItem> items) {
        this.id = id;
        this.name = name;
        this.list = items;
    }

    public Checklist(String name, LinkedList<ChecklistItem> list) {
        this.setName(name);
        this.list = list;
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
}
