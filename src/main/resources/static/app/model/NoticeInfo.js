Ext.define('MyApp.model.NoticeInfo', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'pkid', type: 'string'},
        { name: 'title', type: 'string'},
        { name: 'content', type: 'string'},
        { name: 'senttime', type: 'CHAR(19)'},
        { name: 'fromwho', type: 'string'},
        { name: 'fromwhoname', type: 'string'},
        { name: 'type', type: 'string'}    ]
});

