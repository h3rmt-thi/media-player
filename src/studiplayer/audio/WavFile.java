package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
    public WavFile(String path) throws NotPlayableException {
        super(path);
        readAndSetDurationFromFile();
    }

    public WavFile() {
        super();
    }

    public static long computeDuration(long numberOfFrames, float frameRate) {
        return (long) ((1000000 * numberOfFrames) / frameRate);
    }

    public void readAndSetDurationFromFile() throws NotPlayableException {
        try {
            WavParamReader.readParams(this.getPathname());
            this.setDuration(computeDuration(WavParamReader.getNumberOfFrames(), WavParamReader.getFrameRate()));
        } catch (Exception e) {
            throw new NotPlayableException(this.getPathname(), e);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " - " + this.formatDuration();
    }
}