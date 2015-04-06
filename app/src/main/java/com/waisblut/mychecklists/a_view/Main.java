package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;

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
}
