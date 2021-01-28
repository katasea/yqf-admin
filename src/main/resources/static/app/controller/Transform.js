var typeID = 1;
var type_name = "门诊部门";
var store;
var column_cd_id;//条件编号  原始编号
var column_cd_name;//条件名称 原始名称
var column_aj_id;//调整编号  系统编号
var column_aj_name;//调整名称 系统名称
var hiddenNurse; //隐藏护士比例
var hideColumnValue;//隐藏调整值和条件值

Ext.define('MyApp.controller.Transform', {
    extend: 'Ext.app.Controller',
    stores: ['Transform','TransformType'],
    models: ['Transform'],
    views:  ['transform.Main','transform.CenterPanel','transform.Left','transform.Add','transform.NurseAdd','transform.CommonAdd'],
    init: function () {

        store = this.getTransformStore();
        store.on('beforeload', function () {
            Ext.apply(store.proxy.extraParams, {
                'typeID': typeID
            });
        });

        this.control({
            'actioncolumn#actioncolumnEditAndDel':{
                deleteclick:function(rec){
                    var id = rec.record.data.id;
                    Ext.Msg.confirm("提示", "确定删除当前记录吗？", function (btn) {
                        if (btn === 'yes') {
                            mask.show();
                            Ext.Ajax.request({
                                url: contextPath + '/transform/del',
                                params: {
                                    id: id
                                },
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.JSON.decode(response.responseText);
                                    if (result.success) {
                                        parent.window.showMessage(1, "已经成功删除");
                                        store.load();
                                    } else {
                                        Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                                    }
                                }
                            });
                        }
                    });
                },
                update:function(rec){
                    this.createWin('edit',rec);
                }
            },
            'transformType':{
                itemclick: this.menuClick
            },
            'transformMain button[action=refresh]': {
                click: this.refresh
            },
            'transformMain button[action=add]': {
                click: this.add
            },
            'transformAdd  button[action=addSave]':{
                click: this.addSave
            },
            'transformNurseAdd  button[action=addSave]':{
                click: this.addSave
            },
            'transformCommonAdd  button[action=addSave]':{
                click: this.addSave
            },
            'transformType button[action=transform]':{
                click: this.transform
            }
        });
    },
    menuClick: function(tree, record){
        typeID = record.data.id;
        type_name = record.data.text;
        if(typeID=='1' || typeID=='2' || typeID=='3'|| typeID=='4'){
            column_cd_id= "原始编号";
            column_cd_name="原始名称";
            column_aj_id="系统编号";
            column_aj_name="系统名称";
            hiddenNurse=true;
            hideColumnValue =true;
            viewPort.remove(2);
            viewPort.add(transform1);
            viewPort.doLayout();
        }else if(typeID==5){
            column_cd_id= "执行医生科室编号";
            column_cd_name="执行医生科室名称";
            column_aj_id="护士科室编号";
            column_aj_name="护士科室名称";
            hiddenNurse=false;
            hideColumnValue =true;
            viewPort.remove(2);
            viewPort.add(transform1);
            viewPort.doLayout();
        }else if(typeID==6){
            column_cd_id= "条件字段id";
            column_cd_name="条件字段名称";
            column_aj_id="调整字段id";
            column_aj_name="调整字段名称";
            hiddenNurse=true;
            hideColumnValue =false;
            viewPort.remove(2);
            viewPort.add(transform1);
            viewPort.doLayout();
        }

        store.reload();
    },
    refresh: function(){
        store.reload();
    },
    add: function(){
        this.createWin('add',null);
    },
    createWin: function (flag,rec) {
        var iconCls = '';
        var title = '';
        if (flag === 'add') {
            iconCls = 'Add';
            title = '新增'
        } else if (flag === 'edit') {
            iconCls = 'Edit';
            title = '修改'
        }
        var form;
        if(typeID=="5"){
            form = Ext.widget('transformNurseAdd');
        }else if(typeID=="6"){
            form = Ext.widget('transformCommonAdd');
        }else{
            form = Ext.widget('transformAdd');
        }
        var window = Ext.create('Ext.window.Window', {
            title: title, iconCls: iconCls, height: pageHeight - 50,
            frame: true, border: 0, autoScroll: true, width: 600, minWidth: 350,
            scroll: true, modal: true, layout: 'fit', items: form
        });
        window.show();

        if(rec!=null){
            form.loadRecord(rec.record);
        }
        return window;
    },
    addSave: function(btn){
        var form = btn.up('form').getForm();
        if (form.isValid()) {
            mask.show();
            form.submit({
                params: {typeID: typeID, type_name: type_name},
                success: function () {
                    mask.hide();
                    parent.window.showMessage(1, '保存成功！', 2, '提示');
                    btn.up('window').close();
                    store.reload();
                },
                failure: function (form, action) {
                    mask.hide();
                    Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                }
            });
        }
    },
    transform: function(){
        Ext.Msg.confirm("转换","是否确定进行当月原始数据的转换",function(btn){
            if(btn == "yes" ){
                mask.show();
                Ext.Ajax.request({
                    url: contextPath + '/transform/transform',
                    success: function (response) {
                        mask.hide();
                        var result = Ext.JSON.decode(response.responseText);
                        if (result.success) {
                            parent.window.showMessage(1, "转换成功");
                        } else {
                            Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                        }
                    },
                    failure:function(){
                        mask.hide();
                        Ext.Msg.alert("转换失败","是否超时了?");
                    }
                });
            }
        });
    }
});
