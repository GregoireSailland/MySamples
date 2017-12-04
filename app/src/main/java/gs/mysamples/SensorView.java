package gs.mysamples;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class SensorView extends AppCompatActivity {

    private TextView tv;
    boolean m_stop = false;
    AudioTrack m_audioTrack;
    Thread m_noiseThread;
    private final float duration = .01f; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = (int) (duration * sampleRate);
    private final double sample[] = new double[numSamples];
    private final double freqOfTone = 440; // hz
    private final byte generatedSnd[] = new byte[2 * numSamples];

    Runnable m_noiseGenerator = new Runnable(){
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            while (!m_stop) {
                for (int i = 0; i < numSamples; ++i) {
                    sample[i] = Math.sin(2 * Math.PI * i / (sampleRate /freqOfTone));
                }
                // convert to 16 bit pcm sound array
                // assumes the sample buffer is normalised.
                int idx = 0;
                for (final double dVal : sample) {
                    // scale to maximum amplitude
                    final short val = (short) ((dVal * 32767)*SensorService.lSensormaxvalue/(SensorService.lSensormaxvalue/SensorService.light));
                    // in 16 bit wav PCM, first byte is the low order byte
                    generatedSnd[idx++] = (byte) (val & 0x00ff);
                    generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
                }
                m_audioTrack.write(generatedSnd, 0, generatedSnd.length);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(SensorView.this, SensorService.class);
        startService(i);
        start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(SensorView.this, SensorService.class));
        stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_sensor_view);
        tv=findViewById(R.id.textViewSensor);
        update();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(125);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update();
                            }
                        });
                    }
                } catch (InterruptedException ignored) {}
            }
        };
        t.start();
    }
    private void update() {
        // update your interface here
        tv.setText(Float.toString(SensorService.light));
    }
    void start(){
        m_stop = false;

        /* 8000 bytes per second*/
        m_audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,  sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length /* 1 second buffer */,
                AudioTrack.MODE_STREAM);

        m_audioTrack.play();


        m_noiseThread = new Thread(m_noiseGenerator);
        m_noiseThread.start();
    }
    void stop(){
        m_stop = true;
        m_audioTrack.stop();
    }
}

