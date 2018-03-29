var grid = null;
$(function(){
    grid = lyGrid({
        pagId : 'grid1',
        l_column : [{
            colkey : "NAME",
            name : "部门编码",
        }, {
            colkey : "NAME",
            name : "部门名称",
            align : "left",
        }, {
            colkey : "NAME",
            name : "行政区划",
            renderData : function(rowindex, data, rowdata, column) {
                console.log(data);
                return data;
            }
        }],
        jsonUrl : location.pathname.substr(0,4) + '/user/findByPage',
        checkbox : true,
    });
})
