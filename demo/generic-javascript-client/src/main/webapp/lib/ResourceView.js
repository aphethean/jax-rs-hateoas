(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; }, __hasProp = Object.prototype.hasOwnProperty, __extends = function(child, parent) {
    for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; }
    function ctor() { this.constructor = child; }
    ctor.prototype = parent.prototype;
    child.prototype = new ctor;
    child.__super__ = parent.prototype;
    return child;
  };
  this.ResourceView = (function() {
    __extends(ResourceView, View);
    function ResourceView(selfLink, target) {
      if (target == null) {
        target = '#resource-wrapper';
      }
      this.renderResourceAsJsonLink = __bind(this.renderResourceAsJsonLink, this);
      this.renderForm = __bind(this.renderForm, this);
      this.renderFormHeader = __bind(this.renderFormHeader, this);
      this.renderPropertyList = __bind(this.renderPropertyList, this);
      this.renderLinks = __bind(this.renderLinks, this);
      this.renderResource = __bind(this.renderResource, this);
      this.renderHeader = __bind(this.renderHeader, this);
      this.render = __bind(this.render, this);
      this.refresh = __bind(this.refresh, this);
      ResourceView.__super__.constructor.call(this, target);
      this.selfLink = this.createLink(this, selfLink);
      $.ajax(this.selfLink.model.href, {
        success: this.render
      });
    }
    ResourceView.prototype.refresh = function() {
      return this.selfLink.trigger();
    };
    ResourceView.prototype.render = function(model, textStatus, jqXHR) {
      this.model = model;
      this.clear();
      this.append(this.renderHeader(jqXHR));
      this.append(this.renderLinks(this.model, false, true, 'horizontal-nav'));
      if (!this.isEntryPoint()) {
        return this.append(this.renderResource(this.model));
      }
    };
    ResourceView.prototype.renderHeader = function(jqXHR) {
      var div, props, title;
      div = this.createDiv('header');
      if (this.isEntryPoint()) {
        div.append(this.createSpan('title').text('Entry point'));
      } else {
        title = this.hasRows(this.model) ? 'Collection info' : 'Resource info';
        props = {
          'url': this.selfLink.model.href,
          'method': this.selfLink.model.method,
          'content-type': jqXHR.getResponseHeader('Content-Type'),
          'status': jqXHR.status
        };
        div.append(this.createSpan('title').text(title));
        div.append(this.renderPropertyList(props, 'property-list'));
      }
      return div;
    };
    ResourceView.prototype.renderResource = function(model, isListItem) {
      var div, ol, re;
      if (isListItem == null) {
        isListItem = false;
      }
      div = this.createDiv('resource-data');
      if (!isListItem) {
        div.addClass('border');
      }
      if (this.hasRows(model)) {
        ol = this.createOl('collection-rows');
        ol.appendTo(div);
        _.each(model.rows, __bind(function(row) {
          return ol.append(this.createLi().append(this.renderResource(row, true)));
        }, this));
      } else {
        re = this.createDiv('resource');
        re.append(this.renderPropertyList(model, 'resource-property-list'));
        re.append(this.createDiv('resource-links').append(this.renderLinks(model, isListItem, false, 'vertical-nav')));
        re.appendTo(div);
      }
      return div;
    };
    ResourceView.prototype.renderLinks = function(model, isListItem, isNav, clazz) {
      var div, li, ol;
      div = this.createDiv(clazz);
      ol = this.createOl('nav-bar').appendTo(div);
      _.each(model.links, __bind(function(linkModel) {
        if (this.isEntryPoint()) {
          return ol.append(this.createLi('nav-bar-item').append(this.createLink(this, linkModel).hyperLink));
        } else if (model.rows != null) {
          return ol.append(this.createLi('nav-bar-item').append(this.createLink(this, linkModel).hyperLink));
        } else if (isListItem) {
          return ol.append(this.createLi('nav-bar-item').append(this.createLink(this, linkModel).hyperLink));
        } else {
          if (isNav) {
            if (linkModel.rel === 'self') {
              return ol.append(this.createLi('nav-bar-item').append(this.createLink(this, linkModel).hyperLink));
            }
          } else {
            if (linkModel.rel !== 'self') {
              return ol.append(this.createLi('nav-bar-item').append(this.createLink(this, linkModel).hyperLink));
            }
          }
        }
      }, this));
      if (isNav && !this.isEntryPoint()) {
        li = this.createLi('nav-bar-item right').append(this.renderResourceAsJsonLink());
        li.appendTo(ol);
      }
      return div;
    };
    ResourceView.prototype.renderPropertyList = function(model, clazz, editable) {
      var div, name;
      if (clazz == null) {
        clazz = "";
      }
      if (editable == null) {
        editable = false;
      }
      div = this.createDiv(clazz);
      for (name in model) {
        if (name !== 'links' && name !== 'rows') {
          div.append(this.createLabelAndValue(name, model[name], editable));
        }
      }
      return div;
    };
    ResourceView.prototype.renderFormHeader = function(link) {
      var div, props, title;
      div = this.createDiv('header');
      title = link.model.method === 'POST' ? 'Create resource' : 'Update resource';
      props = {
        'url': link.model.href,
        'method': link.model.method,
        'content-type': link.model.consumes[0]
      };
      div.append(this.createSpan('title').text(title));
      div.append(this.renderPropertyList(props, 'property-list'));
      return div;
    };
    ResourceView.prototype.renderForm = function(formActionLink) {
      var div;
      this.clear();
      div = this.createDiv("");
      div.append(this.renderFormHeader(formActionLink));
      div.append(this.createFormForModel(formActionLink.formModel, this.createButton('Save', formActionLink), this.createButton('Cancel', this.selfLink)));
      return this.append(div);
    };
    ResourceView.prototype.renderResourceAsJsonLink = function() {
      var a;
      a = this.createHyperLink('json').addClass('json-link').click(__bind(function() {
        if ($('.json-link').text() === 'json') {
          $('.json-link').text('model');
          this.clear('.resource-data').append('<pre class="json-view">' + JSON.stringify(this.model, null, 3) + '</pre>');
        } else {
          this.refresh();
        }
        return false;
      }, this));
      return a;
    };
    return ResourceView;
  })();
}).call(this);
