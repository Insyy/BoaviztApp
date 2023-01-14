package fr.univpau.dudesalonso.boaviztapp.ImpactVisualizer;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GrapheDataSet {

    private List<Float> _topDataSet = new ArrayList<>();
    private List<Float> _bottomDataSet = new ArrayList<>();;

    private String _grapheName;
    private Float _usage;
    private Float _manufacturing;
    private Float _mRAM;
    private Float _mCPU;
    private Float _mSDD;
    private Float _mHDD;
    private Float _mOther;

    public GrapheDataSet(JSONObject impacts, JSONObject verbose, String grapheName) throws JSONException {
        _grapheName = grapheName;
        _usage = Float.parseFloat(impacts.getJSONObject(_grapheName).get("use").toString());
        _manufacturing = Float.parseFloat(impacts.getJSONObject(_grapheName).get("manufacture").toString());
        _topDataSet.add(_usage);
        _topDataSet.add(_manufacturing);
        _mRAM = Float.parseFloat(verbose.getJSONObject("RAM-1").getJSONObject("manufacture_impacts").getJSONObject(_grapheName).get("value").toString());
        _mCPU = Float.parseFloat(verbose.getJSONObject("CPU-1").getJSONObject("manufacture_impacts").getJSONObject(_grapheName).get("value").toString());
        _mSDD = Float.parseFloat(verbose.getJSONObject("SSD-1").getJSONObject("manufacture_impacts").getJSONObject(_grapheName).get("value").toString());
        _mHDD =Float.parseFloat(verbose.getJSONObject("CASE-1").getJSONObject("manufacture_impacts").getJSONObject(_grapheName).get("value").toString());
        _mOther = get_manufacturing() - (get_mRAM() + get_mCPU() + get_mSDD() + get_mHDD());

        _bottomDataSet.add(_mRAM);
        _bottomDataSet.add(_mCPU);
        _bottomDataSet.add(_mSDD);
        _bottomDataSet.add(_mHDD);
        _bottomDataSet.add(_mOther);

    }

    public String get_grapheName() {
        return _grapheName;
    }

    public Float get_usage() {
        return _usage;
    }

    public Float get_manufacturing() {
        return _manufacturing;
    }

    public Float get_mRAM() {
        return _mRAM;
    }

    public Float get_mCPU() {
        return _mCPU;
    }

    public Float get_mSDD() {
        return _mSDD;
    }

    public Float get_mHDD() {
        return _mHDD;
    }

    public Float get_mOther() {
        return _mOther;
    }

    public List<Float> get_topDataSet() {
        return _topDataSet;
    }

    public void set_topDataSet(List<Float> _topDataSet) {
        this._topDataSet = _topDataSet;
    }

    public List<Float> get_bottomDataSet() {
        return _bottomDataSet;
    }

    public void set_bottomDataSet(List<Float> _bottomDataSet) {
        this._bottomDataSet = _bottomDataSet;
    }


}
