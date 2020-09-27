package com.skillip.lucene.searching;

import com.skillip.lucene.common.TestUtil;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.Assert;
import org.junit.Test;

/**
 * 需要先执行common包下的CreateTestIndex
 * #A 创建QueryParser
 * #B 解析文本
 */
public class BasicSearchingTest {
    @Test
    public void testTerm() throws Exception {
        Directory dir = TestUtil.getBookIndexDirectory(); //A
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);  //B

        Term t = new Term("subject", "ant");
        Query query = new TermQuery(t);
        TopDocs docs = searcher.search(query, 10);
        Assert.assertEquals("Ant in Action",                //C
                1, docs.totalHits.value);                         //C

        t = new Term("subject", "junit");
        docs = searcher.search(new TermQuery(t), 10);
        Assert.assertEquals("Ant in Action, " +                                 //D
                        "JUnit in Action, Second Edition",                  //D
                2, docs.totalHits.value);                                 //D
        reader.close();
        dir.close();
    }
}
