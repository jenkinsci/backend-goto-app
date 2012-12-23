package test.backbone;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.jvnet.tiger_types.Types;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class ResourceCollection<T extends Resource,ID> implements Iterable<T>, HttpResponse {
    /**
     * What's the type of the resource?
     */
    protected final Class<T> resourceType;

    protected final Class<ID> idType;

    /**
     * Used for JSON databinding.
     */
    protected final ObjectMapper objectMapper;

    public ResourceCollection(Class<T> resourceType, Class<ID> idType) {
        // infer the type if necessary
        Type t = Types.getBaseClass(getClass(),ResourceCollection.class);
        if (resourceType==null)
            resourceType = Types.erasure(Types.getTypeArgument(t,0));
        if (idType==null)
            idType = Types.erasure(Types.getTypeArgument(t,1));

        this.resourceType = resourceType;
        this.idType = idType;

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // these three lines weren't enough to automatically pick up private fields for persistence
//        objectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
//        objectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_FIELDS, true);
//        objectMapper.configure(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
    }

    public ResourceCollection() {
        this(null,null);
    }

    public HttpResponse doIndex(StaplerRequest req) throws IOException {
        String method = req.getMethod();
        if (method.equals("POST"))
            return create(req);
        if (method.equals("GET"))
            return this;

        return HttpResponses.ok();
    }

    protected HttpResponse create(StaplerRequest req) throws IOException {
        T res = objectMapper.readValue(req.getReader(),resourceType);
        System.out.println(res);
        return res;
    }

    public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node) throws IOException, ServletException {
        rsp.setContentType("application/json");
        PrintWriter w = rsp.getWriter();
        w.println("[");
        boolean first = true;
        for (T item : this) {
            if (!first)     w.println(",");
            else            first = false;
            objectMapper.writeValue(new NoCloseWriter(w),item);
        }
        w.println("]");
    }


    protected abstract T get(ID id);

    public T getDynamic(String token) {
        return get(toID(token));
    }

    /**
     * If the given token is a valid ID, return its ID value.
     *
     * @return
     *      null if the conversion fails.
     */
    protected abstract ID toID(String token);
    // commons-beanutils was too lax in acceping everything

    /**
     * Called when a resource in this collection is updated.
     */
    public void onUpdated(T resource) {
    }

    /**
     * Called to delete a resource in this collection.
     */
    public abstract HttpResponse delete(T resource);
}
