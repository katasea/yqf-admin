var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var parentnodeid = '';//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.DeptInfo', {
    extend: 'Ext.app.Controller',
    stores: ['DeptInfo','DicComboInfo'],
    models: ['DeptInfo'],
    views:  ['deptInfo.Main','deptInfo.Add','common.UploadPanel'],
    init: function () {
    	//this can deal and add component listener
    	this.control({
    		'actioncolumn#actioncolumnEditAndDel': {  
    			addclick : function(pa){
    				this.addLeaf(pa.record.data.pid,pa.record);
    			},
    			editclick : function(pa){
    				this.edit(pa.record.data.pid,pa.record);
    			},
    			deleteclick: function(pa) {
    				var name = pa.record.data.deptname;
    				var id = pa.record.data.pid;
    				var parentId = pa.record.data.parentid;
    				currentnodeid = id;
    				parentnodeid = parentId;
                    Ext.Msg.confirm("提示","确定删除<span style='color:red;font-weight:bold'>"+name+"</span>吗？不可恢复！",function(btn){
                    	if(btn=='yes') {
                    		mask.show();
                    		Ext.Ajax.request({
                			    url: contextPath + '/deptInfo/del',
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
                			        	refreshNode(Ext.getCmp('vpcViewport').down('deptInfoMain'), currentnodeid, parentnodeid);
                			        }else {
                			        	Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                			        }
                			    }
                			});
                    	}
    				});
    			}
    		},
    	 	'deptInfoMain button[action=refresh]': {
                click: this.refresh
            },
            'deptInfoMain button[action=add]': {
                click: this.add
            },
            'deptInfoMain button[action=print]': {
                click: this.print
            },
            'deptInfoMain button[action=autoInitFromAccess]': {
                click: this.autoInitFromAccess
            },
            'deptInfoMain menu menuitem[action=importData]': {
                click: this.imp
            },
            'deptInfoMain menu menuitem[action=exportData]': {
                click: this.exportData
            },
            'deptInfoMain menu menuitem[action=downTemplate]': {
                click: this.downTemplate
            },
            'uploadPanel panel button[action=importData]':{
                click: this.doImp
            },
            'deptInfoMain button[action=search]': {
                click: this.search
            },
            'deptInfoAdd button[action=save]': {
                click: this.save
            },
            'deptInfoAdd button[action=cancel]': {
                click: this.cancel
            },
            'deptInfoMain':{
            	itemdblclick : this.gridDBClickListener
            },
            'textfield[name=keyword]': {
            	specialkey: this.searchTxListener
            }
        });

    	this.getDicComboInfoStore().on('beforeload',function(store,operation,eOpts){
            Ext.apply(this.proxy.extraParams,{
                'type' : '1' //获取部门类型
            });
        });
        //Get my store and add listener 
        this.getDeptInfoStore().on('beforeload',function(store, operation, eOpts ){
        	var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
        	Ext.apply(this.proxy.extraParams,{
        		'keyword' : keyword
        	});
        });
    },
    refresh : function(btn) {
    	var store = this.getDeptInfoStore();
    	store.getRootNode().removeAll();store.load();
    },
    add : function(btn){
   		father = null;id = null;
    	var win = this.createAddWin('add');
    	parentnodeid = null;currentnodeid = null;
        win.down('deptInfoAdd').down('textfield[id=deptid]').setDisabled(false);
    },
    addLeaf : function(parentid,record) {
    	father = parentid;
    	id = null;
    	var win = this.createAddWin('add');
    	if(record.data.leaf) {
    		parentnodeid = record.data.parentid;
    	}else {
	    	parentnodeid = record.data.id;
    	}
        currentnodeid = null;
        win.down('deptInfoAdd').down('textfield[id=deptid]').setDisabled(false);
    },
    edit:function(vid,record) {
    	id = vid;
		var win = this.createAddWin('edit');
		old = record.data.deptid;
		parentnodeid = record.data.parentid;
		currentnodeid = id;
		win.down('deptInfoAdd').getForm().loadRecord(record);
		win.down('deptInfoAdd').down('textfield[id=deptid]').setDisabled(true);
    },
    imp: function() {
        var form = Ext.widget('uploadPanel');
        var window = Ext.create('Ext.window.Window', {
            title: '上传数据', iconCls: 'Pageexcel', height: 110,
            frame: true, border: 0, autoScroll: true, width: 450,
            scroll: true, modal: true, layout: 'fit', items: form
        });
        window.show();
    },
    exportData:function() {
        window.location.href = contextPath+"/deptInfo/exportExcel";
    },
    downTemplate:function(){
        window.location.href = contextPath+"/deptInfo/exportTemplate";
    },
    doImp:function (btn) {
        var form = btn.up('panel').up('uploadPanel').down('form');
        if (form.getForm().isValid()) {
            form.getForm().submit({
                url: contextPath+'/deptInfo/importData',
                clientValidation: true,
                method: 'POST',
                params: {},
                waitMsg: '正在导入数据，请稍候.....',
                success: function (response, action) {
                    if (action.result.success) {
                        parent.window.showMessage(1,'成功导入数据！',2,'提示');
                        //关闭上传界面窗口
                        btn.up('panel').up('uploadPanel').up('window').close();
                        //刷新
                        refreshNode(Ext.getCmp('vpcViewport').down('deptInfoMain'), null, null);
                    } else {
                        Ext.Msg.alert('失败了', action.result.message);
                    }
                },
                failure: function (response, action) {
                    Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                }
            });
        }
    },
    createAddWin:function(flag){
    	var iconCls = '';if(flag == 'add') {iconCls = 'Add';}else if(flag == 'edit'){iconCls='Edit';}
    	var form = Ext.widget('deptInfoAdd');
    	var window = Ext.create('Ext.window.Window', {
		    title:'',iconCls:iconCls,height: 400,
		    frame : true,border : 0,autoScroll:true,width:  450,
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
			       refreshNode(Ext.getCmp('vpcViewport').down('deptInfoMain'), currentnodeid, parentnodeid);
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
    			    url: contextPath + '/deptInfo/autoInitFromAccess',params: {},
    			    success: function(response){
    			    	mask.hide();var result = Ext.JSON.decode(response.responseText);
    			        if(result.flag) {
    			        	parent.window.showMessage(1,"初始化内置操作成功！",2,"成功");
    			        	refreshNode(Ext.getCmp('vpcViewport').down('deptInfoMain'), currentnodeid, parentnodeid);
    			        }else {Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');}
    			    }
    			});
        	}
    	});
    },
    gridDBClickListener:function(tree, record, item, index, e, eOpts ) {
    	this.edit(record.data.pid,record);
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
	var parentNode = treePanel.getStore().getNodeById(parentNodeId);
	var rootNode = treePanel.getStore().getRootNode();
	if(parentNodeId == null || parentNodeId == '') {refreshRoot = true;}else {refreshRoot = false;}
	var path;if(currentNode){path = currentNode.getPath('id'); 
	} else if(parentNode){path = parentNode.getPath('id');} else {path = rootNode.getPath('id');}
	var loadNode;if(refreshRoot){loadNode = rootNode;} else {loadNode = parentNode;}
	//刷新节点，展开节点，选中节点
	treePanel.getStore().load({node: loadNode,callback:function(){if(currentNode && currentNode.data.expanded){treePanel.expandPath(path);}treePanel.selectPath(path);}});
}