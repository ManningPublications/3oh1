<div class="form-group ${invalid ? 'has-error' : ''}">
    <label for="${property}">${label}</label>
    <span class="fa fa-sign"
          data-toggle="tooltip"
          data-placement="top"
          title="${message(code: 'shortener.validUntil.empty.info')}"></span>
    <div class="controls">
        <%= raw(widget) %>
    </div>
</div>