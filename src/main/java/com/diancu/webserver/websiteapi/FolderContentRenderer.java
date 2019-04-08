package com.diancu.webserver.websiteapi;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Defines a component that displays the content of a folder resource in a website
 * The default implementations displays the folder content as a HTML page with links to open files or subfolders
  */
public interface FolderContentRenderer {

    default void renderFolderContent(FolderResource folder, OutputStream out) {
        File[] content =  folder.listFiles();
        PrintWriter pw = new PrintWriter(out);
        pw.println("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'><body>");
        pw.println("<h1>" + UIUtils.folderDisplayName(folder.getSourceFile(), folder.isRoot()) + "</h1>");
        pw.println(folder.isRoot() ? ".." :  "<a href='../'>..</a>");
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
    }

    default String getContentType() {
        return "text/html";
    }
}
