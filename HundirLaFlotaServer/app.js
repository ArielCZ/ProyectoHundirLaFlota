'use strict'
var express = require('express');
var bodyParser = require('body-parser');

var app = express();
// cargar archivos rutas
var app_routes = require('./routes/approutes');
//middlewares
app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());

//CORS

//rutas
app.use('/api', app_routes);

//exportar
module.exports = app;