package pro.yagupov.galleryprototype.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import pro.yagupov.galleryprototype.GalleryApplication;
import pro.yagupov.galleryprototype.models.GalleryFile;
import pro.yagupov.galleryprototype.repository.LocalRepository;

public class FileSystemViewModel extends ViewModel {

    @Inject
    LocalRepository repository;


    public FileSystemViewModel() {
        GalleryApplication.di().inject(this);
    }

    public LiveData<List<GalleryFile>> getGalleryFiles(String folder) {
        return repository.getGalleryFiles(folder);
    }
}
