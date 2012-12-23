package test;

import org.kohsuke.stapler.framework.AbstractWebAppMain;

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
        return new Application(context);
    }
}
