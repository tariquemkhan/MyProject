var express = require('express');

// get an instance of router
var router = express.Router();

//var user = require('./helpers/usermodel');

router.post('/', function(req, res) {
	console.log("inside post of signup : ");
	//console.log(user);
	var user = {
		firstName : req.body.firstName,
		lastName : req.body.lastName,
		email : req.body.email,
		password : req.body.password
	}
	db.collection('users').insertOne((user));
	res.redirect('/profile');
})

module.exports = router;