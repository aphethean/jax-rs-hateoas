window.hateoas = window.hateoas || {};


hateoas.util = hateoas.util || (function($) {


    return {

        filterLinks: function(resource, links, predicate) {
            var predicate = predicate || function(model) {
                return true;
            };
            return _.map(_.select(links, predicate), function(link) {
                return new hateoas.link(resource, link)
            });
        },

        detectLink: function(links, predicate) {
            return (typeof _.detect(links, predicate) !== 'undefined');
        },

        isUndefined: function(value) {
            return (typeof value !== 'undefined')
        }

    }
}($));
