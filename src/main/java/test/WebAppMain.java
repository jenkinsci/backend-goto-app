package test;

import org.kohsuke.stapler.framework.AbstractWebAppMain;
import test.jpa.EntityManagerShell;
import test.openid.AuthenticationShell;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletContextEvent;

/**
 * @author Kohsuke Kawaguchi
 */
public class WebAppMain extends AbstractWebAppMain<Application> {
    public WebAppMain() {
        super(Application.class);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
//        WebApp.get(context).facets.add(new StaticViewFacet(".html"));
    }

    @Override
    protected String getApplicationName() {
        return "APP";
    }

    @Override
    protected Object createApplication() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("to-my-jenkins");
        return new AuthenticationShell(new EntityManagerShell(new Application(context),emf));
    }
}
