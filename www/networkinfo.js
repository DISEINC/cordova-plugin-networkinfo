var networinfo = function() {
};

networinfo.getNetworkInfo = function(success, fail) {
	cordova.exec(success, fail, "networinfo", "getNetworkInfo", []);
};

module.exports = networinfo;
