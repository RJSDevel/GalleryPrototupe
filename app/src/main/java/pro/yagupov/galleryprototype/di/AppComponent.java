package pro.yagupov.galleryprototype.di;

import javax.inject.Singleton;

import dagger.Component;
import pro.yagupov.galleryprototype.viewmodel.FileSystemViewModel;

@Singleton
@Component(modules = {RepositoryModule.class})
public interface AppComponent {

    void inject(FileSystemViewModel viewModel);

}
