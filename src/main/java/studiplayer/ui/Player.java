package studiplayer.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SampledFile;
import studiplayer.audio.SortCriterion;
import studiplayer.basic.BasicPlayer;

import java.io.File;
import java.net.URL;

/**
 * +DEFAULT_PLAYLIST : String
 * -PLAYLIST_DIRECTORY : String
 * -INITIAL_PLAY_TIME_LABEL : String
 * -NO_CURRENT_SONG : String
 * <p>
 * -playList : PlayList
 * -useCertPlayList : boolean
 * -playButton : Button
 * -pauseButton : Button
 * -stopButton : Button
 * -nextButton : Button
 * <p>
 * -playListLabel : Label
 * -playTimeLabel : Label
 * -currentSongLabel : Label
 * -sortChoiceBox : ChoiceBox
 * -searchTextField : TextField
 * -filterButton : Button
 * <p>
 * +setUseCertPlayList(value : boolean)
 * +loadPlayList(pathname : String)
 * +createButton(iconfile : String)
 */
public class Player extends Application {
    public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
    private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
    @SuppressWarnings("unused")
    private static final String PLAYLIST_DIRECTORY = "playlists/";
    private static final String NO_CURRENT_SONG = " - ";
    private PThread player;
    private PlayList playList;
    private boolean useCertPlayList = false;
    private Button playButton;
    private Button pauseButton;
    @SuppressWarnings("FieldCanBeLocal")
    private Button nextButton;
    private Button stopButton;
    private Label playTimeLabel;
    @SuppressWarnings("FieldCanBeLocal")
    private Label playListLabel;
    private Label currentSongLabel;
    private ChoiceBox<SortCriterion> sortChoiceBox;
    private TextField searchTextField;
    @SuppressWarnings("FieldCanBeLocal")
    private Button filterButton;
    private SongTable songTable;

    public static void main(String[] args) {
        launch(args);
    }

    public void setUseCertPlayList(boolean useCertPlayList) {
        this.useCertPlayList = useCertPlayList;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("APA Player");
        stage.setResizable(true);
        stage.setWidth(600);
        stage.setHeight(400);
        stage.centerOnScreen();

        // File chooser for Playlist if not set
        if (!useCertPlayList) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Playlist");
            File file = fileChooser.showOpenDialog(stage);
            loadPlayList(file == null ? null : file.getAbsolutePath());
        } else {
            loadPlayList(null);
//            loadPlayList(PLAYLIST_DIRECTORY + "playList.cert.m3u");
        }

        BorderPane bp = new BorderPane();
        searchTextField = new TextField();
        sortChoiceBox = new ChoiceBox<>();
        sortChoiceBox.setValue(SortCriterion.DEFAULT);
        sortChoiceBox.getItems().addAll(SortCriterion.DEFAULT, SortCriterion.ALBUM, SortCriterion.AUTHOR, SortCriterion.DURATION, SortCriterion.TITLE);
        filterButton = new Button("Anzeigen");
        filterButton.setOnAction(event -> {
            playList.setSortCriterion(sortChoiceBox.getValue());
            playList.setSearch(searchTextField.getText());
            songTable.refreshSongs();
        });

        HBox htbox = new HBox();
        htbox.setSpacing(5);
        htbox.setAlignment(Pos.CENTER);
        htbox.getChildren().addAll(sortChoiceBox, filterButton);
        VBox topvbox = new VBox();
        topvbox.setSpacing(5);
        topvbox.getChildren().addAll(searchTextField, htbox);
        bp.setTop(new TitledPane("Filter", topvbox));

        songTable = new SongTable(playList);
        songTable.setRowSelectionHandler(event -> {
            System.out.println("Row clicked");
            if (player != null)
                player.terminate();
            playList.jumpToAudioFile(songTable.getSelectionModel().getSelectedItem().getAudioFile());
            setButtonDisables(true, false, false);
            player = new PThread();
            player.start();
        });
        songTable.refreshSongs();
        bp.setCenter(songTable);

        playButton = createButton("play.jpg");
        playButton.setOnAction(event -> {
            System.out.println("Play Button clicked");
            setButtonDisables(true, false, false);
            player = new PThread();
            player.start();
        });

        pauseButton = createButton("pause.jpg");
        pauseButton.setDisable(true);
        pauseButton.setOnAction(event -> {
            System.out.println("Pause Button clicked");
            setButtonDisables(true, false, false);
            player.pause();
        });

        stopButton = createButton("stop.jpg");
        stopButton.setDisable(true);
        stopButton.setOnAction(event -> {
            System.out.println("Stop Button clicked");
            setButtonDisables(false, true, true);
            player.terminate();
        });

        nextButton = createButton("next.jpg");
        nextButton.setOnAction(event -> {
            System.out.println("Next Button clicked");
            setButtonDisables(true, false, false);
            playList.nextSong();
            player = new PThread();
            player.start();
        });

        playTimeLabel = new Label();
        playListLabel = new Label();
        currentSongLabel = new Label();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(playListLabel, currentSongLabel, playTimeLabel, hbox);
        bp.setBottom(vbox);

        stage.setScene(new Scene(bp, 600, 400));
        updateSongInfo(null);
        stage.show();
    }

    private void updateSongInfo(AudioFile audioFile) {
        if (audioFile == null) {
            playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
            currentSongLabel.setText(NO_CURRENT_SONG);
        } else {
            currentSongLabel.setText(audioFile.toString());
        }
    }

    public void loadPlayList(String pathname) {
        if (pathname == null || pathname.isEmpty()) {
            this.playList = new PlayList(DEFAULT_PLAYLIST);
        } else {
            this.playList = new PlayList(pathname);
        }
        if (songTable != null) {
            songTable.refreshSongs();
        }
    }

    private void setButtonDisables(boolean play, boolean pause, boolean stop) {
        playButton.setDisable(play);
        pauseButton.setDisable(pause);
        stopButton.setDisable(stop);
    }

    private Button createButton(String iconfile) {
        Button button = null;
        try {
            URL url = getClass().getResource("/icons/" + iconfile);
//            URL url = getClass().getResource("/" + iconfile);
//            URL url = getClass().getResource(iconfile);
            assert url != null;
            Image icon = new Image(url.toString());
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button = new Button("", imageView);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setStyle("-fx-background-color: #ffffff;");
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.out.println("Image " + "icons/" + iconfile + " not found!");
            System.exit(1);
        }
        return button;
    }

    private class TThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Platform.runLater(() -> playTimeLabel.setText(SampledFile.timeFormatter(BasicPlayer.getPosition())));
                try {
                    //noinspection BusyWait
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                    return;
                }
            }
        }
    }

    private class PThread extends Thread {
        private TThread timerThread;
        private boolean stopped = false;

        public void terminate() {
            this.stopped = true;
            if (timerThread != null)
                timerThread.interrupt();
            BasicPlayer.stop();
            Platform.runLater(() -> updateSongInfo(null));
        }

        @Override
        public void run() {
            timerThread = new TThread();
            timerThread.start();
            while (!stopped) {
                AudioFile caf = playList.currentAudioFile();
                if (caf == null) {
                    Platform.runLater(() -> updateSongInfo(null));
                    break;
                }
                Platform.runLater(() -> updateSongInfo(caf));
                songTable.selectSong(caf);
                BasicPlayer.play(caf.getPathname());
                // if stopped during playing don't skip to next song
                if (!stopped) {
                    playList.nextSong();
                }
            }
        }

        public void pause() {
            BasicPlayer.togglePause();
            if (timerThread.isAlive()) {
                timerThread.interrupt();
            } else {
                timerThread = new TThread();
                timerThread.start();
            }
        }

//        public void next() {
//            BasicPlayer.stop();
//        }
    }
}
