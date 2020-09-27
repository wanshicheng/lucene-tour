package com.skillip.lucene.indexing;

import com.skillip.lucene.common.TestUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IndexingDemo {
    protected String[] ids = {"1", "2"};
    protected String[] unindexed = {"Netherlands", "Italy"};
    protected String[] unstored = {"Amsterdam has lots of bridges", "Venice has lots of canals"};
    protected String[] text = {"Amsterdam", "Venice"};

    private static final Path INDEX_PATH = Paths.get("/lucene/index/indexing");
    private Directory directory;

    @Before
    public void before() throws Exception {
        IndexWriter writer = getWriter();

        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            doc.add(new StringField("id", ids[i], Field.Store.YES));
            doc.add(new StringField("country", unindexed[i], Field.Store.YES));
            doc.add(new TextField("contents", unstored[i], Field.Store.NO));
            doc.add(new TextField("city", text[i], Field.Store.YES));

            writer.addDocument(doc);
        }
        writer.close();
    }

    /**
     * 创建IndexWriter对象
     * @return
     * @throws IOException
     */
    private IndexWriter getWriter() throws IOException {
        directory = new MMapDirectory(INDEX_PATH);
        IndexWriterConfig config = new IndexWriterConfig();
        return new IndexWriter(directory, config);
    }

    protected long getHitCount(String fieldName, String searchString) throws IOException {
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        Term term = new Term(fieldName, searchString);
        TermQuery query = new TermQuery(term);
        long hitCount = TestUtil.hitCount(indexSearcher, query);
        reader.close();
        return hitCount;
    }

    @Test
    public void testIndexWriter() throws IOException {
        IndexWriter writer = getWriter();
        /** 需要提前commit??? */
        Assert.assertEquals(ids.length, writer.numRamDocs());            //7
        writer.close();
    }

    @Test
    public void testIndexReader() throws IOException {
//        IndexReader reader = IndexReader.open(directory);
//        Assert.assertEquals(ids.length, reader.maxDoc());             //8
//        Assert.assertEquals(ids.length, reader.numDocs());            //8
//        reader.close();
    }
}
