package pro.yagupov.galleryprototype.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pro.yagupov.galleryprototype.R;
import pro.yagupov.galleryprototype.adapter.GalleryFileAdapter;
import pro.yagupov.galleryprototype.models.GalleryFile;
import pro.yagupov.galleryprototype.viewmodel.FileSystemViewModel;

public class MainActivity extends AppCompatActivity implements GalleryFileAdapter.OnItemClickListener {

    private static final int
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1242;

    Unbinder unbinder;

    @BindView(R.id.list)
    RecyclerView vFileList;
    @BindView(R.id.refresh)
    SwipeRefreshLayout vRefresh;


    private GalleryFileAdapter adapter = new GalleryFileAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        vRefresh.setColorSchemeResources(R.color.colorPrimary);
        vRefresh.setOnRefreshListener(this::loadFiles);

        vFileList.setAdapter(adapter);
        vFileList.setLayoutManager(new StickyHeaderLayoutManager());

        loadFiles();
    }

    private void loadFiles() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(R.string.attention)
                        .setMessage(R.string.explanation_message)
                        .setPositiveButton(R.string.allow, (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            ViewModelProviders.of(this)
                    .get(FileSystemViewModel.class)
                    .getGalleryFiles("GalleryPrototype")
                    .observe(this, galleryFiles -> {
                        vRefresh.setRefreshing(false);
                        adapter.add(galleryFiles);
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if ((grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                loadFiles();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.access_not_available_work_is_imposible, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.repeat, v -> loadFiles())
                        .show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onClick(GalleryFile galleryFile) {
        if (galleryFile.getType() != GalleryFile.FOLDER) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(galleryFile.getFile()), galleryFile.getMimeType());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (intent.resolveActivity(getPackageManager()) == null) {
                Snackbar.make(findViewById(android.R.id.content), R.string.app_for_open_not_found, Snackbar.LENGTH_LONG)
                        .show();
            } else {
                startActivity(intent);
            }
        }
    }
}
