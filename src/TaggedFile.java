import studiplayer.basic.TagReader;

import java.util.Map;

public class TaggedFile extends SampledFile {
    private String album;

    TaggedFile(String path) {
        super(path);
        this.readAndStoreTags();
    }

    TaggedFile() {
        super();
    }

    String getAlbum() {
        return this.album;
    }

    void readAndStoreTags() {
        Map<String, Object> tagMap = TagReader.readTags(this.getPathname());
        if (tagMap.containsKey("album"))
            this.album = ((String) tagMap.get("album")).trim();
        if (tagMap.containsKey("author"))
            this.setAuthor(((String) tagMap.get("author")).trim());
        if (tagMap.containsKey("title"))
            this.setTitle(((String) tagMap.get("title")).trim());
        if (tagMap.containsKey("duration"))
            this.setDuration((Long) tagMap.get("duration"));
    }

    @Override
    public String toString() {
        if (this.getAlbum() != null)
            return super.toString() + " - " + this.getAlbum() + " - " + this.formatDuration();
        else
            return super.toString() + " - " + this.formatDuration();
    }
}
