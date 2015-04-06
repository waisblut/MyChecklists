package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.c_data.DSChecklistItem;
import com.waisblut.mychecklists.e_util.Logger;

public class FragmentChecklistItem
        extends Fragment {
    private static final String CHECKLIST_KEY = "checklist";

    private Checklist mChecklist;
    public static OnFragmentChecklistItemListener mListener;
    private DynamicListView mMyListView;

    public static FragmentChecklistItem newInstance(Checklist checklist) {
        FragmentChecklistItem fragment = new FragmentChecklistItem();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKLIST_KEY, checklist);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checklist, container);
        mMyListView = (DynamicListView) rootView.findViewById(R.id.fragment_checklist_listview);
        mChecklist = (Checklist) getArguments().getSerializable(CHECKLIST_KEY);

        MyListAdapter adapter = new MyListAdapter(getActivity(), mChecklist);

        setAdapter(adapter);
        setDragAndDrop(adapter);

        mMyListView.setOnItemClickListener(new MyOnItemClickListener(mMyListView));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setAdapter(MyListAdapter adapter) {
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(mMyListView);
        assert animationAdapter.getViewAnimator() != null;
        animationAdapter.getViewAnimator()
                        .setInitialDelayMillis(100);
        mMyListView.setAdapter(animationAdapter);
    }

    private void setDragAndDrop(MyListAdapter adapter) {
        mMyListView.enableDragAndDrop();
        //mMyListView.setDraggableManager(new TouchViewDraggableManager(R.id.list_row_draganddrop_touchview));
        mMyListView.setOnItemMovedListener(new MyOnItemMovedListener(getActivity(), adapter));
        mMyListView.setOnItemLongClickListener(new MyOnItemLongClickListener(mMyListView));
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    public interface OnFragmentChecklistItemListener {
        void onClickChecklistItem(ChecklistItem checklistItem);

        void onLongClickChecklistItem(int oldPos, int newPos);
    }

    private static class MyListAdapter
            extends ArrayAdapter<ChecklistItem> {

        private final Context mContext;

        MyListAdapter(final Context context, Checklist checklist) {
            mContext = context;
            DSChecklistItem ds = new DSChecklistItem(context);
            addAll(ds.getAllChecklistItems(checklist));
            ds.close();
        }

        @Override
        public long getItemId(final int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext)
                                     .inflate(R.layout.list_row_checklist_item, parent, false);
            }

            ChecklistItem item = getItem(position);

            ((TextView) view.findViewById(R.id.txtItem)).setText(item.getName());
            ImageView img = (ImageView) view.findViewById(R.id.imgItemState);
            switch (item.getState()) {
            case EMPTY:
                img.setImageResource(R.drawable.not_ok);
                break;
            case NOT_OK:
                img.setImageResource(R.drawable.not_ok);
                break;
            case OK:
                img.setImageResource(R.drawable.checked);
                break;
            case REDO:
                break;
            case SKIP:
                break;
            case PAUSE:
                break;
            }

            return view;
        }
    }

    private static class MyOnItemClickListener
            implements AdapterView.OnItemClickListener {
        private final DynamicListView mListView;

        MyOnItemClickListener(final DynamicListView listView) {
            this.mListView = listView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Logger.log('e', "Clicked!!!!!!!!!!!!!!!!!");
            if (this.mListView != null) {
                assert mListener != null;
                mListener.onClickChecklistItem((ChecklistItem) this.mListView.getItemAtPosition(
                        position));
            }
        }
    }

    private static class MyOnItemLongClickListener
            implements AdapterView.OnItemLongClickListener {

        private final DynamicListView mListView;

        MyOnItemLongClickListener(final DynamicListView listView) {
            mListView = listView;
        }

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent,
                                       final View view,
                                       final int position,
                                       final long id) {
            if (mListView != null) {
                mListView.startDragging(position - mListView.getHeaderViewsCount());
                Toast.makeText(view.getContext(), "Position=" + position, Toast.LENGTH_LONG)
                     .show();
            }
            return true;
        }
    }

    private class MyOnItemMovedListener
            implements OnItemMovedListener {

        private final ArrayAdapter<ChecklistItem> mAdapter;
        private final Context mContext;

        MyOnItemMovedListener(Context context, final ArrayAdapter<ChecklistItem> adapter) {
            mAdapter = adapter;
            mContext = context;

        }

        @Override
        public void onItemMoved(final int originalPosition, final int newPosition) {
            DSChecklistItem ds = new DSChecklistItem(mContext);

            ds.updateOrder(mAdapter.getItems());

            ds.close();

            mAdapter.notifyDataSetChanged();
        }
    }


}
