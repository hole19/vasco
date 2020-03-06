# Vasco

Hi! I am [Vasco](https://en.wikipedia.org/wiki/Vasco_da_Gama)\*!

Vasco sends coordinates from a google map on a webpage directly to your terminal.

We at Hole19, use it to interactively navigate the globe using our Android devices and iOS device simulators even when we're seating confortably at our offices.

# Usage

## Run

### With Docker (preferred)
`docker-compose run --rm --service-ports app`

### Without Docker
```
gem install bundler
bundle
ruby vasco.rb
```

## Access
1. open: http://localhost:8000/index.html.erb
1. click on the map
1. watch your command getting executed on the terminal

# Config

Edit `config.yml` file
1. add your google maps api key
1. add the terminal commands ('destinations') that you would like to execute
  1. use `:lat` and `:lng` for the coordinate params

#### Example:

```
maps:
  api_key: <GOOGLE-JS-MAPS-API-KEY>
  lat: 11.380155
  lng: 75.722381
destinations:
  android-generic: adb shell am broadcast -a send.mock -e lat :lat -e lon :lng
  android-specific: adb -s <ANDROID-DEVICE-ID> shell am broadcast -a send.mock -e lat :lat -e lon :lng
  ios: set-simulator-location -c :lat :lng
```

Note:
- for Android devices: [vasco-android](https://github.com/hole19/vasco/tree/master/vasco-android)
- for iOS simulators we're using the following CLI: https://github.com/lyft/set-simulator-location

# Install

```
bundle install
```

\* *as imagined by [@ruigoncalo](https://github.com/ruigoncalo)*
