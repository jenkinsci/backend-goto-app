define(['installation/model'],function(Installation) {
    return Backbone.Collection.extend({
        model: Installation,
        url: '/installations'
    });
});
