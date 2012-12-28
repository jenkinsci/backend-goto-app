package org.jenkinsci.backend.go;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.framework.AbstractWebAppMain;
import org.jenkinsci.backend.go.jpa.EntityManagerShell;
import org.kohsuke.stapler.openid.client.AuthenticationShell;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
        Application app = new Application(context);
        AuthenticationShell shell = new AuthenticationShell(new EntityManagerShell(app, emf)) {
            @Override
            protected String getClaimedIdentity(StaplerRequest request) {
                return "https://jenkins-ci.org/account/openid/";
            }
        };
        app.currentUser = shell.key;
        return shell;
    }
}
