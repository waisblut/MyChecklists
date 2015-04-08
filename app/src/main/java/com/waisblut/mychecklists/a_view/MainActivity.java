package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.c_data.DSChecklist;
import com.waisblut.mychecklists.c_data.DSChecklistItem;

import java.util.LinkedList;

public class MainActivity
        extends Activity {
    private DynamicListView mMyListView;
    private static DSChecklist mDsChecklist;
    private DSChecklistItem mDsChecklistItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamiclistview);

        if (savedInstanceState == null) {
            create_a_test();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyListView = (DynamicListView) findViewById(R.id.fragment_checklist_listview);

        MyListAdapter adapter = new MyListAdapter(this);

        setAdapter(adapter);
        setDragAndDrop(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mDsChecklist != null) {
            mDsChecklist.close();
        }

        if (mDsChecklistItem != null) {
            mDsChecklistItem.close();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    private void setAdapter(MyListAdapter adapter) {
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(mMyListView);
        assert animationAdapter.getViewAnimator() != null;
        animationAdapter.getViewAnimator()
                        .setInitialDelayMillis(600);
        mMyListView.setAdapter(animationAdapter);
    }

    private void setDragAndDrop(MyListAdapter adapter) {
        mMyListView.enableDragAndDrop();
        //mMyListView.setDraggableManager(new TouchViewDraggableManager(R.id.list_row_draganddrop_touchview));
        mMyListView.setOnItemClickListener(new MyOnItemClickListener(mMyListView));
        mMyListView.setOnItemMovedListener(new MyOnItemMovedListener(this, adapter));
        mMyListView.setOnItemLongClickListener(new MyOnItemLongClickListener(mMyListView));
    }

    private static class MyListAdapter
            extends ArrayAdapter<Checklist> {

        private final Context mContext;

        MyListAdapter(final Context context) {
            mContext = context;
            mDsChecklist = new DSChecklist(context);
            addAll(mDsChecklist.getAllChecklists());
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
                                     .inflate(R.layout.list_row_main, parent, false);
            }

            Checklist item = getItem(position);

            ((TextView) view.findViewById(R.id.txtTitle)).setText(item.getName());
            ((TextView) view.findViewById(R.id.txtSubTitle)).setText(
                    "ID=" + item.getId() + " | Order = " + item.getOrder());

            return view;
        }
    }

    private class MyOnItemClickListener
            implements AdapterView.OnItemClickListener {
        private final DynamicListView mListView;

        MyOnItemClickListener(final DynamicListView listView) {
            mListView = listView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mListView != null) {
                //TODO Create intent to MainItem
                Intent intent = new Intent(getBaseContext(), ItemActivity.class);
                intent.putExtra("EXTRA", (Checklist) mListView.getItemAtPosition(position));
                startActivity(intent);
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

        private final ArrayAdapter<Checklist> mAdapter;
        private final Context mContext;

        MyOnItemMovedListener(Context context, final ArrayAdapter<Checklist> adapter) {
            mAdapter = adapter;
            mContext = context;

        }

        @Override
        public void onItemMoved(final int originalPosition, final int newPosition) {
            mDsChecklist = new DSChecklist(mContext);

            mDsChecklist.updateOrder(mAdapter.getItems());

            mAdapter.notifyDataSetChanged();
        }
    }

    private void create_a_test() {
        LinkedList<ChecklistItem> list;
        ChecklistItem item;
        mDsChecklist = new DSChecklist(this);
        mDsChecklistItem = new DSChecklistItem(this);


        list = new LinkedList<>();
        item = new ChecklistItem("Acionar motor", 0);
        list.add(item);
        item = new ChecklistItem("Testar turbina esquerda", 1);
        list.add(item);
        item = new ChecklistItem("Testar turbina direita", 2);
        list.add(item);
        item = new ChecklistItem("Decolar", 3);
        list.add(item);

        Checklist checklist = new Checklist("Lista de Decolagem", list, 0);
        mDsChecklist.open();
        mDsChecklistItem.open();
        mDsChecklist.create(checklist);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar mala", 0);
        list.add(item);
        item = new ChecklistItem("encher de roupa", 1);
        list.add(item);
        item = new ChecklistItem("fechar mala", 2);
        list.add(item);

        checklist = new Checklist("Lista de Viagem", list, 3);
        mDsChecklist.create(checklist);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar vara", 0);
        list.add(item);
        item = new ChecklistItem("Testar molinete", 1);
        list.add(item);
        item = new ChecklistItem("Testar barco", 2);
        list.add(item);
        item = new ChecklistItem("pescar", 3);
        list.add(item);

        checklist = new Checklist("Lista de Pescaria", list, 2);
        mDsChecklist.create(checklist);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar chuteira", 0);
        list.add(item);
        item = new ChecklistItem("pegar bola", 1);
        list.add(item);
        item = new ChecklistItem("pegar mochila", 2);
        list.add(item);
        item = new ChecklistItem("jogar", 3);
        list.add(item);

        checklist = new Checklist("Lista de Futebol", list, 1);
        mDsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste1", list, 4);
        mDsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste1.1", list, 5);
        mDsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste2", list, 6);
        mDsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste3", list, 7);
        mDsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste4", list, 8);
        mDsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste5", list, 9);
        mDsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste1", list, 10);
        mDsChecklist.create(checklist);

        mDsChecklist.selectWholeTableOnLogCat();
        mDsChecklistItem.selectWholeTableOnLogCat();
    }
}
