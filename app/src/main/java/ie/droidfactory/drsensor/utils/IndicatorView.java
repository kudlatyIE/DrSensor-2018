package ie.droidfactory.drsensor.utils;

import android.app.Activity;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ie.droidfactory.drsensor.model.SensorType;

/**
 * Created by kudlaty on 2018-01-18.
 * Not used....
 */

public class IndicatorView {

    private Activity activity;
    private LinearLayout layout;

    public IndicatorView(Activity activity){
        this.activity=activity;
        this.layout= new LinearLayout(activity);
    }

    public LinearLayout getLayout(SensorType sensorType, ArrayList<Float> list){


        return layout;
    }
}
