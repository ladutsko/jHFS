$(function() {
    $('time').each(function(index) {
        $(this).text(new Date(this.getAttribute('datetime')))
    })
})

$(function() {
    $('.dropdown-toggle').dropdown();
})

$(function() {
    $('[title]').tooltip();
})
