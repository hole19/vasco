var selected_dest;

function select_dest(dest) {
  selected_dest = dest;

  var buttons = document.getElementById("dest-buttons").querySelectorAll("button")
  for (var i = 0; i < buttons.length; i++) {
    buttons[i].classList.remove('button-clicked');
  }

  document.getElementById('button-' + dest).classList.add('button-clicked');
}

function addSearchBox(map, element) {
   var searchBox = new google.maps.places.SearchBox(element);
   map.controls[google.maps.ControlPosition.TOP_CENTER].push(element);
   google.maps.event.addListener(searchBox, 'places_changed', function() {
      searchBox.set('map', null);

      var places = searchBox.getPlaces();
      var bounds = new google.maps.LatLngBounds();
      var i, place;
      for (i = 0; place = places[i]; i++) {
        (function(place) {
          var marker = new google.maps.Marker({
            position: place.geometry.location
          });
          marker.bindTo('map', searchBox, 'map');
          google.maps.event.addListener(marker, 'map_changed', function() {
            if (!this.getMap()) {
              this.unbindAll();
            }
          });
          bounds.extend(place.geometry.location);
        }(place));

      }
      map.fitBounds(bounds);
      searchBox.set('map', map);
      map.setZoom(Math.min(map.getZoom(), 18));
   });
}

var marker = null;

function addPlacemark(map, location) {
  if (marker != null) {
    marker.setMap(null);
  }
  marker = new google.maps.Marker({
    position: location,
    map: map
  });
}

function initMap() {
  var map = new google.maps.Map(document.getElementById('map'), {
    center: { lat: -34.3568425, lng: 18.4739882 },
    zoom: 18,
    mapTypeId: google.maps.MapTypeId.HYBRID,
    streetViewControl: false
  });

  map.setTilt(0)

  map.addListener('click', function(e) {
    console.log("Tapped on " + e.latLng);

    addPlacemark(map, e.latLng);

    let path = "/location?destination=" + selected_dest + "&lat=" + e.latLng.lat() + "&lng=" + e.latLng.lng();
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", path, false);
    xmlHttp.send(null);
    xmlHttp.responseText;
  });

  addSearchBox(map, document.getElementById('pac-input'));
}
