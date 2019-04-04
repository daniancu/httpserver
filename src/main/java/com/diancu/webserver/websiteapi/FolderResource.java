package com.diancu.webserver.websiteapi;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        log.debug("Generating directory indexing page for folder: {}", sourceFile);
        File[] content =  sourceFile.listFiles();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'><body>");
        pw.println("<h1>" + UIUtils.folderDisplayName(sourceFile, isRoot) + "</h1>");
        pw.println(isRoot ? ".." :  "<a href='../'>..</a>");
        if (content != null && content.length > 0) {
            pw.println("<table>");
            StringBuffer folderRows = new StringBuffer();
            StringBuffer fileRows = new StringBuffer();
            //check folder content and generate separately table rows for folders and files
            for (File file : content) {
                if (file.isDirectory()) {
                    folderRows.append("<tr>")
                            .append("<td><a href='").append(UIUtils.encodeName(file)).append("/'><b>").append(file.getName()).append("</b></a></td>")
                            .append("<td>-</td>")
                            .append("<td>-</td>")
                            .append("</tr>");
                }
                if (file.isFile()) {
                    fileRows.append("<tr>")
                            .append("<td><a href='").append(UIUtils.encodeName(file)).append("'>").append(file.getName()).append("</a></td>")
                            .append("<td>").append(UIUtils.readableFileSize(file.length())).append("</a></td>")
                            .append("<td>").append(UIUtils.getDisplayContentType(file)).append("</a></td>")
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

}
