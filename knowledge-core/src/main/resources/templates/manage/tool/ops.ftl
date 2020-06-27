<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'west',border:true,split:true" style="width: 200px; overflow: hidden;" align="left">
        <table id="manage_ops_tool_datagrid" class="easyui-datagrid" url="${rc.getContextPath()}/manage/tool/ops.html"
               toolbar="#manage_ops_tool_toolbar" fit="true" border="false" fitColumns="false"
               pagination="false" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
               checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="userId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="realName">名称</th>
                <th field="action"
                    data-options="formatter:function(value, row, index){return formatAction('manage_ops_tool_action',value,row)}">动作
                </th>
            </tr>
            </thead>
        </table>
        <div id="manage_ops_tool_action" style="display: none;">
            <a title="编辑" onclick="$.acooly.framework.edit({url:'/manage/system/user/edit.html',id:'{0}',entity:'user',height:450});"
               href="#"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a title="修改密码" onclick="manage_ops_tool_changePasswd('{0}');" href="#"><i class="fa fa-key fa-lg fa-fw fa-col"></i></a>
            <a title="删除" onclick="$.acooly.framework.remove('/manage/system/user/deleteJson.html','{0}','manage_ops_tool_datagrid');" href="#"><i
                        class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>
        <div id="manage_ops_tool_toolbar">
            <a href="#" class="easyui-linkbutton" data-options="size:'small'" plain="true"
               onclick="$.acooly.framework.create({url:'/manage/system/user/create.html',entity:'user',height:510})"><i
                        class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
        </div>
    </div>
    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:true,split:true">
        <div class="easyui-layout" data-options="fit : true,border : false">
            <div data-options="region:'north',border:true,split:true" style="height: 250px; overflow: hidden;" align="left">
                <textarea id="code" name="code" style="display: none;"></textarea>
            </div>
            <div data-options="region:'center',border:true,split:true">
                <table id="manage_ops_tool_result_datagrid" class="easyui-datagrid"
                       toolbar="#manage_ops_tool_result_toolbar"
                       fit="true" border="false" fitColumns="false"
                       pagination="true" idField="id" pageSize="20"
                       pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
                       checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="userId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                        <th field="realName">名称</th>
                        <th field="action"
                            data-options="formatter:function(value, row, index){return formatAction('manage_ops_tool_action',value,row)}">动作
                        </th>
                    </tr>
                    </thead>
                </table>
                <div id="manage_ops_tool_result_toolbar">
                    <a href="#" class="easyui-linkbutton" data-options="size:'small'" plain="true"
                       onclick="manage_tool_ops_execute()"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>查询</a>
                    <a href="#" class="easyui-linkbutton" data-options="size:'small'" plain="true"
                       onclick="manage_tool_ops_execute()"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>确认修改</a>
                    <a href="#" class="easyui-linkbutton" data-options="size:'small'" plain="true"
                       onclick=""><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>取消编辑</a>
                </div>
            </div>
        </div>

    </div>
</div>
<script type="text/javascript">
    var source = {"sys_user": ["id","name"],"sys_role": ["id","name"],"plt_employee": ["id","name"],"plt_department": ["id","name"]};
    var editor = null;
    $(function () {
        manage_tool_ops_loadDbMeta();
        var editRow = undefined;
        $("#manage_ops_tool_result_datagrid").datagrid({
            onAfterEdit:function(index, data, changes){
                $.post('', {}, function() {}, 'json');
            },
            onDblClickRow: function (rowIndex, rowData) {

            },
            onClickRow: function (rowIndex, rowData) {
                if (editRow != undefined) {
                    $("#manage_ops_tool_result_datagrid").datagrid('endEdit', editRow);
                }
            },
            onDblClickCell: function (rowIndex, field, value) {
                debugger;
                $("#manage_ops_tool_result_datagrid").datagrid('endEdit', editRow);
                editRow = undefined;
                $("#manage_ops_tool_result_datagrid").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
                var columns = $("#manage_ops_tool_result_datagrid").datagrid('options').columns[0];
                var self = this;
                $.each(columns, function(i, v) {
                    if(field != v.field) {
                        if(v.field != 'id' && v.field != 'ID') {
                            var ed = $(self).datagrid('getEditor', {index:rowIndex,field:v.field});
                            $(ed.target).prop('disabled', 'disabled');
                        }
                    }
                });
            },
            onLoadSuccess : function(){
            }
        });
    });
    function manage_tool_ops_loadDbMeta() {
        $.post('/manage/tool/loadDbMeta.html', {}, function(r) {
            source = r;
            editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                lineNumbers: true,
                mode: "text/x-sql",
                matchBrackets: true,
                hintOptions: {tables: source},
                extraKeys: { "Alt-/": "autocomplete" }
            });
            editor.setSize('auto','100%');
            // editor.on("keyup",function(cm, event) {
            //     if(event.keyCode != '8') {..ftl
            //         CodeMirror.commands.autocomplete(cm, null, {});
            //     }
            // });
            editor.setValue('select * from sys_resource');
        }, 'json');
    }
    function manage_tool_ops_execute() {
        var sql = editor.getValue();
        if(sql == null || sql == '') {
            $.messager.alert('提示', '请输入正确的查询语句！');
            return;
        }
        $.post('/manage/tool/execute.html', {sql:sql}, function(r) {
            if(r.success) {
                $("#manage_ops_tool_result_datagrid").datagrid('clearSelections');
                var fieldObjs = [];
                $.each(r.data.fields, function(i, v) {
                    if(v.field != 'id' && v.field != 'ID') {
                        v.editor = {type:'text', options: {}};
                    }
                    fieldObjs.push(v);
                });
                $('#manage_ops_tool_result_datagrid').datagrid({
                    columns:[fieldObjs]
                });
                $('#manage_ops_tool_result_datagrid').datagrid('loadData', r);
            } else {
                $.messager.alert('提示', r.detail);
            }
        }, 'json');
    }
</script>