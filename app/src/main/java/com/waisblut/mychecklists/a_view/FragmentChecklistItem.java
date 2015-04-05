package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.app.Fragment;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.c_data.DSChecklist;
import com.waisblut.mychecklists.c_data.DSChecklistItem;

public class FragmentChecklistItem
        extends Fragment {
    private Checklist mCheckList;
    private DSChecklist mDsChecklist;
    private DSChecklistItem mDsChecklistItem;
    public static OnFragmentChecklistItemListener mListener;
    private DynamicListView mMyListView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentChecklistItemListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentChecklistItemListener {
        void onClickChecklistItem(Checklist checklist);

        void onLongClickChecklistItem(int oldPos, int newPos);
    }
}
