package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.waisblut.mychecklists.R;

public class Main
        extends Activity
        implements FragmentChecklist.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                                .replace(R.id.mainContainer, new FragmentChecklist())
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(long id) {
        TextView txtPosition;
        txtPosition = (TextView) findViewById(R.id.txtPosition);
        txtPosition.setText("Checklist ID = " + id);
    }

    @Override
    public void onLongClickListener(int newId, int oldId) {
        TextView txtPosition;
        txtPosition = (TextView) findViewById(R.id.txtPosition);
        txtPosition.setText("New = " + newId + "Old=" + oldId);
    }
}
