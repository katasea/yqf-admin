var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var parentnodeid = '';//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.BasDicInfo', {
    extend: 'Ext.app.Controller',
    stores: ['BasDicInfo'],
    models: ['BasDicInfo'],
    views:  ['basDicInfo.Main','basDicInfo.Add'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    		'actioncolumn#actioncolumnEditAndDel': {  
    			addclick : function(pa){
    				this.addLeaf(pa.record.data.dicid,pa.record);
    			},
    			editclick : function(pa){
    				this.edit(pa.record.data.dicid,pa.record);
    			},
    			deleteclick: function(pa) {
    				var name = pa.record.data.dicname;
    				var id = pa.record.data.dicid;
    				var parentId = pa.record.data.parentid;
    				currentnodeid = id;
    				parentnodeid = parentId;
                    Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+name+"</span>吗？不可恢复！",function(btn){
                    	if(btn=='yes') {
                    		mask.show();
                    		Ext.Ajax.request({
                			    url: contextPath + '/basDicInfo/del',
                			    params: {
                			        id   : id,
                			        name : name,
                			        parentId:parentId
                			    },
                			    success: function(response){
                			    	mask.hide();
                			        var result = Ext.JSON.decode(response.responseText);
                			        if(result.flag) {
                			        	parent.window.showMessage(1,"已经成功删除指定行："+id +"-"+name,2,"提示");
                			        	refreshNode(Ext.getCmp('vpcViewport').down('basDicInfoMain'), currentnodeid, parentnodeid);
                			        }else {
                			        	Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                			        }
                			    }
                			});
                    	}
    				});
    			}
    		},
    	 	'basDicInfoMain button[action=refresh]': {
                click: this.refresh
            },
            'basDicInfoMain button[action=add]': {
                click: this.add
            },
            'basDicInfoMain button[action=print]': {
                click: this.print
            },
            'basDicInfoMain button[action=autoInitFromAccess]': {
                click: this.autoInitFromAccess
            },
            'basDicInfoMain button[action=search]': {
                click: this.search
            },
            'basDicInfoAdd button[action=save]': {
                click: this.save
            },
            'basDicInfoAdd button[action=cancel]': {
                click: this.cancel
            },
            'basDicInfoMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            },
            'textfield[name=date]': {render:function(e) {e.getEl().on('click',function(){WdatePicker({dateFmt:'yyyy'});});}}
        });
        
        //Get my store and add listener 
        this.getBasDicInfoStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword
        	});
        });
    },
    refresh : function(btn) {
    	var store = this.getBasDicInfoStore();
    	store.getRootNode().removeAll();store.load();
    },
    add : function(btn){
   		father = null;id = null;
    	this.createAddWin('add');
    	parentnodeid = null;currentnodeid = null;
    },
    addLeaf : function(parentid,record) {
    	father = parentid;
    	id = null;
    	this.createAddWin('add'); 
    	if(record.data.leaf) {
    		parentnodeid = record.data.parentid;
    	}else {
	    	parentnodeid = record.data.id;
    	}
    	
    },
    edit:function(vid,record) {
    	id = vid;
		var win = this.createAddWin('edit');
		old = record.data.dicid;
		parentnodeid = record.data.parentid;
		currentnodeid = id;
		win.down('basDicInfoAdd').getForm().loadRecord(record);
    },
    createAddWin:function(flag){
    	var iconCls = '';var title = '';if(flag == 'add') {iconCls = 'Add';title = '新增'}else if(flag == 'edit'){iconCls='Edit';title='修改'}
    	var form = Ext.widget('basDicInfoAdd');
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
        if (form.isValid()) {
        	mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
            	params:{father:father,id:id},
                success: function(form, action) {
                   mask.hide();
                   old = '';
                   parent.window.showMessage(1,'操作成功！',2,'提示');
                   btn.up('window').close();
			       refreshNode(Ext.getCmp('vpcViewport').down('basDicInfoMain'), currentnodeid, parentnodeid);
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
    	parentnodeid = null;currentnodeid = null;
    	Ext.Msg.confirm('提示','确定初始化内置数据吗?操作不可逆，会删除本年已存在数据!确定操作吗?',function(btn) {
    		if(btn=='yes') {mask.show();
        		Ext.Ajax.request({
    			    url: contextPath + '/basDicInfo/autoInitFromAccess',params: {},
    			    success: function(response){
    			    	mask.hide();var result = Ext.JSON.decode(response.responseText);
    			        if(result.flag) {
    			        	parent.window.showMessage(1,"初始化内置操作成功！",2,"成功");
    			        	refreshNode(Ext.getCmp('vpcViewport').down('basDicInfoMain'), currentnodeid, parentnodeid);
    			        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
    			    }
    			});
        	}
    	});
    },
    gridDBClickListener:function(tree, record, item, index, e, eOpts ) {
    	this.edit(record.data.dicid,record);
    }
    
});


/**
 * 刷新树 刷新父节点，展开或不展开当前节点，选中当前节点
 * @param treePanel	
 * @param refreshRoot	是否刷新root
 * @param currentNodeId	当前节点id
 * @param parentNodeId	父节点id
 */
function refreshNode(treePanel, currentNodeId, parentNodeId){
	var refreshRoot = false;var currentNode = treePanel.getStore().getNodeById(currentNodeId); 
	var parentNode = treePanel.getStore().getNodeById(parentNodeId);var rootNode = treePanel.getStore().getRootNode();
	if(parentNodeId == null || parentNodeId == '') {refreshRoot = true;}else {refreshRoot = false;}
	var path;if(currentNode){path = currentNode.getPath('id'); 
	} else if(parentNode){path = parentNode.getPath('id');} else {path = rootNode.getPath('id');}
	var loadNode;if(refreshRoot){loadNode = rootNode;} else {loadNode = parentNode;}
	//刷新节点，展开节点，选中节点
	treePanel.getStore().load({node: loadNode, 
			callback:function(){if(currentNode && currentNode.data.expanded){treePanel.expandPath(path);}treePanel.selectPath(path);}
	});
}