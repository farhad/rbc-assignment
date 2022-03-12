package io.github.farhad.rbc.ui.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.farhad.rbc.ui.navigation.NavigationAction;

public class SplashViewModel extends ViewModel {

    @Inject
    public SplashViewModel() {
        // required for dagger setup
    }

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final MutableLiveData<NavigationAction> _navigationAction = new MutableLiveData<>();
    public final LiveData<NavigationAction> navigationAction = _navigationAction;

    public void onDisplayStarted() {
        executorService.schedule(() -> _navigationAction.postValue(new NavigationAction.ShowAccounts()), 1, TimeUnit.SECONDS);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
