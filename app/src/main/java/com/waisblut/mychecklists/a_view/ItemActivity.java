package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.waisblut.mychecklists.R;
import com.waisblut.mychecklists.b_model.Checklist;
import com.waisblut.mychecklists.b_model.ChecklistItem;
import com.waisblut.mychecklists.c_data.DSChecklistItem;
import com.waisblut.mychecklists.e_util.Logger;

import java.util.ArrayList;
import java.util.Locale;

public class ItemActivity
        extends Activity
        implements TextToSpeech.OnInitListener {
    private DynamicListView mMyListView;
    private Button btnNext;
    private Checklist mChecklist;
    private static int mCounter = 0;
    private static TextToSpeech mTts;
    private static DSChecklistItem mDsChecklistItem;
    private static AudioManager mAudioManager;
    private static int mOldVolume;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamiclistview);

        if (savedInstanceState == null) {
            mMyListView = (DynamicListView) findViewById(R.id.fragment_checklist_listview);
            btnNext = (Button) findViewById(R.id.btnNext);
            mTts = new TextToSpeech(this, this);
            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    setUpVoiceRecon();
                }

                @Override
                public void onDone(String utteranceId) {
                    setUpVoiceRecon();
                }

                @Override
                public void onError(String utteranceId) {

                }
            });
            mCounter = 0;
        }
    }

    @Override
    public void onDestroy() {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mOldVolume, 0);
        }

        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = mTts.setLanguage(new Locale("pt", "BR"));

            setUpMaxVolume();

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Logger.log('e', "This Language is not supported");
            }
            else {
                if (mChecklist != null) {
                    speakOut(mChecklist.getName());
                }
            }

        }
        else {
            Logger.log('e', "Initilization Failed!");
        }

    }

    private void setUpMaxVolume() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mOldVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int amStreamMusicMaxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, amStreamMusicMaxVol, 0);
    }

    protected static void speakOut(final String text) {
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void setUpVoiceRecon() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt_BR");//TODO tirar HARDCODED
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Portugês - BR");//TODO tirar HARDCODED
        do {
            boolean b = mTts.isSpeaking();
        } while (mTts.isSpeaking());
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle b = getIntent().getExtras();

        mChecklist = (Checklist) b.getSerializable("EXTRA");//TODO tirar HARDCODED

        final MyListAdapter adapter = new MyListAdapter(this, mChecklist);

        mMyListView = (DynamicListView) findViewById(R.id.fragment_checklist_listview);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCounter == mMyListView.getCount() - 1) {
                    mCounter = 0;
                }
                else {
                    mCounter++;
                }
                adapter.setSelectedPosition(mCounter);

            }
        });

        setAdapter(adapter);
        setDragAndDrop(adapter);
        mMyListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        Logger.log('e', String.valueOf(mMyListView.getCheckedItemCount()));

        mMyListView.setOnItemClickListener(new MyOnItemClickListener(mMyListView, adapter));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (String s : matches) {
                Logger.log('e', s);
            }
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
                        .setInitialDelayMillis(100);
        mMyListView.setAdapter(animationAdapter);
    }

    private void setDragAndDrop(MyListAdapter adapter) {
        mMyListView.enableDragAndDrop();
        //mMyListView.setDraggableManager(new TouchViewDraggableManager(R.id.list_row_draganddrop_touchview));
        mMyListView.setOnItemMovedListener(new MyOnItemMovedListener(this, adapter));
        mMyListView.setOnItemLongClickListener(new MyOnItemLongClickListener(mMyListView));
    }

    private class MyListAdapter
            extends ArrayAdapter<ChecklistItem> {

        private final Context mContext;

        MyListAdapter(final Context context, Checklist checklist) {
            mContext = context;
            mDsChecklistItem = new DSChecklistItem(context);
            addAll(mDsChecklistItem.getAllChecklistItems(checklist));
            mDsChecklistItem.close();
        }

        public void setSelectedPosition(int pos) {
            mCounter = pos;
            notifyDataSetChanged();
            speakOut(getItem(pos).getName());
            setUpVoiceRecon();
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

            // change the row color based on selected state
            if (mCounter == position) {
                //view.setBackgroundColor(Color.CYAN);
                view.setBackgroundResource(R.drawable.selection_selected);
            }
            else {
                //view.setBackgroundColor(Color.LTGRAY);
                view.setBackgroundResource(R.drawable.selection_normal);

            }

            ChecklistItem item = getItem(position);

            ((TextView) view.findViewById(R.id.txtItem)).setText(item.getName());
            ImageView img = (ImageView) view.findViewById(R.id.imgItemState);
            switch (item.getState()) {
            case EMPTY:
                img.setImageResource(R.drawable.not_ok);
                img.setVisibility(View.INVISIBLE);
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

    private class MyOnItemClickListener
            implements AdapterView.OnItemClickListener {
        private final DynamicListView mListView;

        private MyListAdapter adapter;

        MyOnItemClickListener(final DynamicListView listView, final MyListAdapter adapter) {
            this.mListView = listView;
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (this.mListView != null) {
                adapter.setSelectedPosition(position);
            }
        }
    }

    private class MyOnItemLongClickListener
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
            }
            return true;
        }
    }

    private class MyOnItemMovedListener
            implements OnItemMovedListener {

        private final MyListAdapter mAdapter;
        private final Context mContext;

        MyOnItemMovedListener(Context context, final MyListAdapter adapter) {
            mAdapter = adapter;
            mContext = context;

        }

        @Override
        public void onItemMoved(final int originalPosition, final int newPosition) {
            mDsChecklistItem = new DSChecklistItem(mContext);

            mDsChecklistItem.updateOrder(mAdapter.getItems());

            mDsChecklistItem.close();
            mAdapter.setSelectedPosition(mCounter = 0);

            mAdapter.notifyDataSetChanged();
        }
    }
}
