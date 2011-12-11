(function() {
  $(function() {
    $('#hateoas').append('<div id="entry-point-wrapper"></div><div id="resource-wrapper"></div>');
    return new ResourceView({
      rel: 'self',
      href: '/api/',
      method: 'GET'
    }, '#entry-point-wrapper');
  });
}).call(this);
