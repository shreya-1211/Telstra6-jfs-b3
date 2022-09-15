package com.examplezip.demozip.service.impl;

import com.examplezip.demozip.service.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.FileCopyUtils.BUFFER_SIZE;

@Service
public class DownloadServiceImpl implements DownloadService {
    private Logger logger = LoggerFactory.getLogger(DownloadServiceImpl.class);

    @Override
    public void downloadZipFile(HttpServletResponse response, List<File> listOfFileNames) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {

            for (File file : listOfFileNames) {
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                if (file.isDirectory()) {
                    zipDirectory(file, file.getName(), zipOutputStream);
                    StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                } else {
                    zipFile(file, zipOutputStream);
                    StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                }
            }
            zipOutputStream.finish();


        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void zipFile(File file, ZipOutputStream zipOutputStream) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        long bytesRead = 0;
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = bis.read(bytesIn)) != -1) {
            zipOutputStream.write(bytesIn, 0, read);
            bytesRead += read;
        }
        zipOutputStream.closeEntry();
    }

    private void zipDirectory(File file, String name, ZipOutputStream zipOutputStream) throws IOException {
        for (File files : file.listFiles()) {
            if (files.isDirectory()) {
                zipDirectory(files, name + "/" + files.getName(), zipOutputStream);
                continue;
            }
            zipOutputStream.putNextEntry(new ZipEntry(name + "/" + files.getName()));
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(files));
            long bytesRead = 0;
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = bis.read(bytesIn)) != -1) {
                zipOutputStream.write(bytesIn, 0, read);
                bytesRead += read;
            }

            zipOutputStream.closeEntry();
        }
    }

    }




