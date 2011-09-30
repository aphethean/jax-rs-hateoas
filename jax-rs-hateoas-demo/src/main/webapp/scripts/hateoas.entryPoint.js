hateoas = window.hateoas || {};



hateoas.entryPoint = hateoas.entryPoint || (function(url, options) {

    options = options | hateoas.DEFAULT_OPTIONS;

    var _url = url;

    return {
        url: _url,

        init: function(success) {
            hateoas.ajax.get({url: _url,
                async: false,
                success: function(model, textStatus, jqXHR) {
                    _resource = hateoas.resource(model, jqXHR.getResponseHeader('Content-type'));
                    success(_resource);
                }
            });
        }
    };
});