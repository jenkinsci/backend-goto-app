package test;

import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import test.backbone.ResourceCollection;

/**
 * @author Kohsuke Kawaguchi
 */
public class RecipeCollection extends ResourceCollection<Recipe,Integer> {
    @Override
    protected Recipe get(Integer id) {
        return new Recipe("author"+id,"title"+id);
    }

    @Override
    public HttpResponse delete(Recipe resource) {
        System.out.println("Deleted "+resource);
        return HttpResponses.ok();
    }

    @Override
    public void onUpdated(Recipe resource) {
        System.out.println("Updated to "+resource);
    }
}
