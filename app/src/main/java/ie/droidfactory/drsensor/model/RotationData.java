package ie.droidfactory.drsensor.model;

import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-01-09.
 */

public class RotationData {

    private ArrayList<Double> rotationX;
    private ArrayList<Double> rotationY;
    private ArrayList<Double> rotationZ;
    private double threshold; //minimum acceleration value to record
    private long measurementTotalTime; //total time in miliseconds
    private int samplingFrequency; //sampling per seconds


}