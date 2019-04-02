package com.diancu.webserver.website;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

@Slf4j
public class FileResource implements WebResource {

    private final File sourceFile;
    private final boolean isRoot;

    public FileResource(File sourceFile, boolean isRoot) {

        this.sourceFile = sourceFile;
        this.isRoot = isRoot;
    }

    @Override
    public String getContentType() {
        return getFileContentType(sourceFile);
    }

    private static String getFileContentType(File sourceFile) {
        try {
            return sourceFile.isDirectory() ? "text/html"
                : Files.probeContentType(sourceFile.toPath());
        } catch (IOException e) {
            log.error("error getting content type for file: {}", sourceFile);
            return "application/octetstream";
        }
    }

    @Override
    public long getSize() {
        try {
            return isFolder() ? getFolderInputStream().available() :  sourceFile.length();
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
        return sourceFile.isDirectory();
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        if (isFolder()) {
            return getFolderInputStream();
        } else {
            return new FileInputStream(sourceFile);
        }
    }

    private InputStream getFolderInputStream() {
        log.debug("Generating directory indexing page for folder: {}", sourceFile);
        File[] content =  sourceFile.listFiles();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println("<html><body>");
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
                            .append("<td>").append(FileResource.getFileContentType(file)).append("</a></td>")
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

    //https://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
