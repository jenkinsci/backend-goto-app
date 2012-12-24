define(["text!add-dialog.html","installation/model"], function (dialogTemplate,InstallationModel) {
    var DialogView = Backbone.View.extend({
        template: _.template(dialogTemplate),
        className: "modal hide fade",
        attributes: {
            tabindex:-1,
            role:"dialog"
        },
        events: {
            "click .dialog-accept" : "accept"
        },
        initialize: function() {
        },
        show: function() {
            this.$el.html(this.template(this.model.toJSON()));
            this.$el.modal();
            this.$el.on('hidden',function() {
                this.remove();
                route.navigate('');
            }.bind(this))
        },
        accept: function() {
            this.model.save();
            app.installations.add(this.model);
            this.$el.modal('hide');
        }
    });


    var Workspace = Backbone.Router.extend({
        routes: {
            "add": "add"
        },

        add: function () {
            var i = new InstallationModel({location:window.location.search.substring(1)});
            if (i._validate()) // if the location isn't valid, don't even bother showing the dialog
                new DialogView({model:i}).show();
        }
    });
    var route = new Workspace();
    Backbone.history.start();
})