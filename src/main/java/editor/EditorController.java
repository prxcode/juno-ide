package editor;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.*;

public class EditorController {

    @FXML private TextArea codeArea;
    @FXML private TextArea consoleArea;

    private final String filename = "Main.java";

    @FXML
    public void handleCompile() {
        saveToFile();
        try {
            ProcessBuilder pb = new ProcessBuilder("javac", filename);
            Process process = pb.start();

            String errors = readStream(process.getErrorStream());
            if (errors.isEmpty()) {
                consoleArea.setText("Compilation successful.");
            } else {
                consoleArea.setText("Compilation errors:\n" + errors);
            }
        } catch (IOException e) {
            consoleArea.setText("Error during compilation: " + e.getMessage());
        }
    }

    @FXML
    public void handleRun() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "Main");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String output = readStream(process.getInputStream());
            consoleArea.setText("Program output:\n" + output);
        } catch (IOException e) {
            consoleArea.setText("Error during execution: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.write(codeArea.getText());
        } catch (IOException e) {
            consoleArea.setText("Error saving file: " + e.getMessage());
        }
    }

    private String readStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            output.append(line).append("\n");
        return output.toString();
    }
}
