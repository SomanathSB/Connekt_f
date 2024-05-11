# Connekt_f
A java based file transferring application.

1. **Server-Client Architecture:** 
   - The project uses a server-client model where the server listens for client connections on port 8888. 
   - Each client connection is handled by a separate thread.

2. **Socket Programming:** 
   - Java's ServerSocket and Socket classes are used for communication between the server and clients.

3. **Object Serialization:** 
   - ObjectInputStream and ObjectOutputStream handle object serialization for transferring file-related data.

4. **File Upload and Download:** 
   - The server handles file upload, download, and file listing commands from clients.

5. **Concurrency Control:** 
   - Concurrency is achieved through multi-threading, allowing the server to handle multiple clients simultaneously.

6. **JavaFX GUI:** 
   - The GUI, built with JavaFX, facilitates file upload, download, and displays progress.

7. **Progress Tracking:** 
   - Progress of file transfers is tracked using a ProgressBar in the GUI.

8. **Pause and Resume Functionality:** 
   - Users can pause and resume file transfers.

9. **Error Handling:** 
   - Basic error handling is implemented for IOException and ClassNotFoundException.

10. **Connection Initialization:** 
    - Clients establish connections with the server upon GUI initialization.

11. **Server Logging:** 
    - The server logs important messages for debugging purposes.

12. **File Transfer Directory:** 
    - Both server and clients share a common directory for file storage.
