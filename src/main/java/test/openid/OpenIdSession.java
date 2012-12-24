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
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class OpenIdSession {
    private final ConsumerManager manager;
    private final DiscoveryInformation endpoint;
    private String from;
    private final String finishUrl;
    private String nick;

    public OpenIdSession(ConsumerManager manager, String openid, StaplerRequest req) throws OpenIDException, IOException {
        this.manager = manager;

        List discoveries = manager.discover(openid); // somehow this hangs
        endpoint = manager.associate(discoveries);
//        endpoint = manager.associate(Collections.singletonList(
//            new DiscoveryInformation(new URL("https://jenkins-ci.org/account/openid/entryPoint"),new UrlIdentifier("http://jenkins-ci.org/account/openid/"),DiscoveryInformation.OPENID10)
//        ));

        StringBuffer buf = req.getRequestURL();
        buf.setLength(buf.length() - req.getRequestURI().length());
        buf.append("/openid/finishLogin");
        finishUrl = buf.toString();
    }

    /**
     * Starts the login session.
     */
    public void commence() {
        try {
            this.from = requestUri(Stapler.getCurrentRequest());
            final AuthRequest authReq = manager.authenticate(endpoint, finishUrl);

            SRegRequest sregReq = SRegRequest.createFetchRequest();
            sregReq.addAttribute("fullname", false);
            sregReq.addAttribute("nickname", true);
            sregReq.addAttribute("email", false);
            authReq.addExtension(sregReq);

            String url = authReq.getDestinationUrl(true);

            // remember this in the session
            Stapler.getCurrentRequest().getSession().setAttribute(SESSION_NAME,this);

            throw new HttpRedirect(url);
        } catch (OpenIDException e) {
            throw HttpResponses.error(e);
        }
    }

    String requestUri(StaplerRequest req) {
        StringBuilder from = new StringBuilder(req.getRequestURI());
        if (req.getQueryString()!=null)
            from.append('?').append(req.getQueryString());
        return from.toString();
    }

    String requestUrl(StaplerRequest req) {
        StringBuffer from = req.getRequestURL();
        if (req.getQueryString()!=null)
            from.append('?').append(req.getQueryString());
        return from.toString();
    }

    /**
     * When the identity provider is done with its thing, the user comes back here.
     */
    public HttpResponse doFinishLogin(StaplerRequest request) throws IOException, OpenIDException {
        // extract the parameters from the authentication process
        // (which comes in as a HTTP extend from the OpenID provider)
        ParameterList responselist = new ParameterList(request.getParameterMap());

        // verify the process
        VerificationResult verification = manager.verify(requestUrl(request), responselist, endpoint);

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

    public boolean isAuthenticated() {
        return nick!=null;
    }

    public User authenticate() {
        if (nick==null)
            commence();     // this redirects the user and will never return
        return new User(nick);
    }

    private static final String SESSION_NAME = OpenIdSession.class.getName();
}
