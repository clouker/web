var grid = null;
window.onload = function () {
    grid = lyGrid({
        // id: 'paging',
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
                return "<label class='red'>测试渲染函数</label>";
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
    // $(".export").click(function () {
    //     exportTable();
    // })
    // $(".add").click(function () {
    //     var $addDiv = document.getElementById('addDiv').innerHTML;
    //     //igm是指分别用于指定区分大小写的匹配、全局匹配和多行匹配。
    //     var reg = new RegExp('\\[([^\\[\\]]*?)\\]', 'igm');
    //     var target = $addDiv.replace(reg, function (node, key) {
    //         return {'data': '标题', 'data1': '说的'}[key];
    //     });
    //     swal({
    //         title: "添加用户",
    //         html: $addDiv,
    //         showConfirmButton: false,
    //         backdrop: 'rgba(0,0,123,0.4) url("/images/cool.gif") center left no-repeat'
    //     });
    // })
}

function exportTable() {
    console.log(grid.getColumn());
    // layer.alert('待开发。。。');
    // grid.exportData();
    return;
}



function gridOptions() {// 绑定查询按扭
    //以下每一个配置都可以放到基本属性表格对象,参考上的表格例子配置
    grid.setOptions({
        pageSize: 20
    });
    grid.loadData();
    grid.setOptions({
        beforeComplete: function (conf) {
            var s = "加载之前触发,当前表格配置参数 " + JSON.stringify(conf);
            // layer.alert(s);
        }
    });
    grid.setOptions({
        afterComplete: function (column, currentData) {
            var s = "加载之后触发,当前页数据是 " + JSON.stringify(currentData);
            // layer.alert(s);
        }
    });
    // layer.alert("当前选中多选框的值 " + JSON.stringify(grid.getSelectedCheckbox()));
    // layer.alert("当前先中的值 " + JSON.stringify(grid.selectRow()));
    // //数据上移,可设置参数url  grid.lyGridUp(url); 如果不设置,不请求后台
    // grid.lyGridUp();
    // //数据下移,可设置参数url  grid.lyGridDown(url ); 如果不设置,不请求后台
    // grid.lyGridDown(location.pathname.substr(0, 4) + '/user/findByPage');
    // layer.alert("当前表格所有数据 " + JSON.stringify(grid.resultJSONData()));
    // layer.alert("当前表格表头 " + JSON.stringify(grid.getColumn()));
}

function ConvertToBase64Img(e) {
    var file = e.target.files[0];
    console.info(file);//图片文件
    console.log(e.target.value);
    var reader = new FileReader();
    reader.onload = (function () {
        return function () {
            //this.result就是base64的数据了
            $("#ava")[0].src = this.result;
        };
    })();
    reader.readAsDataURL(e.target.files[0]);
}




