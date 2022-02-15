#include <Wire.h>

#define I2C_ADDRESS 0x41

void setup() {
  Wire.begin(); 
  Serial.begin(9600);
}


void loop() {
  if(Serial.available()) {
    int value = Serial.parseInt(); //read as integer
    while(Serial.available() != 0){Serial.read();}   //clear out anything else from the buffer
    Serial.println(value);
      
    switch(value){
      case 4:
        Serial.println("robot turned on");
        SendI2cData(4);
        break;
         
      case 3:
        Serial.println("lime light");
        SendI2cData(3);
        break;
         
      case 2:
        Serial.println("arm moving");
        SendI2cData(2);
        break;
         
      default:
        Serial.println("error");
        SendI2cData(1);
        break;
    }
  }
}


void SendI2cData(int data){
  Wire.beginTransmission(I2C_ADDRESS);
  Wire.write(data);
  Wire.endTransmission();
}
