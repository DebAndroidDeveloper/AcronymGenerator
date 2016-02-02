package assessment.macys.com.acronymgenerator.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import assessment.macys.com.acronymgenerator.BuildConfig;
import assessment.macys.com.acronymgenerator.utils.Constants;

/**
 * Created by Debasis on 2/1/2016.
 */
public class NetworkManager {
    private static final String TAG = NetworkManager.class.getCanonicalName();
    private Context mContext;
    private int mRequestId = -1;

    public NetworkManager(Context context){
        this.mContext = context;
    }

    public String getHttpResponse(Intent intent){
        String responseString = "";
        mRequestId = intent.getIntExtra(Constants.IntentExtras.REQUEST_ID, -1);
        try {
            OkHttpClient httpClient = new OkHttpClient();
            String baseUrl = BuildConfig.BASE_ACRONYM_API_URL;
            String abbreviatedWord = intent.getStringExtra(Constants.IntentExtras.INPUT_KEY_STRING);
            StringBuilder sbUrl = new StringBuilder(baseUrl);
            sbUrl.append("?sf=");
            sbUrl.append(abbreviatedWord);
            Request request = new Request.Builder().
                url(sbUrl.toString()).
                build();

            Response   response = httpClient.newCall(request).execute();
            responseString = response.body().string();
            if(!response.isSuccessful()){
                handleError(response.code(),request.uri() +"",responseString);
            }else{
                broadcast(Constants.IntentActions.ACTION_SUCCESS,responseString);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            broadcast(Constants.IntentActions.ACTION_ERROR,e.getMessage());
        }

        return responseString;
    }

    private void handleError(int responseCode, String uri, String responseString)  {
        Log.e(TAG," Error executing " + uri + " \nresponse code " + responseCode + " \nMessage: " + responseString);

        broadcast(Constants.IntentActions.ACTION_ERROR, TAG + " <p><b>Error executing:</b> " + uri + " </p><p><b>Response code:</b> " + responseCode + " </p><b>Message:</b> " + responseString);

    }

    /**
     * Util method to broadcast the result
     *
     * @param action
     * @param message
     */
    private void broadcast(String action, String message) {

        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(Constants.IntentExtras.MESSAGE, message);
        intent.putExtra(Constants.IntentExtras.REQUEST_ID, mRequestId);
        mContext.sendBroadcast(intent);
    }

}
