package com.lcsz.first.di

// ApiService não é mais injetado aqui diretamente, mas no UserRepositoryImpl
import com.lcsz.first.data.repository.AuthRepositoryImpl
import com.lcsz.first.data.repository.UserRepositoryImpl
import com.lcsz.first.domain.repository.AuthRepository // Importe a INTERFACE
import com.lcsz.first.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule { // Torne a classe abstrata se usar @Binds

    @Binds // @Binds é mais eficiente que @Provides para mapear uma interface à sua implementação
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl // Hilt sabe como criar UserRepositoryImpl (tem @Inject no construtor)
    ): AuthRepository // Retorna a interface

    @Binds // @Binds é mais eficiente que @Provides para mapear uma interface à sua implementação
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}