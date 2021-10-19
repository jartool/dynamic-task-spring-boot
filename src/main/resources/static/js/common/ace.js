ace.require("ace/ext/language_tools")
const editor = ace.edit("editor")
editor.setFontSize(15);
editor.setOptions({
    theme: "ace/theme/iplastic",
    mode: "ace/mode/" + mode,
    autoScrollEditorIntoView: true,
    enableBasicAutocompletion: true,
    enableSnippets: true,
    enableLiveAutocompletion: true
});
editor.session.setValue(text)

function save() {
    var data = {
        "key": key,
        "data": editor.getValue()
    }
    $.ajax({
        type: "POST",
        url: saveUrl,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(data),
        success: function (response) {
            if (response.code == 1) {
                Qmsg.success(response.message)
            } else {
                Qmsg.error(response.message)
            }
        },
        error: function (err) {
            Qmsg.error('error')
            console.log(err)
        }
    })
}
editor.commands.addCommand({
    name: "save",
    exec: save,
    bindKey: { win: "ctrl-s", mac: "cmd-s" }
});