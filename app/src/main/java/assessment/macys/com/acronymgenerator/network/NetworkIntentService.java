package assessment.macys.com.acronymgenerator.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import assessment.macys.com.acronymgenerator.utils.CommonUtils;
import assessment.macys.com.acronymgenerator.utils.Constants;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class NetworkIntentService extends IntentService {
    private static final String TAG = NetworkIntentService.class.getCanonicalName();

    public NetworkIntentService() {
        super("NetworkIntentService");
    }
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void getAcronymData(Context context,String abbreviatedText) {
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentExtras.INPUT_KEY_STRING, abbreviatedText);
        intent.putExtra(Constants.IntentExtras.REQUEST_ID, Constants.ApiRequestId.GET_ACRONYM_DATA);
        startApiService(context,intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            NetworkManager networkManager = new NetworkManager(this);
            networkManager.getHttpResponse(intent);
        }
    }

    private static boolean startApiService(Context context, Intent intent) {
        if (CommonUtils.isNetworkAvailable(context)) {
            intent.setClass(context, NetworkIntentService.class);
            context.startService(intent);
            return true;
        } else {
            Intent errIntent = new Intent();
            errIntent.setAction(Constants.IntentActions.ACTION_ERROR);
            errIntent.putExtra(Constants.IntentExtras.MESSAGE, Constants.IntentExtras.ERROR_NO_NETWORK);
            context.sendBroadcast(intent);
            return false;
        }
    }
}
