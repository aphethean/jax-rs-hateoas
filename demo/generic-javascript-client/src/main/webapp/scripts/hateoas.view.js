/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

window.hateoas = window.hateoas || {};

hateoas.view = (function() {

    var _containerDiv = null;
    var _entryPointDiv = null;
    var _resourceDiv = null;
    var _httpDebugDiv = null;
    var _dialogDiv = null;


    var renderLink = function(link) {
        var a = $('<a href="javascript:void(0)">' + link.rel() + ' - ' + link.method() + '</a>');
        a.click(link.invoker());
        return a;
    };

    objectType = function(v) {
        try {
            /* Returns type, e.g. "string", "number", "array" etc.
             Note, this is only used for precise typing. */
            if (v === null) {
                return 'null';
            }
            if (v === undefined) {
                return 'undefined';
            }
            var oType = Object.prototype.toString.call(v).match(/\s(.+?)\]/)[1].toLowerCase();
            if (v.nodeType) {
                if (v.nodeType === 1) {
                    return 'domelement';
                }
                return 'domnode';
            }
            if (/^(string|number|array|regexp|function|date|boolean)$/.test(oType)) {
                return oType;
            }
            if (typeof v === 'object') {
                return v.jquery && typeof v.jquery === 'string' ? 'jquery' : 'object';
            }
            if (v === window || v === document) {
                return 'object';
            }
            return 'default';
        } catch(e) {
            return 'default';
        }
    };

    var renderPostTemplate = function(link) {
        //_resourceDiv.empty();
        _dialogDiv.empty();

        var form = $('<form class="postForm"></form>');


        $.each(link.template(), function(name, value) {
            form.append('<label>' + name + '</label>')
                .append('<input id="' + name + '" type="text" value="' + value + '"/>')
                .append('<br/>');
        });

        form.link(link.template());

        var submitter = function(link) {
            return function() {
                hateoas.ajax.post({url: link.href(),
                    data: JSON.stringify(link.template()),
                    type: link.method(),
                    contentType: link.consumes(),
                    success: function(model, textStatus, jqXHR) {
                        $('#dialog').dialog('close');
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

        form.submit(submitter(link));

        form.append('<input type="submit"/>');


        var btnCancel = $('<input type="button" value="cancel"/>');
        btnCancel.click(function(){
            $(_dialogDiv).dialog('close');
        });

        form.append(btnCancel);


        //_resourceDiv.append(form);
        _dialogDiv.append(form);
        $(_dialogDiv).dialog({title: link.consumes()});
    }


    var createLink = function(link) {
        var a = $('<a href="javascript:void(0)">' + link.rel() + ' - ' + link.method() + '</a>');
        a.click(link.invoker());
        return a;
    }

    var renderResourceEdit = function(resource, target) {
        var form = $('<form></form>');

        target.append(form);

        var formSrc = resource.updateModel();
        var table = $('<table><tbody></tbody></table>');

        table.append($('<tr><td colspan="2"><h3>'+resource.contentType+'</h3></td></tr>'));

        _.each(Object.getOwnPropertyNames(formSrc), function(name) {
            var tr = $('<tr></tr>');
            tr.append($('<td><label>' + name + '</label></td>'));
            var td = $('<td><input type="text" name="' + name + '" value="' + formSrc[name] + '"></td>');
            tr.append(td);
            table.append(tr);
        });
        form.append(table);
        form.link(formSrc);
        _.each(resource.links(), function(link) {
            form.append(createLink(link)).append('<br/>');
        });
    };

    var renderObjectTable = function(obj, target) {
        var type = objectType(obj);

        if (/^(string|number|date|boolean)$/.test(type)) {
            target.append(obj.toString());

        } else if (type === 'object') {
            var render = obj;
            if (obj.hasOwnProperty('contentType')) {

                if (obj.supportsUpdate()) {
                    renderResourceEdit(obj, target);
                    return;
                }

                render = obj.model(false);
            }

            var table = $('<table class="objectTable"><tbody></tbody></table>');

            if (obj.hasOwnProperty('contentType') && obj.contentType !== 'unknown') {
                table.append($('<tr><td colspan="2"><h3>'+obj.contentType+'</h3></td></tr>'));
            }

            _.each(Object.getOwnPropertyNames(render), function(name) {
                var tr = $('<tr></tr>');
                tr.append($('<td>' + name + '</td>'));
                var td = $('<td></td>');
                renderObjectTable(render[name], td);
                tr.append(td);
                table.append(tr);
            });

            if (obj.hasOwnProperty('contentType')) {
                var links = obj.links();

                table.append(tr);
                var tr = $('<tr></tr>');
                var td = $('<td colspan="2"></td>');

                _.each(links, function(link) {
                    td.append(createLink(link)).append('<br/>');
                });


                tr.append(td);
                table.append(tr);

            }

            target.append(table);
        } else if (type === 'array') {
            var arrayTable = $('<table class="arrayTable"><tbody></tbody></table>');

            $.each(obj, function(index, item) {

                var evenOrOdd = ((index%2) == 0)?'even':'odd';

                var arrayTr = $('<tr class="' + evenOrOdd + '"></tr>');
                arrayTr.append($('<td>' + index + '</td>'));
                var arrayTd = $('<td></td>');

                var res = hateoas.resource(item, 'unknown');
                renderObjectTable(res, arrayTd);
                arrayTr.append(arrayTd);
                arrayTable.append(arrayTr);

            });
            target.append(arrayTable);
        }
    };


    return {
        init: function() {
            _containerDiv = $('#hateoas');
            _entryPointDiv = $('<div id="hateoasMenu">menu</div>');
            _resourceDiv = $('<div id="hateoasResource">resource</div>');
            _httpDebugDiv = $('<div id="httpDebug">debug</div>');
            _dialogDiv = $('<div id="dialog"></div>');
            _containerDiv.append(_entryPointDiv).append(_resourceDiv).append(_httpDebugDiv);
        },

        renderEntryPoint: function(resource) {
            _entryPointDiv.empty().append('<h3>' + resource.contentType + '</h3>')

            var links = resource.links(function(link) {
                return link.method === 'GET';
            });

            $(links).each(function(link) {
                _entryPointDiv.append(renderLink(this)).append('<br/>');
            });
        },

        renderResource: function(resource) {
            _resourceDiv.empty()
            var container = $('<div class="resource"></div>');
            renderObjectTable(resource, container);

            _resourceDiv.append(container);
            //_resourceDiv.empty().append(_resourceDiv.empty());
        },

        renderPostForm: function(link) {
            renderPostTemplate(link);
        }

    }
})();