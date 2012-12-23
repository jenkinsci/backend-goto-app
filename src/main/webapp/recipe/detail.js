define(['text!recipe/detail.html'],function(template) {
    return Backbone.View.extend({
        tagName: "div",
        className: "detail",
        template: _.template(template),
        events: {
            "click H1" : "shiftLeft"
        },
        render: function () {
            this.$el.html(template(this.model.toJSON()));
            return this;
        },
        shiftLeft: function() {
            $("#recipes").animate({paddingLeft:"-=2em"},200);
        }
    });
});