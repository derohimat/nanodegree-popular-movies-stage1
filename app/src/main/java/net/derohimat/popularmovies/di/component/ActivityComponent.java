package net.derohimat.popularmovies.di.component;

import net.derohimat.popularmovies.di.ActivityScope;
import net.derohimat.popularmovies.view.activity.main.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent {

    void inject(MainActivity mainActivity);
}