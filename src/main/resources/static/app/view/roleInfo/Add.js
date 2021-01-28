var required = '<span style="color:red;font-weight:bold" data-qtip="必填">*</span>';
Ext.define('MyApp.view.roleInfo.Add', {
    extend: 'Ext.form.Panel',
    xtype: 'roleInfoAdd',
    autoScroll: true,
    requires: [
        '*',
        'Ext.ux.DataTip'
    ],
    viewConfig: {loadMask: {msg: "请稍后..."}},
    frame: false, bodyPadding: '10 10 10 10', border: 0,
    url: contextPath + '/roleInfo/save',
    fieldDefaults: {msgTarget: 'side', labelWidth: 100, anchor: '75%'},
    plugins: {ptype: 'datatip'},
    defaultType: 'textfield',
    items: [
        {
            fieldLabel: '角色编号', xtype: 'textfield',
            id: 'roleid', name: 'roleid',
            validator: function (value) {
                var isOk = false;
                var msg = "";
                if (value != old) {
                    Ext.Ajax.request({
                        waitMsg: '正在校验角色编号是否已经存在,请稍后...',
                        url: contextPath + "/roleInfo/validator",
                        params: {'value': value, 'keyid': 'roleid'},//
                        method: "POST", timeout: 40000, async: false,
                        success: function (response, opts) {
                            var resObj = Ext.decode(response.responseText);
                            if (resObj.success) {
                                isOk = true;
                            } else {
                                isOk = false;
                                msg = resObj.msg;
                            }
                        }, failure: function (response, opts) {
                            isOk = false;
                            msg = "错误";
                        }
                    });
                } else {
                    isOk = true;
                }
                if (isOk) {
                    return true;
                } else {
                    return msg;
                }//错误原因
            },
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【必填项】【角色编号】'
        },
        {
            fieldLabel: '角色名称', xtype: 'textfield',
            id: 'roledesc', name: 'roledesc',
            allowBlank: false, afterLabelTextTpl: required, tooltip: '请输入【必填项】【角色名称】'
        },
        {
            fieldLabel: '权限控制', xtype: 'tabpanel', activeTab: 0,
            height:300,
            items: [
                {   title: '关联资源',
                    html: "<div id=\"role4resZtreeV3\" class=\"ztree\"></div>",
                    closable: false, layout: 'fit',
                    listeners: {
                        'afterlayout': function() {
                            //嵌入ztree显示
                            initTree();
                        }
                    }
                },
                {
                    title: '关联用户',
                    closable: false,
                    layout: 'fit',
                    items: [{xtype: 'userMgr'}]
                }
            ]
        }
    ],
    buttons: [{text: '保存', action: 'save'}, {text: '取消', action: 'cancel'}],
    initComponent: function () {
        this.callParent();
    }
});


/**
 * ztree code
 */
function initTree() {
    var setting = {
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        async: {
            enable: true,
            url:contextPath+"/resourcesInfo/getResZTree",
            //autoParam:["id", "name=n", "level=lv"],
            otherParam:{"rolepkid":id},
            dataFilter: filter
        }
    };
    function filter(treeId, parentNode, childNodes) {
        if (!childNodes) return null;
        for (var i=0, l=childNodes.length; i<l; i++) {
            childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
        }
        return childNodes;
    }

    // var zNodes =[
    //     { id:1, pId:0, name:"随意勾选 1", open:true},
    //     { id:11, pId:1, name:"随意勾选 1-1", open:true},
    //     { id:111, pId:11, name:"随意勾选 1-1-1"},
    //     { id:112, pId:11, name:"随意勾选 1-1-2"},
    //     { id:12, pId:1, name:"随意勾选 1-2", open:true},
    //     { id:121, pId:12, name:"随意勾选 1-2-1"},
    //     { id:122, pId:12, name:"随意勾选 1-2-2"},
    //     { id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
    //     { id:21, pId:2, name:"随意勾选 2-1"},
    //     { id:22, pId:2, name:"随意勾选 2-2", open:true},
    //     { id:221, pId:22, name:"随意勾选 2-2-1", checked:true},
    //     { id:222, pId:22, name:"随意勾选 2-2-2"},
    //     { id:23, pId:2, name:"随意勾选 2-3"}
    // ];
    $.fn.zTree.init($("#role4resZtreeV3"), setting);
}
