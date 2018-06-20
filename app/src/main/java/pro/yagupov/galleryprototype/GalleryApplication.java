package pro.yagupov.galleryprototype;

import android.app.Application;

import pro.yagupov.galleryprototype.di.AppComponent;
import pro.yagupov.galleryprototype.di.DaggerAppComponent;
import pro.yagupov.galleryprototype.di.RepositoryModule;

public class GalleryApplication extends Application {

    private static AppComponent sAppComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent
                .builder()
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static AppComponent di() {
        return sAppComponent;
    }
}
