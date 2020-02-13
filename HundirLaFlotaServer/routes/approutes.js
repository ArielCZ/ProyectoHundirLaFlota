'use strict'

var express = require('express')
var JugadorController = require('../controllers/playercontroller');

var router = express.Router();
router.post('/save-player', JugadorController.savePlayer);
router.get('/get-players', JugadorController.getPlayers);
module.exports = router;