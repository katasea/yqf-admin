Ext.define('MyApp.view.noticeInfo.View', {
    extend: 'Ext.grid.Panel',
    //title:'',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.ux.CheckColumn',
        'Ext.util.*',
        'Ext.toolbar.Paging'
    ],
    xtype: 'noticeViewMain',
    lines: true,
    tbar: [
        {xtype: 'button', text: '刷新', action: 'refresh', iconCls: 'Refresh'}, '-',
        {
            xtype : 'combo',
            store : new Ext.data.SimpleStore({
                fields : ["returnValue","displayText"],
                data :[
                    ["0","未读消息"],
                    ["1","历史消息"]
                ]
            }),
            valueField : 'returnValue',
            displayField : 'displayText',
            editable:false,
            value: "0",
            mode : 'local',
            id : 'userNoticeListViewType',
            triggerAction : 'all'
        }, '-',{
            id: 'keyword',
            name: 'keyword',
            width: 170,
            labelWidth: 70,
            xtype: 'textfield',
            emptyText: '请输入关键字'
        }, '-', {
            id: 'querybtn',
            text: '搜索',
            iconCls: 'Find',
            action: 'search'
        }, '-',
        '->', '将鼠标悬浮在操作图标上可获得对应提示'
    ],
    store: 'NoticeInfo',
    rowLines: true,
    viewConfig: {
        loadMask: {msg: "请稍后..."}
    },
    columnLines: true,
    rootVisible: false,
    multiSelect: true,
    enableColumnMove: true,
    enableColumnResize: true,            //是否允许改变列宽
    //autoScroll         : true,
    stripeRows: true,
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'NoticeInfo',
        displayInfo: true
    },
    initComponent: function () {
        this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
        Ext.apply(this, {
            plugins: [this.cellEditing],
            columns: [
                {
                    text: '通知标题',
                    width: 150,
                    dataIndex: 'title',
                    align: 'left'
                },
                {
                    text: '通知内容',
                    flex: 2,
                    sortable: false,
                    dataIndex: 'content'
                },
                {
                    text: '发送时间',
                    width: 150,
                    dataIndex: 'senttime',
                    align: 'center'
                }, {
                    text: '发送者',
                    width: 150,
                    dataIndex: 'fromwhoname',
                    align: 'left'
                }, {
                    text: '通知类型',
                    width: 150,
                    dataIndex: 'type',
                    align: 'left',renderer:function(v){
                        if(v == 1) {
                            return "系统通知 [1]";
                        }else if(v == 2) {
                            return "用户通知 [2]";
                        }else if(v == 3) {
                            return "用户提醒 [3]";
                        }
                    }
                }
            ]
        });
        this.callParent();
    }
});
