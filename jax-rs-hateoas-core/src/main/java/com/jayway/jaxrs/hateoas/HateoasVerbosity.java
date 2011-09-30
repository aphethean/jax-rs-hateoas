package com.jayway.jaxrs.hateoas;

public class HateoasVerbosity {

	public static HateoasVerbosity ATOM = new HateoasVerbosity(
			HateoasOption.REL, HateoasOption.HREF, HateoasOption.TYPE);
	public static HateoasVerbosity MAXIMUM = new HateoasVerbosity(
			HateoasOption.REL, HateoasOption.HREF, HateoasOption.ID,
			HateoasOption.CONSUMES, HateoasOption.PRODUCES,
			HateoasOption.METHOD, HateoasOption.LABEL,
			HateoasOption.DESCRIPTION);
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
