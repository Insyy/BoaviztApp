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

    Integer responsesReceived = 0;
    int responsesNeeded = 5;
    RequestQueue queue;

    public RequestManager(FormularyActivity formularyActivity) {
        this.formularyActivity = formularyActivity;
        queue = Volley.newRequestQueue(formularyActivity);
    }

    boolean isNotConnected() {
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager) formularyActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if( activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting()) {
            formularyActivity.componentManager.refreshConnectionStatusLayout(false);
            return true;
        }

        InetAddress address = null;
        try {
            address = InetAddress.getByName("www.google.com");
        } catch (UnknownHostException e) {
            formularyActivity.componentManager.refreshConnectionStatusLayout(false);
            return true;
        }
        if (address.equals("")){
            formularyActivity.componentManager.refreshConnectionStatusLayout(false);
            return true;
        }else {
            formularyActivity.componentManager.refreshConnectionStatusLayout(true);
            return false;
        }
    }

    public void populateIfInternetAvailable() {
        new Thread(() -> {
            if (isNotConnected()) {
                return;
            }
            Looper.prepare();
            formularyActivity.componentManager.populate();
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
                                            responsesReceived ++;
                                        }
                                    });
                                },
                                error -> {
                                    formularyActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            formularyActivity.componentManager.showNetworkErrorToast();
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
                                            responsesReceived ++;
                                        }
                                    });
                                },
                                error -> {
                                    formularyActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            formularyActivity.componentManager.showNetworkErrorToast();
                                            formularyActivity.componentManager.stopProgressIndicator();
                                        }
                                    });
                                })
        );
    }
}
