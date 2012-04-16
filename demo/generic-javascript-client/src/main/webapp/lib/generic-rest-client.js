(function() {
  var _this = this;

  $(function() {
    $('#hateoas').append('<div id="entry-point-wrapper"></div><div id="messages"></div><div id="resource-wrapper"></div>');
    new ResourceView({
      rel: 'self',
      href: '/api/',
      method: 'GET'
    }, '#entry-point-wrapper');
    return messages('Hello World', '#messages');
  });

  ({
    messages: function(msg, targetDiv) {
      return $(target).appendTo = $(msg);
    }
  });

}).call(this);
