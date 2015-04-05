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
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.c_data.DSChecklist;
import com.waisblut.mychecklists.c_data.DSChecklistItem;

import java.util.LinkedList;


public class FragmentChecklist
        extends Fragment {
    private Checklist mCheckList;
    private DSChecklist mDsChecklist;
    private DSChecklistItem mDsChecklistItem;
    private OnFragmentInteractionListener mListener;
    private DynamicListView mMyListView;

    public FragmentChecklist() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checklist, container);
        mMyListView = (DynamicListView) rootView.findViewById(R.id.fragment_checklist_listview);
        //        mMyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                if (null != mListener) {
        //                    mListener.onClickListener(((Checklist) parent.getItemAtPosition(position)).getId());
        //                }
        //            }
        //        });

        //        mMyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        //            @Override
        //            public boolean onItemLongClick(final AdapterView<?> parent,
        //                                           final View view,
        //                                           final int position,
        //                                           final long id) {
        //                mListener.onLongClickListener(1, 3);
        //                mMyListView.startDragging(position);
        //                return true;
        //            }
        //        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        create_a_test();

        MyListAdapter adapter = new MyListAdapter(getActivity());

        setAdapter(adapter);
        setDragAndDrop(adapter);


    }

    private void setAdapter(MyListAdapter adapter) {
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(mMyListView);
        assert animationAdapter.getViewAnimator() != null;
        animationAdapter.getViewAnimator()
                        .setInitialDelayMillis(300);
        mMyListView.setAdapter(animationAdapter);
    }

    private void setDragAndDrop(MyListAdapter adapter) {
        mMyListView.enableDragAndDrop();
        mMyListView.setDraggableManager(new TouchViewDraggableManager(R.id.list_row_draganddrop_touchview));
        mMyListView.setOnItemMovedListener(new MyOnItemMovedListener(getActivity(), adapter));
        mMyListView.setOnItemLongClickListener(new MyOnItemLongClickListener(mMyListView));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void create_a_test() {
        LinkedList<ChecklistItem> list;
        ChecklistItem item;
        mDsChecklist = new DSChecklist(getActivity());
        mDsChecklistItem = new DSChecklistItem(getActivity());


        list = new LinkedList<>();
        item = new ChecklistItem("Acionar motor");
        list.add(item);
        item = new ChecklistItem("Testar turbina esquerda");
        list.add(item);
        item = new ChecklistItem("Testar turbina direita");
        list.add(item);
        item = new ChecklistItem("Decolar");
        list.add(item);

        mCheckList = new Checklist("Lista de Decolagem", list, 0);
        mDsChecklist.open();
        mDsChecklist.create(mCheckList);
        mDsChecklistItem.open();

        list = new LinkedList<>();
        item = new ChecklistItem("pegar mala");
        list.add(item);
        item = new ChecklistItem("encher de roupa");
        list.add(item);
        item = new ChecklistItem("fechar mala");
        list.add(item);

        mCheckList = new Checklist("Lista de Viagem", list, 3);
        mDsChecklist.create(mCheckList);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar vara");
        list.add(item);
        item = new ChecklistItem("Testar molinete");
        list.add(item);
        item = new ChecklistItem("Testar barco");
        list.add(item);
        item = new ChecklistItem("pescar");
        list.add(item);

        mCheckList = new Checklist("Lista de Pescaria", list, 2);
        mDsChecklist.create(mCheckList);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar chuteira");
        list.add(item);
        item = new ChecklistItem("pegar bola");
        list.add(item);
        item = new ChecklistItem("pegar mochila");
        list.add(item);
        item = new ChecklistItem("jogar");
        list.add(item);

        mCheckList = new Checklist("Lista de Futebol", list, 1);
        mDsChecklist.create(mCheckList);

        list = new LinkedList<>();
        mCheckList = new Checklist("Lista de Teste1", list, 4);
        mDsChecklist.create(mCheckList);

        list = new LinkedList<>();
        mCheckList = new Checklist("Lista de Teste1", list, 5);
        mDsChecklist.create(mCheckList);

        list = new LinkedList<>();
        mCheckList = new Checklist("Lista de Teste2", list, 6);
        mDsChecklist.create(mCheckList);

        list = new LinkedList<>();
        mCheckList = new Checklist("Lista de Teste3", list, 7);
        mDsChecklist.create(mCheckList);

        list = new LinkedList<>();
        mCheckList = new Checklist("Lista de Teste4", list, 8);
        mDsChecklist.create(mCheckList);

        list = new LinkedList<>();
        mCheckList = new Checklist("Lista de Teste5", list, 9);
        mDsChecklist.create(mCheckList);

        list = new LinkedList<>();
        mCheckList = new Checklist("Lista de Teste1", list, 10);
        mDsChecklist.create(mCheckList);
    }

    public interface OnFragmentInteractionListener {
        void onClickListener(long id);

        void onLongClickListener(int oldPos, int newPos);
    }

    private static class MyListAdapter
            extends ArrayAdapter<Checklist> {

        private final Context mContext;

        MyListAdapter(final Context context) {
            mContext = context;
            DSChecklist ds = new DSChecklist(context);
            addAll(ds.getAllChecklists());
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
                                     .inflate(R.layout.list_row_checklist, parent, false);
            }

            Checklist item = getItem(position);

            ((TextView) view.findViewById(R.id.txtTitle)).setText(item.getName());
            ((TextView) view.findViewById(R.id.txtSubTitle)).setText(
                    "ID=" + item.getId() + " | Order = " + item.getOrder());

            return view;
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

        private final ArrayAdapter<Checklist> mAdapter;
        private final Context mContext;

        MyOnItemMovedListener(Context context, final ArrayAdapter<Checklist> adapter) {
            mAdapter = adapter;
            mContext = context;

        }

        @Override
        public void onItemMoved(final int originalPosition, final int newPosition) {
            Toast.makeText(mContext,
                           "initial=" + originalPosition + " | final=" + newPosition,
                           Toast.LENGTH_LONG)
                 .show();
        }
    }
}
