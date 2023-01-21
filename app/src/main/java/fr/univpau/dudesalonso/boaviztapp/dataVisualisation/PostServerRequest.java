package fr.univpau.dudesalonso.boaviztapp.dataVisualisation;

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
import fr.univpau.dudesalonso.boaviztapp.formulary.serverConfig.ServerConfiguration;

public class PostServerRequest {

    RequestQueue queue;
    DataVisualisationActivity _c;
    String url = "https://uppa.api.boavizta.org/v1/server/?verbose=true&allocation=TOTAL";


    public PostServerRequest(DataVisualisationActivity c) {
        queue = Volley.newRequestQueue(c);
        _c =c;
    }

    public void sendRequestServer(ServerConfiguration  config) {
        try {
            JSONObject jsonObject = new JSONObject(config.getAsJson());
            queue.add(new JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    response -> {
                        try {
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
                        _c.stopProgressIndicator();
                    },
                    error -> {
                        _c.stopProgressIndicator();
                        _c.handleErrorRequest();
                    }));
        } catch (JSONException e) {
            _c.stopProgressIndicator();
            e.printStackTrace();
        }

    }

}
