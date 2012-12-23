require.config({
  paths: {
      "text": "adjuncts/org/kohsuke/stapler/require/text"
  }
});

require(['recipe/list','recipe/view'],function(RecipeList,RecipeView) {
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

            // example of calling the vote method on the server-side
            recipe.vote(1,2,function (r) {
                console.log("voted "+r);
            });
        },

        addAll: function() {
            this.recipes.each(this.addOne);
        }
    });

    var app = new AppView();
});
