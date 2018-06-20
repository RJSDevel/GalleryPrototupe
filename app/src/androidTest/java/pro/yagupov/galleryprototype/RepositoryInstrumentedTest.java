package pro.yagupov.galleryprototype;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

import pro.yagupov.galleryprototype.repository.LocalRepository;
import pro.yagupov.galleryprototype.repository.LocalRepositoryImpl;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 * <p>
 * TODO Before start test allow permissions!!!
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepositoryInstrumentedTest {

    private static final String
            TEST_FOLDER = "GalleryPrototypeTest";


    private LocalRepository localRepository;

    private File appFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + TEST_FOLDER);
    private LifecycleOwner lifecycle;

    private static final String[] filesExtention = {
            "jpg",
            "jpeg",
            "bmp",
            "mp3",
            "aac",
            "wav",
            "txt",
            "doc",
    };

    @Before
    // TODO Try get permissions
    public void grantPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName() +
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Before
    public void init() {
        localRepository = new LocalRepositoryImpl();

        if (appFolder.exists()) {
            if (appFolder.list().length > 0) {
                for (String s : appFolder.list()) {
                    new File(appFolder.getAbsolutePath() + File.separator + s).delete();
                }
            }

            appFolder.delete();

            if (appFolder.exists()) {
                throw new RuntimeException("Cant remove test dir");
            }
        }

        lifecycle = new LifecycleOwner() {
            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                LifecycleRegistry registry = new LifecycleRegistry(this);
                registry.markState(Lifecycle.State.STARTED);
                return registry;
            }
        };
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getTargetContext();
        assertEquals("pro.yagupov.galleryprototype", appContext.getPackageName());
    }

    @Test
    public void creationFolder() {
        localRepository.getGalleryFiles(TEST_FOLDER)
                .observe(lifecycle, galleryFiles -> {
                    if (!appFolder.exists()) {
                        throw new RuntimeException("folder not exists");
                    }
                });
    }

    @Test
    public void filesCount() throws IOException {

        if (!appFolder.exists()) {
            appFolder.mkdir();
        }

        int files = 0;
        for (String ext : filesExtention) {
            File file = new File(appFolder.getAbsolutePath() + File.separator + ext + "." + ext);
            if (file.createNewFile()) {
                files++;
            }
        }

        int finalFiles = files;
        localRepository.getGalleryFiles(TEST_FOLDER)
                .observe(lifecycle, galleryFiles -> {
                    if (galleryFiles.size() != finalFiles) {
                        throw new RuntimeException("Created and get files count is not equals");
                    }
                });
    }
}
