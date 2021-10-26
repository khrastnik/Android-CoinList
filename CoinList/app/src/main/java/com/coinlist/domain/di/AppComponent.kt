package com.coinlist.domain.di

import com.coinlist.CoinApplication
import com.coinlist.domain.di.viewmodel.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        NetworkModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent : AndroidInjector<CoinApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: CoinApplication): AppComponent
    }
}