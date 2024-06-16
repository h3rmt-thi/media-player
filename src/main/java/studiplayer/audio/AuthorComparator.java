package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        return (o1.getAuthor() == null ? "" : o1.getAuthor()).compareTo(o2.getAuthor());
    }
}
