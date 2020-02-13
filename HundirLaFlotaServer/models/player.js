'use strict'
var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var jugadorSchema = Schema({
    nombre:{
        type: String,
        
    },
    intentos:{
        type: Number,
    },
    tiempo:{
        type: Number,
    },
    image:{
        type: Number,
    }
})
module.exports = mongoose.model('jugador', jugadorSchema);