package studiplayer.audio;

public class NotPlayableException extends Exception {
    private String pathName;

    public NotPlayableException(String pathname, String msg) {
        super(msg);
        this.pathName = pathname;
    }

    public NotPlayableException(String pathname, Throwable t) {
        super(t);
        this.pathName = pathname;
    }

    public NotPlayableException(String pathname, String msg, Throwable t) {
        super(msg, t);
        this.pathName = pathname;
    }

    @Override
    public String toString() {
        return "studiplayer.audio.NotPlayableException{pathName='" + pathName + '\'' + ", msg='" + getMessage() + '\'' + '}';
    }
}
