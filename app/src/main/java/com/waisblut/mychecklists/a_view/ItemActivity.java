package com.waisblut.mychecklists.a_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
import com.waisblut.mychecklists.d_enum.EnumChecklistItemState;
import com.waisblut.mychecklists.e_util.Logger;

import java.util.ArrayList;
import java.util.List;
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
    private static boolean mHasSpokenTitle = false;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamiclistview);

        if (savedInstanceState == null) {
            mMyListView = (DynamicListView) findViewById(R.id.fragment_checklist_listview);
            btnNext = (Button) findViewById(R.id.btnNext);
            mHasSpokenTitle = false;
            mCounter = 0;
        }

        mTts = new TextToSpeech(this, this);
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

        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.destroy();
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
                if (mChecklist != null && !mHasSpokenTitle) {
                    mHasSpokenTitle = true;
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

    @SuppressWarnings("deprecation")
    protected void speakOut(final String text) {
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void setUpVoiceRecon() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                         RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                                         "pt_BR");//TODO tirar HARDCODED
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                                         "Portugês - BR");//TODO tirar HARDCODED
        do {
            mTts.isSpeaking();
        } while (mTts.isSpeaking());
        //startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

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


        mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                //setUpVoiceRecon();
            }

            @Override
            public void onDone(String utteranceId) {
                //setUpVoiceRecon();
            }

            @Override
            public void onError(String utteranceId) {

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
        //if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
        //            List<String> strOk = new ArrayList<>();
        //            strOk.add("ok");
        //            strOk.add("oque");
        //            strOk.add("oc");
        //            strOk.add("rock");
        //
        //            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        //            Logger.log('w', "ANTES: " + matches.size());
        //            for (String s : matches) {
        //                Logger.log('e', "ORIGINAL: " + s);
        //            }
        //
        //            matches.retainAll(strOk);
        //            Logger.log('w', "DEPOIS: " + matches.size());
        //

        //}
    }

    private EnumChecklistItemState validateVoice(ArrayList<String> voice) {
        //***OK - continua
        List<String> strOk = new ArrayList<>();
        strOk.add("ok");
        strOk.add("oque");
        strOk.add("oc");
        strOk.add("rock");

        //***PARAR - cancela a execucao da leitura da lista
        List<String> strParar = new ArrayList<>();
        strParar.add("parar");
        strParar.add("paraná");
        strParar.add("para");
        strParar.add("pára");
        strParar.add("não");
        strParar.add("nao");

        //***SKIP - pula o item, mas volta nele depois
        List<String> strPular = new ArrayList<>();
        strPular.add("poa");
        strPular.add("boa");
        strPular.add("porra");
        strPular.add("poá");
        strPular.add("rua");
        strPular.add("pua");
        strPular.add("pular");

        //***PAUSE - PAUSA a execucao da leitura da lista
        List<String> strPause = new ArrayList<>();
        strPause.add("esperar");
        strPause.add("espera");

        //VOLTAR - Volta para o item anterior (Nao seta este item e Des-Checka o anterior
        List<String> strBack = new ArrayList<>();
        strBack.add("voltar");
        strBack.add("vou lá");
        strBack.add("vou dar");
        strBack.add("volta");
        strBack.add("olá");


        //Se nao reconhecer nenhum dos comandos....abrir DIALOG com os botoes PROX(OK), ANT, PAUSE, STOP SKIP


        EnumChecklistItemState ret;
        ret = EnumChecklistItemState.OK;

        return ret;
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
                if (mSpeechRecognizer != null) {
                    mSpeechRecognizer.cancel();
                }
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

    private class SpeechRecognitionListener
            implements RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            Logger.log('w', "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            // TODO talvez eliminar essa linha e criar dialog
            Logger.log('w', "OnError");
            //TODO  Prepare a DIALOG
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onResults(Bundle results) {
            //Log.d(TAG, "onResults"); //$NON-NLS-1$
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            List<String> strOk = new ArrayList<>();
            strOk.add("ok");
            strOk.add("oque");
            strOk.add("oc");
            strOk.add("rock");

            Logger.log('w', "ANTES: " + matches.size());
            for (String s : matches) {
                Logger.log('e', "ORIGINAL: " + s);
            }

            matches.retainAll(strOk);
            Logger.log('w', "DEPOIS: " + matches.size());
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }
    }
}
