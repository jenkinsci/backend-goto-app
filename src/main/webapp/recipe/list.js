define(['recipe/model'],function(Recipe) {
    return Backbone.Collection.extend({
        model: Recipe,
        url: '/recipes'
    });
});
