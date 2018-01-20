package ie.droidfactory.drsensor;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements SelectInterface {

    private final static String TAG = MainActivity.class.getSimpleName();
//    private TextView mTextMessage;//just temp control

    public final static String FRAG_MAIN="frag_main";
    private FragmentTransaction ft;
    private Bundle savedInstanceState;
    private static String mainFragmentId = FragmentUtils.FRAGMENT_SENSORS;
    private MainFragment mainFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(FragmentUtils.FRAGMENT_SENSORS);
                    createFragment(FragmentUtils.FRAGMENT_SENSORS);
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(FragmentUtils.FRAGMENT_DATA);
                    createFragment(FragmentUtils.FRAGMENT_DATA);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(FragmentUtils.FRAGMENT_HELP);
                    createFragment(FragmentUtils.FRAGMENT_HELP);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState=savedInstanceState;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragments(mainFragmentId);
    }

    private void loadFragments(String key){

        if(findViewById(R.id.main_fragment_container)!=null) {
            if(savedInstanceState!=null) return; // TODO: check is fragment is savedInstanceState
            if(mainFragment==null){
                ft = getSupportFragmentManager().beginTransaction();
                mainFragment = (MainFragment) setMainFragment(key);
                mainFragment.setSensorSelectCallback(this);
                ft.add(R.id.main_fragment_container, mainFragment, FRAG_MAIN).commit();
                getSupportFragmentManager().executePendingTransactions();
            }else{
                return;
            }
        }
    }

    private Fragment setMainFragment(String key){
        Toast.makeText(this, "set fragment: "+key, Toast.LENGTH_SHORT).show();
        Fragment frag;
        Bundle arg;
        switch (key){
            case FragmentUtils.FRAGMENT_SENSORS:
                frag = FragmentSensors.newInstance("sensor param test", null);
                break;
            case FragmentUtils.FRAGMENT_DATA:
                frag = FragmentDataList.newInstance(FragmentUtils.FRAGMENT_DATA, null);
                break;
            case FragmentUtils.FRAGMENT_HELP:
                frag = FragmentHelp. newInstance(null, null);
                break;
            case FragmentUtils.FRAGMENT_ABOUT:
                frag = FragmentAbout.newInstance(null, null);
                break;
            default:
                frag = FragmentSensors.newInstance("sensor param test", null);
                break;
        }
        return frag;
    }

    private void createFragment(String key){
        if(key == null) return;

        if(mainFragment!=null){
            ft = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager
                    .POP_BACK_STACK_INCLUSIVE);
            ft.remove(mainFragment);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
            mainFragment=null;
        }
        //create new fragment and display in empty main view
        ft = getSupportFragmentManager().beginTransaction();
        ft.disallowAddToBackStack();
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mainFragment = (MainFragment) setMainFragment(key);
        mainFragment.setFragmentSelectCallback(this);
        ft.replace(R.id.main_fragment_container, mainFragment, FRAG_MAIN).commit();

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp(){
        AlertDialog.Builder bld;

        bld = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomHoloDialog));

        bld.setTitle("Exit App");
        bld.setMessage("Are you sure you want to exit?");

        bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
        });
        bld.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { finish(); }
        });
        bld.setCancelable(true);
        bld.create().show();
    }

    @Override
    public void onSensorSelected(String sensorId) {

    }

    @Override
    public void onDataSelected(int dataId) {

    }


}
