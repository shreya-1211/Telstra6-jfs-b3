package com.examplezip.demozip.controller;

import com.examplezip.demozip.service.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DownloadController {
    private Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Autowired
    private DownloadService downloadService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/downloadZipFile")
    public void downloadZipFile(HttpServletResponse response) {
        List<File> listOfFileNames = getListOfFileNames();
        downloadService.downloadZipFile(response, listOfFileNames);
    }

    /**
     * List of file names for testing
     * @return
     */
    private List<File> getListOfFileNames() {
        List<File> listOfFileNames = new ArrayList<>();
        listOfFileNames.add(new File("/Users/shreyabudholiya/Desktop/test/"));
        //listOfFileNames.add(new File("/Users/shreyabudholiya/Desktop/test/running.jpg"));
        //listOfFileNames.add(new File("/Users/shreyabudholiya/Desktop/test/soccer.jpg"));
        return listOfFileNames;
    }

}
