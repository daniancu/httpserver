package com.diancu.webserver.website;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

@Slf4j
public class FolderResource implements WebResource {

    private final File sourceFile;
    private final boolean isRoot;

    public FolderResource(File sourceFile, boolean isRoot) {

        this.sourceFile = sourceFile;
        this.isRoot = isRoot;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    private static String getFileContentType(File sourceFile) {
        try {
            return sourceFile.isDirectory() ? "text/html"
                : Files.probeContentType(sourceFile.toPath());
        } catch (IOException e) {
            log.error("error getting content type for file: {}", sourceFile);
            return "application/octet-stream";
        }
    }

    @Override
    public long getSize() {
        try {
            return getFolderInputStream().available();
        } catch (IOException e) {
            log.debug("Error getting folder size", e);
            return 0;
        }
    }

    @Override
    public Path getPath() {
        return Paths.get(sourceFile.getAbsolutePath());
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public InputStream getInputStream() {
        return getFolderInputStream();
    }

    private InputStream getFolderInputStream() {
        log.debug("Generating directory indexing page for folder: {}", sourceFile);
        File[] content =  sourceFile.listFiles();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'><body>");
        pw.println("<h1>" + folderName() + "</h1>");
        pw.println(isRoot ? ".." :  "<a href='../'>..</a>");
        if (content != null && content.length > 0) {
            pw.println("<table>");
            StringBuffer folderRows = new StringBuffer();
            StringBuffer fileRows = new StringBuffer();
            //check folder content and generate separately table rows for folders and files
            for (File file : content) {
                if (file.isDirectory()) {
                    folderRows.append("<tr>")
                            .append("<td><a href='").append(file.getName()).append("/'><b>").append(file.getName()).append("</b></a></td>")
                            .append("<td>-</td>")
                            .append("<td>-</td>")
                            .append("</tr>");
                }
                if (file.isFile()) {
                    fileRows.append("<tr>")
                            .append("<td><a href='").append(file.getName()).append("'>").append(file.getName()).append("</a></td>")
                            .append("<td>").append(readableFileSize(file.length())).append("</a></td>")
                            .append("<td>").append(FolderResource.getFileContentType(file)).append("</a></td>")
                            .append("</tr>");
                }
            }
            //add folders rows first, then file rows
            pw.println(folderRows);
            pw.println(fileRows);
            pw.println("</table>");
        } else {
            pw.println("<h3>No files found</h3>");
        }
        pw.println("</body></html>");
        pw.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private String folderName() {
        return isRoot ? "Root" : sourceFile.getName();
    }

    //Based on
    //https://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
    private static final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");

    static String readableFileSize(long size) {
        if(size <= 0) return "0";
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return decimalFormat.format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
