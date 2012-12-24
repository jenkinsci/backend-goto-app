define([],function() {
    return Backbone.Stapler.model({
        initialize: function(){
        },

        urlRoot: 'installations',

        vote: Backbone.Stapler.makeJavaScriptProxyCall("vote"),

        defaults: {
            location: null
        },
        validate: function (attrs) {
            if (typeof(attrs.location)!="string" || !attrs.location.match(/^https?:\/\//))
                return "Not an absolute URL";
            return null;
        }
    });
});