package lynn;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** Provides a graphical interface for Lynn while reusing the command-line core. */
public class LynnGui extends Application {
    private static final String WINDOW_TITLE = "Lynn";

    private static final double WINDOW_WIDTH = 700;

    private static final double WINDOW_HEIGHT = 500;

    private final ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();

    private Lynn lynn;

    private TextArea conversation;

    private TextField commandField;

    private Button sendButton;

    /** Builds and displays Lynn's main window. */
    @Override
    public void start(Stage stage) {
        PrintStream responseOutput = new PrintStream(responseBuffer, true, StandardCharsets.UTF_8);
        Ui ui = new Ui(responseOutput);
        lynn = new Lynn(ui);

        conversation = new TextArea();
        conversation.setEditable(false);
        conversation.setWrapText(true);

        commandField = new TextField();
        commandField.setPromptText("Enter a Lynn command");
        commandField.setOnAction(event -> sendCommand());

        sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> sendCommand());

        HBox inputRow = new HBox(8, commandField, sendButton);
        HBox.setHgrow(commandField, javafx.scene.layout.Priority.ALWAYS);
        inputRow.setPadding(new Insets(8));

        BorderPane root = new BorderPane();
        root.setCenter(conversation);
        root.setBottom(inputRow);

        ui.showWelcome();
        appendBufferedResponse();

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
    }

    /** Sends the current command to Lynn and appends the response to the conversation. */
    private void sendCommand() {
        String command = commandField.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        conversation.appendText("You: " + command + "\n");
        boolean shouldExit = lynn.respondTo(command);
        appendBufferedResponse();
        commandField.clear();

        if (shouldExit) {
            commandField.setDisable(true);
            sendButton.setDisable(true);
        }
    }

    /** Copies the latest core response into the conversation and clears the buffer. */
    private void appendBufferedResponse() {
        conversation.appendText(responseBuffer.toString(StandardCharsets.UTF_8));
        responseBuffer.reset();
    }
}
