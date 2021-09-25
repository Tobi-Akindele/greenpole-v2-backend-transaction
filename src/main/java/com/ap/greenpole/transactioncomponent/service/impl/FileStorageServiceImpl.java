package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.service.FileStorageService;
import com.ap.greenpole.transactioncomponent.service.TransactionService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.FileUtil;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/13/2020 11:48 AM
 */

@Service
public class FileStorageServiceImpl implements FileStorageService {

    final static Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final Path rootTmpUploadDir;
    private final Path rootPmtUploadDir;
    private final String resourceLocation;
    private final String downloadServer;
    private final String blobPath;
    private final String blobContainer;

    public FileStorageServiceImpl(@Value("${greenpole.transaction.component.service.file.location:temp}") String fileStorageLocation,
                                  @Value("${file.tmp.upload.dir}") String tmpUploadDir,
                                  @Value("${file.pmt.upload.dir}") String pmtUploadDir,
                                  @Value("${resource.location}") String resourceLocation,
                                  @Value("${download.server}") String downloadServer,
                                  @Value("${blob.path}") String blobPath,
                                  @Value("${blob.container}") String blobContainer) {

        this.rootTmpUploadDir = Paths.get(System.getProperty("user.home") + tmpUploadDir);
        this.rootPmtUploadDir = Paths.get(System.getProperty("user.home") + pmtUploadDir);
        this.resourceLocation = resourceLocation;
        this.downloadServer = downloadServer;
        this.blobPath = blobPath;
        this.blobContainer = blobContainer;

        try {
            Files.createDirectories(rootTmpUploadDir);
            Files.createDirectories(rootPmtUploadDir);
        } catch (IOException e) {
            logger.error("Could not initialize folder for {} and {}. {}", rootTmpUploadDir, rootPmtUploadDir, e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = String.format("%s_%s", System.currentTimeMillis(),file.getOriginalFilename());
        return save(file, fileName);
    }


    @Override
    public String downloadFile(String downloadPath) throws URISyntaxException {
        String[] splitDownloadPath = downloadPath.split("\\/");
        String fileName = splitDownloadPath[splitDownloadPath.length-1];
        return resolveDownloadLink(fileName);
    }

    public String save(MultipartFile file, String filename) {

        try {
            Path path = this.rootTmpUploadDir;
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(this.blobPath).buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(this.blobContainer);
            BlobClient blobClient = containerClient.getBlobClient(filename);

            Files.copy(file.getInputStream(), path.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            blobClient.uploadFromFile(path.resolve(filename).toAbsolutePath().toString(), true);
            String blobUrl = blobClient.getBlobUrl();
            logger.error("File Uploaded to {}", blobUrl);
            return blobUrl;
        } catch (Exception e) {
            logger.error("Could not store the file in tmpUploadDir. Error occurred {}", e);
            return null;
        }
    }

    public String resolveDownloadLink(String fileName) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(this.blobPath).buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(this.blobContainer);
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            String blobUrl = blobClient.getBlobUrl();
            File destination = new File(String.format("%s/%s", this.rootTmpUploadDir, fileName));
            FileUtils.copyURLToFile(new URL(blobUrl), destination);
            Path filePath = destination.toPath();
            Files.copy(destination.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (Exception e) {
            logger.error("Could not store the file in tmpUploadDir. Error occurred {}", e);
            return null;
        }
    }


}
