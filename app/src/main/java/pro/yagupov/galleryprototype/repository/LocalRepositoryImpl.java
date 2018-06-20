package pro.yagupov.galleryprototype.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pro.yagupov.galleryprototype.models.GalleryFile;

public class LocalRepositoryImpl implements LocalRepository {

    private Handler handler;

    @Override
    public LiveData<List<GalleryFile>> getGalleryFiles(String folder) {
        MutableLiveData<List<GalleryFile>> galleryFiles = new MutableLiveData<>();

        if (handler != null) {
            handler.removeMessages(0);
        }

        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                File appFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder);
                if (!appFolder.exists()) {

                    Log.d(getClass().getSimpleName(), "App folder don't exists - " + appFolder.getAbsolutePath() + " Try make");

                    if (!appFolder.mkdir()) {
                        Log.e(getClass().getSimpleName(), "Can't create app folder - " + appFolder.getAbsolutePath());
                    }
                } else {
                    List<GalleryFile> files = new ArrayList<>();
                    for (String fileName : appFolder.list()) {
                        files.add(new GalleryFile(appFolder.getAbsolutePath() + File.separator + fileName));
                    }
                    galleryFiles.setValue(files);
                }
            }
        });

        return galleryFiles;
    }
}
