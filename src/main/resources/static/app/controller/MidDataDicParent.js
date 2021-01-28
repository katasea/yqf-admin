var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.MidDataDicParent', {
    extend: 'Ext.app.Controller',
    stores: ['MidDataDicParent'],
    models: ['MidDataDicParent'],
    views:  ['midDataDicParent.Main','midDataDicParent.Add'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    		'actioncolumn#actioncolumnEditAndDel': {  
    			addclick : function(pa){
    				this.addLeaf(pa.record.data.id,pa.record);
    			},
    			editclick : function(pa){
    				this.edit(pa.record.data.id,pa.record);
    			},
    			deleteclick: function(pa) {
    				var name = pa.record.data.name;
    				var id = pa.record.data.id;
    				currentnodeid = id;
    				var date = Ext.getCmp('date').getValue();
                    Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+name+"</span>吗？不可恢复！",function(btn){
                    	if(btn=='yes') {
                    		mask.show();
                    		Ext.Ajax.request({
                			    url: contextPath + '/midDataDicParent/del',
                			    params: {
                			        id   : id,
                			        name : name,
                			        date:date
                			    },
                			    success: function(response){
                			    	mask.hide();
                			        var result = Ext.JSON.decode(response.responseText);
                			        if(result.flag) {
                			        	parent.window.showMessage(1,"已经成功删除指定行："+id +"-"+name,2,"提示");
                			        	store.load();
                			        }else {
                			        	Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                			        }
                			    }
                			});
                    	}
    				});
    			}
    		},
    	 	'midDataDicParentMain button[action=refresh]': {
                click: this.refresh
            },
            'midDataDicParentMain button[action=add]': {
                click: this.add
            },
            'midDataDicParentMain button[action=print]': {
                click: this.print
            },
            'midDataDicParentMain button[action=autoInitFromAccess]': {
                click: this.autoInitFromAccess
            },
            'midDataDicParentMain button[action=search]': {
                click: this.search
            },
            'midDataDicParentAdd button[action=save]': {
                click: this.save
            },
            'midDataDicParentAdd button[action=cancel]': {
                click: this.cancel
            },
            'midDataDicParentMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            },
            'textfield[name=date]': {render:function(e) {e.getEl().on('click',function(){WdatePicker({dateFmt:'yyyy'});});}}
        });
        store = this.getMidDataDicParentStore();
        //Get my store and add listener 
        this.getMidDataDicParentStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
        	var date = Ext.getCmp('date').getValue();
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword,
        		'date'	  : date
        	});
        });
    },
    refresh : function(btn) {
    	this.getMidDataDicParentStore().load({params:{start:0}});
    },
    add : function(btn){
        old = '';
   		father = null;id = null;
    	this.createAddWin('add');
    	parentnodeid = null;currentnodeid = null;
    },
    edit:function(vid,record) {
    	id = vid;
		old = record.data.id;
		var win = this.createAddWin('edit');
		node = null;
		win.down('midDataDicParentAdd').getForm().loadRecord(record);
    },
    createAddWin:function(flag){
    	var iconCls = '';var title = '';if(flag == 'add') {iconCls = 'Add';title = '新增'}else if(flag == 'edit'){iconCls='Edit';title='修改'}
    	var form = Ext.widget('midDataDicParentAdd');
    	var window = Ext.create('Ext.window.Window', {
		    title:title,iconCls:iconCls,height: pageHeight-100,
		    frame : true,border : 0,autoScroll:true,width: pageWidth-400,minWidth:350,
		    scroll:true,modal:true,layout: 'fit', items: form
		});window.show();return window;
    },
    search:function(btn) {
    	this.refresh();
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
        	this.search();
        }
    },
    save:function(btn){
    	var form = btn.up('form').getForm();
    	var date = Ext.getCmp('date').getValue();
        if (form.isValid()) {
        	mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
            	params:{father:father,id:id,date:date},
                success: function(form, action) {
                   mask.hide();
                   parent.window.showMessage(1,'保存成功！',2,'提示');
                   btn.up('window').close();
                   store.load();
                },
                failure: function(form, action) {
                	mask.hide();
                    Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                }
            });
        }

    },
    cancel:function(btn){
    	btn.up('form').up('window').close();
    },
	autoInitFromAccess:function(btn) {
    	var date = Ext.getCmp('date').getValue();
    	parentnodeid = null;currentnodeid = null;
    	Ext.Msg.confirm('提示','确定初始化内置数据吗?操作不可逆，会删除本年已存在数据!确定操作吗?',function(btn) {
    		if(btn=='yes') {mask.show();
        		Ext.Ajax.request({
    			    url: contextPath + '/midDataDicParent/autoInitFromAccess',params: {date:date},
    			    success: function(response){
    			    	mask.hide();var result = Ext.JSON.decode(response.responseText);
    			        if(result.flag) {
    			        	parent.window.showMessage(1,"初始化内置操作成功！",2,"成功");
    			        	store.load();
    			        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
    			    }
    			});
        	}
    	});
    },
    gridDBClickListener:function(tree, record, item, index, e, eOpts ) {
    	this.edit(record.data.id,record);
    }
    
});
