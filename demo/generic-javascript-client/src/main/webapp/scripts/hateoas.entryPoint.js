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