package com.ap.greenpole.transactioncomponent.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/13/2020 11:48 AM
 */
public interface FileStorageService {

    String storeFile(MultipartFile file);

    String downloadFile(String fileName) throws Exception;
}
