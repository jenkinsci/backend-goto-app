define(['text!installation/view.html'],function(template) {
    return Backbone.View.extend({
        tagName: "div",
        className: "installation",
        template: template,
        events: {
            "click .destroy" : "destroy"
        },
        initialize: function() {
            var view = this;
            this.listenTo(this.model, 'change', this.render);
            this.listenTo(this.model, 'destroy', function(){
                $(view.$el).animate({opacity:0},200,function() {
                    $(view.$el).animate({height:0},200,function() {
                        view.remove();
                    });
                });
            });
        },
        render: function() {
            var tmpl = _.template(this.template);
            this.$el.html(tmpl(this.model.toJSON()));
            return this;
        },
        destroy: function() {
            this.model.destroy();
        }
    });
});