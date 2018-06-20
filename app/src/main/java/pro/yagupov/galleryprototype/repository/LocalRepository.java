package pro.yagupov.galleryprototype.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import pro.yagupov.galleryprototype.models.GalleryFile;

public interface LocalRepository {

    LiveData<List<GalleryFile>> getGalleryFiles(String folder);

}
