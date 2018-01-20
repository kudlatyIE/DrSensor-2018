package ie.droidfactory.drsensor.sensors;

/**
 * Created by kudlaty on 2018-01-12.
 */

public interface SensorListener {

    void sensorCallback(boolean isOn, float sensorData);
}
