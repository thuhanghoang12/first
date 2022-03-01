package com.first.demo.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService {

    private final Path storageForder = Paths.get("uploads");

    public ImageStorageService() {
        try {
            Files.createDirectories(storageForder);
        } catch (IOException exception) {
            throw new RuntimeException("Cannot initialize storage", exception);
        }
    }

    private boolean isEmptyFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg"})
                .contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("haha");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            if (!isEmptyFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            float fileSizeInMegabytes = file.getSize() / 1_000_000;
            if (fileSizeInMegabytes > 20.0f) {
                throw new RuntimeException("File must be <= 20MB");
            }

            String fileExtention = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtention;
            Path destinationFilePath = this.storageForder.resolve(
                            Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageForder.toAbsolutePath())) {
                throw new RuntimeException(
                        "Cannot store file outside current directoty.");
            }
            try (InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            }
            return generatedFileName;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to store flie", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public byte[] readFileContent(String fileName) {
        return new byte[0];
    }
}
