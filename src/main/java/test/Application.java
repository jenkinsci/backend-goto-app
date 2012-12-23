package test;

import org.kohsuke.stapler.framework.adjunct.AdjunctManager;

import javax.servlet.ServletContext;

/**
 * @author Kohsuke Kawaguchi
 */
public class Application {
    public final AdjunctManager adjuncts;

    public final RecipeCollection recipes = new RecipeCollection();

    public Application(ServletContext context) {
        this.adjuncts = new AdjunctManager(context,getClass().getClassLoader(),"adjuncts");
    }
}
