!(function (document) {
    var dom = function (obj) {
        if (obj.split(/\s+/)) {
            var tmp = [];
            for (var i = 0; i < obj.split(/\s+/).length; i++) {
                if (obj.split(/\s+/)[i].split(/\./).length > 1) {//

                } else {//单选择器
                    var _tmp = [];
                    var $tmp = tmp;
                    if ($tmp.length > 1) {//多级节点
                        for (var _i = 0; _i < $tmp.length; _i++) {
                            if ($tmp[_i].childNodes.length != 0) {//排除无子节点项
                                for (var $i = 0; $i < $tmp[_i].childNodes.length; $i++) {
                                    var $dom = $tmp[_i].children[$i];
                                    if (matchExp.TAG.test(obj.split(/\s+/)[i])) {
                                        if ($dom.nodeName.toLowerCase() == obj.split(/\s+/)[i]) {//返回匹配项
                                            tmp = $dom;
                                            break;
                                        }
                                        _tmp = $dom.getElementsByTagName(obj.split(/\s+/)[i]);
                                        if (_tmp.length == 0) break;
                                        tmp[$i] = _tmp;
                                        if (_tmp.length == 1)
                                            tmp[$i] = _tmp[0]
                                    } else if (matchExp.CLASS.test(obj.split(/\s+/)[i])) {
                                        _tmp = $dom.getElementsByClassName(obj.split(/\s+/)[i].replace('.', ''));
                                        tmp[$i] = _tmp;
                                        if (_tmp.length == 1)
                                            tmp[$i] = _tmp[0]
                                    } else if (matchExp.ID.test(obj.split(/\s+/)[i])) {
                                        tmp = document.getElementById(obj.split(/\s+/)[i].replace('#', ''));
                                        break
                                    }
                                }
                            }
                        }
                    } else {//单节点
                        if (matchExp.TAG.test(obj.split(/\s+/)[i])) {
                            _tmp = document.getElementsByTagName(obj.split(/\s+/)[i]);
                            tmp = _tmp;
                            if (_tmp.length == 1)
                                tmp = _tmp[0];
                        } else if (matchExp.CLASS.test(obj.split(/\s+/)[i])) {
                            _tmp = document.getElementsByClassName(obj.split(/\s+/)[i].replace('.', ''));
                            tmp = _tmp;
                            if (_tmp.length == 1)
                                tmp[i] = _tmp[0];
                        } else if (matchExp.ID.test(obj.split(/\s+/)[i]))
                            tmp = document.getElementById(obj.split(/\s+/)[i].replace('#', ''));
                    }
                }
            }
            return tmp;
        } else return null;
        document.getElementsByName("");
        var type = {};
    }
    var matchExp = {
        ID: new RegExp("^#((?:\\\\.|[\\w-]|[^\0-\\xa0])+)"),
        CLASS: new RegExp("^\\.((?:\\\\.|[\\w-]|[^\0-\\xa0])+)"),
        TAG: new RegExp("^((?:\\\\.|[\\w-]|[^\0-\\xa0])+|[*])"),
    }
    var app = {
        click: function () {
            dom.addEventListener("click", function (ev) {

            });
        },
        dom: dom
    }
    // window.app = app;
})(document);