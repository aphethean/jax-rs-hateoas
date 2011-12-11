(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  this.Link = (function() {
    function Link(resource, model) {
      this.resource = resource;
      this.model = model;
      this.doDelete = __bind(this.doDelete, this);
      this.doPost = __bind(this.doPost, this);
      this.doPut = __bind(this.doPut, this);
      this.doGet = __bind(this.doGet, this);
      this.trigger = __bind(this.trigger, this);
      this.formModel = {};
      this.hyperLink = $('<a></a>').attr('href', this.model.href).text(this.model.rel + '-' + this.model.method);
      this.errorHandler = __bind(function(XMLHttpRequest, textStatus, errorThrown) {
        return alert("Error! Status = " + XMLHttpRequest.status);
      }, this);
      switch (this.model.method) {
        case 'GET':
          this.hyperLink.click(__bind(function() {
            return this.doGet();
          }, this));
          this.successHandler = __bind(function(model, textStatus, jqXHR) {
            return new ResourceView(this);
          }, this);
          break;
        case 'DELETE':
          this.hyperLink.click(__bind(function() {
            return this.doDelete();
          }, this));
          this.successHandler = __bind(function(model, textStatus, jqXHR) {
            return new ResourceView({
              rel: 'self',
              href: jqXHR.getResponseHeader('Location'),
              method: 'GET'
            });
          }, this);
          break;
        case 'PUT':
          this.hyperLink.click(__bind(function() {
            return this.doPut();
          }, this));
          this.formModel = this.cloneModel(this.resource.model);
          this.successHandler = __bind(function(model, textStatus, jqXHR) {
            return new ResourceView(this.resource.selfLink);
          }, this);
          break;
        case 'POST':
          this.hyperLink.click(__bind(function() {
            return this.doPost();
          }, this));
          this.formModel = this.model.template;
          this.successHandler = __bind(function(model, textStatus, jqXHR) {
            return new ResourceView({
              rel: 'self',
              href: jqXHR.getResponseHeader('Location'),
              method: 'GET'
            });
          }, this);
      }
    }
    Link.prototype.cloneModel = function(model) {
      var clone;
      clone = {};
      $.extend(true, clone, model);
      if (clone.links != null) {
        delete clone.links;
      }
      return clone;
    };
    Link.prototype.trigger = function() {
      return $.ajax({
        url: this.model.href,
        data: JSON.stringify(this.formModel),
        type: this.model.method,
        contentType: this.model.consumes,
        success: this.successHandler,
        error: this.errorHandler
      });
    };
    Link.prototype.doGet = function() {
      this.trigger();
      return false;
    };
    Link.prototype.doPut = function() {
      this.resource.renderForm(this);
      return false;
    };
    Link.prototype.doPost = function() {
      this.resource.renderForm(this);
      return false;
    };
    Link.prototype.doDelete = function() {
      if (confirm("R U sure?")) {
        this.trigger();
      }
      return false;
    };
    return Link;
  })();
}).call(this);
