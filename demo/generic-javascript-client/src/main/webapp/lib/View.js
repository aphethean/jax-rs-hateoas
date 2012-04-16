(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; },
    __slice = Array.prototype.slice;

  this.View = (function() {

    function View(target) {
      this.target = target;
      this.createInput = __bind(this.createInput, this);
      this.createLink = __bind(this.createLink, this);
      this.hasLinks = __bind(this.hasLinks, this);
      this.hasRows = __bind(this.hasRows, this);
      this.isEntryPoint = __bind(this.isEntryPoint, this);
      this.append = __bind(this.append, this);
      this.clear = __bind(this.clear, this);
    }

    View.prototype.clear = function(selector) {
      if (selector == null) selector = this.target;
      return $(selector).empty();
    };

    View.prototype.append = function(item) {
      $(item).appendTo(this.target);
      return item;
    };

    View.prototype.isEntryPoint = function() {
      return this.target === '#entry-point-wrapper';
    };

    View.prototype.hasRows = function(model) {
      return model.rows != null;
    };

    View.prototype.hasLinks = function(model) {
      return model.links != null;
    };

    View.prototype.createLink = function(resource, model) {
      if (model instanceof Link) {
        return model;
      } else {
        return new Link(resource, model);
      }
    };

    View.prototype.createDiv = function(clazz) {
      if (clazz == null) clazz = "";
      return $("<div class='" + clazz + "'></div>");
    };

    View.prototype.createSpan = function(clazz) {
      if (clazz == null) clazz = "";
      return $("<span class='" + clazz + "'></span>");
    };

    View.prototype.createOl = function(clazz) {
      if (clazz == null) clazz = "";
      return $("<ol class='" + clazz + "'></ol>");
    };

    View.prototype.createLi = function(clazz) {
      if (clazz == null) clazz = "";
      return $("<li class='" + clazz + "'></li>");
    };

    View.prototype.createParagraph = function() {
      return $("<p></p>");
    };

    View.prototype.createLabel = function(text) {
      return $("<label>" + text + "</label>");
    };

    View.prototype.createValue = function(value) {
      return $("<span>" + value + "</span>");
    };

    View.prototype.createInput = function(id, value, type) {
      if (value == null) value = "";
      if (type == null) type = "text";
      return $("<input id='" + id + "' type='" + type + "' value='" + value + "'/>");
    };

    View.prototype.createHyperLink = function(text) {
      return $("<a href='javascript: return false'>" + text + "</a>");
    };

    View.prototype.createButton = function(value, link) {
      var btn;
      if (link == null) link = null;
      btn = $("<input type='button' value='" + value + "'/>");
      if (link != null) {
        btn.click(function() {
          return link.trigger();
        });
      }
      return btn;
    };

    View.prototype.createFormForModel = function() {
      var buttons, form, formModel, p;
      formModel = arguments[0], buttons = 2 <= arguments.length ? __slice.call(arguments, 1) : [];
      form = $('<form class="border"></form>');
      form.append(this.renderPropertyList(formModel, 'resource-property-list', true));
      form.link(formModel);
      if (buttons != null) {
        p = this.createParagraph();
        p.appendTo(form);
        _.each(buttons, function(btn) {
          return p.append(btn);
        });
      }
      return form;
    };

    View.prototype.createLabelAndValue = function(label, value, editable) {
      var p;
      if (editable == null) editable = false;
      p = this.createParagraph();
      p.append(this.createLabel(label));
      if (editable) {
        p.append(this.createInput(label, value));
      } else {
        p.append(this.createValue(value));
      }
      return p;
    };

    return View;

  })();

}).call(this);
