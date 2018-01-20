package ie.droidfactory.drsensor.utils;

import java.util.ArrayList;

import ie.droidfactory.drsensor.sensors.Xyz;

/**
 * Created by kudlaty on 2018-01-20.
 */

public class DataStorage {

    private ArrayList<Float> lightData;
    private ArrayList<Xyz> accData, rotateData;
    private String startDate;
    private long measurementTotalTime;
}
