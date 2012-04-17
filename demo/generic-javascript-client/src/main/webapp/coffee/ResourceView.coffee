class @ResourceView extends View

  constructor: (selfLink, target = '#resource-wrapper') ->
    super target
    @selfLink = @createLink(this, selfLink)
    $.ajax @selfLink.model.href, 
        error: (jqXHR, textStatus, errorThrown) ->
            debugger
            console.log("HTTP Error: #{jqXHR.status} #{this.url} (#{errorThrown})")
            alert("HTTP Error: #{jqXHR.status} #{this.url} (#{errorThrown})")
        success: @render

  refresh: =>
    @selfLink.trigger()

  render: (@model, textStatus, jqXHR) =>
    @clear()
    @append @renderHeader jqXHR
    @append @renderLinks @model, false, true, 'horizontal-nav'
    @append @renderResource @model if not @isEntryPoint()


  renderHeader: ( jqXHR)  =>
    div = @createDiv('header')

    if @isEntryPoint()
      div.append @createSpan('title').text 'Entry point'
    else
      title = if @hasRows @model then 'Collection info' else 'Resource info'
      props = {
        'url': @selfLink.model.href,
        'method': @selfLink.model.method,
        'content-type': jqXHR.getResponseHeader('Content-Type'),
        'status': jqXHR.status
      }
      div.append @createSpan('title').text title
      div.append @renderPropertyList(props, 'property-list')
    div

  renderResource: (model, isListItem = false) =>
    div = @createDiv 'resource-data'
    div.addClass('border') if !isListItem

    if @hasRows model
      ol = @createOl 'collection-rows'
      ol.appendTo div
      _.each model.rows, (row) =>
        ol.append @createLi().append @renderResource(row, true)
    else
      re = @createDiv 'resource'
      re.append @renderPropertyList(model, 'resource-property-list')
      re.append @createDiv('resource-links').append @renderLinks(model, isListItem, false, 'vertical-nav')
      re.appendTo div
    div

  renderLinks : (model, isListItem, isNav, clazz) =>
    div = @createDiv clazz
    ol = @createOl('nav-bar').appendTo div

    _.each model.links, (linkModel) =>
      if @isEntryPoint()
        ol.append @createLi('nav-bar-item').append @createLink(this, linkModel).hyperLink
      else if model.rows?
        ol.append @createLi('nav-bar-item').append @createLink(this, linkModel).hyperLink
      else if isListItem
        ol.append @createLi('nav-bar-item').append @createLink(this, linkModel).hyperLink
      else
        if isNav
          if linkModel.rel is 'self' then ol.append @createLi('nav-bar-item').append @createLink(this, linkModel).hyperLink
        else
          if linkModel.rel isnt 'self' then ol.append @createLi('nav-bar-item').append @createLink(this, linkModel).hyperLink

    if isNav && not @isEntryPoint()
      li = @createLi('nav-bar-item right').append @renderResourceAsJsonLink()
      li.appendTo ol
    div


  renderPropertyList: (model, clazz="", editable=false) =>
    div = @createDiv clazz
    for name of model when name isnt 'links' && name isnt 'rows'
      div.append @createLabelAndValue(name,  model[name], editable)
    div

  renderFormHeader: (link) =>
    div = @createDiv('header')
    title = if link.model.method is 'POST' then 'Create resource' else 'Update resource'
    props = {
      'url': link.model.href,
      'method': link.model.method,
      'content-type': link.model.consumes[0]
    }
    div.append @createSpan('title').text title
    div.append @renderPropertyList(props, 'property-list')
    div

  renderForm: (formActionLink) =>
    @clear()
    div = @createDiv ""
    div.append @renderFormHeader(formActionLink)
    div.append @createFormForModel(formActionLink.formModel,
                        @createButton('Save', formActionLink),
                        @createButton('Cancel', @selfLink))
    @append div

  renderResourceAsJsonLink: =>
    a = @createHyperLink('json').addClass('json-link').click =>
      if $('.json-link').text() is 'json'
        $('.json-link').text('model')
        @clear('.resource-data').append('<pre class="json-view">' + JSON.stringify(@model, null, 3) + '</pre>')
      else
        @refresh()
      false;
    a





