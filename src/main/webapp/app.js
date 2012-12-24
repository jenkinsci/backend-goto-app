require.config({
  paths: {
      "text": "adjuncts/org/kohsuke/stapler/require/text",
      "org/kohsuke/stapler": "adjuncts/org/kohsuke/stapler"
  }
});

require(['installation','text!/adjuncts/webApp/crumbIssuer/crumb','org/kohsuke/stapler/uri/URI','route'],
    function(Installation,crumb,URI,route) {
    window.crumb = crumb.trim();

    var AppView = Backbone.View.extend({
        events: {
            "click .add" : "newForm"
        },

        initialize: function() {
            this.installations = new Installation.List();
            this.listenTo(this.installations, 'add', this.addOne);
            this.listenTo(this.installations, 'reset', this.addAll);
            this.listenTo(this.installations, 'all', this.render);
            this.installations.fetch();
        },

        render: function() {
            // TODO: update headers/footers if any of them depend on the collection
        },

        addOne: function(i) {
            var view = new Installation.View({model: i});
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
            var i = new Installation.Model();
            this.addOne(i);
            i.trigger("firstEdit");
        }
    });

    window.app = new AppView({el:$("#content")});


    function handleQueryString() {
        var uri = new URI(window.location);
        var commands = uri.query(true);
        if (commands.add) {
            route.add(commands.add);
        }
    }
    handleQueryString();
});
