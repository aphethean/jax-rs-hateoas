(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };

  this.Link = (function() {

    function Link(resource, model) {
      var _this = this;
      this.resource = resource;
      this.model = model;
      this.doDelete = __bind(this.doDelete, this);
      this.doPost = __bind(this.doPost, this);
      this.doPut = __bind(this.doPut, this);
      this.doGet = __bind(this.doGet, this);
      this.trigger = __bind(this.trigger, this);
      this.formModel = {};
      this.hyperLink = $('<a></a>').attr('href', this.model.href).text(this.model.rel + '-' + this.model.method);
      this.errorHandler = function(XMLHttpRequest, textStatus, errorThrown) {
        return alert("Error! Status = " + XMLHttpRequest.status);
      };
      switch (this.model.method) {
        case 'GET':
          this.hyperLink.click(function() {
            return _this.doGet();
          });
          this.successHandler = function(model, textStatus, jqXHR) {
            return new ResourceView(_this);
          };
          break;
        case 'DELETE':
          this.hyperLink.click(function() {
            return _this.doDelete();
          });
          this.successHandler = function(model, textStatus, jqXHR) {
            return new ResourceView({
              rel: 'self',
              href: jqXHR.getResponseHeader('Location'),
              method: 'GET'
            });
          };
          break;
        case 'PUT':
          this.hyperLink.click(function() {
            return _this.doPut();
          });
          this.formModel = this.cloneModel(this.resource.model);
          this.successHandler = function(model, textStatus, jqXHR) {
            return new ResourceView(_this.resource.selfLink);
          };
          break;
        case 'POST':
          this.hyperLink.click(function() {
            return _this.doPost();
          });
          this.formModel = this.model.template;
          this.successHandler = function(model, textStatus, jqXHR) {
            return new ResourceView({
              rel: 'self',
              href: jqXHR.getResponseHeader('Location'),
              method: 'GET'
            });
          };
      }
    }

    Link.prototype.cloneModel = function(model) {
      var clone;
      clone = {};
      $.extend(true, clone, model);
      if (clone.links != null) delete clone.links;
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
      if (confirm("R U sure?")) this.trigger();
      return false;
    };

    return Link;

  })();

}).call(this);
