package ie.droidfactory.drsensor;

import android.support.v4.app.Fragment;

/**
 * Created by kudlaty on 2018-01-09.
 */

public class MainFragment extends Fragment implements SelectInterface {

    SelectInterface fragmentSelectedCallback, sensorSelectCallback, dataSelectCallback;

    public void setFragmentSelectCallback(SelectInterface listener){
        this.fragmentSelectedCallback=listener;
    }

    public void setSensorSelectCallback(SelectInterface listener){
        this.sensorSelectCallback=listener;
    }

    public void setDataSelectCallback(SelectInterface listener){
        this.dataSelectCallback=listener;
    }




    @Override
    public void onSensorSelected(String sensorId) {

    }

    @Override
    public void onDataSelected(int dataId) {

    }
}

