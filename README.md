<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
</head>
<body>


<h1 id="overview">Overview</h1>
	<p>Android application written in Java that processes raw NMEA data and calculates metrics based on the data</p>
	<p>The intention of the project was to see if I could implement a fitness tracking app using raw NMEA data </p>
	<p>I built a microcontroller-based GPS tracker wearable prototype using the Adafruit Flora mainboard. </p>
	<p>The GPS protoype wearable records  data and stores it on flash memory which can be read using the Arduino IDE. </p> 
	<p>The NMEA data is parsed in the application and metrics are calculated from it.  </p>
	<p>Metrics:
	<br>- Date/Time
	<br>- Total Distance
	<br>- Total Duration
	<br>- Average Speed
	<br>- Average Pace
	<br>- Estimated Calories Burned </p>

	
<h1 id="issues">Known Issues</h1>
	There is a bug preventing the file picker from obtaining correct filepath. For the purpose of this project, 
	the filepath is hard coded in processFile() method in HomeFragment.java  
	
	
<h1 id="Acknowledgements">Acknowledgements</h1>
<p> For user login and back4app configuration: https://github.com/back4app/android-user-registration </p>
<p> For NMEA data parsing: https://github.com/petr-s/android-nmea-parser </p>

</body>
</html>
