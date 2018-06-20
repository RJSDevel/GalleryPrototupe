package pro.yagupov.galleryprototype.models;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;

public class GalleryFile implements Comparable<GalleryFile> {

    public static final int
            IMAGE = 1,
            VIDEO = 2,
            AUDIO = 3,
            DOCUMENT = 4,
            FOLDER = 5,
            UNKNOWN = 6;

    private File file;


    public GalleryFile(String path) {
        this.file = new File(path);
    }

    public String getName() {
        return file.getName();
    }

    public Calendar getCreated() {
        Calendar created = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                created.setTimeInMillis(attr.creationTime().toMillis());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            created.setTimeInMillis(file.lastModified());
        }
        return created;
    }

    public int getType() {

        if (file.isDirectory()) {
            return FOLDER;
        }

        String type = getMimeType();
        if (!TextUtils.isEmpty(type)) {
            switch (type.substring(0, type.lastIndexOf("/"))) {
                case "image":
                    return IMAGE;
                case "video":
                    return VIDEO;
                case "audio":
                    return AUDIO;
                case "text":
                    return DOCUMENT;
            }
        }

        return UNKNOWN;
    }

    public String getMimeType() {
        String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public File getFile() {
        return file;
    }

    public int files() {
        return file.list().length;
    }

    @Override
    public int compareTo(@NonNull GalleryFile o) {
        return (int) (file.lastModified() - o.file.lastModified());
    }
}
