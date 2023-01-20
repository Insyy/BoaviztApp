package fr.univpau.dudesalonso.boaviztapp.ImpactVisualizer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.univpau.dudesalonso.boaviztapp.Graphe;
import fr.univpau.dudesalonso.boaviztapp.formulary.serverconfig.ServerConfiguration;

public class PostServerRequest {

    RequestQueue queue;
    Graphe _c;
    String url = "https://uppa.api.boavizta.org/v1/server/?verbose=true&allocation=TOTAL";


    public PostServerRequest(Graphe c) {
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
                            Log.d("sendRequestServer", response.toString());
                            JSONObject impacts = response.getJSONObject("impacts");
                            JSONObject verbose = response.getJSONObject("verbose");
                            List<GrapheDataSet> listGds = new ArrayList<>();
                            listGds.add(new GrapheDataSet(impacts, verbose, "gwp"));
                            listGds.add(new GrapheDataSet(impacts, verbose, "pe"));
                            listGds.add(new GrapheDataSet(impacts, verbose, "adp"));

                            _c.initCharts(listGds);

                        } catch (JSONException e) {
                            _c.setOfflineIcon();
                            e.printStackTrace();
                        }
                        _c.setOnlineIcon();
                        _c.stopProgressIndicator();
                    },
                    error -> {
                        _c.setOfflineIcon();
                        _c.stopProgressIndicator();
                        _c.handleErrorRequest();
                    }));
        } catch (JSONException e) {
            _c.stopProgressIndicator();
            _c.setOfflineIcon();
            e.printStackTrace();
        }

    }

}
