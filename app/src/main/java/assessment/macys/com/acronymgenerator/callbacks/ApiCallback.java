package assessment.macys.com.acronymgenerator.callbacks;

import android.content.Intent;

/**
 * Created by Debasis on 2/1/2016.
 */
public interface ApiCallback {
    /**
     * This method is called from broadcast receiver in case of an error
     *
     * @param intent
     */
    void onHttpResponseError(Intent intent);

    /**
     * This method is called from broadcast receiver when data has been loaded
     */
    void onHttpRequestComplete(Intent intent);
}
