var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');


//Initialize connection
module.exports.initDatabase = function (callback) {
	var url = 'mongodb://localhost:27017/test';
	MongoClient.connect(url, function(err, db) {
	  console.log("Database Connected correctly to server.");
	  module.exports.connection = db;
	  callback(err);
	});
}
