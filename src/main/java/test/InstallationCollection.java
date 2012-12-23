package test;

import org.codehaus.jackson.map.ObjectMapper;
import org.kohsuke.stapler.StaplerRequest;
import test.backbone.ResourceCollection;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author Kohsuke Kawaguchi
 */
public class InstallationCollection extends ResourceCollection<Installation,String> {
    public final File root;

    public InstallationCollection(File root) {
        this.root = root;
        root.mkdirs();
    }

    public ObjectMapper getMapper() {
        return objectMapper;
    }

    @Override
    protected Installation create(StaplerRequest req) throws IOException {
        Installation inst = objectMapper.readerForUpdating(new Installation(this)).readValue(req.getReader());
        inst.persist();
        return inst;
    }

    @Override
    protected Installation get(String id) {
        File dir = new File(root,id);
        if (dir.isDirectory())
            try {
                return new Installation(this,dir);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Failed to load "+dir,e);
            }

        return null;
    }

    @Override
    public void onUpdated(Installation resource) {
        System.out.println("Updated to " + resource);
    }

    public Iterator<Installation> iterator() {
        File[] dirs = root.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory();
            }
        });
        List<Installation> installations = new ArrayList<Installation>();
        if (dirs!=null) {
            for (File d : dirs) {
                Installation v = get(d.getName());
                if (v!=null)
                    installations.add(v);
            }
        }
        return installations.iterator();
    }

    @Override
    protected String toID(String token) {
        if (ID_PATTERN.matcher(token).matches())
            return token;
        else
            return null;    // invalid key
    }

    private static final Logger LOGGER = Logger.getLogger(InstallationCollection.class.getName());
    private static final Pattern ID_PATTERN = Pattern.compile("[0-9a-f]+");
}
