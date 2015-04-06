package com.waisblut.mychecklists.d_enum;

public enum EnumChecklistItemState {
    EMPTY(0),
    OK(1),
    REDO(2),
    SKIP(3),
    NOT_OK(4),
    PAUSE(5);

    protected int code;

    EnumChecklistItemState(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }
}
