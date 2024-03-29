package ie.droidfactory.drsensor.sensors;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kudlaty on 2018-01-09.
 */

class SoundAnalizer {

    private MediaRecorder mRecord;
    private AudioRecord audio;
    //	private Context c;
    private static final int SAMPLE_RATE = 8000, SAMPLE_DELAY=75;
    private static int [] mSamplesRate = new int[] {8000, 11025, 22050, 44100};
    private int bufferSize;
    private double lastLevel;
    private Thread thread;
    private Runnable ui;
    public enum RECORD {START,STOP};
    private String result="empty";
    private TextView tvNoise;
    public static ArrayList<Float> noiseList;
    private Activity ac;

    public SoundAnalizer(Activity activity, TextView tv){
        this.ac=activity;
//		this.c=context;
        this.tvNoise=tv;
//		this.bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//		this.audio = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,AudioFormat.CHANNEL_IN_MONO,
//								AudioFormat.ENCODING_PCM_16BIT,bufferSize);
        this.audio = findAudioRecord();
        this.noiseList = new ArrayList<Float>();
    }

    public String getSoundLevel(RECORD rec){

        if(audio !=null){
            switch(rec){
                case START:
                    audio.startRecording();
                    Log.d("AUDIO", "START RECORDING!");
                    thread = new Thread(new Runnable(){

                        @Override
                        public void run() {
                            while (thread!=null && !thread.isInterrupted()){
                                try{
                                    Thread.sleep(SAMPLE_DELAY);

                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                noiseList.add(getLevel(audio));
                                try{
                                    ac.runOnUiThread(ui= new Runnable(){

                                        @Override
                                        public void run() {
                                            result = String.valueOf(getLevel(audio));
//										Log.d("AUDIO", "SOUND CAPTURED: "+result);
                                            tvNoise.setText(result);
                                        }
                                    });
                                }catch(Exception ex){
                                    Log.w("SENSOR", "ui runnable is interupted: "+ex.getMessage());
                                    tvNoise.removeCallbacks(ui);
                                }

                            }
                        }

                    });
                    thread.start();
                    Log.d("AUDIO", "AUDIO THREAD START !");
                    break;
                case STOP:
                    try{
                        tvNoise.removeCallbacks(null);
//					tvNoise.setText(result);
//					tvNoise.notify();
                        Log.d("AUDIO", "AUDIO THREAD INTERUPTED !");
                        if(thread!=null && thread.isAlive()) thread.interrupt();
                        thread=null;
                        audio.stop();
                        audio.release();

                    }catch(Exception ex){
                        Log.w("SENSORS", ex.getMessage());
                    }
                    break;
            }
        }else{
            result = "AudioRecorder state NULL: "+(audio!=null);
        }
        return result;
    }

    private float getLevel(AudioRecord ar){

        short[] buffer = new short[bufferSize];
        int result=1, sumLevel=0;
        if(ar!=null){
            result = ar.read(buffer, 0, bufferSize);
            for(int i=0;i<result;i++){
                sumLevel +=buffer[i];
            }
        }
        return Math.abs(sumLevel/result);
    }


    private AudioRecord findAudioRecord(){
        int[] mAudioFormat = new int[] {AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT};
        int aFormat = AudioFormat.ENCODING_PCM_16BIT;
        int[] mAudioChanels = new int[] {AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.CHANNEL_CONFIGURATION_MONO};
        for(int rate: mSamplesRate){
            for(int af: mAudioFormat){
                Log.d("AUDIO", "ENCODING_PCM_16BIT:  "+af);
                for(int ach: mAudioChanels){
                    try{
                        this.bufferSize = AudioRecord.getMinBufferSize(rate, ach, af);
                        Log.i("AUDIO", "Attempting rate: "+rate+" Hz"+ ", Format: "+af+ ", chanell: "+ach);
                        if(bufferSize != AudioRecord.ERROR_BAD_VALUE){
                            AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,rate,ach,af,bufferSize);
                            if(ar.getState() == AudioRecord.STATE_INITIALIZED) {
                                Log.v("AUDIO", "AudioRecord INITIALIZED! "+ar.getState());
                                Log.v("AUDIO", "Found rate: "+rate+" Hz"+ ", Format: "+af+ ", chanell: "+ach);
                                result = String.valueOf(getLevel(ar));
                                Log.d("AUDIO", "SOUND CAPTURED: "+result);
                                return ar;
                            }else{
                                Log.v("AUDIO", "AudioRecord UNINITIALIZED! "+ar.getState());
                            }
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }

        Log.i("AUDIO", "AudioRecord Not Found!");
        return null;
    }


}
