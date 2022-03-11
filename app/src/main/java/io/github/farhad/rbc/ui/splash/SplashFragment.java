package io.github.farhad.rbc.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import io.github.farhad.rbc.databinding.SplashFragmentBinding;
import io.github.farhad.rbc.ui.util.BaseFragment;

public class SplashFragment extends BaseFragment {
    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    private SplashViewModel splashViewModel;
    private SplashFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var provider = new ViewModelProvider(requireActivity(), viewModelFactory);
        splashViewModel = provider.get(SplashViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SplashFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        splashViewModel.onDisplayStarted();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
