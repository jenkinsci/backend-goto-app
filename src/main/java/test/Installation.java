package test;

import org.codehaus.jackson.annotate.JsonProperty;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import test.backbone.Resource;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;

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
        this.owner = parent.user.id;
        parent.em.persist(this);
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public boolean belongsTo(User owner) {
        return this.owner.equals(owner.id);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JavaScriptMethod // testing this functionality. not actually being used.
    public int vote(int x, int y) {
        System.out.println(x+y);
        return x+y;
    }
}
