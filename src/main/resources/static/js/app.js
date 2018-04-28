var class2type = {};
app = {
    append: function (parent, text) {
        if (typeof text === 'string') {
            var temp = document.createElement('div');
            temp.innerHTML = text;
            // 防止元素太多 进行提速
            var frag = document.createDocumentFragment();
            while (temp.firstChild)
                frag.appendChild(temp.firstChild);
            parent.appendChild(frag);
        }
        else
            parent.appendChild(text);
    },
    isPlainObject: function (obj) {
        var proto, Ctor;
        if (!obj || toString.call(obj) !== "[object Object]")
            return false;
        proto = getProto(obj);
        if (!proto)
            return true;
        Ctor = hasOwn.call(proto, "constructor") && proto.constructor;
        return typeof Ctor === "function" && fnToString.call(Ctor) === ObjectFunctionString;
    },
    extend: function () {
        var options, name, src, copy, copyIsArray, clone,
            target = arguments[0] || {},
            i = 1,
            length = arguments.length,
            deep = false;
        if (typeof target === "boolean") {
            deep = target;
            target = arguments[i] || {};
            i++;
        }
        if (typeof target !== "object" && !app.isFunction(target))
            target = {};
        if (i === length) {
            target = this;
            i--;
        }
        for (; i < length; i++) {
            if ((options = arguments[i]) != null) {
                for (name in options) {
                    src = target[name];
                    copy = options[name];
                    if (target === copy)
                        continue;
                    if (deep && copy && (app.isPlainObject(copy) ||
                        (copyIsArray = Array.isArray(copy)))) {
                        if (copyIsArray) {
                            copyIsArray = false;
                            clone = src && Array.isArray(src) ? src : [];
                        } else
                            clone = src && app.isPlainObject(src) ? src : {};
                        target[name] = app.extend(deep, clone, copy);
                    } else if (copy !== undefined)
                        target[name] = copy;
                }
            }
        }
        return target;
    },
    extend1: function (o, n, override) {
        for (var p in n)
            if (n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))
                o[p] = n[p];
        return o;
    },
    each: function (obj, callback) {
        var length, i = 0;
        if (app.isArrayLike(obj)) {
            length = obj.length;
            for (; i < length; i++)
                if (callback.call(obj[i], i, obj[i]) === false)
                    break;
        } else
            for (i in obj)
                if (callback.call(obj[i], i, obj[i]) === false)
                    break;
        return obj;
    },
    isArrayLike: function (obj) {
        var length = !!obj && "length" in obj && obj.length,
            type = app.toType(obj);
        if ((typeof obj === "function" && typeof obj.nodeType !== "number") || (obj != null && obj === obj.window))
            return false;
        return type === "array" || length === 0 ||
            typeof length === "number" && length > 0 && (length - 1) in obj;
    },
    toType: function (obj) {
        if (obj == null)
            return obj + "";
        return typeof obj === "object" || typeof obj === "function" ?
            class2type[toString.call(obj)] || "object" : typeof obj;
    },
    _getValueByName: function (data, name) {
        if (!data || !name)
            return null;
        if (name.indexOf('.') === -1)
            return data[name];
        else {
            try {
                return new Function("data", "return data." + name + ";")(data);
            } catch (e) {
                return null;
            }
        }
    },
    notEmpty: function (obj) {//判断某对象不为空..返回obj 否则 ""
        if (obj === null)
            return "";
        else if (obj === undefined)
            return "";
        else if (obj === "undefined")
            return "";
        else if (obj === "")
            return "";
        else if (obj === "[]")
            return "";
        else if (obj === "{}")
            return "";
        else
            return obj;
    },
    in_array: function (array, string) {//判断一个值是否在数组中
        for (var s = 0; s < array.length; s++)
            if (array[s].toString() == string)
                return true;
        return false;
    },
    notNull: function (obj) {
        if (obj === null)
            return false;
        else if (obj === undefined)
            return false;
        else if (obj === "undefined")
            return false;
        else if (obj === "")
            return false;
        else if (obj === "[]")
            return false;
        else if (obj === "{}")
            return false;
        else if (obj.length === 0)
            return false;
        else
            return true
    },
    remove: function (obj) {
        obj.parentElement.removeChild(obj)
    },
    submit: function (form_id) {
        var data = document.getElementById(form_id);
        var $data = JSON.stringify((app.formSerialize(data)));
        $.ajax({
            type: "post",
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            url: data.action,
            data: $data,
            success: function (result) {
                console.log(typeof result);
            }
        });
    },
    formSerialize: function (obj) {
        var arr = {};
        for (var i = 0; i < obj.elements.length; i++) {
            var feled = obj.elements[i];
            switch (feled.type) {
                case undefined:
                case 'button':
                case 'file':
                case 'reset':
                case 'submit':
                    break;
                case 'checkbox':
                case 'radio':
                    if (!feled.checked)
                        break;
                default:
                    if (arr[feled.name])
                        arr[feled.name] = arr[feled.name] + ',' + feled.value;
                    else
                        arr[feled.name] = feled.value
            }
        }
        return arr
    }
};