'use strict'
var Jugador = require('../models/player');
var Pusher = require('pusher');
var onlinePlayers = [];
var pusher = new Pusher({
    appId: '948844',
    key: '6471bfe60094a6c3c7c1',
    secret: 'af90d439248396945b17',
    cluster: 'eu',
    encrypted: true
});
var controller = {
    savePlayer: function (req, res) {
        var jugador = new Jugador();
        var params = req.body;
        jugador.nombre = params.nombre;
        jugador.intentos = params.intento;
        jugador.tiempo = params.tiempo;
        jugador.image = params.image;
        jugador.save((err, playerStored)=>{
            if (err) return res.status(500).send({
                message: 'Error al guardar'
            });
            if (!playerStored) return res.status(404).send({
                message: 'No se ha podido guardar'
            })
            pusher.trigger('playerschannel', 'player-save', {
                player:playerStored
            });
            return res.status(200).send({
                player: playerStored
            })
        })
           
       
        
    },
    getPlayers: function (req, res) {
        Jugador.find((err, players) => {
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
    },

    getOnlinePlayers : function(req, res){
       
        
        return res.status(200).send({
            data: onlinePlayers.length
        });
    },

    addOnlinePlayer: function(req, res){
        onlinePlayers.push('jugador');
        pusher.trigger('playerschannel', 'connect-player', {
            num: onlinePlayers.length
        });
        return res.status(200).send({
            data: onlinePlayers.length
        })
    },
    disconnectPlayer: function(req, res){
        onlinePlayers.pop();
        pusher.trigger('playerschannel', 'disconnect-player',{
            num: onlinePlayers.length
        })
        return res.status(200).send({
            data: onlinePlayers.length
        })
    }



}
module.exports = controller;