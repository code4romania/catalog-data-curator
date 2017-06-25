
$(function () {
    $.get("input/parsed", function (data, status) {

        function computeIdColContent(e) {
            // compute id column content
            var id = e.textSourceId;
            if (e.textType == 'DNA') {
                // add link to DNA
                id = "http://www.pna.ro/comunicat.xhtml?id=" + id;
                id = '<a href="' + id + '">Link-to-' + e.textSourceId + '</a>';
            }
            return id;
        }

        function computeFullTextColumnContent(e) {
            // attempt to show highlights
            if(e.parsedFields.length == 0)
            // if no highlights, show first N chars
                return e.fullText.substr(0, Math.min([200, e.fullText.length])) + '...';

            // join highlights only
            // TODO maybe add surrounding text too ? to give some context ?
            var tableText = '';
            $.each(e.parsedFields, function(h) {
                tableText += '...<a id="'+ this.fieldName+'">'+this.parsedValue+'</a>';
            });

            return tableText;
        }

        $.each(data, function (i, e) {
            var id = computeIdColContent(e);
            var text = computeFullTextColumnContent(e);
            $('#findings tr:last').after('' +
                '<tr>' +
                '<td>' + id + '</td>' +
                '<td>' + e.textType + '</td>' +
                '<td>' + text + '</td>' +
                '</tr>');

        });
    });
});
