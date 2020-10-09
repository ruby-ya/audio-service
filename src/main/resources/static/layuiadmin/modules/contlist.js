/** layuiAdmin.std-v1.0.0 LPPL License By http://www.layui.com/admin/ */
;layui.define(["table", "form", "jquery"], function (t) {
    var e = layui.$, i = layui.table, n = layui.form;
    var $ = layui.jquery;
    i.render({
        elem: "#LAY-app-content-list",
        url: "/audio/list",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 100, title: "文件ID", sort: !0}, {
            field: "fileName",
            title: "文件名称",
            minWidth: 70
        }, {field: "filePath", title: "文件路径"}, {
            field: "updateTime",
            title: "修改时间",
            sort: !0,
            align: "center"
        }, {field: "createTime", title: "更新时间", minWidth: 60, align: "center"}, {
            title: "操作",
            minWidth: 300,
            align: "center",
            fixed: "right",
            toolbar: "#table-content-list"
        }]],
        page: !0,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-app-content-list)", function (t) {
        var e = t.data;
        if ("del" === t.event) {
            layer.confirm("确定删除此文章？", function (e) {
                $.ajax({
                    url: '/audio/' + t.data.id,
                    method: 'delete',
                    success: function (res) {
                        layer.msg('删除成功', {icon: 1, time: 1000});
                        layer.close(e);
                        i.reload('LAY-app-content-list');
                    },
                    fail: function (res) {
                        layer.msg('删除失败', {icon: 2, time: 1000});
                        layer.close(e);
                        i.reload('LAY-app-content-list');
                    }
                })

            })
        }

        if ("play" === t.event) {
            layer.open({
                type: 2,
                title: "播放:" + t.data.fileName,
                content: "../../../views/audio/play.html?id=" + t.data.id,
                area: ["320px", "120px"],
            })
        }

        if ("copy" === t.event) {
            layer.open({
                type: 1,
                title: "手动复制:" + t.data.fileName,
                content: "<B style=\"text-align:center;display: block;margin: 0 auto;padding-top: 20px\"> "+location.protocol + "//" + location.host + "/download/audio/" + t.data.id+"</B>",
                area: ["320px", "120px"],
            })
        }

        if ("download" == t.event) {
            location.href = location.protocol + "//" + location.host + "/download/audio/" + t.data.id;
        }
    }), t("contlist", {})
});