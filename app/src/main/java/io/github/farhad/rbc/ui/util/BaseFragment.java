package io.github.farhad.rbc.ui.util;

import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.github.farhad.rbc.di.ViewModelFactory;

public class BaseFragment extends Fragment {

    protected String navigationTag;
    protected Boolean isEntryPoint;

    public String getNavigationTag() {
        return navigationTag;
    }

    public void setNavigationTag(String navigationTag) {
        this.navigationTag = navigationTag;
    }

    public Boolean getEntryPoint() {
        return isEntryPoint;
    }

    public void setEntryPoint(Boolean entryPoint) {
        isEntryPoint = entryPoint;
    }

    public BaseFragment() {
        setEnterTransition(new Slide(Gravity.END).setDuration(250));
        setExitTransition(new Slide(Gravity.START).setDuration(250));
    }

    @Inject
    protected ViewModelFactory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
