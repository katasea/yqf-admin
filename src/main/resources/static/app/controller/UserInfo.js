var id = null;//定义一个全局变量id 有值就是修改无值就是新增
var store = null;//需要刷新哪个节点
var currentnodeid = '';
var old = '';
var father = null;//定义一个全局变量parentid用于记录是新增子节点还是顶级父节点
Ext.define('MyApp.controller.UserInfo', {
    extend: 'Ext.app.Controller',
    stores: ['UserInfo', 'DicComboInfo', 'DeptInfoGrid'],
    models: ['UserInfo', 'DeptInfo'],
    views: ['userInfo.Main', 'userInfo.Add', 'common.ComboGridBox', 'common.UploadPanel'],
    init: function () {
        //this can deal and add component listener
        this.control({
            'actioncolumn#actioncolumnEditAndDel': {
                addclick: function (pa) {
                    this.addLeaf(pa.record.data.userid, pa.record);
                },
                editclick: function (pa) {
                    this.edit(pa.record.data.userid, pa.record);
                },
                deleteclick: function (pa) {
                    var name = pa.record.data.username;
                    var id = pa.record.data.userid;
                    currentnodeid = id;
                    Ext.Msg.confirm("提示", "确定删除<span style='color:red;font-weight:bold'>" + name + "</span>吗？不可恢复！", function (btn) {
                        if (btn === 'yes') {
                            mask.show();
                            Ext.Ajax.request({
                                url: contextPath + '/userInfo/del',
                                params: {
                                    id: id, name: name
                                },
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.JSON.decode(response.responseText);
                                    if (result.flag) {
                                        parent.window.showMessage(1, "已经成功删除指定行：" + id + "-" + name, 2, "提示");
                                        store.load();
                                    } else {
                                        Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                                    }
                                }
                            });
                        }
                    });
                }
            },
            'userInfoMain button[action=refresh]': {
                click: this.refresh
            },
            'userInfoMain button[action=add]': {
                click: this.add
            },
            'userInfoMain menu menuitem[action=importData]': {
                click: this.imp
            },
            'userInfoMain menu menuitem[action=exportData]': {
                click: this.exportData
            },
            'userInfoMain menu menuitem[action=downTemplate]': {
                click: this.downTemplate
            },
            'userInfoMain button[action=uptpwd]': {
                click: this.uptpwd
            },
            'userInfoMain button[action=print]': {
                click: this.print
            },
            'userInfoMain button[action=search]': {
                click: this.search
            },
            'userInfoAdd button[action=save]': {
                click: this.save
            },
            'userInfoAdd button[action=cancel]': {
                click: this.cancel
            },
            'uploadPanel panel button[action=importData]': {
                click: this.doImp
            },
            'userInfoMain': {
                itemdblclick: this.gridDBClickListener
            },
            'textfield[name=keyword]': {
                specialkey: this.searchTxListener
            }
        });
        store = this.getUserInfoStore();
        //Get my store and add listener 
        this.getUserInfoStore().on('beforeload', function () {
            var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
            Ext.apply(this.proxy.extraParams, {
                'keyword': keyword
            });
        });
    },
    refresh: function () {
        var store = this.getUserInfoStore();
        store.load({params: {start: 0}});
    },
    add: function () {
        father = null;
        id = null;
        var win = this.createAddWin('add');
        parentnodeid = null;
        currentnodeid = null;
        win.down('userInfoAdd').down('textfield[id=userid]').setDisabled(false);
    },
    imp: function () {
        var form = Ext.widget('uploadPanel');
        var window = Ext.create('Ext.window.Window', {
            title: '上传数据', iconCls: 'Pageexcel', height: 110,
            frame: true, border: 0, autoScroll: true, width: 450,
            scroll: true, modal: true, layout: 'fit', items: form
        });
        window.show();
    },
    uptpwd: function () {
        //获取选中的用户开始进行重置密码
        var record = Ext.getCmp('vpcViewport').down('userInfoMain').getSelectionModel().getSelection();
        if (record == null || record == '') {
            parent.window.showMessage(3, '请选择一个具体的用户进行操作！', 3, '');
        } else {
            var name = record[0].data.username;
            var userid = record[0].data.userid;
            Ext.Msg.confirm("提示", "确定将<span style='color:red;font-weight:bold'>" + name + "</span>密码重置为初始密码吗？重置后密码为生日日期[例如：19900101]且不可恢复！", function (btn) {
                if (btn === 'yes') {
                    mask.show();
                    Ext.Ajax.request({
                        url: contextPath + '/userInfo/resetPassword',
                        params: {
                            userid:userid
                        },
                        success: function (response) {
                            mask.hide();
                            var result = Ext.JSON.decode(response.responseText);
                            if (result.flag) {
                                parent.window.showMessage(2, "成功重置：" + userid + "-" + name +" 的密码，请通知修改初始密码！", 3, "");
                            } else {
                                Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                            }
                        }
                    });
                }
            });
        }
    },
    exportData: function () {
    	var keyword = encodeURIComponent(Ext.getCmp('keyword').getValue());
        window.location.href = contextPath + "/userInfo/exportExcel?keyword="+ keyword;
    },
    downTemplate: function () {
        window.location.href = contextPath + "/userInfo/exportTemplate";
    },
    doImp: function (btn) {
        var form = btn.up('panel').up('uploadPanel').down('form');
        if (form.getForm().isValid()) {
            form.getForm().submit({
                url: contextPath + '/userInfo/importData',
                clientValidation: true,
                method: 'POST',
                params: {},
                waitMsg: '正在导入数据，请稍候.....',
                success: function (response, action) {
                    if (action.result.success) {
                        parent.window.showMessage(1, '成功导入数据！', 2, '提示');
                        btn.up('panel').up('uploadPanel').up('window').close();
                        store.load();
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
    edit: function (vid, record) {
        id = vid;
        var win = this.createAddWin('edit');
        old = record.data.userid;
        win.down('userInfoAdd').getForm().loadRecord(record);
        win.down('userInfoAdd').down('textfield[id=userid]').setDisabled(true);
    },
    searchDept: function () {
        this.getDeptInfoGridStore().on('beforeload', function () {
            var keyword = encodeURIComponent(Ext.getCmp('deptid').getValue());
            Ext.apply(this.proxy.extraParams, {
                'keyword': keyword
            });
        });
        this.getDeptInfoGridStore().load({params: {start: 0}});
    },
    createAddWin: function (flag) {
        var iconCls = '';
        var title = '';
        if (flag === 'add') {
            iconCls = 'Add';
            title = '新增'
        } else if (flag === 'edit') {
            iconCls = 'Edit';
            title = '修改'
        }
        var form = Ext.widget('userInfoAdd');
        var window = Ext.create('Ext.window.Window', {
            title: title, iconCls: iconCls, height: pageHeight - 50,
            frame: true, border: 0, autoScroll: true, width: 600, minWidth: 350,
            scroll: true, modal: true, layout: 'fit', items: form
        });
        window.show();
        this.searchDept();
        return window;
    },
    search: function () {
        this.refresh();
    },
    searchTxListener: function (textfield, e) {
        if (e.getKey() === Ext.EventObject.ENTER) {
            this.search();
        }
    },
    save: function (btn) {
        var form = btn.up('form').getForm();
        //岗位
        var post4resZtreeV3 = $.fn.zTree.getZTreeObj("postZtreeV3");
        var postChecked = post4resZtreeV3.getCheckedNodes(true);
        var poststr = '';
        var i, single;
        for (i = 0; i < postChecked.length; i++) {
            single = postChecked[i];
            if (poststr === '') {
                poststr = single.id;
            } else {
                poststr = poststr + "," + single.id;
            }
        }
        //角色
        var role4resZtreeV3 = $.fn.zTree.getZTreeObj("roleZtreeV3");
        var rolestr = '';
        if (role4resZtreeV3 != null) {
            var roleChecked = role4resZtreeV3.getCheckedNodes(true);
            for (i = 0; i < roleChecked.length; i++) {
                single = roleChecked[i];
                if (rolestr === '') {
                    rolestr = single.id;
                } else {
                    rolestr = rolestr + "," + single.id;
                }
            }
        } else {
            rolestr = 'NOCHANGE';
        }
        //资源菜单
        var res4resZtreeV3 = $.fn.zTree.getZTreeObj("resZtreeV3");
        var resstr = '';
        if (res4resZtreeV3 != null) {
            var resChecked = res4resZtreeV3.getCheckedNodes(true);
            for (i = 0; i < resChecked.length; i++) {
                single = resChecked[i];
                if (resstr === '') {
                    resstr = single.id;
                } else {
                    resstr = resstr + "," + single.id;
                }
            }
        } else {
            resstr = 'NOCHANGE';
        }

        if (form.isValid()) {
            mask.show();
            form.submit({
                params: {father: father, id: id, poststr: poststr, rolestr: rolestr, resstr: resstr},
                success: function () {
                    mask.hide();
                    old = '';
                    parent.window.showMessage(1, '保存成功！', 2, '提示');
                    btn.up('window').close();
                    store.load();
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
    gridDBClickListener: function (tree, record) {
        this.edit(record.data.userid, record);
    }

});
