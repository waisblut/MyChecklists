package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.c_data.DSChecklist;
import com.waisblut.mychecklists.c_data.DSChecklistItem;

import java.util.LinkedList;


public class FragmentChecklist
        extends ListFragment
{
    private Checklist mCheckList;
    private DSChecklist dsChecklist;
    private DSChecklistItem dsChecklistItem;
    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    // TODO: Rename and change types of parameters
    public static FragmentChecklist newInstance(String param1, String param2)
    {
        FragmentChecklist fragment = new FragmentChecklist();
        //        Bundle args = new Bundle();
        //        args.putString(ARG_PARAM1, param1);
        //        args.putString(ARG_PARAM2, param2);
        //        fragment.setArguments(args);
        return fragment;
    }

    public FragmentChecklist()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            //            mParam1 = getArguments().getString(ARG_PARAM1);
            //            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        create_a_test();

        AdapterChecklist adapter = new AdapterChecklist(getActivity(), R.layout.fragment_checklist);
        setListAdapter(adapter);
        adapter.addAll(dsChecklist.getAllChecklists());

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        if (null != mListener)
        {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(dsChecklist.getAllChecklists()
                                                       .get(position)
                                                       .getId());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Checklist chk = (Checklist) getListView().getItemAtPosition(position);

                return true;
            }
        });
    }

    private void create_a_test()
    {
        LinkedList<ChecklistItem> list;
        ChecklistItem item;
        dsChecklist = new DSChecklist(getActivity());
        dsChecklistItem = new DSChecklistItem(getActivity());


        list = new LinkedList<>();
        item = new ChecklistItem("Acionar motor");
        list.add(item);
        item = new ChecklistItem("Testar turbina esquerda");
        list.add(item);
        item = new ChecklistItem("Testar turbina direita");
        list.add(item);
        item = new ChecklistItem("Decolar");
        list.add(item);

        mCheckList = new Checklist("Lista de Decolagem", list);
        dsChecklist.open();
        dsChecklist.create(mCheckList);
        dsChecklistItem.open();

        list = new LinkedList<>();
        item = new ChecklistItem("pegar mala");
        list.add(item);
        item = new ChecklistItem("encher de roupa");
        list.add(item);
        item = new ChecklistItem("fechar mala");
        list.add(item);

        mCheckList = new Checklist("Lista de Viagem", list);
        dsChecklist.create(mCheckList);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar vara");
        list.add(item);
        item = new ChecklistItem("Testar molinete");
        list.add(item);
        item = new ChecklistItem("Testar barco");
        list.add(item);
        item = new ChecklistItem("pescar");
        list.add(item);

        mCheckList = new Checklist("Lista de Pescaria", list);
        dsChecklist.create(mCheckList);


        list = new LinkedList<>();
        item = new ChecklistItem("pegar chuteira");
        list.add(item);
        item = new ChecklistItem("pegar bola");
        list.add(item);
        item = new ChecklistItem("pegar mochila");
        list.add(item);
        item = new ChecklistItem("jogar");
        list.add(item);

        mCheckList = new Checklist("Lista de Futebol", list);
        dsChecklist.create(mCheckList);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(long id);
    }
}
