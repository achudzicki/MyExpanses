$(document).ready(function () {
    var bar = document.getElementById('js-progressbar');
    UIkit.upload('.js-upload', {
        name: 'file',
        url: '/import/transactions',
        multiple: true,

        loadStart: function (e) {
            bar.removeAttribute('hidden');
            bar.max = e.total;
            bar.value = e.loaded;
        },

        progress: function (e) {
            bar.max = e.total;
            bar.value = e.loaded;
        },
        loadEnd: function (e) {
            bar.max = e.total;
            bar.value = e.loaded;
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