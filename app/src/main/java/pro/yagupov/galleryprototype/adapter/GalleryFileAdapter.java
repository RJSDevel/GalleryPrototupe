package pro.yagupov.galleryprototype.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.yagupov.galleryprototype.R;
import pro.yagupov.galleryprototype.models.GalleryFile;
import pro.yagupov.galleryprototype.ui.holder.SimpleFileViewHolder;

public class GalleryFileAdapter extends SectioningAdapter {

    public interface OnItemClickListener {
        void onClick(GalleryFile galleryFile);
    }

    private static class Section {

        private static final SimpleDateFormat
                DATE_FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        List<GalleryFile> list = new ArrayList<>();


        Section(GalleryFile file) {
            list.add(file);
        }

        String getDate() {
            return DATE_FORMAT.format(list.get(0).getCreated().getTime());
        }
    }

    private OnItemClickListener listener;

    private List<Section> sections = new ArrayList<>();


    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {

        @BindView(R.id.date)
        public TextView date;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public GalleryFileAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void add(List<GalleryFile> gallery) {
        sections.clear();

        Collections.sort(gallery);
        sections.addAll(separateBySections(gallery));

        notifyAllSectionsDataSetChanged();
    }

    private List<Section> separateBySections(List<GalleryFile> gallery) {
        sections = new ArrayList<>();

        for (Iterator<GalleryFile> it = gallery.iterator(); it.hasNext(); it.remove()) {
            GalleryFile file = it.next();
            if (sections.isEmpty()) {
                sections.add(new Section(file));
            } else {
                for (ListIterator<Section> sectionIt = sections.listIterator(sections.size() - 1); sectionIt.hasNext(); ) {
                    Section section = sectionIt.next();
                    Calendar created = section.list.get(0).getCreated();
                    if (created.get(Calendar.YEAR) == file.getCreated().get(Calendar.YEAR) && created.get(Calendar.MONTH) == file.getCreated().get(Calendar.MONTH)) {
                        section.list.add(file);
                        break;
                    } else {
                        sectionIt.add(new Section(file));
                    }
                }
            }
        }

        return sections;
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).list.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_preview, parent, false);
        view.setOnClickListener(v -> listener.onClick((GalleryFile) v.getTag()));
        return new SimpleFileViewHolder(view);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false));
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        GalleryFile file = sections.get(sectionIndex).list.get(itemIndex);
        viewHolder.itemView.setTag(file);
        ((SimpleFileViewHolder) viewHolder).fill(file);
    }

    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        ((HeaderViewHolder) viewHolder).date.setText(sections.get(sectionIndex).getDate());
    }

    // TODO - Fix crash issue in library
    @Override
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        final View ghostView = new View(parent.getContext());
        ghostView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new GhostHeaderViewHolder(ghostView);
    }
}
