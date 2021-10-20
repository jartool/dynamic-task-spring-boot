if (authEnable) {
    const key = sessionStorage.getItem(authKey)

    if (key == null) {
        auth()
    } else {
        check(null, {key: key})
    }
}

const _ = {
    alert: function (text) {
        $.alert({
            title: 'Tips',
            useBootstrap: false,
            boxWidth: '20%',
            content: text
        });
    },
    close: function (jConfirm) {
        if (jConfirm !== undefined && jConfirm !== null) {
            jConfirm.close()
        }
    },
    setItem: function (storage, key, value) {
        if (value !== undefined && value !== null) {
            storage.setItem(key, value)
        }
    }
}

function auth() {
    const jConfirm = $.confirm({
        title: 'Auth',
        useBootstrap: false,
        boxWidth: '20%',
        theme: 'supervan',
        animation: 'scale',
        type: 'orange',
        content: '' +
            '<form action="" class="formName">' +
            '<div class="form-group">' +
            '<input type="text" placeholder="Your name" class="name form-control" required />' +
            '<input type="password" placeholder="Your password" class="password form-control" required />' +
            '</div>' +
            '</form>',
        buttons: {
            formSubmit: {
                text: 'Submit',
                btnClass: 'btn-blue',
                action: function () {
                    const name = this.$content.find('.name').val()
                    const password = this.$content.find('.password').val()
                    if (!name) {
                        _.alert('provide a valid name')
                        return false;
                    }
                    if (!password) {
                        _.alert('provide a valid password')
                        return false
                    }
                    const data = {username: name, password: password};
                    check(this, data)
                    return false
                }
            }
        },
        onContentReady: function () {
            const jc = this
            this.$content.find('form').on('submit', function (e) {
                e.preventDefault()
                jc.$$formSubmit.trigger('click')
            });
        }
    });
}

function check(jConfirm, data) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        async: false,
        url: authUrl,
        dataType: 'json',
        data: JSON.stringify(data),
        success: function (data) {
            if (data.code == 0) {
                _.setItem(sessionStorage, authKey, data.secret)
                _.close(jConfirm)
            } else {
                if (jConfirm != null) {
                    _.alert('passwrod error')
                } else {
                    auth()
                }
            }
        },
        error: function (err) {
            console.log(err)
        }
    })
}