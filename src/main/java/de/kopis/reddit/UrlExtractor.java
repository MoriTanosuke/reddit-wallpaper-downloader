package de.kopis.reddit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlExtractor {
    private static final String[] ACCEPTABLE_EXTENSIONS = new String[] {".jpg", ".png"};
    private static final Gson gson = new GsonBuilder().create();
    private final File directory;

    public static void main(String... args) {
        if(args.length != 2) {
            System.err.println("Wrong number of arguments provided!");
            System.out.println("USAGE: " + UrlExtractor.class.getName() + " <target_directory> <URL_to_JSON>");
            System.out.println("target_directory\t\tProvide directory to download to, i.e. 'C:\\pictures\\reddit'");
            System.out.println("URL_to_JSON\t\tProvide URL of subreddit, i.e. 'https://www.reddit.com/me/m/wallpapers/'");
            System.exit(-1);
        }

        String directory = args[0];
        String url = sanitize(args[1]) + "/top.json";
        new UrlExtractor(directory).run(url);
    }

    public UrlExtractor(String directory) {
        this.directory = new File(directory);
        this.directory.mkdirs();
    }

    private static String sanitize(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public void run(String url) {
        System.out.println("Reading URLs from " + url);
        try {
            URL u = new URL(url);
            List posts = getPosts(u.openStream());
            for (int i = 0; i < posts.size(); i++) {
                final String imageUrl = getUrlForPost(posts, i);
                // check if image - by extension
                if(isImage(imageUrl)) {
                    // TODO maybe check size?
                    download(directory, imageUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void download(File targetDirectory, String url) throws IOException {
        if(!targetDirectory.canWrite() || !targetDirectory.isDirectory()) throw new RuntimeException("Can not download to " + targetDirectory);

        String fileName = getFilename(url);
        URLConnection connection = new URL(url).openConnection();
        File file = new File(targetDirectory, fileName);
        System.out.println("Downloading " + url + " to " + file);
        try(InputStream stream = connection.getInputStream();
            BufferedOutputStream outs = new BufferedOutputStream(new FileOutputStream(file));
        ) {

            int len;
            byte[] buf = new byte[1024];
            while ((len = stream.read(buf)) > 0) {
                outs.write(buf, 0, len);
            }
        }
    }

    private String getFilename(String url) {
        return url.substring(url.lastIndexOf('/'));
    }

    private boolean isImage(String imageUrl) {
        for (String extension : ACCEPTABLE_EXTENSIONS) {
            if(imageUrl.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    public String getUrlForPost(List listing, int postIndex) {
        Map post = getPost(listing, postIndex);
        return getUrl(post);
    }

    private String getUrl(Map post) {
        return (String) ((Map) post.get("data")).get("url");
    }

    private Map getPost(List listing, int position) {
        return (Map) listing.get(position);
    }

    public List getPosts(InputStream input) {
        Map obj = parse(input);
        Map data = (Map) obj.get("data");
        return (List) data.get("children");
    }

    private Map parse(InputStream input) {
        return gson.fromJson(new InputStreamReader(input), HashMap.class);
    }
}