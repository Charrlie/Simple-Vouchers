package com.vanished.vouchers.file;

import java.util.HashMap;

public class FileManager {
    private HashMap<String, CustomFile> fileMap = new HashMap<>();

    public void registerFile(CustomFile customFile) {
        fileMap.putIfAbsent(customFile.getFileName(), customFile);
    }

    public CustomFile getFile(String fileName) {
        if (!hasFile(fileName)) {
            return null;
        }

        return fileMap.get(fileName);
    }

    public boolean hasFile(String fileName) {
        return fileMap.containsKey(fileName);
    }
}