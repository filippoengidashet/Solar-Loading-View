package org.dalol.solarloadingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SolarLoadingView loadingView = new SolarLoadingView(this);
        setContentView(loadingView);
        loadingView.animateNow();
    }
}
