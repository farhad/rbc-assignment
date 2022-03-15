package io.github.farhad.rbc.ui.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.github.farhad.rbc.di.ViewModelFactory;

public class BaseFragment extends Fragment {

    protected String navigationTag;

    public String getNavigationTag() {
        return navigationTag;
    }

    public void setNavigationTag(String navigationTag) {
        this.navigationTag = navigationTag;
    }

    @Inject
    protected ViewModelFactory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
