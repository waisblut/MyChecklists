package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.c_data.DSChecklist;
import com.waisblut.mychecklists.c_data.DSChecklistItem;

import java.util.LinkedList;

public class Main
        extends Activity
        implements FragmentChecklist.onFragmentChecklistListener,
                   FragmentChecklistItem.OnFragmentChecklistItemListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                                .add(R.id.mainContainer, new FragmentChecklist())
                                .commit();

            create_a_test();
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

    private void create_a_test() {
        LinkedList<ChecklistItem> list;
        ChecklistItem item;
        DSChecklist dsChecklist = new DSChecklist(this);
        DSChecklistItem dsChecklistItem = new DSChecklistItem(this);


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
        dsChecklist.open();
        dsChecklistItem.open();
        dsChecklist.create(checklist);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar mala", 0);
        list.add(item);
        item = new ChecklistItem("encher de roupa", 1);
        list.add(item);
        item = new ChecklistItem("fechar mala", 2);
        list.add(item);

        checklist = new Checklist("Lista de Viagem", list, 3);
        dsChecklist.create(checklist);


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
        dsChecklist.create(checklist);


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
        dsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste1", list, 4);
        dsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste1.1", list, 5);
        dsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste2", list, 6);
        dsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste3", list, 7);
        dsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste4", list, 8);
        dsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste5", list, 9);
        dsChecklist.create(checklist);

        list = new LinkedList<>();
        checklist = new Checklist("Lista de Teste1", list, 10);
        dsChecklist.create(checklist);

        dsChecklist.selectWholeTableOnLogCat();
        dsChecklistItem.selectWholeTableOnLogCat();
    }

    //region Interface Methods
    @Override
    public void onClickChecklist(Checklist checklist) {
        TextView txtPosition;
        txtPosition = (TextView) findViewById(R.id.txtPosition);
        txtPosition.setText("Checklist ID = " + checklist.getId());

        getFragmentManager().beginTransaction()
                            .replace(R.id.mainContainer,
                                     FragmentChecklistItem.newInstance(checklist))
                            .addToBackStack(null)
                            .commit();

    }

    @Override
    public void onClickChecklistItem(ChecklistItem checklistItem) {
        TextView txtPosition;
        txtPosition = (TextView) findViewById(R.id.txtPosition);
        txtPosition.setText("ChecklistITEM ID = " + checklistItem.getId());
    }

    @Override
    public void onLongClickChecklistItem(int newId, int oldId) {

    }
    //endregion
}
