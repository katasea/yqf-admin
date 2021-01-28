var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.deptInfo.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'deptInfoAdd',
    autoScroll: true,
    requires: [
        '*',
        'Ext.ux.DataTip'
    ],
    viewConfig: {loadMask: {msg: "请稍后..."}},
    frame: false, bodyPadding: '10 10 10 10', border: 0,
    url: contextPath + '/deptInfo/save',
    fieldDefaults: {msgTarget: 'side', labelWidth: 100, anchor: '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
        {
            fieldLabel: '科室编号', xtype: 'textfield',
            id: 'deptid', name: 'deptid',
            validator: function (value) {
                var isOk = false;
                var msg = "";
                if (value !== old) {
                    Ext.Ajax.request({
                        waitMsg: '正在校验科室编号是否已经存在,请稍后...',
                        url: contextPath + "/deptInfo/validator",
                        params: {'value': value, 'keyid': 'deptid'},//
                        method: "POST", timeout: 40000, async: false,
                        success: function (response) {
                            var resObj = Ext.decode(response.responseText);
                            if (resObj.success) {
                                isOk = true;
                            } else {
                                isOk = false;
                                msg = resObj.msg;
                            }
                        }, failure: function () {
                            isOk = false;
                            msg = "错误";
                        }
                    });
                } else {
                    isOk = true;
                }
                if (isOk) {
                    return true;
                } else {
                    return msg;
                }//错误原因
            },
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【必填项】【科室编号】'
        },
        {
            fieldLabel: '科室名称', xtype: 'textfield',
            id: 'deptname', name: 'deptname',
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【必填项】【科室名称】'
        },
        {
            fieldLabel: '科室类型', xtype: 'textfield',
            id: 'depttype', name: 'depttype',
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【必填项】【科室类型】'
        }, {
            xtype: "combobox",
            name: "depttype",
            id:"depttype",
            fieldLabel: "科室类型",
            store: 'DicComboInfo',
            editable: false,
            displayField: "dicval",
            valueField: "dickey",
            allowBlank: false,
            emptyText: "--请选择--",
            queryMode: "local"
        },
        {
            fieldLabel: '启用时间', xtype: 'textfield',
            id: 'inserttime', name: 'inserttime',
            allowBlank: true, tooltip: '不填写则表示通用',
            listeners: {
                render: function (obj,e) {
                    obj.getEl().on('click', function () {
                        WdatePicker({dateFmt: 'yyyy-MM'});
                    });
                }
            }
        },
        {
            fieldLabel: '科室状态', xtype: 'radiogroup',
            items: [{inputValue: '0', boxLabel: '启用', anchor: '60%', name: 'isstop', checked: true}, {
                inputValue: '1',
                anchor: '60%',
                name: 'isstop',
                boxLabel: '停用'
            }],
            tooltip: '请输入【科室状态】'
        }],
    buttons: [{text: '保存', action: 'save'}, {text: '取消', action: 'cancel'}],
    initComponent: function () {
        this.callParent();
    }
});
