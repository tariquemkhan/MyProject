var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var watchOnline = require('./routes/online');

app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: true }));

app.use('/videos',watchOnline);

app.get('/', function (req, res){
    res.send("Hello Smart Player : ");
    conole.log("inside handling request")
})
console.log("server is starting and listening to port 8083");
app.listen(8083);
