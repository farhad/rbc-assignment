package io.github.farhad.rbc.ui.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SplashViewModel extends ViewModel {

    @Inject
    public SplashViewModel() {
        // required for dagger setup
    }

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final MutableLiveData<SplashAction> _splashActions = new MutableLiveData<>();
    public final LiveData<SplashAction> splashActions = _splashActions;

    public void onDisplayStarted() {
        executorService.schedule(() -> _splashActions.postValue(new SplashAction.ShowAccounts()), 1, TimeUnit.SECONDS);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
