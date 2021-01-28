var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.NoticeView', {
    extend: 'Ext.app.Controller',
    stores: ['NoticeInfo'],
    models: ['NoticeInfo'],
    views:  ['noticeInfo.View'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    	 	'noticeViewMain button[action=refresh]': {
                click: this.refresh
            },
            'noticeViewMain button[action=print]': {
                click: this.print
            },
            'noticeViewMain button[action=search]': {
                click: this.search
            },
            'noticeViewMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            },
            'combo[id=userNoticeListViewType]' : {
                select : this.refresh
            }
        });
        store = this.getNoticeInfoStore();
        //Get my store and add listener 
        this.getNoticeInfoStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
        	var type = Ext.getCmp('userNoticeListViewType').getValue();
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword,
                'type':type,
                'view':1
        	});
        });
    },
    refresh : function(btn) {
    	this.getNoticeInfoStore().load({params:{start:0}});
    },
    search:function(btn) {
    	this.refresh();
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
        	this.search();
        }
    },
    gridDBClickListener:function(tree, record, item, index, e, eOpts ) {
        parent.window.showNoticeDetail(
    	    record.data.pkid,
            record.data.title,
            record.data.content,
            record.data.fromwhoname,
            record.data.senttime);
        //2秒后刷新未读消息列表
        var type = Ext.getCmp('userNoticeListViewType').getValue();
        if(type === '0') {
            setTimeout(function(){store.load({params:{start:0}});}, 1000);
        }
    }
    
});
