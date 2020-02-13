'use strict';
var mongoose = require('mongoose');
var app = require('./app');
var port = 3700;
mongoose.set('useFindAndModify', false);
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/hundirlaflotadb', { useNewUrlParser: true, useUnifiedTopology: true })
    .then(function () {
    console.log('Conexión a la base de datos establecida con éxito...');
    app.listen(port, function () {
        console.log('Servidor corriendo correctamente en la ulr: localhost:3700');
    });
});
