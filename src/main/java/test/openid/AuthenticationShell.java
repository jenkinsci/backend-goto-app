package test.openid;

import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerFallback;
import org.kohsuke.stapler.StaplerRequest;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import test.User;

import java.io.IOException;

/**
 * Protects the delegated URL-bound object via OpenID.
 *
 * <p>
 * If the current request isn't authenticated, send the browser to OpenID authentication sequence
 * with https://jenkins-ci.org/
 *
 * @author Kohsuke Kawaguchi
 */
public class AuthenticationShell implements StaplerFallback {
    private final ConsumerManager manager;
    private final Object delegate;

    public AuthenticationShell(Object delegate) throws ConsumerException {
        this.delegate = delegate;

        manager = new ConsumerManager();
        manager.setAssociations(new InMemoryConsumerAssociationStore());
        manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
    }

    public Object getStaplerFallback() {
        if (Boolean.getBoolean("openID.skip")) {
            new User("debug").associateToRequest(Stapler.getCurrentRequest()); // fake during debug
        } else {
            User user = currentSession().authenticate();
            user.associateToRequest(Stapler.getCurrentRequest());
        }
        return delegate;
    }

    /**
     * Maps the login session to URL.
     */
    public OpenIdSession getOpenid() {
        return currentSession();
    }

    private OpenIdSession currentSession() {
        StaplerRequest req = Stapler.getCurrentRequest();
        OpenIdSession o = OpenIdSession.KEY.get(req);
        if (o==null)
            try {
                OpenIdSession.KEY.set(req,o=new OpenIdSession(manager,"http://jenkins-ci.org/account/openid/",req));
            } catch (OpenIDException e) {
                throw HttpResponses.error(e);
            } catch (IOException e) {
                throw HttpResponses.error(e);
            }
        return o;
    }
}
