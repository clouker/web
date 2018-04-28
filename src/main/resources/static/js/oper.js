/**
 * 自定义弹出层
 */
var oper = function (options) {
    var option = {
        title: false,
        key: [27],
        type: 'info',
        leftMouse: true,
        html: '',
        backgroud: '',
        confirmBtn: false,
        cancelBtn: false,
        style: {
            'z-index': '100',
            background: 'RGBA(255,255,255,0.9)',
            'border-radius': '10px',
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translateX(-50%) translateY(-50%)',
            width: '26%',
            padding: '0 2% 3% 2%'
        }
    }
    if (!options) return;
    if (typeof options != 'string')
        option = app.extend(option, options);
    var theme = function (obj) {
        var html = '';
        switch (option.type) {
            case 'info':
                html = '<li class="fa fa-thumbs-up fa-3x"></li><p class="info">' + options + '</p>';
                break;
            case 'warn':
                obj.style.background = 'rgba(0,161,153,0.8)';
                html = '<li class="fa fa-hand-stop-o fa-3x"></li><p class="warn">' + options + '</p>';
                break;
        }
        obj.innerHTML = html
    }
    var appendConfirmBtn = function (perent) {
        var confirmBox = document.createElement('div');
        confirmBox.setAttribute('class', 'confirmBox');
        // confirmBox.style.textAlign = 'center';
        confirmBox.innerHTML = '<button class="yes">确认</button>';
        perent.appendChild(confirmBox);
    }
    var appendCancelBtn = function (perent, cancelBtn) {

    }
    var createTitle = function (box) {
        var header = document.createElement("div");
        header.setAttribute('data', 'header');
        header.innerHTML = option.title;
        box.appendChild(header);
    }
    var createBox = function ($oper) {
        var box = document.createElement("div");
        box.setAttribute('class', 'box');
        Object.keys(option.style).forEach(function (key) {
            box.style.setProperty(key, option.style[key])
        });
        if (option.title)
            createTitle(box);
        createMain(box);
        $oper.appendChild(box)
    }
    var createMain = function (box) {
        var main = document.createElement("div");
        main.setAttribute('data', 'main');
        main.style.textAlign = 'center';
        box.appendChild(main);
        if (typeof options === 'string') {
            option.type = 'warn';
            theme(main);
            option.confirmBtn = function () {
                app.remove(main)
            }
            appendConfirmBtn(main);
        }
        else {
            if (option.html)
                main.innerHTML = option.html;
            if (option.confirmBtn)
                appendConfirmBtn(main);
            if (option.cancelBtn)
                appendCancelBtn(main)
        }
    }

    var init = function () {
        var _oper = document.getElementsByClassName('oper');
        var $oper;
        if (_oper.length == 0) {
            $oper = document.createElement("div");
            $oper.setAttribute('class', 'oper');
            $oper.style.zIndex = '100'
        }
        else
            $oper = _oper[0];
        var style = {
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
        }
        // 设置背景
        if (option.backgroud.length > 0)
            style.background = option.backgroud;
        Object.keys(style).forEach(function (key) {
            $oper.style.setProperty(key, style[key])
        })
        createBox($oper);
        document.body.appendChild($oper);
        if (option.leftMouse)
            $oper.onclick = function (e) {
                var flag = false;
                if (e.path[0] === this) {
                    flag = true;
                    app.remove(this)
                }
                var target = e.target || e.srcElement;
                if (option.confirmBtn && !!target && target.className.toLowerCase() === 'yes') {
                    app.remove(target.parentElement.parentElement.parentElement);
                    flag = true;
                }
                if (flag)
                    $oper.onclick = null;
            }
        if (option.key.length > 0)
            document.onkeyup = function (e) {
                var flag = false;
                var keyNum = window.event ? e.keyCode : e.which;// 获取被按下的键值
                if (option.key.includes(27) && keyNum == 27) {
                    app.remove($oper);
                    flag = true;
                }
                if (flag)
                    document.onkeyup = null
            }
    }
    init();
}