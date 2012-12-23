package test;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import test.backbone.Resource;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Kohsuke Kawaguchi
 */
public class Installation extends Resource {
    @JsonProperty
    private transient String id;
    @JsonProperty
    private String location;

    private transient final InstallationCollection parent;

    /**
     * Loads from existing data on disk.
     */
    public Installation(InstallationCollection parent, File dir) throws IOException {
        this(parent);
        id = dir.getName();
        parent.getMapper().readerForUpdating(this).readValue(new File(dir, DATA_JSON));
    }

    public Installation(InstallationCollection parent) {
        this.parent = parent;
    }

    public String getId() {
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

    public synchronized void persist() throws IOException {
        if (id==null)
            id = UUID.randomUUID().toString().replace("-", "");

        File dir = getRootDir();
        dir.mkdirs();
        parent.getMapper().writeValue(new File(dir,DATA_JSON),this);
    }

    private File getRootDir() {
        return new File(parent.root, id);
    }

    @Override
    public void destroy() throws IOException {
        FileUtils.deleteDirectory(getRootDir());
    }

    private static final String DATA_JSON = "data.json";
}
