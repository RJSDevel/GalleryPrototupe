package pro.yagupov.galleryprototype.ui.holder;

import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.yagupov.galleryprototype.R;
import pro.yagupov.galleryprototype.models.GalleryFile;

import static pro.yagupov.galleryprototype.models.GalleryFile.AUDIO;
import static pro.yagupov.galleryprototype.models.GalleryFile.DOCUMENT;
import static pro.yagupov.galleryprototype.models.GalleryFile.FOLDER;
import static pro.yagupov.galleryprototype.models.GalleryFile.IMAGE;
import static pro.yagupov.galleryprototype.models.GalleryFile.VIDEO;

public class SimpleFileViewHolder extends SectioningAdapter.ItemViewHolder {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @BindView(R.id.play)
    View vPlay;
    @BindView(R.id.preview)
    ImageView vPreview;
    @BindView(R.id.name)
    TextView vName;
    @BindView(R.id.date)
    TextView vDate;
    @BindView(R.id.subtext)
    TextView vSubText;


    public SimpleFileViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void fill(GalleryFile file) {
        vName.setText(file.getName());
        vDate.setText(sdf.format(file.getCreated().getTime()));

        vPlay.setVisibility(View.GONE);
        vSubText.setVisibility(View.GONE);

        switch (file.getType()) {
            case VIDEO:
                vPlay.setVisibility(View.VISIBLE);
            case IMAGE:
                Glide.with(itemView.getContext())
                        .load(file.getFile())
                        .into(vPreview);
                break;
            case AUDIO:
                vPlay.setVisibility(View.VISIBLE);
                vPreview.setImageResource(R.drawable.ic_audio);
                break;
            case DOCUMENT:
                vPreview.setImageResource(R.drawable.ic_doc);
                break;
            case FOLDER:
                vPreview.setImageResource(R.drawable.ic_folder);
                vSubText.setVisibility(View.VISIBLE);
                vSubText.setText(itemView.getContext().getResources().getQuantityString(R.plurals.files, file.files(), file.files()));
                break;
            default:
                vPreview.setImageResource(R.drawable.ic_unknown);
        }
    }
}
