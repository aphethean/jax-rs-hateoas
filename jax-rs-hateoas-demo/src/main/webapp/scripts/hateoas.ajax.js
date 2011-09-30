window.hateoas = window.hateoas || {};


hateoas.ajax = hateoas.ajax || (function($) {

    //
    //private members
    //

    /**
     *
     * debug errors
     *
     */
    var errorTrap = function(userErrorHandler) {

        if (!userErrorHandler) {
            userErrorHandler = function(XMLHttpRequest, textStatus, errorThrown) {
                if (errorThrown) {
                    alert("Error traped by hateoas.ajax. Status: [" + XMLHttpRequest.status + "]: " + errorThrown);
                } else {
                    alert("Error traped by hateoas.ajax. Status: [" + XMLHttpRequest.status + "]: " + XMLHttpRequest.responseText);
                }
            };
        }
        return userErrorHandler;
    };

    /**
     *
     * httpLogger
     *
     */
    var httpLogger = function(successCallback) {

        return function(model, textStatus, jqXHR) {

            $('#httpDebug').prepend('<hr/>');
            $('#httpDebug').prepend('<br/>').prepend('Status: ' + jqXHR.status);
            $('#httpDebug').prepend('<br/>').prepend('Content-Type: ' + jqXHR.getResponseHeader('Content-Type'));
            $('#httpDebug').prepend('<br/>').prepend('-------------');

            $('#httpDebug').prepend('<br/>').prepend('Method: ' + this.type);
            $('#httpDebug').prepend('<br/>').prepend('Url: ' + this.url);



            if (successCallback) {
                successCallback(model, textStatus, jqXHR);
            }
        }
    };

    var doAjax = function(params) {
        var type = params.type || 'GET';
        var url = params.url || null;
        var data = params.data || null;
        var success = httpLogger(params.success || null);
        var error = params.error || null;
        var cache = params.cache || false;
        var dataType = params.dataType || 'json';
        var contentType = params.contentType || 'application/json';
        var headers = params.headers || {};


        $.ajax({
            type: type,
            url: url,
            data: data,
            cache: false,
            success: success,
            contentType: contentType,
            error: error,
            dataType: dataType,
            headers: headers,
            cache: true
        });
    };


    //
    //public members
    //
    return {

        /**
         * executes an ajax GET.
         */
        get: function(params) {
            params.type = 'GET';
            params.error = errorTrap(params.error);

            doAjax(params);
        },

        /**
         * @see get.  Always 'POST'
         */
        post: function(params) {
            params.type = 'POST';
            params.error = errorTrap(params.error);

            doAjax(params);
        },

        /**
         * @see get.  Always 'PUT'
         */
        put: function(params) {
            params.type = 'PUT';
            params.error = errorTrap(params.error);

            doAjax(params);
        },

        /**
         * @see get.  Always 'DELETE'
         */
        del: function(params) {
            params.type = 'DELETE';
            params.error = errorTrap(params.error);

            doAjax(params);
        },

        dispatch: function(params) {
            params.error = errorTrap(params.error);

            doAjax(params);
        }
    };

})($);
