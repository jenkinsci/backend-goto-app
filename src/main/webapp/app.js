require.config({
  paths: {
      "text": "adjuncts/org/kohsuke/stapler/require/text"
  }
});

require(['text!recipe.html'],function(recipeHtml) {
    var Recipe = Backbone.Stapler.model({
        initialize: function(){
        },

        urlRoot: 'recipes',

        vote: Backbone.Stapler.makeJavaScriptProxyCall("vote"),

        defaults: {
            author: null,
            title: null
        }
    });

    var RecipeList = Backbone.Collection.extend({
        model: Recipe,
        url: '/recipes'
    });

    var RecipeView = Backbone.View.extend({
        tagName: "div",
        className: "recipe",
        template: recipeHtml,
        render: function () {
            var tmpl = _.template(this.template);
            this.$el.html(tmpl(this.model.toJSON()));
            return this;
        }
    });

    var AppView = Backbone.View.extend({
        initialize: function() {
            this.recipes = new RecipeList();
            this.listenTo(this.recipes, 'add', this.addOne);
            this.listenTo(this.recipes, 'reset', this.addAll);
            this.listenTo(this.recipes, 'all', this.render);
            this.recipes.fetch();
        },

        render: function() {
            // TODO: update headers/footers if any of them depend on the collection
        },

        addOne: function(recipe) {
            var view = new RecipeView({model: recipe});
            this.$("#recipes").append(view.render().el);
        },

        addAll: function() {
            this.recipes.each(this.addOne);
        }
    });

    new AppView();
});
