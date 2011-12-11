class @Link

  constructor: (@resource,  @model) ->

    @formModel = {}
    @hyperLink = $('<a></a>').attr('href', @model.href).text(@model.rel + '-' + @model.method)
    @errorHandler = (XMLHttpRequest, textStatus, errorThrown) => alert "Error! Status = #{XMLHttpRequest.status}"

    switch @model.method
      when 'GET'
        @hyperLink.click => @doGet()
        @successHandler = (model, textStatus, jqXHR) => new ResourceView(this)
      when 'DELETE'
        @hyperLink.click => @doDelete()
        @successHandler = (model, textStatus, jqXHR) => new ResourceView({rel: 'self', href: jqXHR.getResponseHeader('Location'), method: 'GET'})
      when 'PUT'
        @hyperLink.click => @doPut()
        @formModel = @cloneModel @resource.model
        @successHandler = (model, textStatus, jqXHR) => new ResourceView(@resource.selfLink)
      when 'POST'
        @hyperLink.click => @doPost()
        @formModel = @model.template
        @successHandler = (model, textStatus, jqXHR) => new ResourceView({rel: 'self', href: jqXHR.getResponseHeader('Location'), method: 'GET'})

  cloneModel: (model)->
    clone = {}
    $.extend true, clone, model
    if clone.links? then delete clone.links
    clone

  trigger: =>
    $.ajax {
      url: @model.href,
      data: JSON.stringify(@formModel),
      type: @model.method,
      contentType: @model.consumes,
      success: @successHandler,
      error: @errorHandler
    }

  doGet: =>
    @trigger()
    false

  doPut: =>
    @resource.renderForm(this)
    false

  doPost: =>
    @resource.renderForm(this)
    false

  doDelete: =>
    if confirm("R U sure?") then @trigger()
    false

