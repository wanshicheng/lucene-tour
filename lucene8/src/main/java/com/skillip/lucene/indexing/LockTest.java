package com.skillip.lucene.indexing;

import com.skillip.lucene.common.CoreConstant;
import com.skillip.lucene.common.TestUtil;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LockTest {
    private Directory dir;
    private Path indexPath;
    private File indexDir;

    @Before
    public void setUp() throws IOException {
        indexPath = Paths.get(System.getProperty("java.io.tmpdir", "tmp") +
                CoreConstant.FILE_SEPARATOR + "index");
        indexDir = Files.createDirectories(indexPath).toFile();
        dir = FSDirectory.open(indexPath);
    }

    @Test
    public void testWriteLock() throws IOException {
        IndexWriterConfig conf1 = new IndexWriterConfig(new SimpleAnalyzer());
        conf1.setInfoStream(System.out);
        IndexWriter writer1 = new IndexWriter(dir, conf1);
        IndexWriter writer2 = null;
        try {
            IndexWriterConfig conf2 = new IndexWriterConfig(new SimpleAnalyzer());
            writer2 = new IndexWriter(dir, conf2);
            Assert.fail("We should never reach this point");
        }
        catch (LockObtainFailedException e) {
             e.printStackTrace();
        }
        finally {
            writer1.close();
            Assert.assertNull(writer2);
            TestUtil.rmDir(indexDir);
        }
    }
}
