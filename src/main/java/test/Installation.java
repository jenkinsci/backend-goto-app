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
    private String location;

    public Installation() {
    }

    public Installation(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Installation{" +
                "id=" + id +
                ", url='" + location + '\'' +
                '}';
    }

    @JavaScriptMethod
    public int vote(int x, int y) {
        System.out.println(x+y);
        return x+y;
    }
}
