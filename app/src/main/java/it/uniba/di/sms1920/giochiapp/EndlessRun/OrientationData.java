package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import it.uniba.di.sms1920.giochiapp.EndlessRun.Constants;

public class OrientationData implements SensorEventListener {

    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    private float[] accelOutput;
    private float[]  magOutput;

    private float [] orientation = new float[3];
    float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null;
    float[] getStartOrientation() {
        return startOrientation;
    }
    void newGame() {
        startOrientation = null;
    }

    OrientationData() {
        //viene istanziato il manager per i sensori. Si inizializzano l'accelerometro e il magnetometro
        manager = (SensorManager) Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        assert manager != null;
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    void register() {
        //vengono registrati i Sensori nel sensormanager
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //alla ricezione di un evento vengono ottenuti i valori relativi al sensore
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelOutput = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magOutput = event.values;
        }
        //se i valori ottenuti dai sensori fossero diversi da null si otterrebbe il booleano risultante da
        //getRotationManager: se un array di 3 float contiene il vettore geomagnetico nelle coordinate del dispositivo
        //il boolean è usato per ottenere l'orientation del SensorManager
        //l'orientation viene poi inizializzata come un vettore di float
        //viene effettuata, infine, una copia del vettore  per rendere effettivo il passaggio dei valori con il passaggio di sensore
        if (accelOutput != null  && magOutput != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);
            if ( success) {
                SensorManager.getOrientation(R, orientation);
                if (startOrientation == null) {
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);
                }
            }
        }
    }
}
