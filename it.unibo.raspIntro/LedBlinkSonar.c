#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>
#include <pthread.h>

#define T_LAMPEGGIO 500 //millisecondi che intercorrono tra accensione e spegnimento di un led

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

int blinkON=0; //variabile globale per sapere se il led sta lampeggiando oppure no

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

void *blinkingThread(void *t)
{
    printf("Inizio blinking\n");
    while(blinkON)
    {
        digitalWrite(LED, HIGH);
        delay(T_LAMPEGGIO);
        digitalWrite(LED, LOW);
        delay(T_LAMPEGGIO);
    }
    printf("Fine blinking\n");
    return NULL;
}

void blinkLed(int cm){
    int rc;
    void *status;
    pthread_t thread_lampeggio;
    if (cm<CLOSE){
        //attivo il lampeggiamento del led creando un nuovo thread che esegue la funzione blinkingThread
        if(blinkON==0)
        {
            blinkON=1;
             rc = pthread_create(&thread_lampeggio, NULL, blinkingThread, NULL);
            if(rc)
            {
                printf("ERRORE LANCIO THREAD PER BLINKING\n");
                exit(-1);
            }
        }
    }
    else
    {
        digitalWrite(LED, LOW);
        if(blinkON==1)
        {
            blinkON=0;
            rc = pthread_join(thread_lampeggio, &status);
            if (status==PTHREAD_CANCELED)
            { 
                printf("Thread cancellato\n");
            }
            else
            {
                printf("Fine thread lampeggio\n");
            }
        }
    }
}

int main(void) {
	int cm ;
	setup();
	while(1) {
 		cm = getCM(); 	
        cout <<  cm   << endl ;  //flush after ending a new line
        blinkLed(cm);	
		
		delay(30);
	}
 	return 0;
}
