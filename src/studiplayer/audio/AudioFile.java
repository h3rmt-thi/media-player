package studiplayer.audio;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AudioFile {
    private static final Pattern namePattern = Pattern.compile("(?:(?<author>.*) +- +)?(?<title>.*?)(?:\\.(?!.*\\.)(?<extension>.*))?");
    private String filename;
    private String pathname;
    private String author;
    private String title;
    private String extension;

    public AudioFile(String path) throws NotPlayableException {
        this.parsePathname(path);
        this.parseFilename(this.filename);
        File file = new File(this.pathname);
        if (!file.exists() || !file.canRead()) {
            throw new NotPlayableException(this.pathname, "File does not exist or is not readable");
        }
    }

    public AudioFile() {
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Parses the pathName into the filePath and fileName, corrects to Path for Windows and Unix
     * <p>
     * Aufgabe:
     *    <ul>
     * <li>Laufwerksangabe behandeln</li>
     * Falls das Programm nicht unter Windows läuft, soll eine Laufwerksangabe am Anfang (z.B. „C:\...“) korrigiert werden.
     * <li>Pfadseparatoren normalisieren</li>
     * In path enthaltene Pfadseparatoren sollen in die für das aktuelle Betriebssystem gültigen Separatoren umgewandelt werden.
     * Zudem sollen Folgen von Pfadseparatoren (z.B. "medien///song.mp3") zu einem zusammenfasst werden (z.B. "medien/song.mp3").
     * <li>Speichern der Ergebnisse in Attributen</li>
     * Das Ergebnis der oben genannten Operationen soll in pathname gespeichert werden. Gibt es Pfadtrenner in path, so ergibt
     * sich der Wert für filename aus der Zeichenfolge rechts vom letzten Pfadtrenner. Andernfalls erhält filename den selben Wert wie pathname.
     * </ul>
     * </p>
     *
     * @param path the pathName to parse
     */
    public void parsePathname(String path) {
        if (isWindows()) {
            // replace unix separators with windows
            path = path.replace("/", "\\");
            // compress \\ to \
            path = path.replaceAll("\\\\+", "\\\\");
            // get name from path
            this.filename = path.substring(path.lastIndexOf("\\") + 1).trim();
        } else {
            // replace "C:" at beginning with /C/
            if (path.matches("^[a-zA-Z]:.*$")) {
                path = path.replaceFirst("^([a-zA-Z]):", "/$1/");
            }
            // replace windows separators with unix
            path = path.replace("\\", "/");
            // compress // to /
            path = path.replaceAll("/+", "/");
            // get name from path
            this.filename = path.substring(path.lastIndexOf("/") + 1).trim();
        }
        this.pathname = path.trim();
    }

    /**
     * Parses the filename to extract Author and Title
     * <p>
     * Aufgabe:
     * Autor‿–‿Titel.Endung
     * <p>
     * parseFilename(String filename) soll den übergebenen Dateinamen in die beiden oben genannten Bestandteile zerlegen
     * und das Ergebnis der Zerlegung in den neuen Attributen author und title speichern.
     * </p>
     *
     * @param filename name of file to parse
     */
    public void parseFilename(String filename) {
        Matcher matcher = namePattern.matcher(filename);

        if (matcher.matches()) {
            String author = matcher.group("author");
            if (author != null && !author.isEmpty()) {
                this.author = author.trim();
            } else {
                this.author = "";
            }

            String title = matcher.group("title");
            if (title != null && !title.isEmpty()) {
                this.title = title.trim();
            } else {
                this.title = "";
            }

            String extension = matcher.group("extension");
            if (extension != null && !extension.isEmpty()) {
                this.extension = extension.trim();
            } else {
                this.extension = "";
            }
        } else {
            System.out.println("String does not match the pattern");
        }
    }

    public String getFilename() {
        return this.filename;
    }

    public String getPathname() {
        return this.pathname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        if (this.getAuthor() == null || this.getAuthor().isEmpty()) {
            return this.getTitle();
        } else {
            return this.getAuthor() + " - " + this.getTitle();
        }
    }

    abstract public void play() throws NotPlayableException;

    abstract public void togglePause();

    abstract public void stop();

    abstract public String formatDuration();

    abstract public String formatPosition();
}