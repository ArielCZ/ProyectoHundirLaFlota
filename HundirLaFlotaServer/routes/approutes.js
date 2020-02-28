'use strict'

var express = require('express')
var JugadorController = require('../controllers/playercontroller');

var router = express.Router();
router.post('/save-player', JugadorController.savePlayer);
router.get('/get-players', JugadorController.getPlayers);
router.get('/getOnlinePlayers', JugadorController.getOnlinePlayers);
router.post('/save-onlinePlayer', JugadorController.addOnlinePlayer);
module.exports = router;