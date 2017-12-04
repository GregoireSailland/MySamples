package gs.mysamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Launcher extends AppCompatActivity {

    private Intent intent;
    private Button addbuttonaction(int btnid, final Class<? extends AppCompatActivity> act){
        Button btn = findViewById(btnid);
        btn.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Launcher.this,act);
                myIntent.putExtra("key", "haha"); //Optional parameters
                Launcher.this.startActivity(myIntent);
            }
        });
        return btn;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        addbuttonaction(R.id.BtnSensorView,SensorView.class);
        addbuttonaction(R.id.button2,SensorView.class);
        addbuttonaction(R.id.Btnapp3,null);

    }
}
