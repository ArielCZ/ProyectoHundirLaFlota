'use strict'

var express = require('express')
var JugadorController = require('../controllers/playercontroller');

var router = express.Router();
router.post('/save-player', JugadorController.savePlayer);
router.get('/get-players', JugadorController.getPlayers);
router.get('/get-online-players', JugadorController.getOnlinePlayers);
router.post('/save-online-player', JugadorController.addOnlinePlayer);
router.post('/delete-online-player', JugadorController.disconnectPlayer);
module.exports = router;