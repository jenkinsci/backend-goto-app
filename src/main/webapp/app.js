require.config({
  paths: {
      "text": "adjuncts/org/kohsuke/stapler/require/text"
  }
});

require(['installation/model','installation/list','installation/view'],function(Installation,InstallationList,InstallationView) {
    var AppView = Backbone.View.extend({
        events: {
            "click .add" : "newForm"
        },

        initialize: function() {
            this.recipes = new InstallationList();
            this.listenTo(this.recipes, 'add', this.addOne);
            this.listenTo(this.recipes, 'reset', this.addAll);
            this.listenTo(this.recipes, 'all', this.render);
            this.recipes.fetch();
        },

        render: function() {
            // TODO: update headers/footers if any of them depend on the collection
        },

        addOne: function(recipe) {
            var view = new InstallationView({model: recipe});
            $("#installations").append(view.render().el);

            // example of calling the vote method on the server-side
            recipe.vote(1,2,function (r) {
                console.log("voted "+r);
            });
        },

        addAll: function() {
            this.recipes.each(this.addOne.bind(this));
        },

        newForm: function() {
            var i = new Installation();
            this.addOne(i);
            i.trigger("firstEdit");
        }
    });

    new AppView({el:$("#content")});
});
