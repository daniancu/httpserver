package com.diancu.webserver.website;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.DecimalFormat;

@Slf4j
public class UIUtils {
    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
    private static final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };

    //Based on
    //https://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
    static String readableFileSize(long size) {
        if(size <= 0) return "0";
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return decimalFormat.format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    static String encodeName(File file) {
        try {
            return URLEncoder.encode(file.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("escape error", e);
            return file.getName();
        }
    }

    static String folderDisplayName(File folder, boolean isRoot) {
        return isRoot ? "Root" : folder.getName();
    }

    public static String getDisplayContentType(File sourceFile) {
        try {
            return sourceFile.isDirectory() ? "text/html" : Files.probeContentType(sourceFile.toPath());
        } catch (IOException e) {
            log.error("error getting content type for file: {}", sourceFile);
            return "application/octet-stream";
        }
    }
}
