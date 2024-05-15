public class AudioFileFactory {
    public static AudioFile createAudioFile(String path) {
        if (path.toLowerCase().endsWith(".mp3") || path.toLowerCase().endsWith(".ogg")) {
            return new TaggedFile(path);
        } else if (path.toLowerCase().endsWith(".wav")) {
            return new WavFile(path);
        } else {
            throw new IllegalArgumentException("Unknown suffix for AudioFile \"" + path + "\"");
        }
    }
}
