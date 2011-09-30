function createEntryPoint(href) {
    var entryPoint = null;
    $.ajax({url: href,
        type: 'GET',
        async: false,
        success: function(model, textStatus, jqXHR) {
            entryPoint = new Resource(model, jqXHR.getResponseHeader('Content-type'));
        }
    });
    return entryPoint;
}

var Resource = function(model, mediaType) {
    var that = this;
    this._model = model;
    this._mediaType = mediaType;
    this._references = _.map(_.select(model.links, function(link) {
        return link.method === 'GET'
    }), function(link) {
        return new Link(link)
    });

    this._commands = _.map(_.select(model.links, function(link) {
        return link.method !== 'GET'
    }), function(link) {
        return new CommandLink(link, that);
    });

    delete this._model.links;

    this.getLink = function(rel, method) {
        var lnk = null;

        lnk = _.detect(this._references, function(link) {
            if ("self" === link.getRel() && "GET" == link.getMethod()) {
                return true;
            }
        });

        return lnk;
    }

    this.getModel = function() {
        return this._model;
    }

    this.getMediaType = function() {
        return this._mediaType;
    }

    this.references = function() {
        return this._references;
    }

    this.render = function(target) {

        //$('#hateoas-src').empty().append(prettyPrint( this._model ));

        if (-1 != this.getMediaType().indexOf('.list.')) {
            var items = $('<div style="width: 600px;"></div>');
            items.append('<hr/>')
            $.each(this._model.rows, function(index, item) {
                var res = new Resource(item);
                var linksDiv = $('<div></div>');
                $.each(res.references(), function(index, link) {
                    linksDiv.append(link.getLink()).append('<br/>');
                });
                delete item.links;
                items.append(prettyPrint(item));


                $(items).append(linksDiv);
                items.append('<hr/>')
            });
            $(target).empty().append(this._mediaType).append(items);
        } else {


            var form = $('<form id="hateoasForm"></form>');


            //var tbl = prettyPrint(this._model);

            form.append(prettyPrint(this._model));
            /*
            $.each(this._model, function(name, value) {
                form.append('<label>' + name + '</label>')
                    .append('<input id="' + name + '" type="text" value="' + value + '"/>')
                    .append('<br/>');
            });
            */

            form.link(this._model);

            $(target).empty().append(this._mediaType).append(form);


            var linksDiv = $('<div></div>');
            $.each(this._references, function(index, link) {


                linksDiv.append(link.getLink()).append('<br/>');

            });
            $(target).append(linksDiv);

            var commandsDiv = $('<div></div>');
            $.each(this._commands, function(index, command) {


                commandsDiv.append(command.getCommandLink()).append('<br/>');

            });
            $(target).append(linksDiv).append(commandsDiv);
        }
        var pathDiv = $('<div></div>');
        $(window.crumbs).each(function(index, link) {
            var span = $('<span></span>');
            span.append(link.getLink());
            pathDiv.append(span).append(" > ");

        });
        $(target).prepend(pathDiv);
    }
};


var CommandLink = function(model, resource) {
    this._model = model;
    this._resource = resource;

    this.getId = function() {
        return this._model.id;
    }

    this.getMethod = function() {
        return this._model.method;
    }

    this.getHref = function() {
        return this._model.href;
    }

    this.getModel = function() {
        return this._model;
    }

    this.getTemplate = function() {
        return this._model.template;
    }

    this.getType = function() {
        return this._model.type;
    }

    this.getRel = function() {
        return this._model.rel;
    }

    this.invoke = function(link, onSuccess) {
        if (link.getMethod() === 'DELETE') {
            $.ajax({url: link._model.href,
                data: JSON.stringify(link._resource._model),
                type: link._model.method,
                contentType: link._resource.getMediaType(),
                success: function(model, textStatus, jqXHR) {

                    //window.location.href = location.protocol + "//" + location.host + "/index.html";
                    entryPoint.render('#entrypoint');
                }
            });
        }
        else if (link.getMethod() === 'PUT') {
            $.ajax({url: link._model.href,
                data: JSON.stringify(link._resource._model),
                type: link._model.method,
                contentType: link._resource.getMediaType(),
                success: function(model, textStatus, jqXHR) {

                    onSuccess(new Resource(model, jqXHR.getResponseHeader('Content-type')));
                }
            });
        }
        else if (link.getMethod() === 'POST') {

            $('#hateoas').empty();

            var form = $('<form id="hateoasForm"></form>');


            $.each(this.getTemplate(), function(name, value) {
                form.append('<label>' + name + '</label>')
                    .append('<input id="' + name + '" type="text" value="' + value + '"/>')
                    .append('<br/>');
            });

            form.link(this.getTemplate());

            var submitter = function(link, onSuccess) {
                return function() {
                    $.ajax({url: link._model.href,
                        data: JSON.stringify(link.getTemplate()),
                        type: link._model.method,
                        contentType: link._model.type[0],
                        success: function(model, textStatus, jqXHR) {
                            //alert('SUCCESS');
                            onSuccess(new Resource(model, jqXHR.getResponseHeader('Content-type')));
                        },
                        error: function(jqXHR, text, e) {
                            alert("ERROR " + text);
                        }
                    });
                    return false;
                };
            };

            form.submit(submitter(this, onSuccess));

            form.append('<input type="SUBMIT"/>');
            $('#hateoas').append(form);


        }
    }

    this.getCommandLink = function() {

        var clickHelper = function(link) {
            return function(event) {
                link.invoke(link, function(resource) {
                    if (link.getMethod() === "POST") {
                        resource.render('#hateoas');
                    }
                });
            };
        }

        var a = $('<a href="javascript:void(0)">' + this.getRel() + ' - ' + this.getMethod() + '</a>');
        a.click(clickHelper(this));

        return a;

    }
}

var Link = function(model) {
    this.model = model;

    this.getId = function() {
        return this.model.id;
    }

    this.getMethod = function() {
        return this.model.method;
    }

    this.getRel = function() {
        return this.model.rel;
    }

    this.getHref = function() {
        return this.model.href;
    }

    this.getModel = function() {
        return this.model;
    }

    this.getType = function() {
        return this.model.type;
    }

    this.invoke = function(onSuccess) {
        $.ajax({url: model.href,
            type: model.method,
            success: function(model, textStatus, jqXHR) {
                onSuccess(new Resource(model, jqXHR.getResponseHeader('Content-type')));
            }
        });
    }

    this.getLink = function() {

        var clickHelper = function(link) {
            return function(event) {
                link.invoke(function(resource) {
                    resource.render('#hateoas');
                });
            };
        }

        var a = $('<a href="javascript:void(0)">' + this.getRel() + ' - ' + this.getMethod() + '</a>');
        a.click(clickHelper(this));

        return a;

    }
}