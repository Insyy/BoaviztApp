package fr.univpau.dudesalonso.boaviztapp.dataVisualisation;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.univpau.dudesalonso.boaviztapp.DataVisualisationActivity;
import fr.univpau.dudesalonso.boaviztapp.R;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.ServerConfiguration;

public class PostServerRequest {

    RequestQueue queue;
    DataVisualisationActivity _c;
    String url = "https://uppa.api.boavizta.org/v1/server/?verbose=true&allocation=TOTAL";
    ServerConfiguration _config;


    public PostServerRequest(DataVisualisationActivity c, ServerConfiguration config) {
        queue = Volley.newRequestQueue(c);
        _c = c;
        _config = config;
    }

    public void sendRequestServer() {
        try {
            JSONObject jsonObject = new JSONObject(_config.getAsJson());
            queue.add(new JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    response -> {
                        try {
                            Log.d("sendRequestServer", response.toString());
                            JSONObject impacts = response.getJSONObject("impacts");
                            JSONObject verbose = response.getJSONObject("verbose");
                            List<GrapheDataSet> listGds = new ArrayList<>();
                            listGds.add(new GrapheDataSet(impacts, verbose, "gwp"));
                            listGds.add(new GrapheDataSet(impacts, verbose, "pe"));
                            listGds.add(new GrapheDataSet(impacts, verbose, "adp"));

                            _c.initCharts(listGds);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DialogGrapheManager.stopProgressIndicator();
                    },
                    error -> {
                        DialogGrapheManager.stopProgressIndicator();
                        DialogGrapheManager.showNetworkErrorToast((Activity) _c, this);
                    }));
        } catch (JSONException e) {
            DialogGrapheManager.stopProgressIndicator();
            DialogGrapheManager.showNetworkErrorToast((Activity) _c, this);
            e.printStackTrace();
        }

    }

}
