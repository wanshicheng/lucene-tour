package com.skillip.lucene.common;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class CreateTestIndex {
    public static Document getDocument(String rootDir, File file) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(file));

        Document doc = new Document();

        // category comes from relative path below the base directory
        String category = file.getParent().substring(rootDir.length());    //1
        category = category.replace(File.separatorChar, '/');              //1

        String isbn = props.getProperty("isbn");         //2
        String title = props.getProperty("title");       //2
        String author = props.getProperty("author");     //2
        String url = props.getProperty("url");           //2
        String subject = props.getProperty("subject");   //2

        String pubmonth = props.getProperty("pubmonth"); //2

        System.out.println(title + "\n" + author + "\n" + subject + "\n" + pubmonth + "\n" + category + "\n---------");

        FieldType storedNotAnalyzed = new FieldType(); // 3
        storedNotAnalyzed.setStored(true); // 3
        storedNotAnalyzed.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        storedNotAnalyzed.setTokenized(false); // 3
        storedNotAnalyzed.setOmitNorms(false); // 3
        storedNotAnalyzed.setStoreTermVectors(false); // 3


        doc.add(new Field("isbn", isbn, storedNotAnalyzed)); // 3
        doc.add(new Field("category", category, storedNotAnalyzed)); // 3


        FieldType storedAnalyzedWithPositionsOffsets = new FieldType();
        storedAnalyzedWithPositionsOffsets.setStored(true);
        storedAnalyzedWithPositionsOffsets.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        storedAnalyzedWithPositionsOffsets.setTokenized(true);
        storedAnalyzedWithPositionsOffsets.setOmitNorms(false);
        storedAnalyzedWithPositionsOffsets.setStoreTermVectors(true);
        storedAnalyzedWithPositionsOffsets.setStoreTermVectorPositions(true);
        storedAnalyzedWithPositionsOffsets.setStoreTermVectorOffsets(true);

        doc.add(new Field("title", title, storedAnalyzedWithPositionsOffsets));


        FieldType storedNotAnalyzedNoNormsWithPositionsOffsets = new FieldType();
        storedNotAnalyzedNoNormsWithPositionsOffsets.setStored(true);
        storedNotAnalyzedNoNormsWithPositionsOffsets.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        storedNotAnalyzedNoNormsWithPositionsOffsets.setTokenized(false);
        storedNotAnalyzedNoNormsWithPositionsOffsets.setOmitNorms(false);
        storedNotAnalyzedNoNormsWithPositionsOffsets.setStoreTermVectors(true);
        storedNotAnalyzedNoNormsWithPositionsOffsets.setStoreTermVectorPositions(true);
        storedNotAnalyzedNoNormsWithPositionsOffsets.setStoreTermVectorOffsets(true);

        doc.add(new Field("title2", title.toLowerCase(), storedNotAnalyzedNoNormsWithPositionsOffsets));  // 3


        FieldType storedNotAnalyzedWithPositionsOffsets = new FieldType();
        storedNotAnalyzedWithPositionsOffsets.setStored(true);
        storedNotAnalyzedWithPositionsOffsets.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        storedNotAnalyzedWithPositionsOffsets.setTokenized(false);
        storedNotAnalyzedWithPositionsOffsets.setOmitNorms(false);
        storedNotAnalyzedWithPositionsOffsets.setStoreTermVectors(true);
        storedNotAnalyzedWithPositionsOffsets.setStoreTermVectorPositions(true);
        storedNotAnalyzedWithPositionsOffsets.setStoreTermVectorOffsets(true);

        // split multiple authors into unique field instances
        String[] authors = author.split(",");            // 3
        for (String a : authors) {                       // 3
            doc.add(new Field("author", a, storedNotAnalyzedWithPositionsOffsets));   // 3
        }


        FieldType storedNotAnalyzedNoNorms = new FieldType();
        storedNotAnalyzedNoNorms.setStored(true);
        storedNotAnalyzedNoNorms.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        storedNotAnalyzedNoNorms.setTokenized(false);
        storedNotAnalyzedNoNorms.setOmitNorms(false);
        storedNotAnalyzedNoNorms.setStoreTermVectors(false);

        doc.add(new Field("url", url,  storedNotAnalyzedNoNorms ));   // 3

        doc.add(new Field("subject", subject,storedAnalyzedWithPositionsOffsets)); // 3  //4

        // todo:
//        doc.add(new NumericField("pubmonth",          // 3
//                Field.Store.YES,     // 3
//                true).setIntValue(Integer.parseInt(pubmonth)));   // 3
        doc.add(new StoredField("pubmonth", Integer.parseInt(pubmonth))); // 3


        Date d; // 3
        try { // 3
            d = DateTools.stringToDate(pubmonth); // 3
        } catch (ParseException pe) { // 3
            throw new RuntimeException(pe); // 3
        }                                             // 3
//        doc.add(new NumericField("pubmonthAsDay")      // 3
//                .setIntValue((int) ()));   // 3
        doc.add(new StoredField("pubmonthAsDay", d.getTime() / (1000 * 3600 * 24))); // 3

        FieldType noStoredAnalyzedWithPositionsOffsets = new FieldType();
        noStoredAnalyzedWithPositionsOffsets.setStored(false);
        noStoredAnalyzedWithPositionsOffsets.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        noStoredAnalyzedWithPositionsOffsets.setTokenized(true);
        noStoredAnalyzedWithPositionsOffsets.setOmitNorms(true);
        noStoredAnalyzedWithPositionsOffsets.setStoreTermVectors(true);
        noStoredAnalyzedWithPositionsOffsets.setStoreTermVectorPositions(true);
        noStoredAnalyzedWithPositionsOffsets.setStoreTermVectorOffsets(true);

        for (String text : new String[]{title, subject, author, category}) {           // 3 // 5
            doc.add(new Field("contents", text, noStoredAnalyzedWithPositionsOffsets));    // 3 // 5
        }

        return doc;
    }

    private static String aggregate(String[] strings) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            buffer.append(strings[i]);
            buffer.append(" ");
        }

        return buffer.toString();
    }

    private static void findFiles(List<File> result, File dir) {
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".properties")) {
                result.add(file);
            } else if (file.isDirectory()) {
                findFiles(result, file);
            }
        }
    }

//    private static class MyStandardAnalyzer extends StandardAnalyzer {  // 6
//        public MyStandardAnalyzer(Version matchVersion) {                 // 6
//            super(matchVersion);                                            // 6
//        }                                                                 // 6
//
//        public int getPositionIncrementGap(String field) {                // 6
//            if (field.equals("contents")) {                                 // 6
//                return 100;                                                   // 6
//            } else {                                                        // 6
//                return 0;                                                     // 6
//            }
//        }
//    }

    public static void main(String[] args) throws IOException {
        String indexDir = CoreConstant.INDEX_DIR;
        String dataDir = CoreConstant.DATA_DIR;
        List<File> results = new ArrayList<>();
        findFiles(results, new File(dataDir));
        System.out.println(results.size() + " books to index");
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter w = new IndexWriter(dir, config);
        for (File file : results) {
            Document doc = getDocument(dataDir, file);
            w.addDocument(doc);
        }
        w.close();
        dir.close();
    }
}

/*
  #1 Get category
  #2 Pull fields
  #3 Add fields to Document instance
  #4 Flag subject field
  #5 Add catch-all contents field
  #6 Custom analyzer to override multi-valued position increment
*/
