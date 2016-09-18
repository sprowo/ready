package com.prowo.ydnamic.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileUtil {
    public static String getUniqueFileNameInDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }
        if (file.isFile()) {
            return "";
        }
        String[] fileNames = file.list();
        if (fileNames != null && fileNames.length == 1) {
            return fileNames[0];
        }
        return "";
    }

    public static File createCSVFile(List<Map<String, Object>> exportData, LinkedHashMap<String, Object> headerMap,
            String outPutPath, String filename) {

        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            csvFile = new File(outPutPath + filename + ".csv");
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            csvFileOutputStream = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"),
                    1024);
            // 写入文件头部
            for (Iterator<Entry<String, Object>> propertyIterator = headerMap.entrySet().iterator(); propertyIterator
                    .hasNext();) {
                Entry<String, Object> propertyEntry = propertyIterator.next();
                csvFileOutputStream.write("\"" + propertyEntry.getValue() + "\"");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();

            // 写入文件内容
            for (Iterator<Map<String, Object>> iterator = exportData.iterator(); iterator.hasNext();) {
                Map<String, Object> row = iterator.next();
                for (Iterator<Entry<String, Object>> propertyIterator = row.entrySet().iterator(); propertyIterator
                        .hasNext();) {
                    Entry<String, Object> propertyEntry = propertyIterator.next();
                    csvFileOutputStream.write("\"" + propertyEntry.getValue() + "\"");
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (csvFileOutputStream != null) {
                    csvFileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

}
