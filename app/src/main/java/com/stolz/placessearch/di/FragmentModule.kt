package com.stolz.placessearch.di

import com.google.android.gms.maps.MapFragment
import com.stolz.placessearch.details.DetailFragment
import com.stolz.placessearch.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment
}