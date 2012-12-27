package org.jenkinsci.backend.go.jpa;

import org.kohsuke.stapler.AttributeKey;
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
        MANAGER_KEY.set(req, this);

        try {
            rsp.forward(delegate,req.getRestOfPath(),req);
        } catch (ServletException e) {
            fail(req);
            throw e;
        } catch (IOException e) {
            fail(req);
            throw e;
        } finally {
            EntityManager em = KEY.get(req);
            if (em!=null) {
                EntityTransaction t = em.getTransaction();
                if (t.isActive()) {
                    if (!t.getRollbackOnly())
                        t.commit();
                    else
                        t.rollback();
                }
                em.close();
                KEY.remove();
            }
        }
    }

    private void fail(StaplerRequest req) {
        EntityManager em = KEY.get(req);
        if (em!=null)
            em.getTransaction().setRollbackOnly();
    }

    public static EntityManager getCurrentEntityManager() {
        StaplerRequest req = Stapler.getCurrentRequest();
        EntityManager em = KEY.get(req);
        if (em==null) {
            em = MANAGER_KEY.get(req).emf.createEntityManager();
            KEY.set(req, em);

            EntityTransaction t = em.getTransaction();
            t.begin();
        }

        return em;
    }

    private static final AttributeKey<EntityManagerShell> MANAGER_KEY = AttributeKey.requestScoped();
    private static final AttributeKey<EntityManager> KEY = AttributeKey.requestScoped();
}
