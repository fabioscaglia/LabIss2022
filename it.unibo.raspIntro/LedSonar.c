#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>


#define TRIG 0
#define ECHO 2
#define LED 6

#define CLOSE 18
#define MEDIUM 21
#define FAR 60

#define POS_LEFT 0.055
#define POS_RIGHT 0.24
#define POS_FORWARD 0.14
using namespace std;
/*
g++  SonarAlone.c -l wiringPi -o  SonarAlone
 */
void setup() {
	//cout << "setUp " << endl;
	wiringPiSetup();
	pinMode(TRIG, OUTPUT);
	pinMode(ECHO, INPUT);
	//TRIG pin must start LOW
	digitalWrite(TRIG, LOW);

    //led
    pinMode(LED, OUTPUT);
    //digitalWrite(LED, LOW);
	delay(30);
}

int getCM() {
	//Send trig pulse
	digitalWrite(TRIG, HIGH);
	delayMicroseconds(20);
	digitalWrite(TRIG, LOW);

	//Wait for echo start
	while(digitalRead(ECHO) == LOW);

	//Wait for echo end
	long startTime = micros();
	while(digitalRead(ECHO) == HIGH);
	long travelTime = micros() - startTime;

	//Get distance in cm
	int distance = travelTime / 58;

	return distance;
}

void accendiLed(int cm){
    if (cm<CLOSE){
        digitalWrite(LED, HIGH);
    }
    else
    {
        digitalWrite(LED, LOW);
    }
}

int main(void) {
	int cm ;
	setup();
	while(1) {
 		cm = getCM(); 	
        accendiLed(cm);	
		cout <<  cm   << endl ;  //flush after ending a new line
		delay(30);
	}
 	return 0;
}
