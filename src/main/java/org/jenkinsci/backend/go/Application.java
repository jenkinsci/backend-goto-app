package org.jenkinsci.backend.go;

import org.kohsuke.stapler.AttributeKey;
import org.kohsuke.stapler.framework.adjunct.AdjunctManager;
import org.jenkinsci.backend.go.jpa.EntityManagerShell;
import org.kohsuke.stapler.openid.client.OpenIDIdentity;

import javax.servlet.ServletContext;

/**
 * @author Kohsuke Kawaguchi
 */
public class Application {
    public final AdjunctManager adjuncts;
    public AttributeKey<OpenIDIdentity> currentUser;

    public Application(ServletContext context) {
        this.adjuncts = new AdjunctManager(context,getClass().getClassLoader(),"adjuncts");
    }

    public InstallationCollection getInstallations() {
        return new InstallationCollection(currentUser.get(), EntityManagerShell.getCurrentEntityManager());
    }
}
