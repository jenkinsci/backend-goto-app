define(["text!add-dialog.html",'org/kohsuke/stapler/uri/URI'], function (dialogTemplate,URI) {
    var AddView = Backbone.View.extend({
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

                // when the user is done selecting, get rid of the query string from the address
                // to improve bookmarkability
                if (history.replaceState)
                    history.replaceState({},document.title,new URI(window.location).query("").toString());
            }.bind(this))
        },
        accept: function() {
            this.model.save();
            app.installations.add(this.model);
            this.$el.modal('hide');
        }
    });

    return function (i) {
        if (i._validate()) // if the location isn't valid, don't even bother showing the dialog
            new AddView({model:i}).show();
    }
});
