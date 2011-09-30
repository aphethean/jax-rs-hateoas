window.hateoas = window.hateoas || {};

window.hateoas.SELF_GET_PREDICATE = function(link) {
    return (link.rel === 'self' && link.method === 'GET');
};
window.hateoas.SELF_PUT_PREDICATE = function(link) {
    return (link.rel === 'self' && link.method === 'PUT');
};
window.hateoas.SELF_DELETE_PREDICATE = function(link) {
    return (link.rel === 'self' && link.method === 'DELETE');
};
window.hateoas.OTHER_RESOURCE_PREDICATE = function(link) {
    return (link.rel !== 'self');
};


window.hateoas.resource = function(model, contentType) {

    var _model = model;
    var _contentType = contentType;
    var _updateModel = null;

    var _resourceAPI = {
        contentType: _contentType,

        links: function(predicate) {
            return hateoas.util.filterLinks(_resourceAPI, _model.links, predicate);
        },

        resourceLinks: function(){
            return hateoas.util.filterLinks(_resourceAPI, _model.links, hateoas.OTHER_RESOURCE_PREDICATE);
        },

        updateLink: function() {
            return hateoas.util.filterLinks(_resourceAPI, _model.links, hateoas.SELF_PUT_PREDICATE)[0];
        },

        deleteLink: function() {
            return hateoas.util.filterLinks(_resourceAPI, _model.links, hateoas.SELF_DELETE_PREDICATE)[0];
        },

        supportsUpdate: function() {
            return hateoas.util.detectLink(_model.links, hateoas.SELF_PUT_PREDICATE);
        },

        supportsDelete: function() {
            return hateoas.util.detectLink(_model.links, hateoas.SELF_DELETE_PREDICATE);
        },

        updateModel: function() {
            if (_updateModel == null) {
                _updateModel = {};
                $.extend(true, _updateModel, _model);
                delete _updateModel.links;
            }
            return _updateModel;
        },

        model: function(includeLinks) {
            var includeLinks = includeLinks || false;

            //var modelClone = $(_model).clone(false, false);
            var modelClone = {};

            $.extend(true, modelClone, _model);

            if (!includeLinks) {
                delete modelClone.links;
            }
            return modelClone;
        }
    };

    return _resourceAPI;


};