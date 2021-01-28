var mask = null;
Ext.application({
    name: "MyApp",
    appFolder: 'app',
    autoCreateViewport: false,
    controllers: ["MidData"],
    launch: function () {
        Ext.BLANK_IMAGE_URL = blank_img_url;
        // 页面加载完成之后执行
        Ext.create('Ext.container.Viewport', {
            layout: "fit",
            id: 'vpcViewport',
            border: '0 0 0 0',
            //自定义方块背景
            cls: "bgSquare",
            items : [{
                xtype : "midDataMainPanel",
            }]
        });
        mask = new Ext.LoadMask(Ext.getBody(), {msg: "请稍候片刻..."});
    }
});

