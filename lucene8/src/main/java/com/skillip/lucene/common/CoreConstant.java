package com.skillip.lucene.common;

public class CoreConstant {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String DATA_DIR = CoreConstant.class.getClassLoader().getResource("").getPath() + "data";
    public static final String INDEX_DIR = "/lucene/index";
}
