define(['text!installation/view.html'],function(template) {
    return Backbone.View.extend({
        tagName: "div",
        className: "installation",
        template: _.template(template),
        events: {
            "click .destroy" : "destroy",
            "click .edit"    : "edit",
            "blur INPUT"     : "cancelEdit",
            "keyup INPUT"    : "doneEditIfEnter"
        },
        initialize: function(opts) {
            var view = this;
            this.go = opts.go;
            this.listenTo(this.model, 'change', this.render);
            this.listenTo(this.model, 'firstEdit', this.edit);
            this.listenTo(this.model, 'error', this.error);
            this.listenTo(this.model, 'destroy', function(){
                $(view.$el).animate({opacity:0},200,function() {
                    $(view.$el).animate({height:0},200,function() {
                        view.remove();
                    });
                });
            });
        },
        edit: function() {// enter the edit mode
            this.$el.addClass("editing");
            this.input.focus();
            this.saving=false;
        },
        doneEdit: function () {
            var value = this.input.val();
            if (!value) {
                this.destroy();
                this.$el.removeClass("editing");
            } else {
                this.saving = true;
                if (this.model.save({location: value}))
                    this.$el.removeClass("editing");
            }
        },
        doneEditIfEnter: function(e) {
            if (e.keyCode == 13) this.doneEdit();
            if (e.keyCode == 27) this.cancelEdit();
        },
        cancelEdit: function() {
            if (this.model.isNew() && !this.saving)
                this.destroy();
            else {
                this.$el.removeClass("editing");
                this.render(); // go back to the view mode
            }
        },
        render: function() {
            this.$el.html(this.template(_.extend({go:this.go},this.model.toJSON())));
            this.input = this.$('INPUT');
            return this;
        },
        error: function() {
            this.input.parents(".control-group").addClass("error");
        },
        destroy: function() {
            this.model.destroy();
        }
    });
});