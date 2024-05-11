import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.Socket;

public class FileTransferClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;

    private ProgressBar progressBar;
    private Label statusLabel;
    private boolean isPaused;

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    // Inside FileTransferClient class
public void clearProgressBar() {
    updateProgressBar(0.0);
}

    // Inside uploadFile method in FileTransferClient
// Inside uploadFile method in FileTransferClient
public void uploadFile(File file) {
    try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
        out.writeObject("UPLOAD");
        out.writeObject(file.getName());
        out.writeObject(file.length());

        try (FileInputStream fileIn = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long fileSize = file.length();

            while ((bytesRead = fileIn.read(buffer)) != -1 && !isPaused) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            
                double progress = (double) totalBytesRead / fileSize;
                System.out.println("Progress: " + progress); // Debug statement
                Platform.runLater(() -> updateProgressBar(progress));
            
                // Introduce a small delay to allow the UI thread to catch up
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            

            if (isPaused) {
                setStatus("Upload paused");
            } else {
                setStatus("File uploaded successfully");
            }
        } finally {
            clearProgressBar(); // Clear progress bar after completion
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    // Inside downloadFile method in FileTransferClient
    // Inside downloadFile method in FileTransferClient
// Inside downloadFile method in FileTransferClient
// Inside downloadFile method in FileTransferClient
public void downloadFile(String fileName, String directory) {
    try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
        out.writeObject("DOWNLOAD");
        out.writeObject(fileName);

        try {
            long fileSize = (long) in.readObject();

            if (fileSize != -1L) {
                // Extract the file name from the provided file path
                String actualFileName = new File(fileName).getName();

                // Create directories if they don't exist
                File saveDirectory = new File(directory);
                if (!saveDirectory.exists()) {
                    saveDirectory.mkdirs();
                }

                try (FileOutputStream fileOut = new FileOutputStream(new File(saveDirectory, actualFileName))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    long totalBytesRead = 0;

                    while (fileSize > 0 && (bytesRead = in.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1 && !isPaused) {
                        fileOut.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        double progress = (double) totalBytesRead / fileSize;
                        updateProgressBar(progress);
                    }

                    // Send acknowledgment to the server
                    String ack = (String) in.readObject();
                    System.out.println("Acknowledgment: " + ack); // Debug statement
                
                    
                    if (isPaused) {
                        setStatus("Download paused");
                    } else {
                        setStatus("File downloaded successfully");
                        Platform.runLater(() -> clearProgressBar());
                    }
                }
            } else {
                setStatus("File does not exist on the server");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Platform.runLater(() -> clearProgressBar()); // Clear progress bar after completion
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    


    public void pauseTransfer() {
        isPaused = true;
        setStatus("Transfer paused");
    }

    public void resumeTransfer() {
        isPaused = false;
        setStatus("Resuming transfer");
    }

    private void updateProgressBar(double progress) {
        // Update JavaFX UI components on the JavaFX Application Thread
        Platform.runLater(() -> progressBar.setProgress(progress));
    }   

    private void setStatus(String status) {
        Platform.runLater(() -> statusLabel.setText(status));
    }
}