package test.backbone;

import org.codehaus.jackson.map.ObjectMapper;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import test.InstallationCollection;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class Resource implements HttpResponse {

    public HttpResponse doIndex(StaplerRequest request) throws IOException {
        String m = request.getMethod();
        if (m.equals("GET"))
            return _get(request);
        if (m.equals("PUT"))
            return _put(request);
        if (m.equals("DELETE"))
            return _delete(request);

        throw new UnsupportedOperationException(m);
    }

    protected HttpResponse _get(StaplerRequest request) {
        return this;
    }

    protected HttpResponse _put(StaplerRequest request) throws IOException {
        getMapper(request).readerForUpdating(this).readValue(request.getReader());
        getParent(request).update(this);
        return HttpResponses.ok();
    }

    protected HttpResponse _delete(StaplerRequest request) throws IOException {
        getParent(request).delete(this);
        return HttpResponses.ok();
    }

    public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node) throws IOException, ServletException {
        rsp.setContentType("application/json;charset=UTF-8");

        getMapper(req).writeValue(rsp.getWriter(), this);
    }

    protected ObjectMapper getMapper(StaplerRequest req) {
        return getParent(req).objectMapper;
    }

    private ResourceCollection getParent(StaplerRequest req) {
        return req.findAncestorObject(ResourceCollection.class);
    }
}
