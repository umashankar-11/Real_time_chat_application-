public import javax.sound.sampled.*;
import java.io.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class VoiceMessageApp extends Application {

    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private SourceDataLine sourceDataLine;
    private File audioFile;
    private FileOutputStream fileOutputStream;
    private DataOutputStream dataOutputStream;

    private boolean isRecording = false;
    private Button recordButton;
    private Button playButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set up buttons
        recordButton = new Button("Start Recording");
        playButton = new Button("Play Recorded Message");
        playButton.setDisable(true);

        recordButton.setOnAction(event -> handleRecordButton());
        playButton.setOnAction(event -> handlePlayButton());

        VBox vbox = new VBox(10, recordButton, playButton);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Voice Message App");
        primaryStage.show();
    }

    private void handleRecordButton() {
        if (!isRecording) {
            // Start recording
            try {
                startRecording();
                recordButton.setText("Stop Recording");
                playButton.setDisable(true);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        } else {
            // Stop recording
            stopRecording();
            recordButton.setText("Start Recording");
            playButton.setDisable(false);
        }
        isRecording = !isRecording;
    }

    private void startRecording() throws LineUnavailableException {
        audioFormat = new AudioFormat(16000, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(audioFormat);
        targetDataLine.start();

        audioFile = new File("voiceMessage.wav");
        fileOutputStream = new FileOutputStream(audioFile);
        dataOutputStream = new DataOutputStream(fileOutputStream);

        Thread recordingThread = new Thread(() -> {
            byte[] buffer = new byte[4096];
            while (isRecording) {
                int bytesRead = targetDataLine.read(buffer, 0, buffer.length);
                try {
                    dataOutputStream.write(buffer, 0, bytesRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                targetDataLine.close();
                dataOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        recordingThread.start();
    }

    private void stopRecording() {
        isRecording = false;
    }

    private void handlePlayButton() {
        try {
            playAudio(audioFile);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playAudio(File audioFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(format);
        sourceDataLine.start();

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
            sourceDataLine.write(buffer, 0, bytesRead);
        }

        sourceDataLine.drain();
        sourceDataLine.close();
    }
}

    

