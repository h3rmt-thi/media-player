package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        String album1;
        String album2;
        if (o1 instanceof TaggedFile) {
            album1 = ((TaggedFile) o1).getAlbum() != null ? ((TaggedFile) o1).getAlbum() : "";
        } else {
            if (o2 instanceof TaggedFile)
                return -1;
            else
                return 0;
        }
        if (o2 instanceof TaggedFile) {
            album2 = ((TaggedFile) o2).getAlbum() != null ? ((TaggedFile) o2).getAlbum() : "";
        } else {
            return 1;
        }
        return album1.compareTo(album2);
    }
}
