package test.openid;

import org.kohsuke.stapler.*;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import test.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Captures the in-progress OpenID authentication session and its result.
 *
 * @author Kohsuke Kawaguchi
 */
public class OpenIdSession implements Serializable  {
    // fake serializable just to make Tomcat happy
    private transient final ConsumerManager manager;
    private transient final DiscoveryInformation endpoint;
    private transient String from;
    private transient final String finishUrl;
    private String nick;

    public OpenIdSession(ConsumerManager manager, String openid, StaplerRequest req) throws OpenIDException, IOException {
        this.manager = manager;

        List discoveries = manager.discover(openid);
        endpoint = manager.associate(discoveries);

        StringBuffer buf = req.getRequestURL();
        buf.setLength(buf.length() - req.getRequestURI().length());
        buf.append("/openid/finishLogin");
        finishUrl = buf.toString();
    }

    /**
     * If the user is already authenticated, return the identity information.
     * Otherwise start an authentication session.
     */
    public User authenticate() {
        if (nick==null)
            commence();     // this redirects the user and will never return
        return new User(nick);
    }

    /**
     * Starts the login session.
     */
    public void commence() {
        try {
            this.from = Stapler.getCurrentRequest().getRequestURIWithQueryString();
            final AuthRequest authReq = manager.authenticate(endpoint, finishUrl);

            SRegRequest sregReq = SRegRequest.createFetchRequest();
            sregReq.addAttribute("fullname", false);
            sregReq.addAttribute("nickname", true);
            sregReq.addAttribute("email", false);
            authReq.addExtension(sregReq);

            String url = authReq.getDestinationUrl(true);

            // remember this in the session
            KEY.set(this);

            throw new HttpRedirect(url);
        } catch (OpenIDException e) {
            throw HttpResponses.error(e);
        }
    }

    /**
     * When the identity provider is done with its thing, the user comes back here.
     */
    public HttpResponse doFinishLogin(StaplerRequest request) throws IOException, OpenIDException {
        // extract the parameters from the authentication process
        // (which comes in as a HTTP extend from the OpenID provider)
        ParameterList responselist = new ParameterList(request.getParameterMap());

        // verify the process
        VerificationResult verification = manager.verify(request.getRequestURLWithQueryString().toString(), responselist, endpoint);

        // examine the verification result and extract the verified identifier
        Identifier verified = verification.getVerifiedId();
        if (verified == null)
            throw HttpResponses.error(500,"Failed to login: " + verification.getStatusMsg());

        AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

//        this.nick = authSuccess.getIdentity();

        SRegResponse sr = (SRegResponse)authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
        this.nick = sr.getAttributeValue("nickname");

        return HttpResponses.redirectTo(from);
    }

    public static final AttributeKey<OpenIdSession> KEY = AttributeKey.sessionScoped();

    private static final long serialVersionUID = 1L;
}
