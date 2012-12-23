package test;

import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import test.backbone.ResourceCollection;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Kohsuke Kawaguchi
 */
public class InstallationCollection extends ResourceCollection<Installation,Integer> {
    @Override
    protected Installation get(Integer id) {
        return new Installation(id,"author"+id,"title"+id);
    }

    @Override
    public HttpResponse delete(Installation resource) {
        System.out.println("Deleted "+resource);
        return HttpResponses.ok();
    }

    @Override
    public void onUpdated(Installation resource) {
        System.out.println("Updated to " + resource);
    }

    public Iterator<Installation> iterator() {
        return Arrays.asList(get(1),get(2),get(3)).iterator();
    }

    @Override
    protected Integer toID(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
