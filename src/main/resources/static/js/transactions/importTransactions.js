$(document).ready(function () {
    UIkit.upload('.js-upload', {
        name: 'file',
        url: '/import/transactions',
        multiple: true,

        beforeAll : function () {
            $('#result').empty();
        },

        completeAll: function (result) {
            var jsonResult;
            try {
               jsonResult = JSON.parse(result.response);
                UIkit.notification({message: '<i style="color: #900000">' + jsonResult.notificationMessage + '</i>'});
            } catch (e) {
                fillTable(result.response);
            }
            setTimeout(function () {
                bar.setAttribute('hidden', 'hidden');
            }, 1000);
        }
    });
});

function fillTable(content) {
    $('#result').append(content);
}