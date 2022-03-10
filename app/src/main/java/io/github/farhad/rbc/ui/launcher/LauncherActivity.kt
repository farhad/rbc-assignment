package io.github.farhad.rbc.ui.launcher

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.github.farhad.rbc.R
import io.github.farhad.rbc.di.ViewModelFactory
import javax.inject.Inject

class LauncherActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}