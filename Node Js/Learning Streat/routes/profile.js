var express = require('express');

// get an instance of router
var router = express.Router();

router.get('/', function(req, res) {
		res.render('profile_page');
})

module.exports = router;