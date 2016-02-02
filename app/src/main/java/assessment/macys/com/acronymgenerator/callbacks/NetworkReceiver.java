package assessment.macys.com.acronymgenerator.callbacks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import assessment.macys.com.acronymgenerator.utils.Constants;

public class NetworkReceiver extends BroadcastReceiver {
    private ApiCallback apiCallback;

    public NetworkReceiver(ApiCallback apiCallback) {
        this.apiCallback = apiCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.IntentActions.ACTION_ERROR)) {
            apiCallback.onHttpResponseError(intent);
        } else if (intent.getAction().equals(Constants.IntentActions.ACTION_SUCCESS)) {
            apiCallback.onHttpRequestComplete(intent);
        }
    }
}
