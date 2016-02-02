package assessment.macys.com.acronymgenerator.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import assessment.macys.com.acronymgenerator.R;
import assessment.macys.com.acronymgenerator.adapters.AcronymListAdapter;
import assessment.macys.com.acronymgenerator.callbacks.ApiCallback;
import assessment.macys.com.acronymgenerator.callbacks.NetworkReceiver;
import assessment.macys.com.acronymgenerator.network.NetworkIntentService;
import assessment.macys.com.acronymgenerator.utils.Constants;

public class MainActivity extends AppCompatActivity implements ApiCallback,View.OnClickListener{
    private static final String TAG = MainActivity.class.getCanonicalName();
    private IntentFilter mFilter;
    private NetworkReceiver mNetworkReceiver;
    private ProgressDialog mProgressDialog;
    private ImageButton searchButton;
    private EditText keyWordEditText;
    private ListView acronymListView;
    private AcronymListAdapter mAcronymListAdapter;
    private List<String> acronymList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.searchButton = (ImageButton) findViewById(R.id.search_button);
        this.keyWordEditText = (EditText) findViewById(R.id.keyword_editText);
        this.acronymListView = (ListView) findViewById(R.id.acronym_listView);
        this.searchButton.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.acronymList = new ArrayList<>();
        this.mAcronymListAdapter = new AcronymListAdapter(this,this.acronymList);
        this.acronymListView.setAdapter(this.mAcronymListAdapter);
        mFilter = new IntentFilter();
        mFilter.addAction(Constants.IntentActions.ACTION_ERROR);
        mFilter.addAction(Constants.IntentActions.ACTION_SUCCESS);
        mNetworkReceiver = new NetworkReceiver(this);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        //mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("One moment please...");
        mProgressDialog.setCancelable(false);
        //mProgressDialog.show();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNetworkReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHttpResponseError(Intent intent) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        String message = intent.getStringExtra(Constants.IntentExtras.MESSAGE);
        Log.e(TAG, message);
        showErrorDialog(message);
    }

    @Override
    public void onHttpRequestComplete(Intent intent) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (intent.getIntExtra(Constants.IntentExtras.REQUEST_ID, -1) == Constants.ApiRequestId.GET_ACRONYM_DATA) {

            try {
                if(!this.acronymList.isEmpty())
                    this.acronymList.clear();
                //List<String> acronymList = new ArrayList<>();
                String acronymDetails = intent.getStringExtra(Constants.IntentExtras.MESSAGE);
                JSONArray jsonArray = new JSONArray(acronymDetails);
                String lfsJsonStringArray = jsonArray.optString(0);
                JSONObject jsonObject = new JSONObject(lfsJsonStringArray);
                JSONArray lfsJsonArray = jsonObject.getJSONArray("lfs");

                for(int i=0;i<lfsJsonArray.length();i++){
                    JSONObject lfJsonObject = lfsJsonArray.getJSONObject(i);
                    String acronym = lfJsonObject.getString("lf");
                    Log.d(TAG,"Acronym :"+acronym);
                    acronymList.add(acronym);
                }
                //Log.d(TAG,lfsJsonString);

                /*JsonReader jsonReader = new JsonReader( new StringReader(lfsJsonString));
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    while(jsonReader.hasNext()){
                        String name = jsonReader.nextName();
                        if(name.equals("lf")){
                            String acronym = jsonReader.nextString();
                            Log.d(TAG,"Acronym :"+acronym);
                            this.acronymList.add(acronym);
                        }
                    }
                    jsonReader.endObject();
                }
                jsonReader.endArray();*/
                //this.mAcronymListAdapter.setAcronymList(acronymList);
                this.mAcronymListAdapter.notifyDataSetChanged();
            }  /*catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
            }*/
            catch (JSONException ex){
                ex.printStackTrace();
                Log.e(TAG,ex.getMessage());
            }

        }
    }

    private void showErrorDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(Html.fromHtml(message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:
                hideSoftKeyboard();
                String keyWord = keyWordEditText.getText().toString();
                keyWord = keyWord.trim().replaceAll("\\s", keyWord);
                keyWord = keyWord.replaceAll("[^a-zA-Z]",keyWord);
                if(validateKeyWord(keyWord)){
                    mProgressDialog.show();
                    NetworkIntentService.getAcronymData(getApplicationContext(),keyWord);
                }
                break;
            default:
                break;
        }
    }

    private boolean validateKeyWord(String inputKeyWord){
        if(inputKeyWord != null && inputKeyWord.length()>0){
            return true;
        }
        else{
            showErrorDialog("Input text can not be blank");
        }
        return false;
    }

    private  void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
    }
}
