package pro.yagupov.galleryprototype;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String
            TEST_FOLDER = "GalleryPrototypeTest";

    @Before
    public void grantPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName() +
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getTargetContext();

        assertEquals("pro.yagupov.galleryprototype", appContext.getPackageName());
    }

    @Test
    public void checkCreationFolder() {

        File appFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + TEST_FOLDER);

        if (appFolder.exists() && !appFolder.delete()) {
            throw new RuntimeException("Cant remove test dir");
        } else {
            if (!appFolder.mkdirs()) {
                throw new RuntimeException("Cant create test dir");
            }
        }
    }
}
