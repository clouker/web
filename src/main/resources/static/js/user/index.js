var grid = null;
$(function(){
    grid = lyGrid({
        pagId : 'grid',
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
                return data;
            }
        }],
        jsonUrl : location.pathname.substr(0,4) + '/user/findByPage',
        checkbox : true,
    });
    var extend = function(o, n, override) {
        for ( var p in n)
            if (n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))
                o[p] = n[p];
    };
})
