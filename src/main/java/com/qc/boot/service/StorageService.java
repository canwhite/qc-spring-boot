package com.qc.boot.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述: storage service
 *
 * @author lijinhua
 * @date 2022/8/24 08:40
 */
public interface StorageService {
    InputStream openInputStream(String uri) throws IOException;
    String store(String extName, InputStream input) throws  IOException;
}
