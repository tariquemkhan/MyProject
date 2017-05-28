var express = require('express');
var router = express.Router();

router.get('/list', function(req, res, next) {
	console.log("inside List api");
	//res.send("HEllo world : ");
	res.send("Hello list")
});

module.exports = router;
