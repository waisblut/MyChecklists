package com.waisblut.mychecklists.d_enum;

public enum EnumChecklistItemState {
    EMPTY(0),
    OK(1),
    REDO(2),
    SKIP(3),
    NOT_OK(4);

    protected int code;
    public int getCode() {
        return this.code;
    }

    EnumChecklistItemState(int i) {
        this.code = i;
    }
}
