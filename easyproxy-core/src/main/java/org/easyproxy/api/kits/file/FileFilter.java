package org.easyproxy.api.kits.file;

import java.io.File;

/**
 * Description :
 * Created by xingtianyu on 16-12-26
 * 下午4:20
 * description:
 */

public interface FileFilter {

    String TXT = ".txt";
    String JSON= ".json";
    String HTML = ".html";
    String CSS = ".css";
    String JS = ".js";

    String PNG = ".png";
    String JPG = ".jpg";
    String JPEG = ".jpeg";
    String GIF = ".gif";

    String EOT = ".eot";
    String TTF = ".ttf";
    String WOFF = ".woff";
    String SVG = ".svg";

    String DOCX = ".docx";
    String DOC = ".doc";
    String XLSX = ".xlsx";
    String XLS = ".xls";
    String PDF = ".pdf";
    
    String IMAGEPATTERN = "(.png|.jpg|.jpeg|.gif)";

    String CACHEPATTERN = "(.html|.css|.js|.png|.jpg|.jpeg|.gif|.eot|.ttf|.woff|.svg|)";


    public boolean accept(File file);
}
