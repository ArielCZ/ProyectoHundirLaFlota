'use strict'
var Jugador = require('../models/player');
var controller = {
    savePlayer: function(req, res){
        var jugador = new Jugador();
        var params = req.body;
        jugador.nombre = params.nombre;
        jugador.intentos = params.intento;
        jugador.tiempo = params.tiempo;
        jugador.image = params.image;
        jugador.save();
    },
    getPlayers: function(req, res){
        Jugador.find().then(result=>{
            console.log(result)
        }).catch(err=>{
            console.log(err);
        });
    }

}
module.exports = controller;