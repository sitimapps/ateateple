package com.sitimapps.ateple.ate.ateateple;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        info = (TextView)findViewById(R.id.info);
        String q = (String) getIntent().getSerializableExtra("select");
        switch (q) {
            case "ate":
                info.setText(getString(R.string.info_ate));
                break;
            case "help":
                info.setText(getString(R.string.info_help));
                break;
            case "feed":
                info.setText(getString(R.string.info_feed));
                break;
            case "rules":
                info.setText(getString(R.string.info_rules));
                break;
        }
    }
}
