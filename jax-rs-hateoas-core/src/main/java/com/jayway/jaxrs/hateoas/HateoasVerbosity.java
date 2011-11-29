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
package com.jayway.jaxrs.hateoas;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mattias Hellborg Arthursson
 * @author Kalle Stenflo
 */
public class HateoasVerbosity {

    public static HateoasVerbosity ATOM = new HateoasVerbosity(
            HateoasOption.REL, HateoasOption.HREF, HateoasOption.TYPE);
    public static HateoasVerbosity MAXIMUM = new HateoasVerbosity(
            HateoasOption.REL, HateoasOption.HREF, HateoasOption.ID,
            HateoasOption.CONSUMES, HateoasOption.PRODUCES,
            HateoasOption.METHOD, HateoasOption.LABEL,
            HateoasOption.DESCRIPTION, HateoasOption.TEMPLATE);
    public static HateoasVerbosity MINIMUM = new HateoasVerbosity(
            HateoasOption.REL, HateoasOption.HREF);
    public static HateoasVerbosity GENERIC_CLIENT = new HateoasVerbosity(
            HateoasOption.REL, HateoasOption.HREF, HateoasOption.CONSUMES,
            HateoasOption.METHOD, HateoasOption.TEMPLATE);

    private static HateoasVerbosity defaultVerbosity = HateoasVerbosity.MAXIMUM;

    private HateoasOption[] options;

    public HateoasVerbosity(HateoasOption... options) {
        this.options = options;
    }

    public static void setDefaultVerbosity(HateoasVerbosity verbosity) {
        defaultVerbosity = verbosity;
    }

    public static HateoasVerbosity valueOf(String optionsString) {
        if (StringUtils.isNotBlank(optionsString)) {
            String[] headerSplit = StringUtils.split(optionsString, ",");
            List<HateoasOption> options = new LinkedList<HateoasOption>();
            for (String oneOption : headerSplit) {
                options.add(HateoasOption.valueOf(oneOption.trim()));
            }

            return new HateoasVerbosity(
                    options.toArray(new HateoasOption[0]));
        } else {
            return defaultVerbosity;
        }

    }

    public HateoasOption[] getOptions() {
        return options;
    }

}
