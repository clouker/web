layer.config({
    skin: 'layer-ext-seaning',
    extend: 'seaning/style.css',//加载新皮肤
    fix: false,// 用于设定层是否不随滚动条而滚动，固定在可视区域。
    skin: 'layer-ext-myskin' //一旦设定，所有弹层风格都采用此主题。
});
var grid = null;
$(function () {
    grid = $("#paging").lyGrid({
        // pagId: 'paging',
        l_column: [{
            colkey: "NAME",
            name: "账号"
        }, {
            colkey: "AVATAR",
            name: "头像",
            renderData: function (rowindex, data, rowdata, column) {
                if (data)
                    return '<img src="data:image/jpeg|png|gif;base64,' + data + '" style="width: 43px;height: 33px;"  alt="暂无图片" />';
                return '<img src="' + '/images/default.jpg" style="width: 43px;height: 33px;"  title="默认图标"/>';
            }
        }, {
            colkey: "SFZH",
            name: "身份证号"
        }, {
            colkey: "EMAIL",
            name: "邮箱"
        }, {
            colkey: "UPDATE_TIME",
            name: "执行时间",
            isSort: true,
            renderData: function (rowindex, data, rowdata, column) {
                return new Date(data).format("yyyy-MM-dd hh:mm:ss");
            }
        }, {
            colkey: "description",
            name: "执行描述",
            renderData: function (rowindex, data, rowdata, column) {
                if (data.indexOf("成功") > -1)
                    return data;
                return "<label class='red'>执行方法异常:</label>";
            }
        }, {
            colkey: "LOCK_FLAG",
            name: "是否启用",
            width: '90px',
            renderData: function (rowindex) {
                var ck = "";
                if (rowindex % 2)
                    ck = "checked='checked'";
                var html = '<label class="inline">';
                html += '<input id="id-button-borders" ' + ck + ' type="checkbox" class="ace ace-switch ace-switch-6">';
                html += '<span class="lbl middle"></span></label>';
                return html;
            }
        }, {
            name: "操作",
            renderData: function () {
                return "<label class='red'>测试渲染函数,自由操作每一列的数据显示!</label>";
            }
        }],
        async: false, // 默认为同步
        setNumber: true,// 是否显示序号
        pageSize: 10, // 每页显示多少条数据
        checkbox: true,// 是否显示复选框
        checkValue: 'id', // 当checkbox为true时，需要设置存放checkbox的值字段 默认存放字段id的值
        checkbox: true,
        // selectPageSize: [10, 20, 50],
        goPage: true,
        jsonUrl: '/user/findByPage', //jsondata
        data: {//传给后台的参数

        },
        trRowClick: function (index, data) { // 双击行事件.index行索引,data行数据JSON.stringify(data)
        }
    });
    $(".export").click(function () {
        exportTable();
    })
});

function exportTable() {
    console.log(grid.getColumn());
    // grid.exportData();
    layer.alert("待开发。。。");
    return;
    layer.open({
        type: 2,
        area: ['700px', '450px'],
        fixed: false, //不固定
        maxmin: true,
        content: 'http://127.0.0.1:8080/'
    })
}

function gridOptions() {// 绑定查询按扭
    //以下每一个配置都可以放到基本属性表格对象,,参考上的表格例子配置
    grid.setOptions({
        pageSize: 20
    });
    grid.loadData();
    grid.setOptions({
        beforeComplete: function (conf) {
            var s = "加载之前触发,当前表格配置参数 " + JSON.stringify(conf);
            layer.alert(s);
        }
    });
    grid.setOptions({
        afterComplete: function (column, currentData) {
            var s = "加载之后触发,当前页数据是 " + JSON.stringify(currentData);
            layer.alert(s);
        }
    });
    var s = "当前选中多选框的值 " + JSON.stringify(grid.getSelectedCheckbox());
    layer.alert(s);
    var s = "当前先中的值 " + JSON.stringify(grid.selectRow());
    layer.alert(s);
    //数据上移,可设置参数url  grid.lyGridUp(url ); 如果不设置,不请求后台
    grid.lyGridUp();
    //数据下移,可设置参数url  grid.lyGridDown(url ); 如果不设置,不请求后台
    grid.lyGridDown(location.pathname.substr(0, 4) + '/user/findByPage');
    var s = "当前表格所有数据 " + JSON.stringify(grid.resultJSONData());
    layer.alert(s);
    var s = "当前表格表头 " + JSON.stringify(grid.getColumn());
    layer.alert(s);
}
