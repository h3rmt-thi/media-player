import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
    WavFile(String path) {
        super(path);
        readAndSetDurationFromFile();
    }

    WavFile() {
        super();
    }

    static long computeDuration(long numberOfFrames, float frameRate) {
        return (long) ((1000000 * numberOfFrames) / frameRate);
    }

    void readAndSetDurationFromFile() {
        WavParamReader.readParams(this.getPathname());
        this.setDuration(computeDuration(WavParamReader.getNumberOfFrames(), WavParamReader.getFrameRate()));
    }

    @Override
    public String toString() {
        return super.toString() + " - " + this.formatDuration();
    }
}
