import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PlayList {
    private final LinkedList<AudioFile> list;
    private int current;

    public PlayList(String pathname) {
        this();
        loadFromM3U(pathname);
    }

    public PlayList() {
        this.list = new LinkedList<>();
        this.current = 0;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<AudioFile> getList() {
        return list;
    }

    public void add(AudioFile file) {
        list.add(file);
    }

    public void remove(AudioFile file) {
        list.remove(file);
    }

    public int size() {
        return list.size();
    }

    public AudioFile currentAudioFile() {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(current);
    }

    public void nextSong() {
        if (current < list.size() - 1) {
            current++;
        } else {
            current = 0;
        }
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
        this.current = 0;

        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }
                this.add(AudioFileFactory.createAudioFile(line));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
