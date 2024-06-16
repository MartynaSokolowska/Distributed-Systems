#ifndef CAM_ICE
#define CAM_ICE

module Cameras
{

  struct came
  {
    int On;
    int P;
    int T;
    int Z;
    int MaxZoom;
  }
  sequence <came> cams;

  exception NoInput {};
  exception OutOfBoundry{};

  interface Cam
  {
    void switch(int id) throws OutOfBoundry;
    came print(int id) throws OutOfBoundry;
    void turnVertical(int id, int val) throws OutOfBoundry;
    void turnHorizontal(int id, int val) throws OutOfBoundry;
    void zoom(int id, int val) throws OutOfBoundry;
  };

};

#endif