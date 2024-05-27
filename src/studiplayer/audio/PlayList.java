package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PlayList implements Iterable<AudioFile> {
    private final LinkedList<AudioFile> list;
    private String search;
    private ControllablePlayListIterator iterator;
    private AudioFile current;
    private SortCriterion sortCriterion;

    public PlayList(String pathname) {
        this();
        loadFromM3U(pathname);
    }

    public PlayList() {
        this.list = new LinkedList<AudioFile>();
        this.search = "";
        this.sortCriterion = SortCriterion.DEFAULT;
        updateIterator();
    }

    public List<AudioFile> getList() {
        return list;
    }

    public void add(AudioFile file) {
        list.add(file);
        updateIterator();
    }

    public void remove(AudioFile file) {
        list.remove(file);
        updateIterator();
    }

    public int size() {
        return list.size();
    }

    public AudioFile currentAudioFile() {
        return this.current;
    }

    public void jumpToAudioFile(AudioFile tf2) {
        current = this.iterator.jumpToAudioFile(tf2);
    }

    public void nextSong() {
        AudioFile next = this.iterator.next();
        if (next == null) {
            updateIterator();
            next = this.current;
        }
        this.current = next;
    }

    public void saveAsM3U(String pathname) {
        String sep = System.getProperty("line.separator");

        try (FileWriter writer = new FileWriter(pathname)) {
            for (AudioFile file : this.list) {
                // write the current line + newline char
                writer.write(file.getPathname() + sep);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to write file " + pathname + "!");
        }
    }

    public void loadFromM3U(String path) {
        this.list.clear();

        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }
                try {
                    this.add(AudioFileFactory.createAudioFile(line));
                } catch (NotPlayableException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
        this.updateIterator();
    }

    public SortCriterion getSortCriterion() {
        return sortCriterion;
    }

    public void setSortCriterion(SortCriterion sortCriterion) {
        this.sortCriterion = sortCriterion;
        this.updateIterator();
    }

    private void updateIterator() {
        this.iterator = new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
        this.current = iterator.next();
    }

    @Override
    public Iterator<AudioFile> iterator() {
        return new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
    }

    @Override
    public String toString() {
        return "[" + StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(new ControllablePlayListIterator(this.list, this.search, this.sortCriterion), Spliterator.ORDERED), false)
                .map(AudioFile::toString).collect(Collectors.joining(", ")) + "]";
    }
}