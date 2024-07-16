package com.roland.android.destore

import android.app.Application
import com.roland.android.destore.ui.screen.cart.CartViewModel
import com.roland.android.destore.ui.screen.details.DetailsViewModel
import com.roland.android.destore.ui.screen.home.HomeViewModel
import com.roland.android.destore.ui.screen.list.ListViewModel
import com.roland.android.remotedatasource.di.NetworkModule.remoteDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class DeStoreApp : Application() {

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@DeStoreApp)
			androidLogger(Level.INFO)
			modules(appModule, remoteDataModule)
		}
	}

}

private val appModule = module {
	viewModel { HomeViewModel() }
	viewModel { DetailsViewModel() }
	viewModel { ListViewModel() }
	viewModel { CartViewModel() }
}