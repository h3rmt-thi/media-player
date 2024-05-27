package studiplayer.audio;

public class AudioFileFactory {
    public static AudioFile createAudioFile(String path) throws NotPlayableException {
        if (path.toLowerCase().endsWith(".mp3") || path.toLowerCase().endsWith(".ogg")) {
            return new TaggedFile(path);
        } else if (path.toLowerCase().endsWith(".wav")) {
            return new WavFile(path);
        } else {
            throw new NotPlayableException(path, "Unknown suffix for AudioFile \"" + path + "\"");
        }
    }

    public static void main(String[] args) throws NotPlayableException {
        PlayList pl = new PlayList();
        pl.add(new TaggedFile("audiofiles/Rock 812.mp3"));
        pl.add(new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
        pl.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
// Verändern Sie folgende Konfigurationen
        pl.setSearch("Eisbach");
        pl.setSortCriterion(SortCriterion.AUTHOR);
// Beispiel für Iteration mit for-each
        for (AudioFile file : pl) {
            System.out.println(file);
        }
    }
}