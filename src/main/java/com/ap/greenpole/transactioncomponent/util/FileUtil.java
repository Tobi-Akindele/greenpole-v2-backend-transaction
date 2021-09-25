package com.ap.greenpole.transactioncomponent.util;

import com.ap.greenpole.transactioncomponent.config.ApplicationPropertiesConfig;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.service.TransactionService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/10/2020 10:46 AM
 */

@Component
public class FileUtil {

    final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private final ApplicationPropertiesConfig propertiesConfig;


    public FileUtil(ApplicationPropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    public String writeToFileServer(MultipartFile fileToWrite) throws IOException {
        String fileExtension = getFileExtension(fileToWrite);
        String fileName = getFileName(fileToWrite);
        if (fileExtension.equals("xls") || fileExtension.equals("xlsx")) {
            String filePath = propertiesConfig.getFilePath();
//        Path rootTmpUploadDir = Paths.get(System.getProperty("user.home") + filePath);
            Path rootTmpUploadDir = Paths.get(filePath);
            Files.createDirectories(rootTmpUploadDir);
            logger.info("[+] >>>>>>>>>>> {}", rootTmpUploadDir.toString());
            File file = new File(String.format("%s/upload/%s_%s.%s", rootTmpUploadDir, fileName, System.currentTimeMillis(), fileExtension));
            FileUtils.writeByteArrayToFile(file, fileToWrite.getBytes());
            logger.info("[+] File successfully written to file server {}", filePath);
            return file.getAbsolutePath();
        } else {
            logger.info("[-] FileType {} Not Supported", fileExtension);
            throw new IOException("FileType Not Supported");
        }

    }

    public String getFileExtension(MultipartFile fileToWrite) {
        String[] splitExtension = fileToWrite.getOriginalFilename().split("\\.");
        return splitExtension[1];
    }

    public String getFileName(MultipartFile fileToWrite) {
        String[] splitExtension = fileToWrite.getOriginalFilename().split("\\.");
        return splitExtension[0];
    }


}
