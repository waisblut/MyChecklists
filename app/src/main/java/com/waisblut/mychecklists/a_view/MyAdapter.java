package com.waisblut.mychecklists.a_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.c_data.DSChecklist;

import java.util.List;

public class MyAdapter
        extends ArrayAdapter<Checklist> {

    private final int myResource;
    private Context mContext;
    private List<Checklist> mList;

    public MyAdapter(final Context context, final int resource) {
        mContext = context;
        this.myResource = resource;
        DSChecklist ds = new DSChecklist(context);
        mList = ds.getAllChecklists();
        ds.close();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public Checklist getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        super.swapItems(positionOne, positionTwo);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Checklist checklist = getItem(position);

        // Setting the title view is straightforward
        viewHolder.titleView.setText(checklist.getName());
        viewHolder.subTitleView.setText("ID=" + checklist.getId());

        return view;
    }

    private View getWorkingView(final View convertView) {
        // The workingView is basically just the convertView re-used if possible
        // or inflated new if not possible
        View workingView;

        if (null == convertView) {
            final Context context = mContext;
            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(myResource, null);
        }
        else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView) {
        // The viewHolder allows us to avoid re-looking up view references
        // Since views are recycled, these references will never change
        final Object tag = workingView.getTag();
        ViewHolder viewHolder;


        if (null == tag || !(tag instanceof ViewHolder)) {
            viewHolder = new ViewHolder();

            viewHolder.titleView = (TextView) workingView.findViewById(R.id.txtTitle);
            viewHolder.subTitleView = (TextView) workingView.findViewById(R.id.txtSubTitle);

            workingView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    /**
     * ViewHolder allows us to avoid re-looking up view references
     * Since views are recycled, these references will never change
     */
    private static class ViewHolder {
        public TextView titleView;
        public TextView subTitleView;
    }
}
