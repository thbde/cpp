/**
 * 
 */
// set map to Zweibruecken
var map = L.map('map').setView([49.24, 7.36], 13);
//var OpenStreetMap_Mapnik = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
//	attribution: '&copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>'
//});
var OpenStreetMap_BlackAndWhite = L.tileLayer('http://{s}.www.toolserver.org/tiles/bw-mapnik/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>'
});
//var mapLayer = OpenStreetMap_Mapnik;
var mapLayer = OpenStreetMap_BlackAndWhite;
map.addLayer(mapLayer);

function createSegment(pointList,numberOfOccurrences,maxIndex){
	var col = "hsl(230,50%,"+(90-80*(numberOfOccurrences-1)/(6-1))+"%)";
	var line = L.polyline(pointList,{weight:5,opacity:1,color:col});
	line.addTo(map);
};

function isLess(point1,point2){
	var lat1 = point1[0];
	var lat2 = point2[0];
	if(lat1<lat2){
		return true;
	}
	if(lat2<lat1){
		return false;
	}
	var lon1 = point1[1];
	var lon2 = point2[1];
	if(lon1<lon2){
		return true;
	}
	return false;
};

function hashSegment(segment){
	var numberOfPoints = segment.length;
	var oneIsSmaller = isLess(segment[0],segment[numberOfPoints-1]);
	var hash;
	if(oneIsSmaller){
		hash = segment[0][0].toString() + " "+ segment[0][1].toString() + " " + segment[numberOfPoints-1][0].toString() + " "+ segment[numberOfPoints-1][1].toString();
	}
	else {
		hash = segment[numberOfPoints-1][0].toString() + " "+ segment[numberOfPoints-1][1].toString() + " " +segment[0][0].toString() + " "+ segment[0][1].toString();
	}
	if(numberOfPoints == 2){
		return hash;
	}
	if(oneIsSmaller){
		hash = hash + " " + segment[1][0].toString() + " "+ segment[1][1].toString()
	}
	else {
		hash = hash + " " + segment[numberOfPoints-2][0].toString() + " "+ segment[numberOfPoints-2][1].toString()
	}
	return hash;
	
};

function createOccurrenceMap(listOfSegments){
	var numberOfSegments = listOfSegments.length;
	var dictOcc = {};
	var dictSeg = {};
	var listOfHashs = [];
        var maxIndex = 1;
	for(var i = 0; i < numberOfSegments; ++i){
		var segment = listOfSegments[i];
		var hash = hashSegment(segment);
		var occ = dictOcc[hash];
		if(occ === undefined){
			occ = 0;
			listOfHashs.push(hash);
			dictSeg[hash]=segment;
		}
		dictOcc[hash]=occ+1;
                if(occ+1 > maxIndex){
                        maxIndex=occ+1;
                }
	}
	
	for(var i = 0; i< listOfHashs.length;++i){
		var hash = listOfHashs[i];
		var segment = dictSeg[hash];
		var occ = dictOcc[hash];
		createSegment(segment,occ,maxIndex);
	}
};

createOccurrenceMap(segments);

document.getElementById("distance").innerHTML = distance;
