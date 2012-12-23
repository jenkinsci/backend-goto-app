define(['text!recipe.html'],function(template) {
    return Backbone.View.extend({
        tagName: "li",
        className: "recipe",
        template: template,
        events: {
            "click H1" : "shiftLeft"
        },
        render: function () {
            var tmpl = _.template(this.template);
            this.$el.html(tmpl(this.model.toJSON()));
            return this;
        },
        shiftLeft: function() {
            $("#recipes").animate({paddingLeft:"-=2em"},200);
        }
    });
});