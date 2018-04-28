var grid = null;
window.onload = function () {
    grid = lyGrid({
        // id: 'paging',
        l_column: [{
            colkey: "name",
            name: "账号"
        }, {
            colkey: "avatar",
            name: "头像",
            renderData: function (rowindex, data) {
                if (data)/*data:image/jpeg|png|gif;base64,*/
                    return '<img style="width: 35px;height:35px;border-radius: 80%" src="' + data + '" style="width: 43px;"/>';
                return '<img src="' + '/images/ava.jpg" style="width: 35px;height:35px;border-radius: 80%" title="默认图标"/>';
            }
        }, {
            colkey: "idCard",
            name: "身份证号"
        }, {
            colkey: "sex",
            name: "性别",
            renderData: function (rowindex, data) {
                if (data == 1)
                    return '女';
                else if (data == 0)
                    return '男';
                return '未知';
            }
        }, {
            colkey: "email",
            name: "邮箱"
        }, {
            colkey: "updateTime",
            name: "更新时间",
            isSort: true
        }, {
            colkey: "comment",
            name: "描述"
        }, {
            colkey: "lockFlag",
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
        }],
        async: false, // 默认为同步
        setNumber: true,
        pageSize: 10,
        checkbox: true,
        checkValue: 'id',
        checkbox: true,
        selectPageSize: [10, 20, 50],
        goPage: true,
        jsonUrl: '/user/findByPage',
        data: {},
        trRowClick: function (index, data) {
        }
    });
    $('.search-btn').click(function () {
        // if ($('.search-input').val().length == 0)
        //     return swal("请输入条件");
        grid.setOptions({data: {search: $('.search-input').val()}})
    });
    // $(".export").click(function () {
    //     exportTable();
    // })


    $(".add").click(function () {
        // var $addDiv = document.getElementById('addDiv').innerHTML;
        //igm是指分别用于指定区分大小写的匹配、全局匹配和多行匹配。
        // var reg = new RegExp('\\[([^\\[\\]]*?)\\]', 'igm');
        // var target = $addDiv.replace(reg, function (node, key) {
        //     return {'data': '标题', 'data1': '说的'}[key];
        // })
        oper({
            title: '<h3>用户添加</h3>',
            html: $('#addDiv').html(),
            backgroud: "RGBA(114,115,244,0.5) url(/images/cool.gif) no-repeat left center"
        });

        // swal({
        //     title: "添加用户",
        //     html: $('#addDiv').html(),
        //     // showConfirmButton: false,
        //     backdrop: 'rgba(0,0,123,0.4) url("/images/cool.gif") center left no-repeat'
        // }).then(function (isConfirm) {
        //     if (isConfirm.value) {
        //         $('.add-user').submit(function (e) {
        //             var form = $(this);
        //         });
        //     }
        // })
    })
}

function exportTable() {
    console.log(grid.getColumn());
    // oper.jsalert('待开发。。。');
    // grid.exportData();
    return;
}

function gridOptions() {// 绑定查询按扭
    grid.setOptions({
        pageSize: 20
    });
    grid.loadData();
    grid.setOptions({
        beforeComplete: function (conf) {
            var s = "加载之前触发,当前表格配置参数 " + JSON.stringify(conf);
            // operjs.alert(s);
        }
    });
    grid.setOptions({
        afterComplete: function (column, currentData) {
            var s = "加载之后触发,当前页数据是 " + JSON.stringify(currentData);
            // oper.jsalert(s);
        }
    });
    // operjs.alert("当前选中多选框的值 " + JSON.stringify(grid.getSelectedCheckbox()));
    // oper.jsalert("当前先中的值 " + JSON.stringify(grid.selectRow()));
    // //数据上移,可设置参数url  grid.lyGridUp(url); 如果不设置,不请求后台
    // grid.lyGridUp();
    // //数据下移,可设置参数url  grid.lyGridDown(url ); 如果不设置,不请求后台
    // grid.lyGridDown(location.pathname.substr(0, 4) + '/user/findByPage');
    // oper.jsalert("当前表格所有数据 " + JSON.stringify(grid.resultJSONData()));
    // oper.jsalert("当前表格表头 " + JSON.stringify(grid.getColumn()));
}

function ConvertToBase64Img(e) {
    var file = e.target.files[0];//图片文件
    if (file.size > 1024 * 1024)
        return new oper("图片尺寸过大！！！");
    var reader = new FileReader();
    reader.onload = (function () {
        return function () {
            $("#ava")[0].src = this.result;
            $(".avatar").val(this.result);
        }
    })();
    reader.readAsDataURL(e.target.files[0]);
}




