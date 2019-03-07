$(function () {

})

/**
 * 测试数据源连接信息
 */
function testConnection() {
    var data = getDataSource();
    if (data.length <= 2 || data === "{}") {
        return false;
    }
    $.ajax({
        type: "POST",
        url: "datasource/test",
        contentType: "application/json;charset=utf-8",
        data: data,
        dataType: "json",
        success: function (result) {
            if (result.success === true || result.success === "true") {
                toastr.success('测试成功，可以保存数据源！');
            } else {
                toastr.error('测试连接失败：' + result.message);
            }
        },
        error: function (result) {
            toastr.error('测试连接失败：' + result.message);
        }
    });
}

/**
 * 保存
 * @returns {boolean}
 */
function saveConnection() {
    var data = getDataSource();
    if (data.length <= 2 || data === "{}") {
        return false;
    }
    $.ajax({
        type: "POST",
        url: "datasource/host",
        contentType: "application/json;charset=utf-8",
        data: data,
        dataType: "json",
        success: function (result) {
            if (result.success === true || result.success === "true") {
                toastr.success('操作成功，3S后刷新页面！');
                setTimeout("location.reload()", "3000");
            } else {
                toastr.error('操作失败：' + result.message);
            }
        },
        error: function (result) {
            toastr.error('操作失败：' + result.message);
        }
    });
}

/**
 * 获取数据源数据
 */
function getDataSource() {
    var dataSourceJson = $("#wizard").serializeObject();
    if (dataSourceJson.toString().length === 0) {
        return false;
    }
    return JSON.stringify(dataSourceJson);
}

/**
 * 获取生成对象的配置信息
 */
function getGeneratorConfig() {
    var generatorConfig = $("#tableForm").serializeObject();
    if (generatorConfig.toString().length === 0) {
        return;
    }
    return JSON.stringify(generatorConfig);
}

/**
 * 获取form区域数据，并判断是否为空
 * @returns {{}}
 */
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (this.value === "" || this.value === null) {
            if (this.name !== "generateKeys") {
                alert(this.name + "不能为空！");
                return false;
            }
        }

        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}


function clickTree(node, rootNode) {
    var clickTree = node.id;
    if (clickTree !== "" && clickTree !== "undefined" && clickTree !== null) {
        console.log("开始加载表：" + clickTree);
        $("#tableName").val(node.text);
        handTable(clickTree);
        $("#indexDiv").hide();
        $("#tableDiv").show();
    }
}

/**
 * 处理表名
 * @param tableName
 */
function handTable(tableName) {
    $.ajax({
        type: "GET",
        url: "datasource/name/" + tableName,
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function (result) {
            if (result.success === true || result.success === "true") {
                $("#mapperName").val(result.data.mapperName);
                $("#domainObjectName").val(result.data.domainObjectName);
            } else {
                toastr.error('操作失败：' + result.message);
            }
        },
        error: function (result) {
            toastr.error('操作失败：' + result.message);
        }
    });
}

/**
 * 处理数据源相关信息节点
 */
function handDataSourceTree(dataSource) {
    var dataSourceList = JSON.parse(dataSource);
    var dataTree = [];
    if (dataSourceList.length > 0) {
        for (var i = 0; i < dataSourceList.length; i++) {
            var nodeArray = handNode(dataSourceList[i].tableNames);
            var dataTreeNode = {
                text: dataSourceList[i].dataSourceName, //节点显示的文本值  string
                selectedIcon: "glyphicon glyphicon-ok", //节点被选中时显示的图标       string
                selectable: true, //标记节点是否可以选择。false表示节点应该作为扩展标题，不会触发选择事件。  string
                state: { //描述节点的初始状态    Object
                    checked: true, //是否选中节点
                    /*disabled: true,*/ //是否禁用节点
                    expanded: true, //是否展开节点
                    selected: true //是否选中节点
                },
                nodes: nodeArray
            };
            dataTree.push(dataTreeNode);
        }
    }
    return dataTree;
}

/**
 * 处理数据源对应的表名的节点
 * @param tableNames
 */
function handNode(tableNames) {
    var nodeArray = [];
    for (var i = 0; i < tableNames.length; i++) {
        var node = {text: "" + tableNames[i] + "", id: "" + tableNames[i] + ""};
        nodeArray.push(node);
    }
    return nodeArray;
}

/**
 * 生成配置信息
 */
function generatorTable() {
    var generatorConfig = getGeneratorConfig();
    if (generatorConfig.length <= 2 || generatorConfig === "{}") {
        toastr.warn("请填写完对应配置信息！");
        return;
    }
    $.ajax({
        type: "POST",
        url: "generator/single",
        contentType: "application/json;charset=utf-8",
        data: generatorConfig,
        dataType: "json",
        success: function (result) {
            if (result.success === true || result.success === "true") {
                $("#hidden_zip_download_key").val(result.data);
                $("#downloadBtn").prop("disabled", false);
                toastr.success("生成成功，请点击下载按钮进行下载！")
            } else {
                toastr.error('操作失败：' + result.message);
            }
        },
        error: function (result) {
            toastr.error('操作失败：' + result.message);
        }
    });
}

function downloadZip() {
    var cacheKey = $("#hidden_zip_download_key").val();
    window.location.href = "generator/download/" + cacheKey;
}