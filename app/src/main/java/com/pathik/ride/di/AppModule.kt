package com.pathik.ride.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pathik.ride.R
import com.pathik.ride.network.FirebaseAuthSource
import com.pathik.ride.network.FirebaseDataSource
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    @Singleton
    @Provides
    fun getAuthSource(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): FirebaseAuthSource {
        return FirebaseAuthSource(firebaseAuth, fireStore)
    }

    @Singleton
    @Provides
    fun getDataSource(
        firebaseAuthSource: FirebaseAuthSource,
        fireStore: FirebaseFirestore,
        storageReference: StorageReference,
    ): FirebaseDataSource {
        return FirebaseDataSource(firebaseAuthSource, fireStore,storageReference)
    }

    @Singleton
    @Provides
    fun providePicassoInstance(
        @ApplicationContext context: Context, url: String
    ): RequestCreator = Picasso.get().load(url).placeholder(R.drawable.ic_user_place_holder)
        .error(R.drawable.ic_user_place_holder)

}