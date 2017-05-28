var express = require('express');
var app = express();
var middleware = require('./helpers/middleware');
var mongo = require('./helpers/db');
var login = require('./routes/login');
var signup = require('./routes/signup');
var profile = require('./routes/profile');
var bodyParser = require('body-parser')
app.use( bodyParser.json() );
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
})); 
// set the view engine to ejs
app.set('view engine', 'ejs');

//Use static javascript and css files
app.use(express.static('public'));
app.use("/script", express.static(__dirname + '/node_modules/bootstrap-validator/dist/'))

mongo.initDatabase(function (error) {
	if (error) {
		throw new error;
	}
	console.log("After making connection : ");
	global.db = mongo.connection;
})

//Adding middleware
app.use(middleware);
app.use('/login', login);
app.use('/signup', signup);
app.use('/profile', profile);

app.get('/', function (req, res) {
	console.log("inside get of request : ");
	res.render('index');
})




app.listen(3000, function () {
	console.log("Listening to port 3000 : ")
})