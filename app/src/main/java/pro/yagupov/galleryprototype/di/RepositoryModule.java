package pro.yagupov.galleryprototype.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pro.yagupov.galleryprototype.repository.LocalRepository;
import pro.yagupov.galleryprototype.repository.LocalRepositoryImpl;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    LocalRepository provideRemoteRepository() {
        return new LocalRepositoryImpl();
    }

}
