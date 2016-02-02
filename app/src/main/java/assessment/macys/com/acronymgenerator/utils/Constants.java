package assessment.macys.com.acronymgenerator.utils;

import assessment.macys.com.acronymgenerator.network.NetworkManager;

/**
 * Created by Debasis on 2/1/2016.
 */
public class Constants {
    public interface IntentExtras {
        String ERROR_NO_NETWORK = "com.macys.acronymgenerator.appIntentExtras.ERROR_NO_NETWORK";
        String MESSAGE = "com.macys.acronymgenerator.appIntentExtras.MESSAGE";
        String JSON_RESPONSE = "com.macys.acronymgenerator.appIntentExtras.JSON_RESPONSE";
        String REQUEST_ID = "com.macys.acronymgenerator.appIntentExtras.ID";
        String INPUT_KEY_STRING = "com.macys.acronymgenerator.appIntentExtras.INPUT_KEY";
    }

    public interface IntentActions {
        String ACRONYM_DETAILS = NetworkManager.class.getName();
        String ACTION_ERROR = "com.macys.acronymgenerator.appIntentExtras.ACTION_ERROR";
        String ACTION_SUCCESS = "com.macys.acronymgenerator.appIntentExtras.ACTION_SUCCESS";
    }

    public interface ApiRequestId {
        int API_BASE_VALUE = 200;
        int GET_ACRONYM_DATA = API_BASE_VALUE + 1;

    }
}
