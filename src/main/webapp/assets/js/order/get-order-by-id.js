$('.double-click').dblclick(function(e) {
    window.location.href = e.target.parentElement.id;
});