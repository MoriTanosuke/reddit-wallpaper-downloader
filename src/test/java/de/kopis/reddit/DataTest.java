package de.kopis.reddit;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class DataTest {
    final String[] URLS = new String[]{"http://i.imgur.com/nZpDO5i.jpg", "http://i.imgur.com/yzFt0u2.jpg", "http://i.imgur.com/5FAjabF.jpg", "http://i.imgur.com/bYtaCHw.jpg"};

    @Test
    public void parseDataFromJson() throws IOException {
        UrlExtractor extractor = new UrlExtractor("src/test/resources");
        InputStream input = getClass().getResourceAsStream("/top.json");
        List listing = extractor.getPosts(input);
        for (int i = 0; i < listing.size(); i++) {
            final String url = extractor.getUrlForPost(listing, i);
            assertEquals((i + 1) + ". URL does not match", URLS[i], url);
        }
    }
}