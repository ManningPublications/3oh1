<div class="form-group ${invalid ? 'has-error' : ''}">
    <label for="${property}">${label}</label>
    <div class="controls">
        <%= raw(widget) %>
    </div>
</div>