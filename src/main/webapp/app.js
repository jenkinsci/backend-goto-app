require.config({
  paths: {
      "text": "adjuncts/org/kohsuke/stapler/require/text",
      "org/kohsuke/stapler": "adjuncts/org/kohsuke/stapler"
  }
});

require(['installation','text!/adjuncts/webApp/crumbIssuer/crumb','org/kohsuke/stapler/uri/URI','add-dialog','text!app.html'],
    function(Installation,crumb,URI,addDialog,appTemplate) {
    window.crumb = crumb.trim();

    var AppView = Backbone.View.extend({
        events: {
            "click .add" : "newForm"
        },
        title: "Your Jenkins installations",
        blurb: "",

        start: function() {
            this.$el.html(_.template(appTemplate)(this));
            this.installations = new Installation.List();
            this.listenTo(this.installations, 'add', this.addOne);
            this.listenTo(this.installations, 'reset', this.addAll);
//            this.listenTo(this.installations, 'all', this.render);
            this.installations.fetch();
        },

        addOne: function(i) {
            var view = new Installation.View({model: i, go:this.go});
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
            var i = new Installation.Model({location:""});
            this.addOne(i);
            i.trigger("firstEdit");
        },

        enterGoMode: function(loc) {
            // enter into the model that lets the user select the Jenkins instance and go to the specific URL within it
            // this method needs to be called before start()
            this.title = "Choose Jenkins instance";
            this.blurb = "You are about to go to <tt>"+ _.escape(loc)+"</tt> of your Jenkins";
            this.go = loc;
        }
    });

    var app = new AppView({el:$("#content")});


    (function () {// handle commands in the query string
        var commands = new URI(window.location).query(true);
        if (commands.add) {
            addDialog(new Installation.Model({location:commands.add}));
        }
        if (commands.go) {
            app.enterGoMode(commands.go);
        }
    })();

    app.start();
});
