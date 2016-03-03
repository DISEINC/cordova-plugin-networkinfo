var networkinfo = function() {
};

networkinfo.getNetworkInfo = function(success, fail) {
	cordova.exec(success, fail, "networkinfo", "getNetworkInfo", []);
};

module.exports = networkinfo;
