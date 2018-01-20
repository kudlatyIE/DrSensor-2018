package ie.droidfactory.drsensor.model;

import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-01-13.
 */

public enum SensorType {

    //motion sensors
    TYPE_ACCELEROMETER(true, 20),
    TYPE_ACCELEROMETER_UNCALIBRATED(false, 0),
    TYPE_GRAVITY(false, 0),
    TYPE_GYROSCOPE(true, 10),
    TYPE_GYROSCOPE_UNCALIBRATED(false, 0),
    TYPE_LINEAR_ACCELERATION(false, 0),
    TYPE_ROTATION_VECTOR(false, 0),
    TYPE_SIGNIFICANT_MOTION(false, 0),//not used
    TYPE_STEP_COUNTER(true, 5),
    TYPE_STEP_DETECTOR(false, 0),


    //position sensors
    TYPE_GAME_ROTATION_VECTOR(false, 0),
    TYPE_GEOMAGNETIC_ROTATION_VECTOR(false, 0),
    TYPE_MAGNETIC_FIELD(false, 0),
    TYPE_MAGNETIC_FIELD_UNCALIBRATED(false, 0),
    TYPE_ORIENTATION(false, 0),//deprecated
    TYPE_PROXIMITY(false, 0),

    //environment sensors
    TYPE_AMBIENT_TEMPERATURE(false, 0),
    TYPE_LIGHT(true, 4000),
    TYPE_PRESSURE(false, 0),
    TYPE_RELATIVE_HUMIDITY(false, 0),
    TYPE_TEMPERATURE(false, 0);

    private boolean isUsed;
    private float maxValue;
    SensorType(boolean isUsed, float max){
        this.isUsed=isUsed;
        this.maxValue = max;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public static ArrayList<SensorType> getSensorsList(){
        ArrayList<SensorType> list = new ArrayList<>();
        for(SensorType sensor: SensorType.values()){
            if(sensor.isUsed()) list.add(sensor);
        }
        return list;
    }
}
