define([],function() {
    return Backbone.Stapler.model({
        initialize: function(){
        },

        urlRoot: 'installations',

        vote: Backbone.Stapler.makeJavaScriptProxyCall("vote"),

        defaults: {
            location: null
        }
    });
});