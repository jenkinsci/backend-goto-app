require.config({
  paths: {
      "text": "adjuncts/org/kohsuke/stapler/require/text"
  }
});

require(['installation/model','installation/list','installation/view','route'],function(Installation,InstallationList,InstallationView) {
    var AppView = Backbone.View.extend({
        events: {
            "click .add" : "newForm"
        },

        initialize: function() {
            this.installations = new InstallationList();
            this.listenTo(this.installations, 'add', this.addOne);
            this.listenTo(this.installations, 'reset', this.addAll);
            this.listenTo(this.installations, 'all', this.render);
            this.installations.fetch();
        },

        render: function() {
            // TODO: update headers/footers if any of them depend on the collection
        },

        addOne: function(i) {
            var view = new InstallationView({model: i});
            $("#installations").append(view.render().el);

            // example of calling the vote method on the server-side
            i.vote(1,2,function (r) {
                console.log("voted "+r);
            });
        },

        addAll: function() {
            this.installations.each(this.addOne.bind(this));
        },

        newForm: function() {
            var i = new Installation();
            this.addOne(i);
            i.trigger("firstEdit");
        }
    });

    window.app = new AppView({el:$("#content")});
});
