$(document).ready(function () {
    $("#languageDropdownMenuButton a").click(function (e) {
        e.preventDefault(); // cancel the link behaviour
        var languageSelectedText = $(this).text();
        var languageSelectedValue = $(this).attr("value");
        $("#btnLanguage").text(languageSelectedText);
        window.location.replace('?lang=' + languageSelectedValue);
        return false;
    });
});