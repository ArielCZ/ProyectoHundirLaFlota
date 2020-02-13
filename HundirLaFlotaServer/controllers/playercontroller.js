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
        Jugador.find((err, players)=>{
            if (err) return res.status(500).send({
                message: 'Error al devolver los jugadores'
            });
            if (!players) return res.status(404).send({
                message: 'No hay players que mostrar'
            });
            return res.status(200).send({
                players
            });
        });
    }
}
module.exports = controller;