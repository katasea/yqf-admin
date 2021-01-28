var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.baseFormula.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'baseFormulaAdd',
    autoScroll: true,
    requires: [
        '*',
        'Ext.ux.DataTip'
    ],
    viewConfig: {loadMask: {msg: "请稍后..."}},
    frame: false, bodyPadding: '10 10 10 10', border: 0,
    url: contextPath + '/baseFormula/save',
    fieldDefaults: {msgTarget: 'side', labelWidth: 100, anchor: '95%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
        {
            fieldLabel: '占位名称', xtype: 'textfield',
            id: 'name', name: 'name',
            validator: function (value) {
                var isOk = false;
                var msg = "";
                if (value != old) {
                    Ext.Ajax.request({
                        waitMsg: '正在校验占位名称是否已经存在,请稍后...',
                        url: contextPath + "/baseFormula/validator",
                        params: {'value': value, 'keyid': 'name'},//
                        method: "POST", timeout: 40000, async: false,
                        success: function (response, opts) {
                            var resObj = Ext.decode(response.responseText);
                            if (resObj.success) {
                                isOk = true;
                            } else {
                                isOk = false;
                                msg = resObj.msg;
                            }
                        }, failure: function (response, opts) {
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
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【必填项】【占位名称】'
        },
        {
            xtype: "combobox",
            name: "isstop",
            id: "isstop",
            fieldLabel: '状态',
            store: new Ext.data.ArrayStore({
                fields: ['dickey', 'dicval'],
                data: [["0", '0 启用'],["1",'1 停用']]
            }),
            editable: false,
            displayField: "dicval",
            valueField: "dickey",
            allowBlank: false,
            afterLabelTextTpl: required,
            emptyText: "--请选择--",
            value:"0",
            queryMode: "local",
            tooltip: '请输入【状态】'
        },
        {
            xtype: "combobox",
            name: "type",
            id: "type",
            fieldLabel: '类型',
            store: new Ext.data.ArrayStore({
                fields: ['dickey', 'dicval'],
                data: [["1", '1 SQL'],["2",'2 JSON'],["3",'3 DATE']]
            }),
            editable: false,
            displayField: "dicval",
            valueField: "dickey",
            allowBlank: false,
            afterLabelTextTpl: required,
            emptyText: "--请选择--",
            value:"1",
            queryMode: "local",
            tooltip: '请输入【状态】'
        },
        {
            fieldLabel: '公式内容', xtype: 'textarea',
            id: 'formula', name: 'formula',
            rows: 15, cols: 50,
            tooltip: '请输入【公式内容】'
        }],
    buttons: [{text: '保存', action: 'save'}, {text: '取消', action: 'cancel'}],
    initComponent: function () {
        this.callParent();
    }
});
