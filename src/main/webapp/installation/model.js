define([],function() {
    return Backbone.Stapler.model({
        initialize: function(){
        },

        urlRoot: 'installations',

        vote: Backbone.Stapler.makeJavaScriptProxyCall("vote"),

        validate: function (attrs) {
            if (attrs.location==null || attrs.location=="")   return null;    // undefined value (to be removed)
            if (typeof(attrs.location)!="string" || !attrs.location.match(/^https?:\/\//))
                return "Not an absolute URL";
            return null;
        }
    });
});