Date.prototype.yyyymmdd = function() {
   var yyyy = this.getFullYear().toString();
   var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
   var dd  = this.getDate().toString();
   return yyyy + "-" + (mm[1]?mm:"0"+mm[0]) + "-" + (dd[1]?dd:"0"+dd[0]); // padding
};

window.Handlebars.registerHelper('select', function( value, options ){
	var $el = $('<select />').html( options.fn(this) );
	$el.find('[value=' + value + ']').attr({'selected':'selected'});
	return $el.html();
});

function getAvg(array){
	var sum=0;
	for (var i=0;i<array.length;i++){
		sum += array[i][1]; 
	}
	var avg = sum / array.length;
	return parseFloat(avg).toFixed(2);
};

function depthOf(object) {
	var level = 1;
	var key;
	for(key in object) {
		if (!object.hasOwnProperty(key)) continue;

		if(typeof object[key] == 'object'){
			var depth = depthOf(object[key]) + 1;
			level = Math.max(depth, level);
		}
	}
	return level;
}
	
function Comparator(a,b){
	if (a[0] < b[0]) return -1;
	if (a[0] > b[0]) return 1;
	return 0;
}
	
function ValueComparator(a,b){
	if (a[1] < b[1]) return -1;
	if (a[1] > b[1]) return 1;
	return 0;
};

function getValuesByKeyValue(source, key, value){
	var target={};
	$.each(source, function(i,val){
		if (val[key]==value)
			target=val;
	});
	return target;
}

function getIndexIfObjWithAttr(array, attr, value) {
	for(var i = 0; i < array.length; i++) {
		if(array[i][attr] === value) {
			return i;
		}
	}
	return -1;
}

function stringToBoolean(string){
	switch(string.toLowerCase()){
		case "true": case "yes": case "1": return true;
		case "false": case "no": case "0": case null: return false;
		default: return Boolean(string);
	}
}