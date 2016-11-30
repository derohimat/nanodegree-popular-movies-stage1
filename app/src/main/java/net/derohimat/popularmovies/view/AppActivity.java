package net.derohimat.popularmovies.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.derohimat.popularmovies.BaseApplication;
import net.derohimat.popularmovies.di.component.ActivityComponent;
import net.derohimat.popularmovies.di.component.DaggerActivityComponent;

public abstract class AppActivity extends AppCompatActivity {

    private ActivityComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder().applicationComponent(getApp().getApplicationComponent()).build();
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

    protected BaseApplication getApp() {
        return (BaseApplication) getApplicationContext();
    }

}
