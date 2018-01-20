package ie.droidfactory.drsensor.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-01-09.
 */

public class MySensors {

    private final static String TAG = MySensors.class.getSimpleName();
    private SensorManager seseManager;
    private Sensor sensorLight, sensorAcc, sensorRotate, sensorStep;
//    private static MySensors sensors=null;
    private Context c;
    private float light, noise,accX, accY, accZ,rotX, rotY, rotZ;
    private double mini=0, max=0;
    private int stepMumber, stepInit = 0;;
    private long startTime=0;
    private ArrayList<Float> lightArr;
    private ArrayList<Xyz> accArr, rotateArr;
    private SensorEventListener listenerLight, listenerAcc, listenerRotate, listenerSteps;
    private SoundAnalizer sound;
    private Activity ac;

//    private TextView tvLight, tvAcc,tvNoise, tvRotate;


    public MySensors(Activity activity){
        this.ac=activity;
        this.c=activity.getApplicationContext();
        this.seseManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        for(Sensor sese: seseManager.getSensorList(Sensor.TYPE_ALL)){
            Log.i("SENSORS", sese.getName()+" STATUS: "+sese.getPower());
        }
//        if(MySensors.sensors ==null) MySensors.sensors = new MySensors(0,0,0,0);
    }

    /*
     * will use for create final average for each of sensor
     */
//    private MySensors(float light, float noise, float vibration, float other){
//        this.light=light;
//        this.noise=noise;
//    }

    public void  checkLight(SensorListener callback){
        lightArr = new ArrayList<Float>();

        listenerLight = new SensorEventListener(){

            @Override
            public void onSensorChanged(SensorEvent event) {
                float x;
                x=event.values[0];
//				Log.d("LIGHT", "light: "+x);
//				miniLight=x; maxLight=x;
                lightArr.add(x);

//                callback.sensorCallback(true,String.valueOf(x));
                callback.sensorCallback(true, round(x));
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorLight = seseManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensorLight!=null){
            seseManager.registerListener(listenerLight, sensorLight, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void checkSteps(SensorListener callback){
        startTime = System.currentTimeMillis();
        stepInit = 0;
        listenerSteps = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float steps[] = sensorEvent.values;

                if(steps.length>0){
                    if(stepInit<1) stepInit = (int) steps[0];
                }
                stepMumber = (int) steps[0] - stepInit;
//                callback.sensorCallback(true, String.valueOf(stepMumber));
                callback.sensorCallback(true, stepMumber);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorStep = seseManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensorStep!=null)
            seseManager.registerListener(listenerSteps, sensorStep, SensorManager.SENSOR_DELAY_UI);

    }

    public void checkAcceleration(SensorListener callback){
        callback.sensorCallback(false, 0);
        accArr = new ArrayList<Xyz>();

        listenerAcc = new SensorEventListener(){
            float x,y,z;
            @Override
            public void onSensorChanged(SensorEvent event) {
                x=Math.abs(event.values[0]);
                y=Math.abs(event.values[1]);
                z=Math.abs(event.values[2]);
                accArr.add(new Xyz(x,y,z));
//                display.setText("X: "+String.valueOf(x)+"\n"+
//                        "Y: "+String.valueOf(y)+"\n"+
//                        "Z: "+String.valueOf(z));
//                callback.sensorCallback(true, "X: "+String.valueOf(x)+"\n"+
//                        "Y: "+String.valueOf(y)+"\n"+
//                        "Z: "+String.valueOf(z));
//                String output = "ACC: "+String.valueOf(x)+"\n"+
//                        "Y: "+String.valueOf(y)+"\n"+
//                        "Z: "+String.valueOf(z);
//                Log.d(TAG, output);
                callback.sensorCallback(true, (round((x+y+z)/3)));
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorAcc = seseManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensorAcc!=null) seseManager.registerListener(listenerAcc, sensorAcc, SensorManager.SENSOR_DELAY_UI);
    }

    public void checkRotation(SensorListener callback){
        callback.sensorCallback(false, 0);
        rotateArr= new ArrayList<Xyz>();

        listenerRotate = new SensorEventListener(){
            float x,y,z;
            @Override
            public void onSensorChanged(SensorEvent event) {
                // TODO Auto-generated method stub
                x=Math.abs(event.values[0]);
                y=Math.abs(event.values[1]);
                z=Math.abs(event.values[2]);
                rotateArr.add(new Xyz(x,y,z));
//                display.setText("X: "+String.valueOf(x)+"\n"+
//                        "Y: "+String.valueOf(y)+"\n"+
//                        "Z: "+String.valueOf(z));
//                callback.sensorCallback(true,"X: "+String.valueOf(x)+"\n"+
//                        "Y: "+String.valueOf(y)+"\n"+
//                        "Z: "+String.valueOf(z));
//                String output = "GYRO: "+String.valueOf(x)+"\n"+
//                        "Y: "+String.valueOf(y)+"\n"+
//                        "Z: "+String.valueOf(z);
//                Log.d(TAG, output);
                callback.sensorCallback(true, round((x+y+z)/3));
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorRotate = seseManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(sensorRotate!=null) seseManager.registerListener(listenerRotate, sensorRotate, SensorManager.SENSOR_DELAY_UI);
    }

    public void checkNoise(TextView display){
        sound = new SoundAnalizer(ac,display);
        sound.getSoundLevel(SoundAnalizer.RECORD.START);

    }

    public boolean disableSensorManager(SensorListener light, SensorListener rotation, SensorListener acceleration, SensorListener step){
        boolean result=false;
        try{
            this.seseManager.unregisterListener(listenerLight, sensorLight);
//            tvLight.setText(getAverage1D("Light", lightArr));
            light.sensorCallback(true, getAverage1D("Light", lightArr));
            result=true;
        }catch(Exception ex){
            ex.printStackTrace();
            result=false;
        }
        try {
            this.seseManager.unregisterListener(listenerSteps, sensorStep);
            step.sensorCallback(true, getSteps());
            result = true;
        }catch (Exception ex){
            ex.printStackTrace();
            result = false;
        }
//        try{
////			this.seseManager.unregisterListener(listenerNoise, sensorNoise);
//            sound.getSoundLevel(SoundAnalizer.RECORD.STOP);
//            tvNoise.setText(getAverage1D("Noise",SoundAnalizer.noiseList));
////			ac.runOnUiThread(new Runnable(){
////
////				@Override
////				public void run() {
////					tvNoise.setText(getAverage1D("Noise",SoundAnalizer.noiseList));
////				}
////			});
//            result=true;
//            Log.i("SENSOR", "audio sensor listener disabled: "+result);
//        }catch(Exception ex){
//            Log.e("SENSOR", "audio sensor listener disabled: "+result);
//            ex.printStackTrace();
//            result=false;
//        }
        try{
            this.seseManager.unregisterListener(listenerAcc, sensorAcc);
            result=true;
//            tvAcc.setText(getAverage3D(accArr));
            acceleration.sensorCallback(true, getAverage3D(accArr));
        }catch(Exception ex){
//            Log.e("SENSOR", "vibration sensor listener disabled: "+result);
            ex.printStackTrace();
            result=false;
        }
        try{
            this.seseManager.unregisterListener(listenerRotate, sensorRotate);
            result=true;
//            tvRotate.setText(getAverage3D(rotateArr));
            rotation.sensorCallback(true, getAverage3D(rotateArr));
        }catch(Exception ex){
//            Log.e("SENSOR", "rotate sensor listener disabled: "+result);
            ex.printStackTrace();
            result=false;
        }

        return result;
    }

    private float getSteps(){
        long sec = (System.currentTimeMillis()-startTime)/1000;
        Log.d(TAG, "start time: "+startTime);
        Log.d(TAG, "total time: "+sec);
        float average = (float)stepMumber/sec;
        String result = "total steps: "+stepMumber+"\n"+
                "per sec: "+average;
//        return result;
        return stepMumber;
    }

    private float getAverage1D(String name, ArrayList<Float> list){
        float total=0;
        mini=list.get(0);
        max=list.get(0);
        for(float d: list){
            total=total+d;
            if(mini > d) mini = d;
            if(max < d) max= d;
        }
        Log.d("SENSOR", "ARR "+name+" total: "+total);
        Log.d("SENSOR", "ARR "+name+" size: "+list.size());
        Log.d("SENSOR", "ARR "+name+" average: "+total/list.size()+"\n MINI: "+mini+", MAX: "+max);
//        return "average: "+total/list.size()+"\n MINI: "+mini+", MAX: "+max;
        return round(total/list.size());
    }

    private float getAverage3D(ArrayList<Xyz> list){
        int size = list.size();
        float totalX=0, totalY=0, totalZ=0;
        float minX,minY,minZ, maxX,maxY, maxZ;
        minX=list.get(0).getX();
        minY=list.get(0).getY();
        minZ=list.get(0).getZ();
        maxX=list.get(0).getX();
        maxY=list.get(0).getY();
        maxZ=list.get(0).getZ();
        for(Xyz d:list){
            totalX = totalX+d.getX();
            totalY=totalY+d.getY();
            totalZ=totalZ+d.getZ();
            if(minX>d.getX()) minX=d.getX();
            if(maxX<d.getX()) maxX=d.getX();
            if(minY>d.getY()) minY=d.getY();
            if(maxY<d.getY()) maxY=d.getY();
            if(minZ>d.getZ()) minZ=d.getZ();
            if(maxZ<d.getZ()) maxZ=d.getZ();
        }
        Log.d("SENSOR", "ARR ACC X total: "+totalX);
        Log.d("SENSOR", "ARR ACC Y total: "+totalY);
        Log.d("SENSOR", "ARR ACC Y total: "+totalY);
        Log.d("SENSOR", "ARR ACC size: "+list.size());

        String output =  "X: "+totalX/size +"\n minX: "+minX+",maxX: "+maxY+"\n"+
                "Y: "+totalY/size +"\n minY: "+minY+",maxY: "+maxY+"\n"+
                "Z: "+totalZ/size +"\n minZ: "+minZ+",maxZ: "+maxZ;
        Log.d(TAG, "ACC/GYRO: "+output);
        return round(maxFloat(totalX, totalY, totalZ)/size);
    }

    private float maxFloat(float f1, float f2, float f3){
        float max;
        if(f1>f2) max=f1;
        else max=f2;
        if(max<f3) max = f3;
        return round(max);
    }

    private float round(float x){
        BigDecimal a = new BigDecimal(String.valueOf(x));
        return a.setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }



}

