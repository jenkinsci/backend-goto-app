package test;

import org.codehaus.jackson.annotate.JsonProperty;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import test.backbone.Resource;

/**
 * @author Kohsuke Kawaguchi
 */
public class Installation extends Resource {
    @JsonProperty
    private int id;
    private String author, title;

    public Installation() {
    }

    public Installation(int id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "author='" + author + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @JavaScriptMethod
    public int vote(int x, int y) {
        System.out.println(x+y);
        return x+y;
    }
}
