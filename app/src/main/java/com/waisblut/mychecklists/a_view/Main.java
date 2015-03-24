package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.waisblut.mychecklists.R;

public class Main
        extends Activity
        implements FragmentChecklist.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ListFragment frgChecklists = new FragmentChecklist();
        ListFragment frgChecklists = new FragmentChecklist();

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                                .replace(R.id.mainContainer, new FragmentChecklist())
                                .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(long id)
    {
        TextView txtPosition = new TextView(this);
        txtPosition = (TextView) findViewById(R.id.txtPosition);
        txtPosition.setText("Checklist ID = " + id);


    }
}
