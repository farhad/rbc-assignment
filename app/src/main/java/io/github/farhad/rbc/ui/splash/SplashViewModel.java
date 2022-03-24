package io.github.farhad.rbc.ui.splash;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.farhad.rbc.ui.navigation.NavigationAction;
import io.github.farhad.rbc.ui.util.SingleLiveEvent;

public class SplashViewModel extends ViewModel {

    @Inject
    public SplashViewModel() {
        // required for dagger setup
    }

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final SingleLiveEvent<NavigationAction> _navigationAction = new SingleLiveEvent<>();

    public SingleLiveEvent<NavigationAction> navigationAction() {
        return _navigationAction;
    }

    public void onDisplayStarted() {
        executorService.schedule(() -> _navigationAction.postValue(new NavigationAction.ShowAccounts()), 1, TimeUnit.SECONDS);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
