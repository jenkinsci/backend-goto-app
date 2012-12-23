
function makeJavaScriptProxyCall(name) {
    return function() {
        var args = Array.prototype.slice.call(arguments);
        var model = this;

        var options = args.pop();
        if (typeof(options)=="function")
          options = {success:options};
        else
          options = options ? _.clone(options) : {};

        var success = options.success;

        options = _.extend({
            success : function(retVal) {
              model.trigger(name, retVal, model, options);
              if (success) success(retVal, model, options);
            },
            url : model.url()+name,
            type: "POST",
            contentType: "application/x-stapler-method-invocation",
            headers: {"Crumb":crumb},
            data: JSON.stringify(args)
        });
        return Backbone.ajax(options);
    }
}

Recipe = Backbone.Model.extend({
    initialize: function(){
    },

    url: function() { return 'recipes/'+this.id+'/';},

    vote: makeJavaScriptProxyCall("vote"),

    defaults: {
        author: null,
        title: null
    }

});

//var recipe = new Recipe();
//recipe.save({title:"Soup", author:"kohsuke"}, {
//    success: function (recipe) {
//        alert(JSON.stringify(recipe.toJSON()));
//    }
//});


recipe = new Recipe({id: 1});

// The fetch below will perform GET /user/1
// The server should return the id, name and email from the database
recipe.fetch({
   success: function (recipe) {
       console.log(JSON.stringify(recipe.toJSON()));

       recipe.set({title:"New title"});
       recipe.save();

       recipe.vote(1,2,function (r) {
           console.log("voted "+r);
       });

       //recipe.delete();
       var directory = new DirectoryView();
   }
});

var Directory = Backbone.Collection.extend({
    model: Recipe
});

var RecipeView = Backbone.View.extend({
    tagName: "div",
    className: "recipe",
    template: "<p>title:<%=title%>, author:<%=author%></p>",
    render: function () {
        var tmpl = _.template(this.template);
        this.$el.html(tmpl(this.model.toJSON()));
        return this;
    }
});

var DirectoryView = Backbone.View.extend({
    el: $("#recipes"),
    initialize: function () {
        this.collection = new Directory([recipe]);
        this.render();
    },
    render: function () {
        var that = this;
        _.each(this.collection.models, function (item) {
            that.renderContact(item);
        }, this);
    },
    renderContact: function (item) {
        var recipeView = new RecipeView({
            model: item
        });
        this.$el.append(recipeView.render().el);
    }
});

