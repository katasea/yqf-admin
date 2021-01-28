Ext.define('MyApp.view.common.UploadPanel', {
    extend: 'Ext.panel.Panel',
    xtype:'uploadPanel',
    frame: false,
    border: 0,
    layout: 'column',
    region: 'center',
    height: 200,
    bodyStyle: 'text-align : center;padding-top : 30px',
    items: [
        {
            xtype:'form',
            border: 0,
            fileUpload: true,
            autoScroll: false,
            height: 40,
            bodyStyle: 'padding:0 0 0 5px',
            labelWidth: 20,
            width: 300,
            items: [{
                xtype: 'filefield',
                fieldLabel: '选择xls文件',
                buttonText: '浏览...',
                height: 25,
                name: 'excelFile',
                id: 'excelFile',
                allowBlank: false
            }]
        },
        {
            xtype:'panel',
            width: 110,
            border: 0,
            layout: 'form',
            bodyStyle: 'padding:0 5px 0',
            items: [{
                xtype: 'button',
                action: 'importData',
                text: '导入数据',
                height: 25,
                width: 90
            }]
        }
    ]
});