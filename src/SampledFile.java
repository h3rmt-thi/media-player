import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
    private long duration;

    SampledFile(String path) {
        super(path);
    }

    SampledFile() {
        super();
    }

    public static String timeFormatter(long timeInMicroSeconds) {
        if (timeInMicroSeconds < 0) {
            throw new IllegalArgumentException("Time must be greater than zero");
        }
        long totalSeconds = timeInMicroSeconds / 1_000_000;

        // Extract minutes and seconds
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (minutes >= 100) {
            throw new IllegalArgumentException("Minutes must be less than 100");
        }
        // Format the output as "mm-ss"
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void play() {
        BasicPlayer.play(this.getPathname());
    }

    @Override
    public void togglePause() {
        BasicPlayer.togglePause();
    }

    @Override
    public void stop() {
        BasicPlayer.stop();
    }

    @Override
    public String formatDuration() {
        return timeFormatter(this.getDuration());
    }

    @Override
    public String formatPosition() {
        return timeFormatter(BasicPlayer.getPosition());
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
