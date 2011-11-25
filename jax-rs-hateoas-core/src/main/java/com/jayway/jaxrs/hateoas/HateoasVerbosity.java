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

	private HateoasOption[] options;

	public HateoasVerbosity(HateoasOption... options) {
		this.options = options;
	}

	public HateoasOption[] getOptions() {
		return options;
	}

}
