// 代码生成时间: 2025-09-23 05:18:16
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class to organize folder structure.
 * This class helps to move files from a source directory to a destination directory based on file type.
 */
public class FolderStructureOrganizer {

    private static final String SOURCE_DIR = "/path/to/source";
    private static final String DESTINATION_DIR = "/path/to/destination";

    /**
     * Entry point of the program.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            // Initialize FolderStructureOrganizer instance
            FolderStructureOrganizer organizer = new FolderStructureOrganizer();

            // Organize folder structure
            organizer.organizeFolders();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Organize folders by moving files to their respective directories based on file type.
     * @throws IOException If an I/O error occurs.
     */
    public void organizeFolders() throws IOException {
        File sourceDirectory = new File(SOURCE_DIR);
        File[] files = sourceDirectory.listFiles();

        if (files == null) {
            throw new IOException("Error reading files from source directory.");
        }

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

                // Define destination directories based on file extension
                String destinationPath = DESTINATION_DIR + File.separator + fileExtension;
                File destinationDirectory = new File(destinationPath);

                // Create destination directory if it does not exist
                if (!destinationDirectory.exists() && !destinationDirectory.mkdirs()) {
                    throw new IOException("Error creating destination directory.");
                }

                // Move file to the destination directory
                Files.move(file.toPath(), destinationDirectory.toPath().resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
