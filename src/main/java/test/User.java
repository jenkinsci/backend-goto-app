package test;

import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import test.openid.OpenIdSession;

import java.io.File;

/**
 * Represents the currently logged in user.
 *
 * This is created by {@link OpenIdSession} as a result of the authentication dance.
 *
 * @author Kohsuke Kawaguchi
 */
public final class User {
    /**
     * User ID on jenkins-ci.org
     */
    public final String id;

    public User(String id) {
        this.id = id;
    }

    public File getRootDir() {
        return new File("data/"+id);
    }

    public void associateToRequest(StaplerRequest req) {
        req.setAttribute(KEY,this);
    }

    public static User current() {
        return (User)Stapler.getCurrentRequest().getAttribute(KEY);
    }

    private static final String KEY = User.class.getName();
}
