define(['text!installation/view.html'],function(template) {
    return Backbone.View.extend({
        tagName: "div",
        className: "installation",
        template: template,
        events: {
            "click .destroy" : "destroy",
            "click .edit"    : "edit",
            "blur INPUT"     : "cancelEdit",
            "keyup INPUT"    : "doneEditIfEnter"
        },
        initialize: function() {
            var view = this;
            this.listenTo(this.model, 'change', this.render);
            this.listenTo(this.model, 'firstEdit', this.edit);
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
        },
        doneEdit: function() {
          var value = this.input.val();
          if (!value) {
            this.destroy();
          } else {
            this.model.save({location: value});
            this.$el.removeClass("editing");
          }
        },
        doneEditIfEnter: function(e) {
            if (e.keyCode == 13) this.doneEdit();
        },
        cancelEdit: function() {
            this.$el.removeClass("editing");
            this.render()
        },

        render: function() {
            var tmpl = _.template(this.template);
            this.$el.html(tmpl(this.model.toJSON()));
            this.input = this.$('INPUT');
            return this;
        },
        destroy: function() {
            this.model.destroy();
        }
    });
});