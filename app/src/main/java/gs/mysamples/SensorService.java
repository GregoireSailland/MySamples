package gs.mysamples;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SensorService extends Service implements SensorEventListener{
    static float lSensormaxvalue=0;

    public SensorService() {}
    private SensorManager sensorManager;    // this instance of SensorManager class will be used to get a reference to the sensor service.
    private Sensor mSensor,lSensor;         // this instance of Sensor class is used to get the sensor we want to use.
    private float[] mGravity;
    public static float mAccel;
    public static float light;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        Log.d("MyService", "onCreate");
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);          // get an instance of the SensorManager class, lets us access sensors.
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);    // get Accelerometer sensor from the list of sensors.
        lSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);            // get light sensor from the list of sensors.
        lSensormaxvalue = lSensor.getMaximumRange();
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d("MyService", "onDestroy");
        //sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        //sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ALL));
        sensorManager=null;
        //player.stop();
    }
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("MyService", "onStartCommand");
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);          // get an instance of the SensorManager class, lets us access sensors.
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);    // get Accelerometer sensor from the list of sensors.
        lSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);            // get light sensor from the list of sensors.
        sensorManager.registerListener( this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener( this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), sensorManager.SENSOR_DELAY_GAME);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        return START_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Log.d("sensorService", "onSensorChanged.");
        //Toast.makeText(SensorService.this, "onSensorChanged", Toast.LENGTH_SHORT).show();

        /* check sensor type */
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

            mGravity = sensorEvent.values.clone();
            // assign directions
            float x=sensorEvent.values[0];
            float y=sensorEvent.values[1];
            float z=sensorEvent.values[2];

            float x1=sensorEvent.values[0];
            float y1=sensorEvent.values[1];
            float z1=sensorEvent.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);      // we calculate the length of the event because these values are independent of the co-ordinate system.
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel*0.9f + delta;
            if(mAccel > 3)
            {
                Toast.makeText(SensorService.this, "Movement Detected.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(sensorEvent.sensor.getType()==Sensor.TYPE_LIGHT){
            light = sensorEvent.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
