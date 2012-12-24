package test;

import org.kohsuke.stapler.framework.adjunct.AdjunctManager;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * @author Kohsuke Kawaguchi
 */
public class Application {
    public final AdjunctManager adjuncts;

    public Application(ServletContext context) {
        this.adjuncts = new AdjunctManager(context,getClass().getClassLoader(),"adjuncts");
    }

    public InstallationCollection getInstallations() {
        return new InstallationCollection(User.current().getRootDir());
    }
}
