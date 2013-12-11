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

function createSegment(pointList,index){
    var colors = ['#2B3CFC'];
    var line = L.polyline(pointList,{weight:5,opacity:1,color:colors[index % colors.length]});
    line.addTo(map);
    line.setText('► '+(index+1), {repeat: false,offset: 0,attributes: {fill:'black'}});
    return line;
};
function updateInLoop() {
    var i = -1;
    return function up(){
          if((i+1)% segments.length < i) {
              clearInterval(loop);
          }
          else {
          i = (i+1)% segments.length;
          displayedLine = createSegment(segments[i],i);
          }
    }
}
var update = updateInLoop();
loop = setInterval(update,50);

document.getElementById("distance").innerHTML = distance;
