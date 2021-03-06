/**
 * js表格分页工具
 */
lyGrid = (function (options) {
    var grid = document.getElementById('paging');
    if (options.id)
        grid = document.getElementById(options.id);
    if (!grid) return;
    var confs = {
        l_column: [],
        dymCol: false,
        width: '100%',
        height: '100%',
        theadHeight: '28px',
        tbodyHeight: '27px',
        jsonUrl: '',
        isFixed: false,
        usePage: true,
        setNumber: false,
        selectPageSize: [10, 20, 30, 40, 50, 100],
        goPage: false,
        local: false,
        records: 'records',
        pageNow: 'pageNow',
        totalPages: 'pageCount',
        totalRecords: 'rowCount',
        pagecode: '10',
        async: false,
        data: {order: '', sort: ''},
        pageSize: 10,
        checkbox: false,
        checkValue: 'id',
        trRowClick: null,
        beforeComplete: null,
        afterComplete: null,
        treeGrid: {
            type: 1,
            id: "id",
            pid: "pid",
            name: 'name',
            checkChild: false,
            tree: false, hide: false
        }
    };
    var l_col = {
        colkey: null,
        name: null,
        width: 'auto',
        theadClass: '',
        tbodyClass: '',
        height: 'auto',
        align: 'center',
        hide: false,
        isSort: false,
        renderData: null
    };
    var conf = app.extend(confs, options);
    var l_tree = conf.treeGrid;
    var col = [];
    for (var i = 0; i < conf.l_column.length; i++)
        col.push(l_col);
    for (var i = 0; i < col.length; i++)
        for (var p in col[i])
            if (col[i].hasOwnProperty(p) && (!conf.l_column[i].hasOwnProperty(p)))
                conf.l_column[i][p] = col[i][p];
    var column = conf.l_column;
    var init = function () {
        if (conf.beforeComplete) conf.beforeComplete(conf);
        createHtml();
        if (conf.afterComplete) conf.afterComplete(column, currentData);
        lyickeck()
    };
    var returnData = '';
    var tee = "1-0";
    var createHtml = function () {
        var jsonData = jsonRequest();
        if (jsonData === '')
            return;
        returnData = jsonData;
        grid.innerHTML = '';
        if (String(conf.isFixed) === "true")
            cHeadTable();
        cBodyTh();
        cBodytb(returnData);
        if (String(conf.usePage) === "true")
            fenyeDiv(returnData)
    };
    var replayData = function (o, key, sort) {
        if (conf.beforeComplete) conf.beforeComplete(conf);
        if (o) {
            if (!(returnData !== '' && returnData.records.length > 0))
                returnData = jsonRequest();
            var _array = _.sortBy(returnData.records, key);
            if (sort === "asc")
                returnData.records = _array.reverse();
            else
                returnData.records = _array
        } else
            returnData = jsonRequest();
        cBodytb(returnData);
        if (String(conf.usePage) === "true")
            fenyeDiv(returnData);
        else
            $("#" + grid.id + " div.fenyeDiv").remove();
        lyickeck();
        if (conf.afterComplete) conf.afterComplete(column, currentData)
    };
    var cHeadTable = function () {
        var table = document.createElement("table");
        table.id = "table_head";
        table.className = "pp-list table table-striped table-bordered";
        table.style.marginBottom = '0px';
        grid.appendChild(table);
        var thead = document.createElement('thead');
        table.appendChild(thead);
        var tr = document.createElement('tr');
        tr.style.lineHeight = conf.tbodyHeight;
        thead.appendChild(tr);
        var cn = "";
        if (String(conf.setNumber) == "false")
            cn = "none";
        var th = document.createElement('th');
        th.setAttribute("style", "text-align:center;width: 15px;vertical-align: middle;display: " + cn + ";");
        tr.appendChild(th);
        var cbk = "";
        if (String(conf.checkbox) == "false")
            cbk = "none";
        var cth = document.createElement('th');
        cth.setAttribute("style", "text-align:center;width: 28px;vertical-align: middle;text-align:center;display: " + cbk + ";");
        var chkbox = document.createElement("INPUT");
        chkbox.type = "checkbox";
        chkbox.className = "i-checks";
        chkbox.setAttribute("pagId", grid.id);
        cth.appendChild(chkbox);
        tr.appendChild(cth);
        app.each(column, function (o) {
            if (!column[o].hide || column[o].hide == undefined) {
                var th = document.createElement('th');
                th.className = column[o].theadClass;
                th.setAttribute("style", "text-align:" + column[o].align + ";width: " + column[o].width + ";height:" + conf.theadHeight + ";vertical-align: middle;");
                if (column[o].isSort)
                    th.innerHTML = column[o].name + '<span class="wj-glyph-up"></span>';
                else
                    th.innerHTML = column[o].name;
                tr.appendChild(th)
            }
        })
    };
    var cBodyTh = function () {
        var tdiv = document.createElement("div");
        var h = '';
        var xy = "hidden";
        if (conf.height == "100%") {
            if (String(conf.isFixed) == "false")
                h = "auto";
            else {
                xy = "auto";
                h = $(window).height() - $("#" + (grid.id) + " #table_head").offset().top - $("#" + (grid.id) + " #table_head").find('th:last').eq(0).height();
                if (String(conf.usePage) == "true")
                    h -= 55;
                h += "px"
            }
        } else
            h = conf.height;
        tdiv.setAttribute("style", 'overflow-y: ' + xy + '; height: ' + h + '; background: white;');
        tdiv.className = "t_table";
        grid.appendChild(tdiv);
        var table2 = document.createElement("table");
        table2.id = "mytable";
        table2.className = "pp-list table table-striped table-bordered table-hover";
        table2.setAttribute("style", "margin-bottom: 0px;width:" + conf.width);
        tdiv.appendChild(table2);
        var thead = document.createElement("thead");
        table2.appendChild(thead);
        if (String(conf.isFixed) == "false") {
            var tr = document.createElement('tr');
            tr.setAttribute("style", "line-height:" + conf.tbodyHeight + ";");
            thead.appendChild(tr);
            var cn = "";
            if (String(conf.setNumber) == "false")
                cn = "none";
            var th = document.createElement('th');
            th.setAttribute("style", "text-align:center;width: 15px;vertical-align: middle;display: " + cn + ";");
            tr.appendChild(th);
            var cbk = "";
            if (String(conf.checkbox) == "false")
                cbk = "none";
            var cth = document.createElement('th');
            cth.setAttribute("style", "text-align:center;width: 28px;vertical-align: middle;text-align:center;display: " + cbk + ";");
            var chkbox = document.createElement("INPUT");
            chkbox.type = "checkbox";
            chkbox.className = "i-checks";
            chkbox.setAttribute("pagId", grid.id);
            cth.appendChild(chkbox);
            tr.appendChild(cth);
            app.each(column, function (o) {
                var th = document.createElement('th');
                th.className = column[o].theadClass;
                var at = "text-align:" + column[o].align + ";width: " + column[o].width + ";height:" + conf.theadHeight + ";vertical-align: middle;";
                if (column[o].isSort) {
                    th.innerHTML = column[o].name + '<span class="wj-glyph-up" title="' + column[o].colkey + ',asc"></span>';
                    th.onclick = sortBind.bind();
                    at += "cursor:pointer;"
                } else
                    th.innerHTML = column[o].name;
                if (column[o].hide == true) at += "display:" + (column[o].hide ? 'none' : 'block');
                th.setAttribute("style", at);
                tr.appendChild(th)
            });
            if (conf.dymCol) {
                var ico = document.createElement("i");
                ico.className = "fa fa-thumb-tack";
                ico.setAttribute("style", "float: right;margin-top: 3px;cursor: pointer;");
                ico.onclick = dmycol.bind();
                tr.lastChild.appendChild(ico)
            }
        }
    };
    var currentData;
    var cBodytb = function (jsonData) {
        $('#' + grid.id + ' table > tbody').remove();
        var tbody = document.createElement("tbody");
        grid.getElementsByTagName('table')[0].appendChild(tbody);
        var json = app._getValueByName(jsonData, conf.records);
        var d = 0;
        var e = json.length;
        if (String(conf.local) == "true") {
            pNow = parseInt(app._getValueByName(jsonData, conf.pageNow), 10);
            d = (pNow - 1) * conf.pageSize;
            e = pNow * conf.pageSize
        }
        currentData = [];
        for (; d < e; d++) {
            var rowdata = json[d];
            currentData.push(rowdata);
            if (app.notNull(rowdata)) {
                var tr = document.createElement('tr');
                tr.ondblclick = trRowDBClick.bind();
                tr.setAttribute("style", "line-height:" + conf.tbodyHeight + ";");
                var sm = parseInt(tee.substring(tee.lastIndexOf("-") + 1), 10) + 1;
                tee = tee.substring(0, tee.lastIndexOf("-"));
                tee = tee + "-" + sm;
                tr.setAttribute("d-tree", tee);
                tbody.appendChild(tr);
                var cn = "";
                if (String(conf.setNumber) == "false") {
                    cn = "none"
                }
                var ntd_d = tr.insertCell(-1);
                ntd_d.setAttribute("style", "text-align:center;width: 15px;display: " + cn + ";");
                var rowindex = tr.rowIndex;
                ntd_d.innerHTML = rowindex;
                var cbk = "";
                if (String(conf.checkbox) == "false") {
                    cbk = "none"
                }
                var td_d = tr.insertCell(-1);
                td_d.setAttribute("style", "text-align:center;width: 28px;display: " + cbk + ";");
                var chkbox = document.createElement("INPUT");
                chkbox.type = "checkbox";
                chkbox.className = "i-checks";
                chkbox.setAttribute("cid", app._getValueByName(rowdata, l_tree.id));
                chkbox.setAttribute("pid", app._getValueByName(rowdata, l_tree.pid));
                chkbox.setAttribute("_l_key", "checkbox");
                chkbox.value = app._getValueByName(rowdata, conf.checkValue);
                td_d.appendChild(chkbox);
                $.each(column, function (o) {
                    var td_o = tr.insertCell(-1);
                    td_o.className = column[o].tbodyClass;
                    var at = "text-align:" + column[o].align + ";width: " + column[o].width + ";vertical-align: middle;word-break: keep-all;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;";
                    var colkey = column[o].colkey;
                    var data = app.notEmpty(app._getValueByName(rowdata, colkey));
                    if (String(l_tree.tree) == "true") {
                        var lt = l_tree.name.split(",");
                        if (app.in_array(lt, colkey)) {
                            var itree = document.createElement("i");
                            if (String(l_tree.hide) == "true") itree.className = "fa fa-arrows"; else itree.className = "fa fa-arrows-h";
                            itree.onclick = datatree.bind();
                            td_o.appendChild(itree);
                            var divspan = document.createElement("span");
                            divspan.className = "l_test";
                            divspan.setAttribute("style", "line-height:" + conf.tbodyHeight + ";");
                            if (column[o].renderData) {
                                var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                                divspan.innerHTML = fn(rowindex, data, rowdata, colkey)
                            } else {
                                divspan.innerHTML = data
                            }
                            td_o.appendChild(divspan)
                        } else {
                            if (column[o].renderData) {
                                var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                                td_o.innerHTML = fn(rowindex, data, rowdata, colkey)
                            } else {
                                td_o.innerHTML = data
                            }
                        }
                    } else {
                        if (column[o].renderData) {
                            var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                            td_o.innerHTML = fn(rowindex, data, rowdata, colkey)
                        } else {
                            td_o.innerHTML = data
                        }
                    }
                    if (column[o].hide == true) at += "display:" + (column[o].hide ? 'none' : 'block');
                    td_o.setAttribute("style", at)
                });
                if (String(l_tree.tree) == "true") {
                    if (l_tree.type == 1) {
                        tee = tee + "-0";
                        treeHtml(tbody, rowdata)
                    } else {
                        var obj = json[d];
                        delete json[d];
                        nb = 28;
                        treeSimpleHtml(tbody, json, obj)
                    }
                }
            }
        }
    };
    var fenyeDiv = function (jsonData) {
        $("#" + grid.id + " div.fenyeDiv").remove();
        var totalRecords = app._getValueByName(jsonData, conf.totalRecords);
        var totalPages = app._getValueByName(jsonData, conf.totalPages);
        var pageNow = app._getValueByName(jsonData, conf.pageNow);
        if (String(conf.local) == "true") {
            totalRecords = jsonData.records.length;
            totalPages = Math.ceil(totalRecords / conf.pageSize)
        }
        var bdiv = document.createElement("div");
        bdiv.id = "fenyeDiv";
        bdiv.setAttribute("style", "vertical-align: middle;");
        bdiv.className = "fenyeDiv span12 center";
        grid.appendChild(bdiv);
        var btable = document.createElement("table");
        btable.width = "100%";
        bdiv.appendChild(btable);
        var btr = document.createElement("tr");
        btable.appendChild(btr);
        var btd_1 = document.createElement("td");
        btd_1.style.textAlign = "left";
        btr.appendChild(btd_1);
        var btddiv = document.createElement("div");
        btddiv.className = "pagination";
        btd_1.appendChild(btddiv);
        var divul = document.createElement("ul");
        btddiv.appendChild(divul);
        var ulli = document.createElement("li");
        ulli.className = "prev";
        divul.appendChild(ulli);
        var lia = document.createElement("a");
        lia.href = "javascript:void(0);";
        ulli.appendChild(lia);
        lia.innerHTML = '总 ' + totalRecords + ' 条 每页 ' + conf.pageSize + ' 条 共 ' + totalPages + ' 页';
        var btd_1 = document.createElement("td");
        btd_1.style.textAlign = "right";
        btr.appendChild(btd_1);
        var divul_2 = document.createElement("ul");
        divul_2.className = "dataTables_paginate paging_bootstrap pagination";
        btd_1.appendChild(divul_2);
        var ulli_8 = document.createElement("li");
        divul_2.appendChild(ulli_8);
        var lia_8_span = document.createElement("span");
        lia_8_span.setAttribute("style", "padding: 1px 5px;");
        lia_8_span.innerHTML = "每页显示";
        ulli_8.appendChild(lia_8_span);
        var lia_8_A = document.createElement("select");
        lia_8_A.setAttribute("style", "border:0;");
        lia_8_A.onchange = pageBind.bind();
        lia_8_span.appendChild(lia_8_A);
        var sp = conf.selectPageSize;
        for (var it in sp) {
            var op = new Option(sp[it], sp[it]);
            lia_8_A.options.add(op);
            if (sp[it] == conf.pageSize) {
                op.selected = 'selected'
            }
        }
        if (String(conf.goPage) == "true") {
            var ulli_8 = document.createElement("li");
            divul_2.appendChild(ulli_8);
            var ulli_input = document.createElement("input");
            ulli_input.onkeydown = pageBind.bind();
            ulli_input.className = "col-xs-1 text-center";
            ulli_input.setAttribute("style", "WIDTH:40PX;margin-left: 10px;");
            ulli_input.type = "text";
            ulli_input.value = pageNow;
            ulli_8.appendChild(ulli_input);
            var lia_8_A = document.createElement("a");
            lia_8_A.onclick = pageBind.bind();
            lia_8_A.setAttribute("style", "margin-right: 10px;");
            lia_8_A.href = "javascript:void(0);";
            lia_8_A.className = "btn btn-sm btn-danger";
            lia_8_A.innerHTML = 'GO';
            lia_8_A.id = "page_go";
            ulli_8.appendChild(lia_8_A)
        }
        if (pageNow > 1) {
            var ulli_2 = document.createElement("li");
            divul_2.appendChild(ulli_2);
            var lia_2 = document.createElement("a");
            lia_2.onclick = pageBind.bind();
            lia_2.id = "pagNum_" + (pageNow - 1);
            lia_2.href = "javascript:void(0);";
            lia_2.innerHTML = '上一页';
            ulli_2.appendChild(lia_2)
        } else {
            var ulli_2 = document.createElement("li");
            ulli_2.className = "prev disabled";
            divul_2.appendChild(ulli_2);
            var lia_2 = document.createElement("a");
            lia_2.href = "javascript:void(0);";
            lia_2.innerHTML = '上一页';
            ulli_2.appendChild(lia_2)
        }
        var pg = pagesIndex(conf.pagecode, pageNow, totalPages);
        var startpage = pg.start;
        var endpage = pg.end;
        if (startpage != 1) {
            var ulli_3 = document.createElement("li");
            divul_2.appendChild(ulli_3);
            var lia_3 = document.createElement("a");
            lia_3.onclick = pageBind.bind();
            lia_3.href = "javascript:void(0);";
            lia_3.id = "pagNum_1";
            lia_3.innerHTML = '1...';
            ulli_3.appendChild(lia_3)
        }
        for (var i = startpage; i <= endpage; i++) {
            if (i == pageNow) {
                var ulli_5 = document.createElement("li");
                ulli_5.className = "active";
                divul_2.appendChild(ulli_5);
                var lia_5 = document.createElement("a");
                lia_5.href = "javascript:void(0);";
                lia_5.innerHTML = i;
                ulli_5.appendChild(lia_5)
            } else {
                var ulli_5 = document.createElement("li");
                divul_2.appendChild(ulli_5);
                var lia_5 = document.createElement("a");
                lia_5.onclick = pageBind.bind();
                lia_5.href = "javascript:void(0);";
                lia_5.id = "pagNum_" + i;
                lia_5.innerHTML = i;
                ulli_5.appendChild(lia_5)
            }
        }
        if (endpage != totalPages) {
            var ulli_6 = document.createElement("li");
            divul_2.appendChild(ulli_6);
            var lia_6 = document.createElement("a");
            lia_6.onclick = pageBind.bind();
            lia_6.href = "javascript:void(0);";
            lia_6.id = "pagNum_" + totalPages;
            lia_6.innerHTML = '...' + totalPages;
            ulli_6.appendChild(lia_6)
        }
        if (pageNow >= totalPages) {
            var ulli_7 = document.createElement("li");
            ulli_7.className = "prev disabled";
            divul_2.appendChild(ulli_7);
            var lia_7 = document.createElement("a");
            lia_7.href = "javascript:void(0);";
            lia_7.innerHTML = '下一页';
            ulli_7.appendChild(lia_7)
        } else {
            var ulli_7 = document.createElement("li");
            ulli_7.className = "next";
            divul_2.appendChild(ulli_7);
            var lia_7 = document.createElement("a");
            lia_7.onclick = pageBind.bind();
            lia_7.href = "javascript:void(0);";
            lia_7.id = "pagNum_" + (pageNow + 1);
            lia_7.innerHTML = '下一页';
            ulli_7.appendChild(lia_7)
        }
    };
    var nb = '28';
    var treeHtml = function (tbody, data) {
        if (data == undefined) return;
        var jsonTree = data.children;
        if (jsonTree == undefined || jsonTree == '' || jsonTree == null) {
        } else {
            var tte = false;
            $.each(jsonTree, function (jt) {
                var tte = false;
                if (jsonTree[jt].children != undefined && jsonTree[jt].children != '' && jsonTree[jt].children != null) {
                    tte = true
                }
                var tr = document.createElement('tr');
                var di = '';
                if (String(l_tree.hide) == "true") di = 'display: none;';
                tr.setAttribute("style", "line-height:" + conf.tbodyHeight + ";" + di);
                var sm = parseInt(tee.substring(tee.lastIndexOf("-") + 1), 10) + 1;
                tee = tee.substring(0, tee.lastIndexOf("-"));
                tee = tee + "-" + sm;
                tr.setAttribute("d-tree", tee);
                tbody.appendChild(tr);
                var cn = "";
                if (String(conf.setNumber) == "false") {
                    cn = "none"
                }
                var ntd_d = tr.insertCell(-1);
                ntd_d.setAttribute("style", "text-align:center;width: 15px;display: " + cn + ";");
                var rowindex = tr.rowIndex;
                ntd_d.innerHTML = rowindex;
                var cbk = "";
                if (String(conf.checkbox) == "false") {
                    cbk = "none"
                }
                var td_d = tr.insertCell(-1);
                td_d.setAttribute("style", "text-align:center;width: 28px;display: " + cbk + ";");
                var chkbox = document.createElement("INPUT");
                chkbox.type = "checkbox";
                chkbox.className = "i-checks";
                chkbox.setAttribute("cid", app._getValueByName(jsonTree[jt], l_tree.id));
                chkbox.setAttribute("pid", app._getValueByName(jsonTree[jt], l_tree.pid));
                chkbox.setAttribute("_l_key", "checkbox");
                chkbox.value = app._getValueByName(jsonTree[jt], conf.checkValue);
                td_d.appendChild(chkbox);
                app.each(column, function (o) {
                    if (!column[o].hide || column[o].hide == undefined) {
                        var td_o = tr.insertCell(-1);
                        td_o.setAttribute("style", "text-align:" + column[o].align + ";width: " + column[o].width + ";vertical-align: middle;");
                        var rowdata = jsonTree[jt];
                        var colkey = column[o].colkey;
                        var data = app.notEmpty(app._getValueByName(rowdata, colkey));
                        if (String(l_tree.tree) == "true") {
                            var lt = l_tree.name.split(",");
                            if (app.in_array(lt, column[o].colkey)) {
                                var itree = document.createElement("i");
                                itree.onclick = datatree.bind();
                                if (tte) {
                                    itree.className = "fa fa-arrows-h"
                                }
                                td_o.appendChild(itree);
                                var divspan = document.createElement("span");
                                divspan.className = "l_test";
                                divspan.setAttribute("style", "line-height:" + conf.tbodyHeight + ";");
                                if (column[o].renderData) {
                                    var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                                    divspan.innerHTML = fn(rowindex, data, rowdata, colkey)
                                } else {
                                    divspan.innerHTML = data
                                }
                                td_o.appendChild(divspan)
                            } else {
                                if (column[o].renderData) {
                                    var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                                    td_o.innerHTML = fn(rowindex, data, rowdata, colkey)
                                } else {
                                    td_o.innerHTML = data
                                }
                            }
                        } else {
                            if (column[o].renderData) {
                                var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                                td_o.innerHTML = fn(rowindex, data, rowdata, colkey)
                            } else {
                                td_o.innerHTML = data
                            }
                        }
                    }
                });
                if (tte) {
                    tee = tee + "-0";
                    nb = parseInt(nb, 10) + 28;
                    treeHtml(tbody, jsonTree[jt])
                }
            });
            tee = tee.substring(0, tee.lastIndexOf("-"));
            nb = 28
        }
    };
    var itree;
    var treeSimpleHtml = function (tbody, jsonTree, obj) {
        var tte = false;
        tee = tee + "-0";
        app.each(jsonTree, function (jt) {
            if (app.notNull(jsonTree[jt])) {
                var jsb = app._getValueByName(jsonTree[jt], l_tree.pid);
                var ob = app._getValueByName(obj, l_tree.id);
                if (jsb == ob) {
                    tte = true;
                    var tr = document.createElement('tr');
                    var di = '';
                    if (String(l_tree.hide) == "true") di = 'display: none;';
                    tr.setAttribute("style", "line-height:" + conf.tbodyHeight + ";" + di);
                    var sm = parseInt(tee.substring(tee.lastIndexOf("-") + 1), 10) + 1;
                    tee = tee.substring(0, tee.lastIndexOf("-"));
                    tee = tee + "-" + sm;
                    tr.setAttribute("d-tree", tee);
                    tbody.appendChild(tr);
                    var cn = "";
                    if (String(conf.setNumber) == "false") {
                        cn = "none"
                    }
                    var ntd_d = tr.insertCell(-1);
                    ntd_d.setAttribute("style", "text-align:center;width: 15px;display: " + cn + ";");
                    var rowindex = tr.rowIndex;
                    ntd_d.innerHTML = rowindex;
                    var cbk = "";
                    if (String(conf.checkbox) == "false") {
                        cbk = "none"
                    }
                    var td_d = tr.insertCell(-1);
                    td_d.setAttribute("style", "text-align:center;width: 28px;display: " + cbk + ";");
                    var chkbox = document.createElement("INPUT");
                    chkbox.type = "checkbox";
                    chkbox.className = "i-checks";
                    chkbox.setAttribute("cid", app._getValueByName(jsonTree[jt], l_tree.id));
                    chkbox.setAttribute("pid", app._getValueByName(jsonTree[jt], l_tree.pid));
                    chkbox.setAttribute("_l_key", "checkbox");
                    chkbox.value = app._getValueByName(jsonTree[jt], conf.checkValue);
                    td_d.appendChild(chkbox);
                    $.each(column, function (o) {
                        if (!column[o].hide || column[o].hide == undefined) {
                            var td_o = tr.insertCell(-1);
                            td_o.setAttribute("style", "text-align:" + column[o].align + ";width: " + column[o].width + ";vertical-align: middle;");
                            var rowdata = jsonTree[jt];
                            var colkey = column[o].colkey;
                            var data = app.notEmpty(app._getValueByName(rowdata, colkey));
                            if (String(l_tree.tree) == "true") {
                                var lt = l_tree.name.split(",");
                                if (app.in_array(lt, column[o].colkey)) {
                                    td_o.setAttribute("style", "padding-left: " + nb + "px");
                                    itree = document.createElement("i");
                                    if (String(l_tree.hide) == "true") itree.className = "fa fa-arrows"; else itree.className = "fa fa-arrows-h";
                                    itree.onclick = datatree.bind();
                                    td_o.appendChild(itree);
                                    var divspan = document.createElement("span");
                                    divspan.className = "l_test";
                                    divspan.setAttribute("style", "line-height:" + conf.tbodyHeight + ";");
                                    if (column[o].renderData) {
                                        var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                                        divspan.innerHTML = fn(rowindex, data, rowdata, colkey)
                                    } else {
                                        divspan.innerHTML = data
                                    }
                                    td_o.appendChild(divspan)
                                } else {
                                    if (column[o].renderData) {
                                        var fn = new Function('return ' + column[o].renderData)(rowindex, data, rowdata, colkey);
                                        td_o.innerHTML = fn(rowindex, data, rowdata, colkey)
                                    } else {
                                        td_o.innerHTML = data
                                    }
                                }
                            } else {
                                if (column[o].renderData) {
                                    var fn = new Function('return ' + column[o].renderData);
                                    td_o.innerHTML = fn(rowindex, data, rowdata, colkey)
                                } else {
                                    td_o.innerHTML = data
                                }
                            }
                        }
                    });
                    var o = jsonTree[jt];
                    delete jsonTree[jt];
                    nb = parseInt(nb, 10) + 28;
                    treeSimpleHtml(tbody, jsonTree, o)
                }
            }
        });
        if (!tte) {
            if (app.notNull(itree)) itree.remove(itree.selectedIndex)
        }
        tee = tee.substring(0, tee.lastIndexOf("-"));
        nb = parseInt(nb, 10) - 28
    };
    var uptr = function (getUp) {
        if (getUp.css("display") == "none") {
            return uptr(getUp.prev())
        }
        return getUp
    };
    var downtr = function (getdown) {
        if (getdown.css("display") == "none") {
            return downtr(getdown.next())
        }
        return getdown
    };
    var lyGridUp = function (jsonUrl) {
        var ck = $("#" + grid.id + " input[_l_key='checkbox']:checkbox:checked")[0];
        if (ck) {
            var onthis = $(ck).parent().parent().parent();
            var getUp = onthis.prev();
            var ck_tree = $(onthis).attr("d-tree");
            var getUp_tree = $(getUp).attr("d-tree");
            if (ck_tree.indexOf(getUp_tree) > -1) {
                return
            }
            var pup = uptr(getUp);
            $(pup).before(onthis);
            $(onthis).after($("tr[d-tree^='" + $(onthis).attr("d-tree") + "-']"));
            var row = rowline();
            var data = [];
            $.each(row, function (i) {
                data.push(conf.checkValue + "[" + i + "]=" + row[i].rowId);
                data.push("rowId[" + i + "]=" + row[i].rowNum)
            });
            if (jsonUrl) $.ajax({type: 'POST', data: data.join("&"), url: jsonUrl, dataType: 'json',})
        }
    };
    var lyGridDown = function (jsonUrl) {
        var ck = $("#" + grid.id + " input[_l_key='checkbox']:checkbox:checked")[0];
        if (ck) {
            var onthis = $(ck).parent().parent().parent();
            onthis.find("td").eq(2).find("i").removeClass("fa fa-arrows-h").addClass("fa fa-arrows");
            var trd = $("tr[d-tree^='" + $(onthis).attr("d-tree") + "-']");
            trd.hide();
            var getdown = onthis.next();
            var pup = downtr(getdown);
            var ck_tree = $(onthis).attr("d-tree").split("-");
            var downUp_tree = $(pup).attr("d-tree").split("-");
            if (ck_tree.length != downUp_tree.length) return;
            $(onthis).before(pup);
            $(pup).after($("#" + (grid.id) + " tr[d-tree^='" + $(pup).attr("d-tree") + "-']"));
            var row = rowline();
            var data = [];
            $.each(row, function (i) {
                data.push(conf.checkValue + "[" + i + "]=" + row[i].rowId);
                data.push("rowId[" + i + "]=" + row[i].rowNum)
            });
            if (jsonUrl) $.ajax({type: 'POST', data: data.join("&"), url: jsonUrl, dataType: 'json',})
        }
    };
    var highlight = function (event) {
        var tr = event.parentNode.parentNode.parentNode;
        event.checked ? setBgColor(tr) : restoreBgColor(tr);
        var ttr = $(tr).attr("d-tree");
        var tr_tree = $("#" + (grid.id) + " tr[d-tree^='" + ttr + "-']");
        if (conf.treeGrid.checkChild) {
            tr_tree.each(function () {
                var cn = $(this).find("td").eq(1).find("div").eq(0);
                var checkboxes = cn.find("input");
                if (event.checked) {
                    checkboxes.prop('checked', true);
                    cn.addClass("checked");
                    setBgColor(this)
                } else {
                    checkboxes.prop('checked', false);
                    cn.removeClass("checked");
                    restoreBgColor(this)
                }
            })
        }
    };
    var selectRow = function (pagId) {
        var ck = getSelectedCheckbox(pagId);
        var json = app._getValueByName(returnData, conf.records);
        var ret = [];
        $.each(json, function (d) {
            $.each(ck, function (c) {
                if (ck[c] == app._getValueByName(json[d], conf.checkValue)) ret.push(json[d])
            })
        });
        return ret
    };
    var trRowDBClick = function (event) {
        if (conf.trRowClick) {
            var tr = $(event.currentTarget)[0];
            var checkboxes = $(event.currentTarget).find("input")[0];
            var cn = $(checkboxes.parentNode);
            if (checkboxes.checked) {
                checkboxes.checked = false;
                cn.removeClass("checked");
                restoreBgColor(tr)
            } else {
                checkboxes.checked = true;
                cn.addClass("checked");
                setBgColor(tr)
            }
            var rowdata = app._getValueByName(returnData, conf.records);
            conf.trRowClick(tr.rowIndex, rowdata[tr.rowIndex - 1])
        }
    };
    var trClick = function () {
    };
    var checkboxbind = function (event) {
        var checkboxes = $("#" + grid.id + " input[_l_key='checkbox']");
        if (event.checked) {
            checkboxes.prop('checked', true)
        } else {
            checkboxes.prop('checked', false)
        }
        checkboxes.each(function () {
            var tr = this.parentNode.parentNode.parentNode;
            var cn = $(this.parentNode);
            if (event.checked) {
                cn.addClass("checked");
                setBgColor(tr)
            } else {
                cn.removeClass("checked");
                restoreBgColor(tr)
            }
        })
    };
    var pageBind = function (e) {
        var evt = arguments[0] || window.event;
        var b = true;
        if (evt.keyCode != undefined && evt.keyCode != 13) {
            b = false
        }
        if (b) {
            var obj = evt.srcElement || evt.target;
            var page = returnData.pageNow;
            var pageSize = conf.pageSize;
            if (obj.nodeName == "INPUT") {
                page = obj.value
            } else if (obj.nodeName == "SELECT") {
                pageSize = obj.value
            } else if (obj.id == "page_go") {
                page = obj.previousSibling.value
            } else if (obj.nodeName == "A") {
                page = obj.id.split('_')[1]
            }
            if (isNaN(page)) return;
            if (String(conf.local) == "true") {
                returnData.pageNow = parseInt(page, 10);
                conf = app.extend(conf, {pageSize: parseInt(pageSize, 10)});
                cBodytb(returnData);
                if (String(conf.usePage) == "true") {
                    fenyeDiv(returnData);
                    lyickeck()
                }
            } else {
                conf.data = app.extend(conf.data, {pageNow: page, pageSize: pageSize});
                replayData()
            }
        }
    };
    var sortBind = function () {
        var evt = arguments[0] || window.event;
        var th = evt.srcElement || evt.target;
        var t = th.title.split(",");
        if (t[0] == "") {
            th = th.firstElementChild;
            t = th.title.split(",")
        }
        var $sort = "";
        if (t[1] == "asc") {
            th.className = "wj-glyph-down";
            th.title = t[0] + ",desc";
            $sort = "desc"
        } else {
            th.className = "wj-glyph-up";
            th.title = t[0] + ",asc";
            $sort = "asc"
        }
        conf.data = app.extend(conf.data, {order: t[0], sort: $sort});
        if (String(conf.local) == "true") replayData('0', t[0], sc); else replayData()
    };
    var datatree = function () {
        var evt = arguments[0] || window.event;
        var img = evt.srcElement || evt.target;
        var ttr = img.parentElement.parentElement.getAttribute('d-tree');
        if (img.className.indexOf("fa fa-arrows-h") > -1) {
            img.className = "fa fa-arrows";
            var tr = $("#" + (grid.id) + " tr[d-tree^='" + ttr + "-']");
            tr.each(function () {
                $(this).find("td").eq(2).find("i").removeClass("fa fa-arrows-h").addClass("fa fa-arrows")
            });
            tr.hide()
        } else {
            img.className = "fa fa-arrows-h";
            for (var m = 0; m < 20; m++) $("tr[d-tree='" + ttr + "-" + m + "']").show()
        }
    };
    var getChkBox = function (tr) {
        return tr.cells[1].firstChild.firstChild
    };
    var restoreBgColor = function (tr) {
        for (var i = 0; i < tr.childNodes.length; i++) {
            tr.childNodes[i].style.backgroundColor = ""
        }
    };
    var setBgColor = function (tr) {
        for (var i = 0; i < tr.childNodes.length; i++) {
            tr.childNodes[i].style.backgroundColor = "rgba(0,0,0,.075)"
        }
    };
    var rowline = function () {
        var cb = [];
        var arr = $("#" + grid.id + " table  tr");
        for (var i = arr.length - 1; i > 0; i--) {
            var cbox = getChkBox(arr[i]).value;
            var row = arr[i].rowIndex;
            var sort = {};
            sort.rowNum = row;
            sort.rowId = cbox;
            cb.push(sort)
        }
        return cb.reverse()
    };
    var pagesIndex = function (pagecode, pageNow, pageCount) {
        pagecode = parseInt(pagecode, 10);
        pageNow = parseInt(pageNow, 10);
        pageCount = parseInt(pageCount, 10);
        var startpage = pageNow - (pagecode % 2 == 0 ? pagecode / 2 - 1 : pagecode / 2);
        var endpage = pageNow + pagecode / 2;
        if (startpage < 1) {
            startpage = 1;
            if (pageCount >= pagecode) endpage = pagecode; else endpage = pageCount
        }
        if (endpage > pageCount) {
            endpage = pageCount;
            if ((endpage - pagecode) > 0) startpage = endpage - pagecode + 1; else startpage = 1
        }
        return {start: startpage, end: endpage}
    };
    var loadData = function () {
        app.extend(conf, options);
        replayData()
    };
    var setOptions = function (options) {
        var data;
        if (options.data) {
            data = app.extend(conf.data, options.data);
            options.data = data
        }
        if (options.pageSize) {
            conf.data.pageSize = options.pageSize
        }
        conf = app.extend(conf, options);
        conf.data.pageNow = 1;
        replayData()
    };
    var getSelectedCheckbox = function (pagId) {
        if (pagId == '' || pagId == undefined) {
            pagId = grid.id
        }
        var arr = [];
        $("#" + pagId + " input[_l_key='checkbox']:checkbox:checked").each(function () {
            arr.push($(this).val())
        });
        return arr
    };
    var selectTreeRow = function (pagId) {
        var ck = getSelectedCheckbox(pagId);
        var json = app._getValueByName(returnData, conf.records);
        var ret = [];
        $.each(json, function (d) {
            $.each(ck, function (c) {
                if (ck[c] == app._getValueByName(json[d], conf.checkValue)) {
                    ret.push(json[d])
                } else {
                    $.each(json[d].children, function (child) {
                        if (ck[c] == app._getValueByName(json[d].children[child], conf.checkValue)) {
                            ret.push(json[d].children[child])
                        }
                    })
                }
            })
        });
        return ret
    };
    var getColumn = function () {
        return column
    };
    var exportData = function (url) {
        var form = $("<form>");
        form.attr("style", "display:none");
        form.attr("target", "");
        form.attr("method", "post");
        form.attr("action", url);
        $("body").append(form);
        var input1 = $("<input>");
        input1.attr("type", "hidden");
        input1.attr("name", "exportData");
        input1.attr("value", JSON.stringify(column));
        form.append(input1);
        var par = conf.data;
        for (var p in par) {
            var input1 = $("<input>");
            input1.attr("type", "hidden");
            input1.attr("name", p);
            input1.attr("value", par[p]);
            form.append(input1)
        }
        form.submit()
    };
    var getCurrentData = function () {
        return currentData
    };
    var dmycolcheck = function (e) {
        var u = $(e.target).attr("span_value");
        if ($(e.target).attr("class").indexOf("checked") == -1) {
            $(e.target).addClass("checked");
            $(grid).find('table:eq(0) tr th:nth-child(' + (parseInt(u, 10) + 3) + ')').hide();
            $(grid).find('table:eq(0) tr td:nth-child(' + (parseInt(u, 10) + 3) + ')').hide()
        } else {
            $(e.target).removeClass("checked");
            $(grid).find('table:eq(0) tr th:nth-child(' + (parseInt(u, 10) + 3) + ')').show();
            $(grid).find('table:eq(0) tr td:nth-child(' + (parseInt(u, 10) + 3) + ')').show()
        }
    };
    var dmycol = function (e) {
        if ($('#' + grid.id + ' #ul_dmycol').length > 0) {
            $('#' + grid.id + ' #ul_dmycol').toggle()
        } else {
            var ul = document.createElement("ul");
            ul.className = "dmycol-menu";
            ul.id = "ul_dmycol";
            var w = $(e.target.parentNode).width();
            if (w < 130) {
                var f = 130 - w;
                ul.setAttribute("style", "margin-left:-" + f + "px;")
            } else {
                ul.setAttribute("style", "margin-left:0;width:" + w + "px")
            }
            $.each(column, function (i, o) {
                var li = document.createElement("li");
                var spanbox = document.createElement("span");
                if (o.hide === true) spanbox.className = "span_checkbox checked"; else spanbox.className = "span_checkbox";
                spanbox.setAttribute("span_value", i);
                spanbox.name = o.colkey;
                spanbox.onclick = dmycolcheck.bind();
                li.appendChild(spanbox);
                var sp = document.createElement("span");
                sp.innerHTML = " " + o.name;
                li.appendChild(sp);
                ul.appendChild(li)
            });
            e.target.parentNode.insertBefore(ul, null)
        }
    };
    var lyickeck = function () {
        $('#' + grid.id + ' .i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green'
        });
        $("#" + grid.id + " th:eq(1) input[class*='i-checks']").on('ifChanged', function () {
            checkboxbind(this)
        });
        $("#" + grid.id + " input[_l_key='checkbox']").on('ifChanged', function () {
            highlight(this)
        })
    };
    var jsonRequest = function () {
        var json = {};
        var p = {};
        if (conf.data.pageSize) {
            p.pageSize = conf.data.pageSize
        } else {
            p.pageSize = conf.pageSize
        }
        var d = app.extend(conf.data, p);
        if (String(conf.local) === "true") {
            json.records = conf.jsonUrl;
            json.pageSize = conf.pageSize;
            json.pageNow = 1;
            json.totalRecords = 0;
            json.totalPages = 0
        } else {
            json = '';
            $.ajax({
                type: 'POST',
                async: false,
                data: d,
                url: conf.jsonUrl,
                dataType: 'json',
                success: function (result) {
                    if (result.data !== undefined)
                        json = result.data;
                    else
                        json = result
                },
                error: function (msg) {
                    json = ''
                }
            })
        }
        conf = app.extend(conf, {pagecode: json.pagecode, pageSize: json.pageSize});
        return json
    };
    init();
    return {
        setOptions: setOptions,
        loadData: loadData,
        getSelectedCheckbox: getSelectedCheckbox,
        selectRow: selectRow,
        selectTreeRow: selectTreeRow,
        lyGridUp: lyGridUp,
        lyGridDown: lyGridDown,
        resultJSONData: jsonRequest,
        exportData: exportData,
        getColumn: getColumn,
        getCurrentData: getCurrentData
    }
});
