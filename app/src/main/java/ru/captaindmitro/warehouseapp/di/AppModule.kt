package ru.captaindmitro.warehouseapp.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import ru.captaindmitro.warehouseapp.data.RemoteDataSource
import ru.captaindmitro.warehouseapp.data.RemoteDataSourceImpl
import ru.captaindmitro.warehouseapp.data.RepositoryImpl
import ru.captaindmitro.warehouseapp.data.WarehouseApi
import ru.captaindmitro.warehouseapp.domain.Repository
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideRemoteDataSource(
        warehouseApi: WarehouseApi,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ) : RemoteDataSource = RemoteDataSourceImpl(warehouseApi, dispatcher)

    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): Repository = RepositoryImpl(remoteDataSource, dispatcher)

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl("https://edo.ilexx.ru").build()

    @Provides
    fun provideWarehouseApi(retrofit: Retrofit): WarehouseApi = retrofit.create(WarehouseApi::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher