//package com.skillip.lucene.searching;
//
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.TermQuery;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.store.RAMDirectory;
//import org.junit.Test;
//
//public class NearRealTimeTest {
//
//    @Test
//    public void testNearRealTime() throws Exception {
//        RAMDirectory dir = new RAMDirectory();
//        IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
//        IndexWriter writer = new IndexWriter(dir, conf);
//        for(int i=0;i<10;i++) {
//            Document doc = new Document();
//            doc.add(new Field("id", ""+i, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
//            doc.add(new Field("text", "aaa", Field.Store.NO, Field.Index.ANALYZED));
//            writer.addDocument(doc);
//        }
//        IndexReader reader = writer.getReader();                 // #1
//        IndexSearcher searcher = new IndexSearcher(reader);      // #A
//
//        Query query = new TermQuery(new Term("text", "aaa"));
//        TopDocs docs = searcher.search(query, 1);
//        assertEquals(10, docs.totalHits);                        // #B
//
//        writer.deleteDocuments(new Term("id", "7"));             // #2
//
//        Document doc = new Document();                           // #3
//        doc.add(new Field("id",                                  // #3
//                "11",                                  // #3
//                Field.Store.NO,                        // #3
//                Field.Index.NOT_ANALYZED_NO_NORMS));   // #3
//        doc.add(new Field("text",                                // #3
//                "bbb",                                 // #3
//                Field.Store.NO,                        // #3
//                Field.Index.ANALYZED));                // #3
//        writer.addDocument(doc);                                 // #3
//
//        IndexReader newReader = reader.reopen();                 // #4
//        assertFalse(reader == newReader);                        // #5
//        reader.close();                                          // #6
//        searcher = new IndexSearcher(newReader);
//
//        TopDocs hits = searcher.search(query, 10);               // #7
//        assertEquals(9, hits.totalHits);                         // #7
//
//        query = new TermQuery(new Term("text", "bbb"));          // #8
//        hits = searcher.search(query, 1);                        // #8
//        assertEquals(1, hits.totalHits);                         // #8
//
//        newReader.close();
//        writer.close();
//    }
//}
