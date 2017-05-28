var express = require('express');

// get an instance of router
var router = express.Router();

router.post('/', function(req, res) {
	var reqData = {
		email : req.body.email,
		password : req.body.password
	};
	console.log("inside login");
	console.log(reqData);
	//console.log(db);
	/*var result = db.users.find({
		"email" : reqData.email
	}).pretty();*/
	db.collection('users').findOne({
		email : reqData.email,
		password : reqData.password
	}, function (err, user){
		console.log(user);
		if (err) {
			return res.status(500).send();
		}
		if (!user) {
			return res.status(404).send();
		}
		res.redirect('/profile');
	})
	   
	//var result = db.users.find().pretty();
	//console.log(result);
	//res.json(result);

})
module.exports = router;