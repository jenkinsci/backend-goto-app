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
