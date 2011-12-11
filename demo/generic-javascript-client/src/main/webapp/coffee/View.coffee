class @View
  constructor: (@target) ->

  clear: (selector = @target) => $(selector).empty()

  append: (item) =>
    $(item).appendTo @target
    item

  isEntryPoint: => @target is '#entry-point-wrapper'

  hasRows: (model) => model.rows?

  hasLinks: (model) => model.links?

  createLink: (resource, model) =>
    if model instanceof Link then model else new Link(resource, model)

  #
  #
  # HTML element factory methods
  #
  #
  createDiv: (clazz = "") -> $("<div class='#{clazz}'></div>")

  createSpan: (clazz = "") -> $("<span class='#{clazz}'></span>")

  createOl: (clazz = "") -> $("<ol class='#{clazz}'></ol>")

  createLi: (clazz = "") -> $("<li class='#{clazz}'></li>")

  createParagraph:  -> $("<p></p>")

  createLabel: (text) -> $("<label>#{text}</label>")

  createValue: (value) -> $("<span>#{value}</span>")

  createInput: (id, value="", type="text") => $("<input id='#{id}' type='#{type}' value='#{value}'/>")

  createHyperLink: (text) -> $("<a href='javascript: return false'>#{text}</a>")

  createButton: (value, link = null) ->
    btn = $("<input type='button' value='#{value}'/>")
    if link? then btn.click -> link.trigger()
    btn

  createFormForModel: (formModel, buttons...) ->
    form = $('<form class="border"></form>')
    form.append @renderPropertyList formModel, 'resource-property-list', true
    form.link(formModel);
    if buttons?
      p = @createParagraph()
      p.appendTo form
      _.each buttons, (btn) -> p.append(btn)
    form

  createLabelAndValue: (label, value, editable=false) ->
    p = @createParagraph()
    p.append @createLabel label
    if editable
      p.append @createInput label, value
    else
      p.append @createValue value
    p



