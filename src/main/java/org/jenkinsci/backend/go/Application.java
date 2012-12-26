package org.jenkinsci.backend.go;

import org.kohsuke.stapler.framework.adjunct.AdjunctManager;
import org.jenkinsci.backend.go.jpa.EntityManagerShell;

import javax.servlet.ServletContext;

/**
 * @author Kohsuke Kawaguchi
 */
public class Application {
    public final AdjunctManager adjuncts;

    public Application(ServletContext context) {
        this.adjuncts = new AdjunctManager(context,getClass().getClassLoader(),"adjuncts");
    }

    public InstallationCollection getInstallations() {
        return new InstallationCollection(User.current(), EntityManagerShell.getCurrentEntityManager());
    }
}
