package test;

import org.kohsuke.stapler.framework.adjunct.AdjunctManager;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * @author Kohsuke Kawaguchi
 */
public class Application {
    public final AdjunctManager adjuncts;

    public final InstallationCollection installations;

    public Application(ServletContext context) {
        this.adjuncts = new AdjunctManager(context,getClass().getClassLoader(),"adjuncts");
        installations = new InstallationCollection(new File("./data"));
    }
}
