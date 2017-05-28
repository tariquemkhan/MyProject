var middleware = function (req, res, next) {
	console.log("I am inside middleware : ");
	next()
}

module.exports = middleware;