package test;

import org.codehaus.jackson.map.ObjectMapper;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.StaplerRequest;
import test.backbone.ResourceCollection;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author Kohsuke Kawaguchi
 */
public class InstallationCollection extends ResourceCollection<Installation,Integer> {
    /*package*/ final @Nonnull EntityManager em;

    /**
     * The user who's collection we are managing.
     */
    public final @Nonnull User user;

    /**
     * All installations that belong to the user. Lazy computed.
     */
    private List<Installation> all;

    public InstallationCollection(User user, EntityManager em) {
        this.em = em;
        this.user = user;
    }

    public ObjectMapper getMapper() {
        return objectMapper;
    }

    @Override
    protected Installation create(StaplerRequest req) throws IOException {
        Installation inst = objectMapper.readValue(req.getReader(),Installation.class);
        inst.persist(this);
        return inst;
    }

    @Override
    protected Installation get(Integer id) {
        Installation i = em.find(Installation.class, id);
        if (i.belongsTo(user))   return i;
        return null;    // not users
    }

    @Override
    public void update(Installation resource) {
        Installation existing = em.find(Installation.class, resource.getId());
        if (existing==null || !resource.belongsTo(user) || !existing.belongsTo(user)) {
            em.getTransaction().setRollbackOnly();
            throw HttpResponses.error(403,new SecurityException("Trying to update a non-existent (or someone else's) resource"));
        }
    }

    @Override
    public void delete(Installation resource) throws IOException {
        if (!resource.belongsTo(user))
            throw HttpResponses.error(403,new SecurityException("Trying to delete someone else's resource"));
        em.remove(resource);
    }

    public Iterator<Installation> iterator() {
        if (all==null)
            all = em.createQuery("FROM Installation where owner='" + user.id + "'", Installation.class).getResultList();
        return all.iterator();
    }

    @Override
    protected Integer toID(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(InstallationCollection.class.getName());
}
