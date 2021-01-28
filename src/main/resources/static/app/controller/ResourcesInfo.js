var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var parentnodeid = '';//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
var resourcePkid = '';//查看菜单所属用户，菜单的pkid
var resourceName = '';
Ext.define('MyApp.controller.ResourcesInfo', {
    extend: 'Ext.app.Controller',
    stores: ['ResourcesInfo', 'UserMgr','UserInfo','UserInfoNoPage'],
    models: ['ResourcesInfo', 'UserInfo'],
    views: ['resourcesInfo.Main', 'resourcesInfo.Add', 'common.UserMgr','common.ComboGridBox'],
    init: function () {
        //this can deal and add component listener
        this.control({
            'actioncolumn#actioncolumnEditAndDel': {
                addclick: function (pa) {
                    this.addLeaf(pa.record.data.pkid, pa.record);
                },
                editclick: function (pa) {
                    this.edit(pa.record.data.pkid, pa.record);
                },
                deleteclick: function (pa) {
                    var name = pa.record.data.name;
                    var id = pa.record.data.pkid;
                    var parentId = pa.record.data.parentid;
                    currentnodeid = id;
                    parentnodeid = parentId;
                    Ext.Msg.confirm("提示", "确定删除<span style='color:red;font-weight:bold'>" + name + "</span>吗？不可恢复！", function (btn) {
                        if (btn == 'yes') {
                            mask.show();
                            Ext.Ajax.request({
                                url: contextPath + '/resourcesInfo/del',
                                params: {
                                    id: id,
                                    name: name,
                                    parentId: parentId
                                },
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.JSON.decode(response.responseText);
                                    if (result.flag) {
                                        parent.window.showMessage(1, "已经成功删除指定行：" + id + "-" + name, 2, "提示");
                                        refreshNode(Ext.getCmp('vpcViewport').down('resourcesInfoMain'), currentnodeid, parentnodeid);
                                    } else {
                                        Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                                    }
                                }
                            });
                        }
                    });
                },
                userMgrclick: function (pa) {
                    this.userMgr(pa.record.data.pkid, pa.record);
                }
            },
            'actioncolumn#actioncolumnEditAndDelOfWin': {
                deleteUserMgrClick: function (pa) {
                    this.removeUserOutRes(pa.record);
                }
            },
            'resourcesInfoMain button[action=refresh]': {
                click: this.refresh
            },
            'resourcesInfoMain button[action=add]': {
                click: this.add
            },
            'resourcesInfoMain button[action=print]': {
                click: this.print
            },
            'resourcesInfoMain button[action=autoInitFromAccess]': {
                click: this.autoInitFromAccess
            },
            'button[action=search]': {
                click: this.search
            },
            'button[action=searchUser]': {
                click: this.searchUser
            },
            'button[action=addUserAuth]':{
                click: this.addUserAuth
            },
            'resourcesInfoAdd button[action=save]': {
                click: this.save
            },
            'resourcesInfoAdd button[action=cancel]': {
                click: this.cancel
            },
            'resourcesInfoMain': {
                itemdblclick: this.gridDBClickListener
            },
            'resourcesInfoMain textfield[name=keyword]': {
                specialkey: this.searchTxListener
            },
            'textfield[id=keyword4User]': {
                specialkey: this.searchUserTxListener
            }
        });

        //Get my store and add listener 
        this.getResourcesInfoStore().on('beforeload', function (store, operation, eOpts) {
            var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
            Ext.apply(this.proxy.extraParams, {
                'keyword': keyword
            });
        });
    },
    refresh: function (btn) {
        var store = this.getResourcesInfoStore();
        store.getRootNode().removeAll();
        store.load({params:{start:0}});
    },
    add: function (btn) {
        father = null;
        id = null;
        this.createAddWin('add');
        parentnodeid = null;
        currentnodeid = null;
    },
    addLeaf: function (parentid, record) {
        father = parentid;
        id = null;
        this.createAddWin('add');
        if (record.data.leaf) {
            parentnodeid = record.data.parentid;
        } else {
            parentnodeid = record.data.id;
        }

    },
    edit: function (vid, record) {
        id = vid;
        var win = this.createAddWin('edit');
        old = record.data.resid;
        parentnodeid = record.data.parentid;
        currentnodeid = id;
        win.down('resourcesInfoAdd').getForm().loadRecord(record);
    },
    userMgr: function (pkid, record) {
        //获取该菜单下面所有拥有查看权限的用户
        var userMgr = Ext.widget('userMgr', {});
        var window = Ext.create('Ext.window.Window', {
            title: '所属用户列表',
            iconCls: 'Sitemap',
            height: pageHeight - 90,
            frame: true, border: 0, autoScroll: true, width: pageWidth - 400, minWidth: 350,
            scroll: true, modal: true, layout: 'fit', items: userMgr
        });
        window.show();
        resourcePkid = pkid;
        resourceName = record.data.name;
        this.searchUser();
    },
    removeUserOutRes: function (record) {
        var userpkid = record.data.pkid;
        var resfrom = record.data.resfrom;
        var username = record.data.username;
        var controller = this;
        Ext.Msg.confirm("提示", "确定移除<span style='color:red;font-weight:bold'>" + username + "</span>查看资源<span style='color:red;font-weight:bold'>" + resourceName + "</span>的权限吗？", function (btn) {
            if (btn == 'yes') {
                mask.show();
                Ext.Ajax.request({
                    url: contextPath + '/userInfo/delUserResRoleAuth',
                    params: {
                        resourcePkid: resourcePkid,
                        userpkid: userpkid,
                        resfrom: resfrom
                    },
                    success: function (response) {
                        mask.hide();
                        var result = Ext.JSON.decode(response.responseText);
                        if (result.flag) {
                            controller.searchUser();
                            parent.window.showMessage(1, '移除权限成功！', 2, '提示');
                        } else {
                            Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                        }
                    }
                });
            }
        });
    },
    createAddWin: function (flag) {
        var iconCls = '';
        var title = '';
        if (flag == 'add') {
            iconCls = 'Add';
            title = '新增';
        } else if (flag == 'edit') {
            iconCls = 'Edit';
            title = '修改';
        }
        var form = Ext.widget('resourcesInfoAdd');
        var window = Ext.create('Ext.window.Window', {
            title: title, iconCls: iconCls, height: 250,
            frame: true, border: 0, autoScroll: true, width: 400,
            scroll: true, modal: true, layout: 'fit', items: form
        });
        window.show();
        return window;
    },
    search: function (btn) {
        this.refresh();
    },
    searchUser: function (btn) {
        this.getUserMgrStore().on('beforeload', function (store, operation, eOpts) {
            var keyword = encodeURIComponent(Ext.getCmp('keyword4User').getValue());
            Ext.apply(this.proxy.extraParams, {
                'resourcePkid': resourcePkid,
                'keyword': keyword
            });
        });
        this.getUserMgrStore().load();

        this.getUserInfoStore().on('beforeload',function(store, operation, eOpts) {
            Ext.apply(this.proxy.extraParams, {
                'resourcePkid': resourcePkid
            });
        });
        this.getUserInfoStore().load();

    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
            this.search();
        }
    },
    searchUserTxListener: function (textfield, e) {
        if (e.getKey() == Ext.EventObject.ENTER) {
            this.searchUser();
        }
    },
    addUserAuth: function(btn) {
        var comboOfSelectUser = btn.up('userMgr').down('comboGridBox');
        var username = comboOfSelectUser.getDisplayValue();
        if(username === '' || username == null) {
            parent.window.showMessage(3,'请选择用户后再点添加！',3,'错误');
        }else {
            var controller = this;
            Ext.Msg.confirm("提示", "确定增加 <span style='color:red;font-weight:bold'>" + username + "</span> 查看资源<span style='color:red;font-weight:bold'>" + resourceName + "</span>的权限吗？", function (btn) {
                if (btn == 'yes') {
                    mask.show();
                    Ext.Ajax.request({
                        url: contextPath + '/userInfo/addUserResAuth',
                        params: {
                            resourcePkid: resourcePkid,
                            userpkid: comboOfSelectUser.getValue()
                        },
                        success: function (response) {
                            mask.hide();
                            var result = Ext.JSON.decode(response.responseText);
                            if (result.flag) {
                                controller.searchUser();
                                comboOfSelectUser.setValue("");
                                parent.window.showMessage(1, result.msg ? result.msg : '新增权限成功！', 2, '提示');
                            } else {
                                Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                            }
                        }
                    });
                }
            });
        }
    },
    save: function (btn) {
        var form = btn.up('form').getForm();
        if (form.isValid()) {
            mask.show();
            // Submit the Ajax request and handle the response
            form.submit({
                params: {father: father, id: id},
                success: function (form, action) {
                    mask.hide();
                    old = '';
                    btn.up('window').close();
                    refreshNode(Ext.getCmp('vpcViewport').down('resourcesInfoMain'), currentnodeid, parentnodeid);
                    parent.window.showMessage(1, '操作成功！', 2, '提示');
                },
                failure: function (form, action) {
                    mask.hide();
                    Ext.Msg.alert('失败了', action.result ? action.result.message : '服务器未响应');
                }
            });
        }

    },
    cancel: function (btn) {
        btn.up('form').up('window').close();
    },
    autoInitFromAccess: function (btn) {
        parentnodeid = null;
        currentnodeid = null;
        Ext.Msg.confirm('提示', '确定初始化内置数据吗?操作不可逆，会删除本年已存在数据!确定操作吗?', function (btn) {
            if (btn == 'yes') {
                mask.show();
                Ext.Ajax.request({
                    url: contextPath + '/resourcesInfo/autoInitFromAccess', params: {},
                    success: function (response) {
                        mask.hide();
                        var result = Ext.JSON.decode(response.responseText);
                        if (result.flag) {
                            parent.window.showMessage(1, "初始化内置操作成功！", 2, "成功");
                            refreshNode(Ext.getCmp('vpcViewport').down('resourcesInfoMain'), currentnodeid, parentnodeid);
                        } else {
                            Ext.Msg.alert('失败', result ? result.msg : '服务器未响应');
                        }
                    }
                });
            }
        });
    },
    gridDBClickListener: function (tree, record, item, index, e, eOpts) {
        this.edit(record.data.pkid, record);
    }

});


/**
 * 刷新树 刷新父节点，展开或不展开当前节点，选中当前节点
 * @param treePanel
 * @param refreshRoot    是否刷新root
 * @param currentNodeId    当前节点id
 * @param parentNodeId    父节点id
 */
function refreshNode(treePanel, currentNodeId, parentNodeId) {
    var refreshRoot = false;
    var currentNode = treePanel.getStore().getNodeById(currentNodeId);
    var parentNode = treePanel.getStore().getNodeById(parentNodeId);
    var rootNode = treePanel.getStore().getRootNode();
    if (parentNodeId == null || parentNodeId == '') {
        refreshRoot = true;
    } else {
        refreshRoot = false;
    }
    var path;
    if (currentNode) {
        path = currentNode.getPath('id');
    } else if (parentNode) {
        path = parentNode.getPath('id');
    } else {
        path = rootNode.getPath('id');
    }
    var loadNode;
    if (refreshRoot) {
        loadNode = rootNode;
    } else {
        loadNode = parentNode;
    }
    //刷新节点，展开节点，选中节点
    treePanel.getStore().load({
        node: loadNode,
        callback: function () {
            if (currentNode && currentNode.data.expanded) {
                treePanel.expandPath(path);
            }
            treePanel.selectPath(path);
        }
    });
}