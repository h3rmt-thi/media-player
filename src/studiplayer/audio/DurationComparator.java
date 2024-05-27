package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        Long duration1 = 1L;
        long duration2 = 0L;
        if (o1 instanceof SampledFile) {
            duration1 = ((SampledFile) o1).getDuration();
        }
        if (o2 instanceof SampledFile) {
            duration2 = ((SampledFile) o2).getDuration();
        }
        return duration1.compareTo(duration2);
    }
}
