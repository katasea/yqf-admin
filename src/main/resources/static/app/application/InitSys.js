var mask = null;
Ext.application({
    name: "MyApp",
    appFolder: 'app',
    autoCreateViewport: false,
    controllers: ["InitSys"],
    launch: function () {
        // 定义函数: 验证再次输入的密码是否一致
        Ext.apply(Ext.form.VTypes, {
            confirmPwd: function (value, field) {
                // field 的 confirmPwd 属性
                if (field.confirmPwd) {
                    var first = field.confirmPwd.first;
                    var second = field.confirmPwd.second;

                    this.firstField = Ext.getCmp('password');
                    this.seconField = Ext.getCmp('rePassword');
                    var firstPwd = this.firstField.getValue();
                    var secondPwd = this.seconField.getValue();
                    if (firstPwd == secondPwd) {
                        return true;
                    } else {
                        return false;
                    }
                }
            },
            confirmPwdText: '两次输入的密码不一致！'
        });

        Ext.BLANK_IMAGE_URL = blank_img_url;
        // 页面加载完成之后执行

        Ext.create('Ext.container.Viewport', {
            layout : "fit",
            id: 'vpcViewport',
            //是否需要有间距
            //padding : '7 7 7 7',
            border: '0 0 0 0',
            //自定义方块背景
            border: false,
            cls: "bgSquare",
            padding: '50 270 150 270',
            frame: false,
            items: [{
                title: '系统初始化',
                xtype: 'myViewInitSysForm'
            }]
        });
        mask = new Ext.LoadMask( Ext.getBody(),{msg:"请稍候片刻..."});
    }
});
