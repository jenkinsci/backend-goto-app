define([],function() {
    return Backbone.Stapler.model({
        initialize: function(){
        },

        urlRoot: 'recipes',

        vote: Backbone.Stapler.makeJavaScriptProxyCall("vote"),

        defaults: {
            author: null,
            title: null
        }
    });
});