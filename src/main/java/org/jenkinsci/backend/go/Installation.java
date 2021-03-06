package org.jenkinsci.backend.go;

import org.codehaus.jackson.annotate.JsonProperty;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.jenkinsci.backend.go.backbone.Resource;
import org.kohsuke.stapler.openid.client.OpenIDIdentity;

import javax.persistence.*;

/**
 * @author Kohsuke Kawaguchi
 */
@Entity
public class Installation extends Resource {
    @JsonProperty
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @JsonProperty
    @Column
    private String location;

    @Column
    private String owner;

    public Installation() {
    }

    public Installation(String location, String owner) {
        this.location = location;
        this.owner = owner;
    }

    /**
     * Creates a new persisted instance.
     */
    public void persist(InstallationCollection parent) {
        this.owner = parent.user.getNick();
        parent.em.persist(this);
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public boolean belongsTo(OpenIDIdentity owner) {
        return this.owner.equals(owner.getNick());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JavaScriptMethod // testing this functionality. not actually being used.
    public int vote(int x, int y) {
        return x+y;
    }
}
