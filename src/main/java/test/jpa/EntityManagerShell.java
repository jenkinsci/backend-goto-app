package test.jpa;

import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class EntityManagerShell {
    private final EntityManagerFactory emf;
    private final Object delegate;

    public EntityManagerShell(Object delegate, EntityManagerFactory emf) {
        this.emf = emf;
        this.delegate = delegate;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {
        req.setAttribute(MANAGER_KEYNAME,this);

        try {
            rsp.forward(delegate,req.getRestOfPath(),req);
        } catch (ServletException e) {
            fail(req);
            throw e;
        } catch (IOException e) {
            fail(req);
            throw e;
        } finally {
            EntityManager em = getOrNull(req);
            if (em!=null) {
                EntityTransaction t = em.getTransaction();
                if (t.isActive()) {
                    if (!t.getRollbackOnly())
                        t.commit();
                    else
                        t.rollback();
                }
                em.close();
            }
        }
    }

    private void fail(StaplerRequest req) {
        EntityManager em = getOrNull(req);
        if (em!=null)
            em.getTransaction().setRollbackOnly();
    }

    public static EntityManager getCurrentEntityManager() {
        StaplerRequest req = Stapler.getCurrentRequest();
        EntityManager em = getOrNull(req);
        if (em==null) {
            EntityManagerFactory emf = ((EntityManagerShell)req.getAttribute(MANAGER_KEYNAME)).emf;
            em = emf.createEntityManager();
            req.setAttribute(KEYNAME, em);

            EntityTransaction t = em.getTransaction();
            t.begin();
        }

        return em;
    }

    private static EntityManager getOrNull(StaplerRequest req) {
        return (EntityManager) req.getAttribute(KEYNAME);
    }

    private static final String MANAGER_KEYNAME = EntityManager.class.getName()+".manager";
    private static final String KEYNAME = EntityManager.class.getName();
}
