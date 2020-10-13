package com.skillip.lucene.searching;

import com.skillip.lucene.common.TestUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.StandardDirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueryParserTest {

    private Analyzer analyzer;
    private Directory dir;
    private DirectoryReader reader;
    private IndexSearcher searcher;

    @Before
    public void setUp() throws Exception {
        analyzer = new WhitespaceAnalyzer();
        dir = TestUtil.getBookIndexDirectory();
        reader = StandardDirectoryReader.open(dir);
        searcher = new IndexSearcher(reader);
    }

    protected void tearDown() throws Exception {
        reader.close();
        dir.close();
    }

    @Test
    public void testQueryParser() throws Exception {
        QueryParser parser = new QueryParser("contents", new SimpleAnalyzer());

        Query query = parser.parse("+JUNIT +ANT -MOCK");
        TopDocs docs = searcher.search(query, 10);
        Assert.assertEquals(1, docs.totalHits.value);
    }

//    public void testToString() throws Exception {
//        BooleanQuery query = new BooleanQuery();
//        query.add(new FuzzyQuery(new Term("field", "kountry")),
//                BooleanClause.Occur.MUST);
//        query.add(new TermQuery(new Term("title", "western")),
//                BooleanClause.Occur.SHOULD);
//        assertEquals("both kinds", "+kountry~0.5 title:western",
//                query.toString("field"));
//    }
//
//    public void testPrefixQuery() throws Exception {
//        QueryParser parser = new QueryParser(Version.LUCENE_30,
//                "category",
//                new StandardAnalyzer(Version.LUCENE_30));
//        parser.setLowercaseExpandedTerms(false);
//        System.out.println(parser.parse("/Computers/technology*").toString("category"));
//    }
//
//    public void testFuzzyQuery() throws Exception {
//        QueryParser parser = new QueryParser(Version.LUCENE_30,
//                "subject", analyzer);
//        Query query = parser.parse("kountry~");
//        System.out.println("fuzzy: " + query);
//
//        query = parser.parse("kountry~0.7");
//        System.out.println("fuzzy 2: " + query);
//    }
//
//    public void testGrouping() throws Exception {
//        Query query = new QueryParser(
//                Version.LUCENE_30,
//                "subject",
//                analyzer).parse("(agile OR extreme) AND methodology");
//        TopDocs matches = searcher.search(query, 10);
//
//        assertTrue(TestUtil.hitsIncludeTitle(searcher, matches,
//                "Extreme Programming Explained"));
//        assertTrue(TestUtil.hitsIncludeTitle(searcher,
//                matches,
//                "The Pragmatic Programmer"));
//    }
//
//    public void testTermQuery() throws Exception {
//        QueryParser parser = new QueryParser(Version.LUCENE_30,
//                "subject", analyzer);
//        Query query = parser.parse("computers");
//        System.out.println("term: " + query);
//    }
//
//    public void testTermRangeQuery() throws Exception {
//        Query query = new QueryParser(Version.LUCENE_30,                        //A
//                "subject", analyzer).parse("title2:[Q TO V]"); //A
//        assertTrue(query instanceof TermRangeQuery);
//
//        TopDocs matches = searcher.search(query, 10);
//        assertTrue(TestUtil.hitsIncludeTitle(searcher, matches,
//                "Tapestry in Action"));
//
//        query = new QueryParser(Version.LUCENE_30, "subject", analyzer)  //B
//                .parse("title2:{Q TO \"Tapestry in Action\"}");    //B
//        matches = searcher.search(query, 10);
//        assertFalse(TestUtil.hitsIncludeTitle(searcher, matches,  // C
//                "Tapestry in Action"));
//    }
//  /*
//    #A Verify inclusive range
//    #B Verify exclusive range
//    #C Exclude Mindstorms book
//  */
//
//    public void testPhraseQuery() throws Exception {
//        Query q = new QueryParser(Version.LUCENE_30,
//                "field",
//                new StandardAnalyzer(
//                        Version.LUCENE_30))
//                .parse("\"This is Some Phrase*\"");
//        assertEquals("analyzed",
//                "\"? ? some phrase\"", q.toString("field"));
//
//        q = new QueryParser(Version.LUCENE_30,
//                "field", analyzer).parse("\"term\"");
//        assertTrue("reduced to TermQuery", q instanceof TermQuery);
//    }
//
//    public void testSlop() throws Exception {
//        Query q = new QueryParser(Version.LUCENE_30,
//                "field", analyzer)
//                .parse("\"exact phrase\"");
//        assertEquals("zero slop",
//                "\"exact phrase\"", q.toString("field"));
//
//        QueryParser qp = new QueryParser(Version.LUCENE_30,
//                "field", analyzer);
//        qp.setPhraseSlop(5);
//        q = qp.parse("\"sloppy phrase\"");
//        assertEquals("sloppy, implicitly",
//                "\"sloppy phrase\"~5", q.toString("field"));
//    }
//
//    public void testLowercasing() throws Exception {
//        Query q = new QueryParser(Version.LUCENE_30,
//                "field", analyzer).parse("PrefixQuery*");
//        assertEquals("lowercased",
//                "prefixquery*", q.toString("field"));
//
//        QueryParser qp = new QueryParser(Version.LUCENE_30,
//                "field", analyzer);
//        qp.setLowercaseExpandedTerms(false);
//        q = qp.parse("PrefixQuery*");
//        assertEquals("not lowercased",
//                "PrefixQuery*", q.toString("field"));
//    }
//
//    public void testWildcard() {
//        try {
//            new QueryParser(Version.LUCENE_30,
//                    "field", analyzer).parse("*xyz");
//            fail("Leading wildcard character should not be allowed");
//        } catch (ParseException expected) {
//            assertTrue(true);
//        }
//    }
//
//    public void testBoost() throws Exception {
//        Query q = new QueryParser(Version.LUCENE_30,
//                "field", analyzer).parse("term^2");
//        assertEquals("term^2.0", q.toString("field"));
//    }
//
//    public void testParseException() {
//        try {
//            new QueryParser(Version.LUCENE_30,
//                    "contents", analyzer).parse("^&#");
//        } catch (ParseException expected) {
//            // expression is invalid, as expected
//            assertTrue(true);
//            return;
//        }
//
//        fail("ParseException expected, but not thrown");
//    }

}
