package com.skillip.lucene.indexing;

import com.skillip.lucene.analysis.DelimiterPayloadAnalyzer;
import org.apache.lucene.analysis.payloads.FloatEncoder;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.MMapDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class IndexingDemo {
    public static void main(String[] args) throws IOException {
        MMapDirectory dir = new MMapDirectory(Paths.get("D:\\lucene\\index\\demo"));
        IndexWriterConfig conf = new IndexWriterConfig(new DelimiterPayloadAnalyzer('|', new FloatEncoder()));
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(dir, conf);

        Document doc = new Document();
        TextField name = new TextField("title", "Lucene Indexing Demo", Field.Store.YES);
        StoredField url = new StoredField("url", "http://skillip.com");
        TextField content1 = new TextField("content", "Apache Lucene|1.8 is an open source project available for free download.", Field.Store.YES);
        TextField content2 = new TextField("content", "Lucene offers powerful features through a simple API.", Field.Store.YES);
        doc.add(name);
        doc.add(url);
        doc.add(content1);
        doc.add(content2);

        writer.addDocument(doc);
        writer.close();
        dir.close();
    }
}
