package ie.droidfactory.drsensor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ie.droidfactory.drsensor.model.SensorType;
import ie.droidfactory.drsensor.sensors.MySensors;
import ie.droidfactory.drsensor.sensors.SensorListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSensors.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSensors#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSensors extends MainFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvInfo, tvRotat, tvAcceler, tvLight, tvStep;
    private Button btStart, btnStop;
    private ImageView imgIndicatorLight, imgIndicatorAccler, imgIndicatorRot, imgIndicatorStep;
//    private MySensors sensors;

    private SelectInterface mListener;


    private SensorListener accellerationCallback = new SensorListener() {


        @Override
        public void sensorCallback(boolean isOn, float sensorData) {
            tvAcceler.setText("average: "+String.valueOf(sensorData)+" m/s2");
            displayIndicator(SensorType.TYPE_ACCELEROMETER, sensorData);

        }
    };
    private SensorListener rotationCallback = new SensorListener() {

        @Override
        public void sensorCallback(boolean isOn, float sensorData) {
            tvRotat.setText("average: "+String.valueOf(sensorData)+" rad/s");
            displayIndicator(SensorType.TYPE_GYROSCOPE, sensorData);
        }
    };

    private SensorListener lightCallback = new SensorListener() {


        @Override
        public void sensorCallback(boolean isOn, float sensorData) {
            tvLight.setText("average: "+String.valueOf(sensorData)+" lx");
            displayIndicator(SensorType.TYPE_LIGHT, sensorData);
        }
    };

    private SensorListener stepCallback = new SensorListener() {
        @Override
        public void sensorCallback(boolean isOn, float sensorData) {
            tvStep.setText("total steps : "+String.valueOf(sensorData));
        }
    };

    public FragmentSensors() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSensors.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSensors newInstance(String param1, String param2) {
        FragmentSensors fragment = new FragmentSensors();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sensors, container, false);
        tvInfo = v.findViewById(R.id.fragment_sensors_text_info);
        tvAcceler = v.findViewById(R.id.fragment_sensors_text_acceleration);
        tvRotat = v.findViewById(R.id.fragment_sensors_text_rotation);
        tvLight = v.findViewById(R.id.fragment_sensors_text_light);
        tvStep = v.findViewById(R.id.fragment_sensors_text_step);
        btStart = v.findViewById(R.id.fragment_sensors_btn_start);
        btnStop = v.findViewById(R.id.fragment_sensors_btn_stop);
        imgIndicatorAccler = v.findViewById(R.id.fragment_sensors_img_acceleration);
        imgIndicatorRot = v.findViewById(R.id.fragment_sensors_img_rotation);
        imgIndicatorLight = v.findViewById(R.id.fragment_sensors_img_light);
        imgIndicatorStep = v.findViewById(R.id.fragment_sensors_img_step);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyButtons buttons = new MyButtons(getActivity());
        btStart.setOnClickListener(buttons);
        btnStop.setOnClickListener(buttons);
    }

    private void displayIndicator(SensorType sensorType, float value){
        int indicatorSize = 10;
        int x = 0;
        ImageView view = null;
        if(sensorType==SensorType.TYPE_LIGHT){
            x = (int)(Math.sqrt(value)/6);
            view = imgIndicatorLight;
        }
        if(sensorType==SensorType.TYPE_ACCELEROMETER){
            x = (int)(value*indicatorSize/sensorType.getMaxValue());
            view = imgIndicatorAccler;
        }
        if(sensorType==SensorType.TYPE_GYROSCOPE){
            x = (int)(value*indicatorSize/sensorType.getMaxValue());
            view = imgIndicatorRot;
        }
        if(view!=null) view.setImageResource(getImageIndicator(x));


    }

    private int getImageIndicator(int value){

        switch (value){
            case 0: return R.mipmap.ic_indicator_0;
            case 1: return R.mipmap.ic_indicator_1;
            case 2: return R.mipmap.ic_indicator_2;
            case 3: return R.mipmap.ic_indicator_3;
            case 4: return R.mipmap.ic_indicator_4;
            case 5: return R.mipmap.ic_indicator_5;
            case 6: return R.mipmap.ic_indicator_6;
            case 7: return R.mipmap.ic_indicator_7;
            case 8: return R.mipmap.ic_indicator_8;
            case 9: return R.mipmap.ic_indicator_9;
            case 10: return R.mipmap.ic_indicator_10;
            default: return R.mipmap.ic_indicator_10;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String sensorId) {
        if (mListener != null) {
            mListener.onSensorSelected(sensorId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectInterface) {
            mListener = (SelectInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SelectInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MyButtons implements View.OnClickListener{
        MySensors sensors;
        MyButtons(Activity activity){
            sensors = new MySensors(activity);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fragment_sensors_btn_start:
                    sensors.checkAcceleration(accellerationCallback);
                    sensors.checkRotation(rotationCallback);
                    sensors.checkLight(lightCallback);
                    sensors.checkSteps(stepCallback);
                    break;
                case R.id.fragment_sensors_btn_stop:
                    sensors.disableSensorManager(lightCallback, rotationCallback, accellerationCallback, stepCallback);
                    break;
            }
        }
    }

    private class MyAdapter extends BaseAdapter{

        ArrayList<SensorType> typeSensorList;

        MyAdapter(ArrayList<SensorType> sensorList){
            this.typeSensorList=sensorList;

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
