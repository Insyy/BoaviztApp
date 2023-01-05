package fr.univpau.dudesalonso.boaviztapp.formulary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.univpau.dudesalonso.boaviztapp.FormularyActivity;
import fr.univpau.dudesalonso.boaviztapp.R;

public class RequestManager {

    FormularyActivity formularyActivity;
    RequestQueue queue;

    public RequestManager(FormularyActivity formularyActivity) {
        this.formularyActivity = formularyActivity;
        queue = Volley.newRequestQueue(formularyActivity);
    }

    boolean isConnected() {
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager) formularyActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public void populateIfInternetAvailable() {
        if (!isConnected()) {
            formularyActivity.componentManager.showNetworkErrorToast(R.string.internet_connection_not_available);
            return;
        }

        new Thread(() -> {
            Looper.prepare();
            try {
                InetAddress address = InetAddress.getByName("www.google.com");
                if (!address.equals(""))
                    formularyActivity.componentManager.populate();
            } catch (UnknownHostException e) {
                formularyActivity.componentManager.showNetworkErrorToast(R.string.internet_connection_not_available);
            }
        }).start();
    }

    public void sendGetArrayRequestAndPopulate(String url, int materialAutoCompleteId) {
        formularyActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                formularyActivity.componentManager.startProgressIndicator();
            }
        });

        queue.add(
                new JsonArrayRequest
                        (Request.Method.GET,
                                url,
                                null,
                                response -> {
                                    ArrayList<String> values = new ArrayList<>();
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            values.add(response.getString(i));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    formularyActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            formularyActivity.componentManager.populateAutocompleteDropdownValues(materialAutoCompleteId, values);
                                            formularyActivity.componentManager.stopProgressIndicator();
                                        }
                                    });
                                },
                                error -> {
                                    formularyActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            formularyActivity.componentManager.showNetworkErrorToast(R.string.network_error_message);
                                            formularyActivity.componentManager.stopProgressIndicator();
                                        }
                                    });
                                })
        );
    }

    public void sendGetObjectRequestAndPopulate(String url, int materialAutocompleteId) {
        formularyActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                formularyActivity.componentManager.startProgressIndicator();
            }
        });
        queue.add(
                new JsonObjectRequest
                        (Request.Method.GET,
                                url,
                                null,
                                response -> {
                                    Map<String, String> values = new HashMap<>();
                                    response.keys().forEachRemaining
                                            (s ->
                                                    {
                                                        try {
                                                            values.put(s, response.getString(s));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                            );
                                    if (formularyActivity.componentManager.countriesMap == null)
                                        formularyActivity.componentManager.countriesMap = values;
                                    formularyActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            formularyActivity.componentManager.populateAutocompleteDropdownValues(materialAutocompleteId, new ArrayList<>(values.keySet()));
                                            formularyActivity.componentManager.stopProgressIndicator();
                                        }
                                    });
                                },
                                error -> {
                                    formularyActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            formularyActivity.componentManager.showNetworkErrorToast(R.string.network_error_message);
                                            formularyActivity.componentManager.stopProgressIndicator();
                                        }
                                    });
                                })
        );
    }
}
