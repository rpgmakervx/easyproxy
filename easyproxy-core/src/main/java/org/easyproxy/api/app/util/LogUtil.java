package org.easyproxy.api.app.util;

import org.easyarch.netpet.kits.file.FileKits;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xingtianyu on 17-5-10
 * 下午10:57
 * description:
 */

public class LogUtil {

    public static List<File> getLogs(){
        return FileKits.filter("/home/code4j/IDEAWorkspace/easyproxy/logs", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                Pattern pattern = Pattern.compile("access\\.(\\d){4}-(\\d){2}-(\\d){2}\\.log");
                Matcher matcher = pattern.matcher(pathname.getName());
                return matcher.matches();
            }
        });
    }

    public static List<String> getAllContents() throws Exception {
        List<File> logs = getLogs();
        List<String> contents = new ArrayList<>();
        for (File file:logs){
            String content = FileKits.cat(file);
            Collections.addAll(contents, content.split("\\n"));
        }
        return contents;
    }
}
