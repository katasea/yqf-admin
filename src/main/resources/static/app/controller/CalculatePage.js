var step = "";
Ext.define('MyApp.controller.CalculatePage', {
    extend: 'Ext.app.Controller',
    stores: ['CalculatePage'],
    models: ['CalculatePage'],
    views:  ['calculatePage.Main'],
    init: function () {
        this.control({
            'calculatePage button[action=calculate]': {
                click: this.calculate
            },'calculatePage':{
                itemclick: this.stepClick
            },
        });
    },
    calculate:function(){

        var url = "";
        if(step=='第一步'){
            url = contextPath + '/transform/transform';
        }else if(step=='第二步'){
            url = contextPath + '/Allot/calculateAllot';
        }else if(step=='第三步'){

        }else if(step=='第四步'){

        }else if(step=='第五步'){

        }
        if(url!=""){
            mask.show();
            Ext.Ajax.request({
                url: url,
                success: function (response) {
                    mask.hide();
                    var result = Ext.JSON.decode(response.responseText);
                    if (result.success==true) {
                        parent.window.showMessage(1, "计算"+step+"成功");
                    } else {
                        Ext.Msg.alert('失败了', result ? result.msg : '服务器未响应');
                    }
                }
            });
        }
    },
    stepClick:function(grid,record){
        step = record.data.id;
    }
});
