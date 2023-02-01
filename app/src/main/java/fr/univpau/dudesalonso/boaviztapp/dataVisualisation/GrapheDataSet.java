package fr.univpau.dudesalonso.boaviztapp.dataVisualisation;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GrapheDataSet {

    private List<String> _topDataSet = new ArrayList<>();
    private List<String> _bottomDataSet = new ArrayList<>();;

    private String _grapheName;
    private String _usage;
    private String _manufacturing;
    private String _mRAM;
    private String _mCPU;
    private String _mSDD;
    private String _mHDD;
    private String _mOther;

    private String _mTotal;

    public GrapheDataSet(JSONObject impacts, JSONObject verbose, String grapheName) throws JSONException {
        _grapheName = grapheName;

        _usage = valueAssigmentTopSet(impacts,"use");
        _manufacturing = valueAssigmentTopSet(impacts,"manufacture");
        _topDataSet.add(_usage);
        _topDataSet.add(_manufacturing);

        //_mRAM = Float.parseFloat(verbose.getJSONObject("RAM-1").getJSONObject("manufacture_impacts").getJSONObject(_grapheName).get("value").toString());
        _mRAM = valueAssigmentBottomSet(verbose,"RAM-1","manufacture_impacts");
        _mCPU = valueAssigmentBottomSet(verbose,"CPU-1","manufacture_impacts");
        _mSDD = valueAssigmentBottomSet(verbose,"SSD-1","manufacture_impacts");
        _mHDD = valueAssigmentBottomSet(verbose,"HDD-1","manufacture_impacts");
        _mOther = String.valueOf(Float.parseFloat(get_manufacturing()) - Float.parseFloat(get_mRAM()) - Float.parseFloat(get_mCPU()) - Float.parseFloat(get_mSDD())- Float.parseFloat(get_mHDD()));
        _mTotal =  String.valueOf(Float.parseFloat(_mRAM) + Float.parseFloat(_mCPU) + Float.parseFloat(_mSDD) + Float.parseFloat(_mHDD) + Float.parseFloat(_mOther) + Float.parseFloat(_usage) + Float.parseFloat(_manufacturing));

        _bottomDataSet.add(_mRAM);
        _bottomDataSet.add(_mCPU);
        _bottomDataSet.add(_mSDD);
        _bottomDataSet.add(_mHDD);
        _bottomDataSet.add(_mOther);

    }

    public String valueAssigmentTopSet(JSONObject impacts, String gwpType) throws JSONException {
        String value = impacts.getJSONObject(_grapheName).get(gwpType).toString();
        if(value.equals("not implemented"))
            return "0.0";
        return value;
    }

    public String valueAssigmentBottomSet(JSONObject verbose, String stringObject1, String stringObject2) throws JSONException {
        String value = verbose.getJSONObject(stringObject1).getJSONObject(stringObject2).getJSONObject(_grapheName).get("value").toString();
        if(value.equals("not implemented"))
            return "0.0";
        return value;
    }

    public String get_grapheName() {
        return _grapheName;
    }

    public String get_usage() {
        return _usage;
    }

    public String get_manufacturing() {
        return _manufacturing;
    }

    public String get_mRAM() {
        return _mRAM;
    }

    public String get_mCPU() {
        return _mCPU;
    }

    public String get_mSDD() {
        return _mSDD;
    }

    public String get_mHDD() {
        return _mHDD;
    }

    public String get_mOther() {
        return _mOther;
    }

    public String get_mTotal() {
        return _mTotal;
    }

    public List<String> get_topDataSet() {
        return _topDataSet;
    }

    public void set_topDataSet(List<String> _topDataSet) {
        this._topDataSet = _topDataSet;
    }

    public List<String> get_bottomDataSet() {
        return _bottomDataSet;
    }

    public void set_bottomDataSet(List<String> _bottomDataSet) {
        this._bottomDataSet = _bottomDataSet;
    }


}
