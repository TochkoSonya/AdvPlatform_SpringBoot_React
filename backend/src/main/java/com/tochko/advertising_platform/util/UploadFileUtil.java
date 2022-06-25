package com.tochko.advertising_platform.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UploadFileUtil {

    public static Byte[] getBytesFromMultipartFile(MultipartFile file) throws IOException {
        Byte[] bytes = new Byte[file.getBytes().length];
        int i = 0;
        for (byte b : file.getBytes()) {
            bytes[i++] = b;
        }
        return bytes;
    }
}
