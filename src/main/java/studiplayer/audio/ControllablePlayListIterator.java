package studiplayer.audio;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
    private final List<AudioFile> list;
    private int next;

    public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sortCriterion) {
        Stream<AudioFile> stream = list.stream().filter(ControllablePlayListIterator.filter(search));
        Comparator<AudioFile> controller = ControllablePlayListIterator.getSort(sortCriterion);
        if (controller != null)
            stream = stream.sorted(controller);
        this.list = stream.collect(Collectors.toList());
        this.next = 0;
    }

    public ControllablePlayListIterator(List<AudioFile> list) {
        this.list = list;
        this.next = 0;
    }

    private static Comparator<AudioFile> getSort(SortCriterion order) {
        switch (order) {
            case AUTHOR:
                return new AuthorComparator();
            case TITLE:
                return new TitleComparator();
            case ALBUM:
                return new AlbumComparator();
            case DURATION:
                return new DurationComparator();
            default:
                return null;
        }
    }

    private static Predicate<AudioFile> filter(String search) {
        return file -> search.isEmpty() || file.getAuthor().contains(search) || file.getTitle().contains(search) || file instanceof TaggedFile && ((TaggedFile) file).getAlbum() != null && ((TaggedFile) file).getAlbum().contains(search);
    }

    @Override
    public boolean hasNext() {
        return this.next < this.list.size();
    }

    @Override
    public AudioFile next() {
        if (this.next >= list.size())
            return null;
        else {
            AudioFile el = this.list.get(next);
            next++;
            return el;
        }
    }

    public AudioFile jumpToAudioFile(AudioFile file) {
        int i = list.indexOf(file);
        if (i == -1) {
            next = 0;
            return null;
        } else {
            next = i + 1;
        }
        return file;
    }
}
