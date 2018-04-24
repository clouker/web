/**
 * 自定义弹出层
 */
var oper = function (options) {
    var option = {
        title: false,
        escKey: true,
        leftMouse: true,
        html: '',
        backgroud: '',
        confirmBtn: false,
        style: {
            'z-index': '100',
            background: 'RGB(255,255,255)',
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translateX(-50%) translateY(-50%)',
            width: '26%',
            padding: '1% 2%'
        }
    }
    if (!options) return;
    if (typeof options != 'string')
        option = clc.extend(option, options);

    var appendConfirmBtn = function () {

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
    };
    var createMain = function (box) {
        var main = document.createElement("div");
        main.setAttribute('data', 'main');
        box.appendChild(main);
        if (typeof options === 'string') {
            main.innerHTML = '<h3>' + options + '</h3><button class="confirmBtn">确认</button>';
            option.confirmBtn = function () {
                remove(main.percent);
            }
        }

        else {
            if (option.html)
                main.innerHTML = option.html;
            if (option.confirmBtn)
                appendConfirmBtn(main, option.confirmBtn)
        }
    }

    var init = function () {
        var _oper = document.getElementsByClassName('oper');
        var $oper;
        if (_oper.length != 0)
            $oper = _oper[0];
        else {
            $oper = document.createElement("div");
            $oper.setAttribute('class', 'oper');
            $oper.style.zIndex = '100';
        }
        var style = {
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
        };
        // 设置背景
        if (option.backgroud.length > 0)
            style.background = option.backgroud;
        Object.keys(style).forEach(function (key) {
            $oper.style.setProperty(key, style[key])
        });
        createBox($oper);
        document.body.appendChild($oper);
        if (option.leftMouse)
            $oper.addEventListener('click', function (ev) {
                if (ev.path[0] === this)
                    remove(this)
            })
        if (option.escKey)
            document.onkeyup = function (ev) {
                var keyNum = window.event ? ev.keyCode : ev.which;// 获取被按下的键值
                if (keyNum == 27)
                    remove($oper)
            }

        if (option.confirmBtn) {
            var confirmBtn = document.getElementsByClassName('confirmBtn');
            confirmBtn.addEventListener('click', function () {
                option.confirmBtn();
            })
        }
    }
    init();
    var remove = function (obj) {
        if (document.contains(obj))
            document.body.removeChild(obj);
        document.onkeyup = null
    }
}

/*
动画函数：
    dom:要运动的节点对象
    o:{属性：目标值，属性：目标值....}  (透明度使用属性：opacity:100) 透明度的值是0-100；  里面的opacity 和  filter会自动做转换。
    time:切换的频率，表示运动的快慢
    fn：回调函数，在运动执行完毕后执行。
 */
function animate(dom, o, time, fn) {
    if (time == undefined)  //默认的切换频率
        time = 10;
    //dom.termId :为每一个运动的物体添加一个属于自己的线程标识
    clearInterval(dom.termId);
    dom.termId = setInterval(function () { //创建一个定时器，实现运动
        dom.isOver = true; //是否可以停止定時器
        for (var property in o) {
            if (property == "opacity")//如果是透明度
                var currentValue = parseInt(getStylePropertyValue(dom, property) * 100); //当前样式属性的值
            else//其他样式属性
                var currentValue = parseInt(getStylePropertyValue(dom, property)); ////当前样式属性的值
            //速度   正速度  负速度
            var speed = (o[property] - currentValue) / 10;
            // 三元表达式  三目运算符
            speed = currentValue > o[property] ? Math.floor(speed) : Math.ceil(speed)
            currentValue += speed; //改变样式属性的值
            if (currentValue != o[property])
                dom.isOver = false; //標識不停止定時器
            if (property == "opacity") {
                dom.style.opacity = currentValue / 100;
                dom.style.filter = 'alpha(opacity= ' + currentValue + ')';
            } else
                dom.style[property] = currentValue + "px"; //改变物体的样式属性值
        }
        if (dom.isOver) {  //停止定时器
            clearInterval(dom.termId);
            if (fn)  //执行回调函数
                fn();
        }
    }, time)  //基于切换的频率来改变运动的快慢
}

/*获取指定样式的属性值*/
function getStylePropertyValue(dom, property) {
    if (window.getComputedStyle) //標準瀏覽器
        return getComputedStyle(dom, null)[property];
    else
        return dom.currentStyle[property]; //IE瀏覽器
}