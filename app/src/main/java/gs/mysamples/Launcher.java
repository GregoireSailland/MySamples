package gs.mysamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Launcher extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Intent i = new Intent(Launcher.this, SensorService.class);
        startService(i);
        Button clickButton = (Button) findViewById(R.id.BtnSensorView);

        clickButton.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Launcher.this, SensorView.class);
                myIntent.putExtra("key", "haha"); //Optional parameters
                Launcher.this.startActivity(myIntent);
            }
        });

    }
}
