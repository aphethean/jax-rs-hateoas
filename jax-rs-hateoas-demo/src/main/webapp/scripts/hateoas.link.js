window.hateoas = window.hateoas || {};


window.hateoas.link = function(resource, model) {

    var _resource = resource;
    var _model = model;

    var GET_HANDLER = function(link) {
        return function(event) {
            hateoas.ajax.get({url: link.href(),
                success: function(model, textStatus, jqXHR) {
                    var res = hateoas.resource(model, jqXHR.getResponseHeader('Content-type'));
                    hateoas.view.renderResource(res);
                }
            });
            return false;
        };
    };

    var PUT_HANDLER = function(link) {
        return function(event) {
            hateoas.ajax.put({url: link.href(),
                data: JSON.stringify(link.resource().updateModel()),
                type: link.method(),
                contentType: link.resource().contentType,
                success: function(model, textStatus, jqXHR) {
                    alert('ok');
                    var res = hateoas.resource(model, jqXHR.getResponseHeader('Content-type'));
                    hateoas.view.renderResource(res);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
            return false;
        };
    };

    var DELETE_HANDLER = function(link) {
        return function(event) {
            hateoas.ajax.del({url: link.href(),
                success: function(model, textStatus, jqXHR) {
                    alert('deleted');



                    var location = jqXHR.getResponseHeader('Location')
                    if(location){
                        hateoas.ajax.get({url: location,
                            success: function(model, textStatus, jqXHR) {
                            var resource = hateoas.resource(model, jqXHR.getResponseHeader('Content-type'));
                            hateoas.view.renderResource(resource);
                            }
                        });
                    }
                    //var res = hateoas.resource(model, jqXHR.getResponseHeader('Content-type'));
                    //hateoas.view.renderResource(res);
                }
            });
            return false;
        };
    };

    var POST_HANDLER = function(link) {
        return function(event) {
            hateoas.view.renderPostForm(link);
        };
    };

    var linkAPI = {
        resource: function() {
            return _resource;
        },
        href: function() {
            return _model.href;
        },
        rel: function() {
            return _model.rel;
        },
        method: function() {
            return _model.method;
        },
        consumes: function() {
            return _model.consumes[0];
        },
        template: function(){
            return _model.template;
        },
        invoker: function() {
            switch (_model.method) {
                case 'GET':
                    return GET_HANDLER(linkAPI);
                    break;
                case 'PUT':
                    return PUT_HANDLER(linkAPI);
                    break;
                case 'POST':
                    return POST_HANDLER(linkAPI);
                    break;
                case 'DELETE':
                    return DELETE_HANDLER(linkAPI);
                    break;
            }
        }

    };

    return linkAPI;
};