package test_system.service;

import org.springframework.stereotype.Service;
import test_system.data.FileData;
import test_system.exception.CustomRuntimeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GalleryService {
    private final static String GALLERY_FOLDER_NAME = "gallery";
    private final static Path PATH_TO_GALLERY_FOLDER = LabService.STATIC_FOLDER_PATH.resolve(GALLERY_FOLDER_NAME);

    public List<FileData> getFiles() {
        try (Stream<Path> paths = Files.walk(PATH_TO_GALLERY_FOLDER)) {
            return paths
                    .filter(v -> Files.isRegularFile(v))
                    .filter(v -> {
                        try {
                            return Files.probeContentType(v).contains("image");
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .map(
                            p -> new FileData(p.getFileName().toString(), "/" + GALLERY_FOLDER_NAME + "/" + p.getFileName())
                    )
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            throw new CustomRuntimeException("Error with reading files in folder", e);
        }
    }
}
