var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.InitSysForm', {
    extend: 'Ext.form.Panel',
    xtype: 'myViewInitSysForm',
    autoScroll: true,
    requires: [
        '*',
        'Ext.ux.DataTip'
    ],
    frame: false, bodyPadding: '10 10 10 10', border: 0,
    url: contextPath + '/init/do',
    fieldDefaults: {msgTarget: 'side', labelWidth: 100, anchor: '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
        {
            fieldLabel: '单位编号', xtype: 'textfield',
            id: 'companyid', name: 'companyid',
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【单位编号|eg: C001】'
        },{
            fieldLabel: '单位名称', xtype: 'textfield',
            id: 'companyname', name: 'companyname',
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【单位名称|eg: xxx单位】'
        },{
            xtype: 'textfield',
            inputType: 'password',
            name: 'password',
            id: 'password',
            fieldLabel: '登录密码',
            tooltip: '请设置管理员admin的密码',
            afterLabelTextTpl: required,
            allowBlank: false,
            blankText: '请输入密码',
            regex: /^[\s\S]{0,15}$/,
            regexText: '密码长度不能超过15个字符',

        },
        {
            xtype: 'textfield',
            inputType: 'password',
            name: 'rePassword',
            id: 'rePassword',
            vtype: 'confirmPwd',
            fieldLabel: '重输密码',
            afterLabelTextTpl: required,
            allowBlank: false,
            blankText: '确认密码不能为空',
            regex: /^[\s\S]{0,32}$/,
            regexText: '密码长度不能超过32个字符',
            // 调用密码验证中的定义的属性
            confirmPwd: {
                first: 'password',
                second:'rePassword'
            }
        }],
    buttons: [{text: '初始化', action: 'save'}, {text: '取消', action: 'cancel'}],
    initComponent: function () {
        this.callParent();
    }
});
