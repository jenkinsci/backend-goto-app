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

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
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
//        User user = currentSession().authenticate();
//        user.associateToRequest(Stapler.getCurrentRequest());

        new User("kohsuke").associateToRequest(Stapler.getCurrentRequest()); // fake during debug
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
        HttpSession s = req.getSession();
        String NAME = OpenIdSession.class.getName();
        OpenIdSession o = (OpenIdSession) s.getAttribute(NAME);
        if (o==null)
            try {
                s.setAttribute(NAME,o=new OpenIdSession(manager,"http://jenkins-ci.org/account/openid/",req));
            } catch (OpenIDException e) {
                throw HttpResponses.error(e);
            } catch (IOException e) {
                throw HttpResponses.error(e);
            }
        return o;
    }
}
